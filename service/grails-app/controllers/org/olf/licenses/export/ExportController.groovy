package org.olf.licenses.export

import org.olf.licenses.License
import com.k_int.okapi.OkapiTenantAwareController
import com.opencsv.CSVWriterBuilder
import com.opencsv.ICSVWriter
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

/**
 * The ExportController provides endpoints for exporting content in specific formats
 * harvested by the erm module.
 */
@Slf4j
@CurrentTenant
class ExportController extends OkapiTenantAwareController<License> {

    ExportService exportService

    ExportController() {
        super(License, true)
    }

    def index() {
        def objToBind = getObjectToBind()

        ExportControlObject exportObj = new ExportControlObject()

        /**
         * we want to bind this directly with bindData,
         * but the exportObj stayed empty, so these next 3 lines were necessary
         * we should investigate and refactor this later
         */
        exportObj.ids = objToBind?.ids ?: []
        exportObj.include = objToBind?.include ?: [:]
        exportObj.terms = objToBind?.terms ?: [:]

        bindData exportObj, objToBind

        log.debug("ExportController::index")

        // Set the file disposition.
        OutputStreamWriter osWriter = null

        try {
            response.setHeader("Content-disposition", "attachment; filename=export.csv")
            osWriter = new OutputStreamWriter(new BufferedOutputStream(response.outputStream))
            ICSVWriter csvWriter = new CSVWriterBuilder(osWriter)
                .withSeparator(ICSVWriter.DEFAULT_SEPARATOR)          // ASCII 44: ,
                .withQuoteChar(ICSVWriter.DEFAULT_QUOTE_CHARACTER)    // ASCII 34: "
                .withEscapeChar(ICSVWriter.DEFAULT_ESCAPE_CHARACTER)  // ASCII 34: "
                .withLineEnd(ICSVWriter.DEFAULT_LINE_END)             // "\n"
                .build()

            exportService.exportLicensesAsCsv(csvWriter, exportObj)
        } catch (Exception e) {
            log.error("Error during CSV export: ${e.message}", e)
        } finally {
            // Always close the stream.
            try {
                if (osWriter != null) {
                    osWriter.close()
                }
            } catch (IOException e) {
                log.error("Failed to close the OutputStreamWriter: ${e.message}", e)
            }
        }
    }
}
