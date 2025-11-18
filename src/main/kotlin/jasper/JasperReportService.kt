package com.cubis.jasper

import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.export.*
import net.sf.jasperreports.engine.export.oasis.JROdsExporter
import net.sf.jasperreports.engine.export.oasis.JROdtExporter
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

enum class ExportFormat {
    PDF, HTML, XML, CSV, XLSX, DOCX, RTF, ODT, ODS, PNG, JPEG
}

class JasperReportService {
    
    fun compileReport(jrxmlInputStream: InputStream): JasperReport {
        return JasperCompileManager.compileReport(jrxmlInputStream)
    }
    
    fun compileReport(jrxmlFile: File): JasperReport {
        return JasperCompileManager.compileReport(jrxmlFile.absolutePath)
    }
    
    fun fillReport(jasperReport: JasperReport, parameters: Map<String, Any?>, dataSource: JRDataSource? = null): JasperPrint {
        // Add default parameters for UTF-8 encoding and proper rendering
        val params = parameters.toMutableMap()
        
        // Set locale for proper character rendering
        if (!params.containsKey("REPORT_LOCALE")) {
            params["REPORT_LOCALE"] = java.util.Locale.getDefault()
        }
        
        // Enable font extension for better Unicode support
        if (!params.containsKey("net.sf.jasperreports.awt.ignore.missing.font")) {
            params["net.sf.jasperreports.awt.ignore.missing.font"] = "true"
        }
        
        return if (dataSource != null) {
            JasperFillManager.fillReport(jasperReport, params, dataSource)
        } else {
            JasperFillManager.fillReport(jasperReport, params, JREmptyDataSource())
        }
    }
    
    fun exportReport(jasperPrint: JasperPrint, format: ExportFormat): ByteArray {
        val outputStream = ByteArrayOutputStream()
        
        when (format) {
            ExportFormat.PDF -> {
                val exporter = JRPdfExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleOutputStreamExporterOutput(outputStream))
                
                val configuration = net.sf.jasperreports.export.SimplePdfExporterConfiguration()
                exporter.setConfiguration(configuration)
                
                val reportConfiguration = net.sf.jasperreports.export.SimplePdfReportConfiguration()
                exporter.setConfiguration(reportConfiguration)
                
                exporter.exportReport()
            }
            ExportFormat.HTML -> {
                val exporter = HtmlExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleHtmlExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.XML -> {
                JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream)
            }
            ExportFormat.CSV -> {
                val exporter = JRCsvExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleWriterExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.XLSX -> {
                val exporter = JRXlsxExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleOutputStreamExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.DOCX -> {
                val exporter = JRDocxExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleOutputStreamExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.RTF -> {
                val exporter = JRRtfExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleWriterExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.ODT -> {
                val exporter = JROdtExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleOutputStreamExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.ODS -> {
                val exporter = JROdsExporter()
                exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(net.sf.jasperreports.export.SimpleOutputStreamExporterOutput(outputStream))
                exporter.exportReport()
            }
            ExportFormat.PNG -> {
                // Render each page as PNG image
                val images = mutableListOf<BufferedImage>()
                for (pageIndex in 0 until jasperPrint.pages.size) {
                    val image = BufferedImage(
                        jasperPrint.pageWidth + 1,
                        jasperPrint.pageHeight + 1,
                        BufferedImage.TYPE_INT_RGB
                    )
                    
                    val exporter = JRGraphics2DExporter()
                    exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput.getInstance(listOf(jasperPrint)))
                    
                    val exporterOutput = net.sf.jasperreports.export.SimpleGraphics2DExporterOutput()
                    exporterOutput.setGraphics2D(image.createGraphics())
                    exporter.setExporterOutput(exporterOutput)
                    
                    val configuration = net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration()
                    configuration.pageIndex = pageIndex
                    exporter.setConfiguration(configuration)
                    
                    exporter.exportReport()
                    images.add(image)
                }
                
                // Write first page as PNG (or combine multiple pages if needed)
                if (images.isNotEmpty()) {
                    ImageIO.write(images[0], "PNG", outputStream)
                }
            }
            ExportFormat.JPEG -> {
                // Render each page as JPEG image
                val images = mutableListOf<BufferedImage>()
                for (pageIndex in 0 until jasperPrint.pages.size) {
                    val image = BufferedImage(
                        jasperPrint.pageWidth + 1,
                        jasperPrint.pageHeight + 1,
                        BufferedImage.TYPE_INT_RGB
                    )
                    
                    val exporter = JRGraphics2DExporter()
                    exporter.setExporterInput(net.sf.jasperreports.export.SimpleExporterInput.getInstance(listOf(jasperPrint)))
                    
                    val exporterOutput = net.sf.jasperreports.export.SimpleGraphics2DExporterOutput()
                    exporterOutput.setGraphics2D(image.createGraphics())
                    exporter.setExporterOutput(exporterOutput)
                    
                    val configuration = net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration()
                    configuration.pageIndex = pageIndex
                    exporter.setConfiguration(configuration)
                    
                    exporter.exportReport()
                    images.add(image)
                }
                
                // Write first page as JPEG (or combine multiple pages if needed)
                if (images.isNotEmpty()) {
                    ImageIO.write(images[0], "JPEG", outputStream)
                }
            }
        }
        
        return outputStream.toByteArray()
    }
    
    fun renderReport(
        jrxmlInputStream: InputStream,
        parameters: Map<String, Any?>,
        format: ExportFormat,
        dataSource: JRDataSource? = null
    ): ByteArray {
        val jasperReport = compileReport(jrxmlInputStream)
        val jasperPrint = fillReport(jasperReport, parameters, dataSource)
        return exportReport(jasperPrint, format)
    }
    
    fun renderFromTemplateFile(
        jrxmlFile: File,
        parameters: Map<String, Any?>,
        format: ExportFormat,
        dataSource: JRDataSource? = null
    ): ByteArray {
        val jasperReport = compileReport(jrxmlFile)
        val jasperPrint = fillReport(jasperReport, parameters, dataSource)
        return exportReport(jasperPrint, format)
    }
}
