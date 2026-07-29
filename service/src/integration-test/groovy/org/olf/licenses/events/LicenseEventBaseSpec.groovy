package org.olf.licenses.events

import java.time.Duration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.PartitionInfo
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

import org.olf.licenses.BaseSpec

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import spock.lang.Shared

@Slf4j
abstract class LicenseEventBaseSpec extends BaseSpec {

    @Shared
    JsonSlurper jsonSlurper = new JsonSlurper()

    @Shared
    String kafkaBootstrapServers =
        "${System.getenv('KAFKA_HOST') ?: 'localhost'}:${System.getenv('KAFKA_PORT') ?: '9092'}".toString()

    String topicFor(String entity) {
        String envPrefix = System.getenv('ENV') ?: 'folio'
        String tenantCollection = System.getenv('KAFKA_TENANT_COLLECTION') ?: 'ALL'
        String tenantSegment = (tenantCollection == 'ALL') ? 'ALL' : getCurrentTenant()
        "${envPrefix}.${tenantSegment}.licenses.${entity}".toString()
    }

    /**
     * The tenant value events are expected to carry, which is NOT
     * {@code getCurrentTenant()} verbatim.
     *
     * BaseSpec sends the tenant header as {@code this.class.simpleName}, i.e.
     * CamelCase. Okapi turns that into a Postgres schema name and lower-cases
     * it on the way (OkapiTenantResolver.getTenantSchemaName), so what
     * OkapiTenantContext reads back out via schemaNameToTenantId is
     * lower-case — the original casing is gone and cannot be recovered.
     * Real FOLIO tenant ids are already lower-case, so this is a
     * test-harness artefact, not a production concern.
     */
    String getExpectedEventTenant() {
        getCurrentTenant()?.toLowerCase()
    }

    /**
     * Read the topic from offset 0 to its current end and return every event on it.
     *
     * Uses manual partition assignment plus an explicit seek rather than
     * {@code subscribe()}: a subscribing consumer has to join a consumer group
     * and wait out a rebalance before its first fetch, which regularly costs
     * more than the poll budget of a test and yields zero records even though
     * the messages are sitting on the broker. {@code assign()} skips group
     * coordination entirely, so the first poll returns data immediately and
     * the read is deterministic.
     *
     * The topic is shared by every spec, so callers must filter for their own
     * events rather than assuming what they find belongs to them.
     */
    List<Map> readAllEvents(String topic, long timeoutMs = 10_000L) {
        List<Map> events = []

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps())
        try {
            List<PartitionInfo> partitionInfo = consumer.partitionsFor(topic)
            if (!partitionInfo) {
                log.debug("Topic {} does not exist yet — no events", topic)
                return events
            }

            List<TopicPartition> partitions = partitionInfo.collect {
                new TopicPartition(it.topic(), it.partition())
            }
            consumer.assign(partitions)

            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions)
            consumer.seekToBeginning(partitions)

            long deadline = System.currentTimeMillis() + timeoutMs
            while (System.currentTimeMillis() < deadline) {
                // Stop as soon as every partition has been read up to the end
                // offset captured above; no need to burn the whole budget.
                if (partitions.every { consumer.position(it) >= endOffsets[it] }) break

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500))
                for (ConsumerRecord<String, String> r : records) {
                    log.debug("Got event on {} key={}: {}", topic, r.key(), r.value())
                    events << (jsonSlurper.parseText(r.value()) as Map)
                }
            }
        } finally {
            consumer.close(Duration.ofSeconds(5))
        }
        return events
    }

    /**
     * Give in-flight async sends a moment to land, then read the topic.
     * For negative assertions ("no event was produced for this request").
     */
    List<Map> drainEvents(String topic, long settleMs = 3_000L) {
        Thread.sleep(settleMs)
        readAllEvents(topic)
    }

    /**
     * Re-read the topic until an event satisfies {@code predicate} or
     * {@code timeoutMs} elapses. Publishing happens after commit and the send
     * itself is async, so the event is not guaranteed to be there the instant
     * the HTTP response returns.
     */
    Map pollForEvent(String topic, long timeoutMs = 15_000L, Closure<Boolean> predicate) {
        long deadline = System.currentTimeMillis() + timeoutMs
        while (true) {
            Map match = readAllEvents(topic).find(predicate)
            if (match != null) return match
            if (System.currentTimeMillis() >= deadline) return null
            Thread.sleep(500)
        }
    }

    private Properties consumerProps() {
        Properties props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers)
        // Required by the client even though assign() does no group coordination.
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-${UUID.randomUUID()}".toString())
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, 'earliest')
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, 'false')
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.name)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.name)
        return props
    }
}