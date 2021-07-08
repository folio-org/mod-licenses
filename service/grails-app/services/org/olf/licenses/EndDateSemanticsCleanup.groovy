package org.olf.licenses
import org.olf.licenses.License

/**
 * This service checks for existing Licenses with empty values for endDateSemantics
 * and sets them to 'implicit' (RefDataValue)
 */
public class EndDateSemanticsCleanup {

    def checkUnsetValues() {
        log.debug("EndDateSemanticsCleanup: Check for unset values")

          License lic = License.findByEndDateSemanticsIsNull()
          log.debug(lic)
//        RemoteKB kb = RemoteKB.findByName('LOCAL')
//        if (kb) {
//            if (!kb.readonly) {
//                log.debug("Found 'LOCAL' RemoteKB with readonly==false. Setting it to true...")
//                kb.readonly = Boolean.TRUE
//                kb.save(flush:true, failOnError:true)
//            }
//        }
    }

}
