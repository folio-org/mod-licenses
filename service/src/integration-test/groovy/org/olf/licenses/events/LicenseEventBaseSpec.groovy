package org.olf.licenses.events

import java.time.Duration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
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
     * Read the topic from the beginning and return what is found, stopping
     * early once {@code minRecords} have been collected.
     *
     * Each call uses a throwaway group id with {@code auto.offset.reset=earliest},
     * so every call replays the topic in full rather than resuming an offset.
     * Pass {@code Integer.MAX_VALUE} to burn the whole timeout and collect
     * everything — that is what negative assertions want.
     *
     * The topic is shared by every spec, so callers must filter for their own
     * events rather than assuming what they find belongs to them.
     */
    List<Map> pollForEvents(String topic, int minRecords = 1, long timeoutMs = 10_000L) {
        Properties props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-${UUID.randomUUID()}".toString())
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, 'earliest')
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, 'false')
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.name)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.name)

        List<Map> events = []
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)
        try {
            consumer.subscribe([topic])
            long deadline = System.currentTimeMillis() + timeoutMs
            while (System.currentTimeMillis() < deadline && events.size() < minRecords) {
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
}