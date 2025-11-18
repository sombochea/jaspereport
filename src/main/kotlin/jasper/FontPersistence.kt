package com.cubis.jasper

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

/**
 * Persistent storage for registered fonts using H2 database
 */
object FontPersistence {
    
    private val dbFile = File("data/fonts.db")
    private var database: Database? = null
    
    object RegisteredFonts : Table("registered_fonts") {
        val name = varchar("name", 255).uniqueIndex()
        val normalPath = varchar("normal_path", 1024)
        val boldPath = varchar("bold_path", 1024).nullable()
        val italicPath = varchar("italic_path", 1024).nullable()
        val boldItalicPath = varchar("bold_italic_path", 1024).nullable()
        val pdfEncoding = varchar("pdf_encoding", 50).default("Identity-H")
        val pdfEmbedded = bool("pdf_embedded").default(true)
        
        override val primaryKey = PrimaryKey(name)
    }
    
    /**
     * Initialize database connection
     */
    fun init() {
        // Create data directory if it doesn't exist
        dbFile.parentFile?.mkdirs()
        
        // Connect to H2 database
        database = Database.connect(
            url = "jdbc:h2:file:${dbFile.absolutePath.removeSuffix(".db")};DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        
        // Create table if it doesn't exist
        transaction {
            SchemaUtils.create(RegisteredFonts)
        }
        
        println("✓ Font persistence initialized: ${dbFile.absolutePath}")
    }
    
    /**
     * Save font to database
     */
    fun saveFont(fontInfo: FontRegistry.FontInfo): Boolean {
        return try {
            transaction {
                // Check if font already exists
                val existing = RegisteredFonts.select { RegisteredFonts.name eq fontInfo.name }.singleOrNull()
                
                if (existing != null) {
                    // Update existing font
                    RegisteredFonts.update({ RegisteredFonts.name eq fontInfo.name }) {
                        it[normalPath] = fontInfo.normalPath
                        it[boldPath] = fontInfo.boldPath
                        it[italicPath] = fontInfo.italicPath
                        it[boldItalicPath] = fontInfo.boldItalicPath
                        it[pdfEncoding] = fontInfo.pdfEncoding
                        it[pdfEmbedded] = fontInfo.pdfEmbedded
                    }
                    println("  ✓ Updated font in database: ${fontInfo.name}")
                } else {
                    // Insert new font
                    RegisteredFonts.insert {
                        it[name] = fontInfo.name
                        it[normalPath] = fontInfo.normalPath
                        it[boldPath] = fontInfo.boldPath
                        it[italicPath] = fontInfo.italicPath
                        it[boldItalicPath] = fontInfo.boldItalicPath
                        it[pdfEncoding] = fontInfo.pdfEncoding
                        it[pdfEmbedded] = fontInfo.pdfEmbedded
                    }
                    println("  ✓ Inserted font into database: ${fontInfo.name}")
                }
            }
            
            // Verify the save
            val count = getFontCount()
            println("  Database now contains $count font(s)")
            true
        } catch (e: Exception) {
            println("  ✗ Failed to save font: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Load all fonts from database
     */
    fun loadAllFonts(): List<FontRegistry.FontInfo> {
        return try {
            transaction {
                val count = RegisteredFonts.selectAll().count()
                println("  Database contains $count font(s)")
                
                RegisteredFonts.selectAll().map { row ->
                    FontRegistry.FontInfo(
                        name = row[RegisteredFonts.name],
                        normalPath = row[RegisteredFonts.normalPath],
                        boldPath = row[RegisteredFonts.boldPath],
                        italicPath = row[RegisteredFonts.italicPath],
                        boldItalicPath = row[RegisteredFonts.boldItalicPath],
                        pdfEncoding = row[RegisteredFonts.pdfEncoding],
                        pdfEmbedded = row[RegisteredFonts.pdfEmbedded]
                    )
                }
            }
        } catch (e: Exception) {
            println("  Error loading fonts: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Delete font from database
     */
    fun deleteFont(fontName: String): Boolean {
        return try {
            transaction {
                RegisteredFonts.deleteWhere { RegisteredFonts.name eq fontName } > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Clear all fonts from database
     */
    fun clearAllFonts(): Boolean {
        return try {
            transaction {
                RegisteredFonts.deleteAll()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Check if font exists in database
     */
    fun fontExists(fontName: String): Boolean {
        return try {
            transaction {
                RegisteredFonts.select { RegisteredFonts.name eq fontName }.count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get font count
     */
    fun getFontCount(): Long {
        return try {
            transaction {
                RegisteredFonts.selectAll().count()
            }
        } catch (e: Exception) {
            0
        }
    }
}
