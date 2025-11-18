package com.cubis.jasper

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun Route.fontRoutes() {
    route("/api/fonts") {
        
        // List all registered fonts
        get {
            val fonts = FontRegistry.getRegisteredFonts()
            call.respond(mapOf(
                "fonts" to fonts.map { (name, info) ->
                    mapOf(
                        "name" to name,
                        "normalPath" to info.normalPath,
                        "boldPath" to info.boldPath,
                        "italicPath" to info.italicPath,
                        "boldItalicPath" to info.boldItalicPath,
                        "pdfEncoding" to info.pdfEncoding,
                        "pdfEmbedded" to info.pdfEmbedded
                    )
                }
            ))
        }
        
        // Register a new font
        post("/register") {
            val multipart = call.receiveMultipart()
            var fontName: String? = null
            var pdfEncoding = "Identity-H"
            var pdfEmbedded = true
            val fontFiles = mutableMapOf<String, File>()
            
            // Create fonts directory if it doesn't exist
            val fontsDir = File("fonts")
            if (!fontsDir.exists()) {
                fontsDir.mkdirs()
            }
            
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val fileName = part.originalFileName ?: "font.ttf"
                        @Suppress("DEPRECATION")
                        val fileBytes = part.streamProvider().readBytes()
                        val file = File(fontsDir, fileName)
                        file.writeBytes(fileBytes)
                        
                        when (part.name) {
                            "normal" -> fontFiles["normal"] = file
                            "bold" -> fontFiles["bold"] = file
                            "italic" -> fontFiles["italic"] = file
                            "boldItalic" -> fontFiles["boldItalic"] = file
                        }
                    }
                    is PartData.FormItem -> {
                        when (part.name) {
                            "name" -> fontName = part.value
                            "pdfEncoding" -> pdfEncoding = part.value
                            "pdfEmbedded" -> pdfEmbedded = part.value.toBoolean()
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }
            
            if (fontName == null || !fontFiles.containsKey("normal")) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Font name and normal font file are required")
                )
                return@post
            }
            
            val fontInfo = FontRegistry.FontInfo(
                name = fontName!!,
                normalPath = fontFiles["normal"]!!.absolutePath,
                boldPath = fontFiles["bold"]?.absolutePath,
                italicPath = fontFiles["italic"]?.absolutePath,
                boldItalicPath = fontFiles["boldItalic"]?.absolutePath,
                pdfEncoding = pdfEncoding,
                pdfEmbedded = pdfEmbedded
            )
            
            val success = FontRegistry.registerFont(fontInfo)
            
            if (success) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf("message" to "Font registered successfully", "fontName" to fontName)
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to register font")
                )
            }
        }
        
        // Register font from file paths (for server-side fonts)
        post("/register-path") {
            val request = call.receive<Map<String, Any>>()
            val fontName = request["name"] as? String
            val normalPath = request["normalPath"] as? String
            val boldPath = request["boldPath"] as? String
            val italicPath = request["italicPath"] as? String
            val boldItalicPath = request["boldItalicPath"] as? String
            val pdfEncoding = request["pdfEncoding"] as? String ?: "Identity-H"
            val pdfEmbedded = request["pdfEmbedded"] as? Boolean ?: true
            
            if (fontName == null || normalPath == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Font name and normalPath are required")
                )
                return@post
            }
            
            val fontInfo = FontRegistry.FontInfo(
                name = fontName,
                normalPath = normalPath,
                boldPath = boldPath,
                italicPath = italicPath,
                boldItalicPath = boldItalicPath,
                pdfEncoding = pdfEncoding,
                pdfEmbedded = pdfEmbedded
            )
            
            val success = FontRegistry.registerFont(fontInfo)
            
            if (success) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf("message" to "Font registered successfully", "fontName" to fontName)
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to register font")
                )
            }
        }
        
        // Remove a registered font
        delete("/{fontName}") {
            val fontName = call.parameters["fontName"]
            
            if (fontName == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Font name is required"))
                return@delete
            }
            
            val success = FontRegistry.removeFont(fontName)
            
            if (success) {
                call.respond(mapOf("message" to "Font removed successfully"))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "Font not found or failed to remove")
                )
            }
        }
        
        // Check if font is registered
        get("/{fontName}") {
            val fontName = call.parameters["fontName"]
            
            if (fontName == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Font name is required"))
                return@get
            }
            
            val fontInfo = FontRegistry.getRegisteredFonts()[fontName]
            
            if (fontInfo != null) {
                call.respond(mapOf(
                    "registered" to true,
                    "fontInfo" to mapOf(
                        "name" to fontInfo.name,
                        "normalPath" to fontInfo.normalPath,
                        "boldPath" to fontInfo.boldPath,
                        "italicPath" to fontInfo.italicPath,
                        "boldItalicPath" to fontInfo.boldItalicPath,
                        "pdfEncoding" to fontInfo.pdfEncoding,
                        "pdfEmbedded" to fontInfo.pdfEmbedded
                    )
                ))
            } else {
                call.respond(mapOf("registered" to false))
            }
        }
        
        // Clear all fonts
        delete {
            FontRegistry.clearAllFonts()
            call.respond(mapOf("message" to "All fonts cleared"))
        }
        
        // Get font extensions status
        get("/extensions") {
            val hasFontExtensions = JasperFontExtensionManager.hasFontExtensions()
            val fontsXml = JasperFontExtensionManager.getFontsXmlContent()
            val extensionProperties = JasperFontExtensionManager.getExtensionPropertiesContent()
            
            call.respond(mapOf(
                "hasExtensions" to hasFontExtensions,
                "fontsXml" to fontsXml,
                "extensionProperties" to extensionProperties,
                "registeredFontsCount" to FontRegistry.getRegisteredFonts().size
            ))
        }
        
        // Regenerate font extensions
        post("/extensions/regenerate") {
            JasperFontExtensionManager.generateFontExtensions()
            call.respond(mapOf(
                "message" to "Font extensions regenerated",
                "fontsCount" to FontRegistry.getRegisteredFonts().size
            ))
        }
    }
    
    // Google Fonts integration
    route("/api/google-fonts") {
        
        // Download and register a font from Google Fonts
        post("/download") {
            val request = call.receive<Map<String, Any>>()
            val fontFamily = request["fontFamily"] as? String
            val apiKey = request["apiKey"] as? String
            val variantsRaw = request["variants"] as? List<*>
            val variants = variantsRaw?.mapNotNull { it as? String } ?: listOf("regular", "700", "italic", "700italic")
            
            if (fontFamily == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "fontFamily is required")
                )
                return@post
            }
            
            val result = GoogleFontsService.downloadAndRegisterFont(fontFamily, apiKey, variants)
            
            if (result.isSuccess) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf(
                        "message" to result.getOrNull(),
                        "fontFamily" to fontFamily
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to result.exceptionOrNull()?.message)
                )
            }
        }
        
        // Quick register popular fonts
        post("/quick-register/{fontName}") {
            val fontName = call.parameters["fontName"]
            
            if (fontName == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Font name is required"))
                return@post
            }
            
            val result = GoogleFontsService.downloadAndRegisterFont(fontName)
            
            if (result.isSuccess) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf(
                        "message" to result.getOrNull(),
                        "fontFamily" to fontName
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to result.exceptionOrNull()?.message)
                )
            }
        }
        
        // List downloaded Google Fonts
        get("/downloaded") {
            val fonts = GoogleFontsService.listDownloadedFonts()
            call.respond(mapOf("fonts" to fonts))
        }
        
        // Clear Google Fonts cache
        delete("/cache") {
            GoogleFontsService.clearCache()
            call.respond(mapOf("message" to "Google Fonts cache cleared"))
        }
    }
}
