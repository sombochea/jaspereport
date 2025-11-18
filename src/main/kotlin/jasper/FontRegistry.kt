package com.cubis.jasper

import net.sf.jasperreports.engine.DefaultJasperReportsContext
import net.sf.jasperreports.engine.fonts.FontFamily
import net.sf.jasperreports.engine.fonts.SimpleFontFace
import net.sf.jasperreports.extensions.ExtensionsEnvironment
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object FontRegistry {
    private val registeredFonts = ConcurrentHashMap<String, FontInfo>()
    
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
     * Register a custom font family
     * Note: This is a simplified registration that stores font info.
     * For full JasperReports integration, fonts should be registered via jasperreports_extension.xml
     */
    fun registerFont(fontInfo: FontInfo): Boolean {
        return try {
            // Validate font files exist
            if (!File(fontInfo.normalPath).exists()) {
                throw IllegalArgumentException("Normal font file not found: ${fontInfo.normalPath}")
            }
            
            // Store in registry
            registeredFonts[fontInfo.name] = fontInfo
            
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
            
            true
        } catch (e: Exception) {
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
            
            // Remove system properties
            System.clearProperty("jasperreports.font.${fontName}.normal")
            System.clearProperty("jasperreports.font.${fontName}.bold")
            System.clearProperty("jasperreports.font.${fontName}.italic")
            System.clearProperty("jasperreports.font.${fontName}.bolditalic")
            
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
        val fontNames = registeredFonts.keys.toList()
        fontNames.forEach { removeFont(it) }
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
