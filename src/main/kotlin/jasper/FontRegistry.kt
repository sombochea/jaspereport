package com.cubis.jasper

import java.io.File
import java.util.concurrent.ConcurrentHashMap

object FontRegistry {
    private val registeredFonts = ConcurrentHashMap<String, FontInfo>()
    private var initialized = false
    
    data class FontInfo(
        val name: String,
        val normalPath: String,
        val boldPath: String? = null,
        val italicPath: String? = null,
        val boldItalicPath: String? = null,
        val pdfEncoding: String = "Identity-H",
        val pdfEmbedded: Boolean = true
    )
    
    /**
     * Initialize font registry and load persisted fonts
     */
    fun init() {
        if (initialized) return
        
        try {
            // Initialize persistence
            FontPersistence.init()
            
            // Load all persisted fonts
            val persistedFonts = FontPersistence.loadAllFonts()
            println("Loading ${persistedFonts.size} persisted fonts...")
            
            persistedFonts.forEach { fontInfo ->
                // Validate font file still exists
                if (File(fontInfo.normalPath).exists()) {
                    registeredFonts[fontInfo.name] = fontInfo
                    println("  ✓ Loaded: ${fontInfo.name}")
                } else {
                    println("  ✗ Skipped: ${fontInfo.name} (file not found)")
                    // Remove from persistence if file doesn't exist
                    FontPersistence.deleteFont(fontInfo.name)
                }
            }
            
            // Generate font extensions if fonts were loaded
            if (registeredFonts.isNotEmpty()) {
                JasperFontExtensionManager.generateFontExtensions()
            }
            
            initialized = true
            println("✓ Font registry initialized with ${registeredFonts.size} fonts")
        } catch (e: Exception) {
            println("✗ Failed to initialize font registry: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * Check if font file is a static font (not variable font)
     */
    private fun isStaticFont(fontPath: String): Boolean {
        val fileName = File(fontPath).name
        // Variable fonts typically have [wght], [wdth], etc. in their names
        return !fileName.contains("[") && !fileName.contains("]")
    }
    
    /**
     * Validate font file
     */
    private fun validateFontFile(fontPath: String): String? {
        val file = File(fontPath)
        
        if (!file.exists()) {
            return "Font file not found: $fontPath"
        }
        
        if (!file.canRead()) {
            return "Font file is not readable: $fontPath"
        }
        
        val extension = file.extension.lowercase()
        if (extension !in listOf("ttf", "otf", "ttc")) {
            return "Invalid font file type: $extension (must be .ttf, .otf, or .ttc)"
        }
        
        if (!isStaticFont(fontPath)) {
            return "Variable fonts are not supported. Please use static font files. File: ${file.name}"
        }
        
        return null // Valid
    }
    
    /**
     * Register a custom font family
     * Automatically generates JasperReports font extensions and persists to database
     */
    fun registerFont(fontInfo: FontInfo): Boolean {
        return try {
            // Validate normal font file
            val normalError = validateFontFile(fontInfo.normalPath)
            if (normalError != null) {
                throw IllegalArgumentException(normalError)
            }
            
            // Validate optional font files
            fontInfo.boldPath?.let { path ->
                val error = validateFontFile(path)
                if (error != null) throw IllegalArgumentException("Bold font: $error")
            }
            fontInfo.italicPath?.let { path ->
                val error = validateFontFile(path)
                if (error != null) throw IllegalArgumentException("Italic font: $error")
            }
            fontInfo.boldItalicPath?.let { path ->
                val error = validateFontFile(path)
                if (error != null) throw IllegalArgumentException("Bold Italic font: $error")
            }
            
            // Store in memory registry
            registeredFonts[fontInfo.name] = fontInfo
            
            // Persist to database
            FontPersistence.saveFont(fontInfo)
            
            // Set system properties for font paths
            System.setProperty("jasperreports.font.${fontInfo.name}.normal", fontInfo.normalPath)
            fontInfo.boldPath?.let {
                System.setProperty("jasperreports.font.${fontInfo.name}.bold", it)
            }
            fontInfo.italicPath?.let {
                System.setProperty("jasperreports.font.${fontInfo.name}.italic", it)
            }
            fontInfo.boldItalicPath?.let {
                System.setProperty("jasperreports.font.${fontInfo.name}.bolditalic", it)
            }
            
            // Generate JasperReports font extensions
            JasperFontExtensionManager.generateFontExtensions()
            
            println("✓ Registered font: ${fontInfo.name}")
            true
        } catch (e: Exception) {
            println("✗ Failed to register font: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Remove a registered font
     */
    fun removeFont(fontName: String): Boolean {
        return try {
            val fontInfo = registeredFonts.remove(fontName) ?: return false
            
            // Remove from persistence
            FontPersistence.deleteFont(fontName)
            
            // Remove system properties
            System.clearProperty("jasperreports.font.${fontName}.normal")
            System.clearProperty("jasperreports.font.${fontName}.bold")
            System.clearProperty("jasperreports.font.${fontName}.italic")
            System.clearProperty("jasperreports.font.${fontName}.bolditalic")
            
            // Regenerate font extensions
            if (registeredFonts.isEmpty()) {
                JasperFontExtensionManager.removeFontExtensions()
            } else {
                JasperFontExtensionManager.generateFontExtensions()
            }
            
            println("✓ Removed font: $fontName")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get all registered fonts
     */
    fun getRegisteredFonts(): Map<String, FontInfo> {
        return registeredFonts.toMap()
    }
    
    /**
     * Check if a font is registered
     */
    fun isFontRegistered(fontName: String): Boolean {
        return registeredFonts.containsKey(fontName)
    }
    
    /**
     * Clear all registered fonts
     */
    fun clearAllFonts() {
        registeredFonts.clear()
        
        // Clear from persistence
        FontPersistence.clearAllFonts()
        
        // Remove font extensions
        JasperFontExtensionManager.removeFontExtensions()
        
        println("✓ Cleared all fonts")
    }
    
    /**
     * Get font file path for use in templates
     */
    fun getFontPath(fontName: String, style: String = "normal"): String? {
        val fontInfo = registeredFonts[fontName] ?: return null
        return when (style.lowercase()) {
            "bold" -> fontInfo.boldPath ?: fontInfo.normalPath
            "italic" -> fontInfo.italicPath ?: fontInfo.normalPath
            "bolditalic" -> fontInfo.boldItalicPath ?: fontInfo.normalPath
            else -> fontInfo.normalPath
        }
    }
}
