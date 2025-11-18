package com.cubis.jasper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object GoogleFontsService {
    private val objectMapper = jacksonObjectMapper()
    private val fontsDir = File("fonts/google-fonts")
    private val cacheFile = File("fonts/google-fonts-cache.json")
    
    data class GoogleFont(
        val family: String,
        val variants: List<String>,
        val files: Map<String, String>
    )
    
    data class GoogleFontsResponse(
        val items: List<GoogleFont>
    )
    
    init {
        // Create fonts directory if it doesn't exist
        if (!fontsDir.exists()) {
            fontsDir.mkdirs()
        }
    }
    
    /**
     * Download and register a font from Google Fonts
     */
    fun downloadAndRegisterFont(
        fontFamily: String,
        apiKey: String? = null,
        variants: List<String> = listOf("regular", "700", "italic", "700italic")
    ): Result<String> {
        return try {
            // Search for the font
            val googleFont = searchGoogleFont(fontFamily, apiKey)
                ?: return Result.failure(Exception("Font '$fontFamily' not found in Google Fonts"))
            
            // Create font directory
            val fontDir = File(fontsDir, fontFamily.replace(" ", "-"))
            if (!fontDir.exists()) {
                fontDir.mkdirs()
            }
            
            // Download font files
            val downloadedFiles = mutableMapOf<String, String>()
            
            variants.forEach { variant ->
                val fileUrl = googleFont.files[variant]
                if (fileUrl != null) {
                    try {
                        val fileName = "${fontFamily.replace(" ", "-")}-${variant}.ttf"
                        val targetFile = File(fontDir, fileName)
                        
                        // Download if not already cached
                        if (!targetFile.exists()) {
                            downloadFile(fileUrl, targetFile)
                        }
                        
                        downloadedFiles[variant] = targetFile.absolutePath
                    } catch (e: Exception) {
                        println("Failed to download variant $variant: ${e.message}")
                    }
                }
            }
            
            if (downloadedFiles.isEmpty()) {
                return Result.failure(Exception("No font files could be downloaded"))
            }
            
            // Register the font
            val registryFontInfo = FontRegistry.FontInfo(
                name = fontFamily,
                normalPath = downloadedFiles["regular"] ?: downloadedFiles.values.first(),
                boldPath = downloadedFiles["700"] ?: downloadedFiles["bold"],
                italicPath = downloadedFiles["italic"],
                boldItalicPath = downloadedFiles["700italic"] ?: downloadedFiles["bolditalic"],
                pdfEncoding = "Identity-H",
                pdfEmbedded = true
            )
            
            val success = FontRegistry.registerFont(registryFontInfo)
            
            if (success) {
                Result.success("Font '$fontFamily' downloaded and registered successfully")
            } else {
                Result.failure(Exception("Failed to register font"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Search for a font in Google Fonts
     */
    private fun searchGoogleFont(fontFamily: String, apiKey: String?): GoogleFont? {
        return try {
            val key = apiKey ?: System.getenv("GOOGLE_FONTS_API_KEY") ?: ""
            
            // Try to load from cache first
            val cachedFonts = loadCachedFonts()
            val cached = cachedFonts?.items?.find { 
                it.family.equals(fontFamily, ignoreCase = true) 
            }
            if (cached != null) {
                return cached
            }
            
            // If API key is available, fetch from Google Fonts API
            if (key.isNotEmpty()) {
                val url = "https://www.googleapis.com/webfonts/v1/webfonts?key=$key"
                val response = URL(url).readText()
                val fontsResponse = objectMapper.readValue<GoogleFontsResponse>(response)
                
                // Cache the response
                cacheFile.writeText(response)
                
                return fontsResponse.items.find { 
                    it.family.equals(fontFamily, ignoreCase = true) 
                }
            }
            
            // Fallback: Use predefined popular fonts
            getPopularFont(fontFamily)
        } catch (e: Exception) {
            println("Error searching Google Font: ${e.message}")
            getPopularFont(fontFamily)
        }
    }
    
    /**
     * Load cached fonts list
     */
    private fun loadCachedFonts(): GoogleFontsResponse? {
        return try {
            if (cacheFile.exists()) {
                objectMapper.readValue<GoogleFontsResponse>(cacheFile)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Get popular font URLs (fallback when API key is not available)
     */
    private fun getPopularFont(fontFamily: String): GoogleFont? {
        val popularFonts = mapOf(
            "Roboto" to GoogleFont(
                family = "Roboto",
                variants = listOf("regular", "700", "italic", "700italic"),
                files = mapOf(
                    "regular" to "https://github.com/google/roboto/raw/main/src/hinted/Roboto-Regular.ttf",
                    "700" to "https://github.com/google/roboto/raw/main/src/hinted/Roboto-Bold.ttf",
                    "italic" to "https://github.com/google/roboto/raw/main/src/hinted/Roboto-Italic.ttf",
                    "700italic" to "https://github.com/google/roboto/raw/main/src/hinted/Roboto-BoldItalic.ttf"
                )
            ),
            "Open Sans" to GoogleFont(
                family = "Open Sans",
                variants = listOf("regular", "700", "italic", "700italic"),
                files = mapOf(
                    "regular" to "https://github.com/googlefonts/opensans/raw/main/fonts/ttf/OpenSans-Regular.ttf",
                    "700" to "https://github.com/googlefonts/opensans/raw/main/fonts/ttf/OpenSans-Bold.ttf",
                    "italic" to "https://github.com/googlefonts/opensans/raw/main/fonts/ttf/OpenSans-Italic.ttf",
                    "700italic" to "https://github.com/googlefonts/opensans/raw/main/fonts/ttf/OpenSans-BoldItalic.ttf"
                )
            ),
            "Noto Sans" to GoogleFont(
                family = "Noto Sans",
                variants = listOf("regular", "700", "italic", "700italic"),
                files = mapOf(
                    "regular" to "https://github.com/notofonts/noto-fonts/raw/main/hinted/ttf/NotoSans/NotoSans-Regular.ttf",
                    "700" to "https://github.com/notofonts/noto-fonts/raw/main/hinted/ttf/NotoSans/NotoSans-Bold.ttf",
                    "italic" to "https://github.com/notofonts/noto-fonts/raw/main/hinted/ttf/NotoSans/NotoSans-Italic.ttf",
                    "700italic" to "https://github.com/notofonts/noto-fonts/raw/main/hinted/ttf/NotoSans/NotoSans-BoldItalic.ttf"
                )
            ),
            "Lato" to GoogleFont(
                family = "Lato",
                variants = listOf("regular", "700", "italic", "700italic"),
                files = mapOf(
                    "regular" to "https://github.com/googlefonts/LatoGFVersion/raw/main/fonts/ttf/Lato-Regular.ttf",
                    "700" to "https://github.com/googlefonts/LatoGFVersion/raw/main/fonts/ttf/Lato-Bold.ttf",
                    "italic" to "https://github.com/googlefonts/LatoGFVersion/raw/main/fonts/ttf/Lato-Italic.ttf",
                    "700italic" to "https://github.com/googlefonts/LatoGFVersion/raw/main/fonts/ttf/Lato-BoldItalic.ttf"
                )
            ),
            // Khmer Fonts
            "Hanuman" to GoogleFont(
                family = "Hanuman",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/hanuman/Hanuman%5Bwght%5D.ttf"
                )
            ),
            "Battambang" to GoogleFont(
                family = "Battambang",
                variants = listOf("regular", "700"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/battambang/Battambang-Regular.ttf",
                    "700" to "https://raw.githubusercontent.com/google/fonts/main/ofl/battambang/Battambang-Bold.ttf"
                )
            ),
            "Bayon" to GoogleFont(
                family = "Bayon",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/bayon/Bayon-Regular.ttf"
                )
            ),
            "Bokor" to GoogleFont(
                family = "Bokor",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/bokor/Bokor-Regular.ttf"
                )
            ),
            "Chenla" to GoogleFont(
                family = "Chenla",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/chenla/Chenla-Regular.ttf"
                )
            ),
            "Content" to GoogleFont(
                family = "Content",
                variants = listOf("regular", "700"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/content/Content-Regular.ttf",
                    "700" to "https://raw.githubusercontent.com/google/fonts/main/ofl/content/Content-Bold.ttf"
                )
            ),
            "Dangrek" to GoogleFont(
                family = "Dangrek",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/dangrek/Dangrek-Regular.ttf"
                )
            ),
            "Fasthand" to GoogleFont(
                family = "Fasthand",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/fasthand/Fasthand-Regular.ttf"
                )
            ),
            "Freehand" to GoogleFont(
                family = "Freehand",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/freehand/Freehand-Regular.ttf"
                )
            ),
            "Khmer" to GoogleFont(
                family = "Khmer",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/khmer/Khmer-Regular.ttf"
                )
            ),
            "Koulen" to GoogleFont(
                family = "Koulen",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/koulen/Koulen-Regular.ttf"
                )
            ),
            "Metal" to GoogleFont(
                family = "Metal",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/metal/Metal-Regular.ttf"
                )
            ),
            "Moul" to GoogleFont(
                family = "Moul",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/moul/Moul-Regular.ttf"
                )
            ),
            "Moulpali" to GoogleFont(
                family = "Moulpali",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/moulpali/Moulpali-Regular.ttf"
                )
            ),
            "Odor Mean Chey" to GoogleFont(
                family = "Odor Mean Chey",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/odormeanchey/OdorMeanChey-Regular.ttf"
                )
            ),
            "Preahvihear" to GoogleFont(
                family = "Preahvihear",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/preahvihear/Preahvihear-Regular.ttf"
                )
            ),
            "Siemreap" to GoogleFont(
                family = "Siemreap",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/siemreap/Siemreap-Regular.ttf"
                )
            ),
            "Suwannaphum" to GoogleFont(
                family = "Suwannaphum",
                variants = listOf("regular", "700"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/suwannaphum/Suwannaphum-Regular.ttf",
                    "700" to "https://raw.githubusercontent.com/google/fonts/main/ofl/suwannaphum/Suwannaphum-Bold.ttf"
                )
            ),
            "Taprom" to GoogleFont(
                family = "Taprom",
                variants = listOf("regular"),
                files = mapOf(
                    "regular" to "https://raw.githubusercontent.com/google/fonts/main/ofl/taprom/Taprom-Regular.ttf"
                )
            ),
            "Noto Sans Khmer" to GoogleFont(
                family = "Noto Sans Khmer",
                variants = listOf("regular", "700"),
                files = mapOf(
                    "regular" to "https://github.com/google/fonts/raw/main/ofl/notosanskhmer/NotoSansKhmer%5Bwdth%2Cwght%5D.ttf",
                    "700" to "https://github.com/google/fonts/raw/main/ofl/notosanskhmer/NotoSansKhmer%5Bwdth%2Cwght%5D.ttf"
                )
            ),
            "Noto Serif Khmer" to GoogleFont(
                family = "Noto Serif Khmer",
                variants = listOf("regular", "700"),
                files = mapOf(
                    "regular" to "https://github.com/google/fonts/raw/main/ofl/notoserifkhmer/NotoSerifKhmer%5Bwdth%2Cwght%5D.ttf",
                    "700" to "https://github.com/google/fonts/raw/main/ofl/notoserifkhmer/NotoSerifKhmer%5Bwdth%2Cwght%5D.ttf"
                )
            )
        )
        
        return popularFonts.entries.find { 
            it.key.equals(fontFamily, ignoreCase = true) 
        }?.value
    }
    
    /**
     * Download a file from URL
     */
    private fun downloadFile(urlString: String, targetFile: File) {
        val url = URL(urlString)
        url.openStream().use { input ->
            Files.copy(input, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }
    
    /**
     * List all downloaded Google Fonts
     */
    fun listDownloadedFonts(): List<String> {
        return if (fontsDir.exists()) {
            fontsDir.listFiles()?.filter { it.isDirectory }?.map { it.name } ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    /**
     * Clear font cache
     */
    fun clearCache() {
        if (fontsDir.exists()) {
            fontsDir.deleteRecursively()
            fontsDir.mkdirs()
        }
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }
}
