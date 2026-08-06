import org.olf.licenses.events.OkapiTenantContext
import org.olf.licenses.events.TopicNameResolver

// Place your Spring DSL code here
beans = {
  // Kafka domain-event plumbing — these classes live in src/main/groovy and are
  // not scanned by Grails convention; register them explicitly.
  tenantContext(OkapiTenantContext)
  topicNameResolver(TopicNameResolver, ref('tenantContext'))
}
