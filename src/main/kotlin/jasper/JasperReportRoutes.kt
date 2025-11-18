package com.cubis.jasper

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import java.io.File

fun Route.jasperReportRoutes() {
    val jasperService = JasperReportService()
    
    route("/api/reports") {
        
        // Render report from uploaded JRXML template
        post("/render") {
            val multipart = call.receiveMultipart()
            var jrxmlBytes: ByteArray? = null
            var format = ExportFormat.PDF
            val parameters = mutableMapOf<String, Any?>()
            var dataJson: String? = null
            
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        if (part.name == "template") {
                            @Suppress("DEPRECATION")
                            jrxmlBytes = part.streamProvider().readBytes()
                        }
                    }
                    is PartData.FormItem -> {
                        when (part.name) {
                            "format" -> {
                                format = try {
                                    ExportFormat.valueOf(part.value.uppercase())
                                } catch (e: Exception) {
                                    ExportFormat.PDF
                                }
                            }
                            "data" -> {
                                dataJson = part.value
                            }
                            else -> {
                                parameters[part.name!!] = part.value
                            }
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }
            
            if (jrxmlBytes == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template file is required"))
                return@post
            }
            
            try {
                val dataSource = dataJson?.let {
                    // Parse JSON and create data source if needed
                    // For simplicity, using empty data source here
                    null
                }
                
                val reportBytes = jasperService.renderReport(
                    jrxmlInputStream = jrxmlBytes!!.inputStream(),
                    parameters = parameters,
                    format = format,
                    dataSource = dataSource
                )
                
                val contentType = getContentType(format)
                val fileName = "report.${format.name.lowercase()}"
                
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(
                        ContentDisposition.Parameters.FileName,
                        fileName
                    ).toString()
                )
                
                call.respondBytes(reportBytes, contentType)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to render report: ${e.message}")
                )
            }
        }
        
        // Render report from server-side template file
        post("/render/{templateName}") {
            val templateName = call.parameters["templateName"]
            val format = call.request.queryParameters["format"]?.let {
                try {
                    ExportFormat.valueOf(it.uppercase())
                } catch (e: Exception) {
                    ExportFormat.PDF
                }
            } ?: ExportFormat.PDF
            
            if (templateName == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template name is required"))
                return@post
            }
            
            val templateFile = File("templates/$templateName.jrxml")
            if (!templateFile.exists()) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Template not found"))
                return@post
            }
            
            try {
                val parameters = runCatching { call.receive<Map<String, Any?>>() }.getOrNull() ?: emptyMap()
                
                val reportBytes = jasperService.renderFromTemplateFile(
                    jrxmlFile = templateFile,
                    parameters = parameters,
                    format = format
                )
                
                val contentType = getContentType(format)
                val fileName = "$templateName.${format.name.lowercase()}"
                
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(
                        ContentDisposition.Parameters.FileName,
                        fileName
                    ).toString()
                )
                
                call.respondBytes(reportBytes, contentType)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to render report: ${e.message}", "details" to (e.cause?.message ?: ""))
                )
            }
        }
        
        // Health check endpoint
        get("/health") {
            call.respond(mapOf("status" to "ok", "service" to "jasper-reports"))
        }
    }
}

private fun getContentType(format: ExportFormat): ContentType {
    return when (format) {
        ExportFormat.PDF -> ContentType.Application.Pdf
        ExportFormat.HTML -> ContentType.Text.Html
        ExportFormat.XML -> ContentType.Application.Xml
        ExportFormat.CSV -> ContentType.Text.CSV
        ExportFormat.XLSX -> ContentType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        ExportFormat.DOCX -> ContentType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        ExportFormat.RTF -> ContentType.parse("application/rtf")
        ExportFormat.ODT -> ContentType.parse("application/vnd.oasis.opendocument.text")
        ExportFormat.ODS -> ContentType.parse("application/vnd.oasis.opendocument.spreadsheet")
        ExportFormat.PNG -> ContentType.Image.PNG
        ExportFormat.JPEG -> ContentType.Image.JPEG
    }
}
