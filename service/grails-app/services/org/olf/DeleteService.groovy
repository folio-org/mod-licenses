package org.olf

import com.google.common.eventbus.Subscribe
import grails.events.annotation.Events
import grails.events.annotation.Subscriber
import grails.events.annotation.gorm.Listener
import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j

import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.mapping.engine.event.DatastoreInitializedEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.olf.licenses.License

@Slf4j
@Transactional
class DeleteService {
  
  @Listener
  void onPreDelete(PreDeleteEvent event) {
    Class type = event.entityAccess.persistentEntity.javaClass
    Serializable id = event.entityAccess.identifier
    GormEntity dr
    
    def transaction = transactionStatus.transaction
    log.info "Transaction ${transaction}"
    if (type == License) {
      // Check for related items.
    
      // Cancel the event. Hopefully this should veto the operation.
      event.cancel()
      transactionStatus.setRollbackOnly()
    }
    
    log.info("Delete ${type} with id ${id} attempted. ${event.cancelled ? 'Was' : 'Was Not'} Vetoed")
  }
}
