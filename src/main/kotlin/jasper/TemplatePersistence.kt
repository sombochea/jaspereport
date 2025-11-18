package com.cubis.jasper

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

/**
 * Persistent storage for JRXML templates using H2 database
 */
object TemplatePersistence {
    
    object Templates : Table("templates") {
        val name = varchar("name", 255).uniqueIndex()
        val content = text("content")
        val description = varchar("description", 500).nullable()
        val category = varchar("category", 100).nullable()
        val createdAt = timestamp("created_at").default(Instant.now())
        val updatedAt = timestamp("updated_at").default(Instant.now())
        
        override val primaryKey = PrimaryKey(name)
    }
    
    data class TemplateInfo(
        val name: String,
        val content: String,
        val description: String? = null,
        val category: String? = null,
        val createdAt: Instant = Instant.now(),
        val updatedAt: Instant = Instant.now()
    )
    
    /**
     * Initialize database tables
     */
    fun init() {
        transaction {
            SchemaUtils.create(Templates)
        }
        println("✓ Template persistence initialized")
    }
    
    /**
     * Save template to database
     */
    fun saveTemplate(templateInfo: TemplateInfo): Boolean {
        return try {
            transaction {
                val existing = Templates.select { Templates.name eq templateInfo.name }.singleOrNull()
                
                if (existing != null) {
                    // Update existing template
                    Templates.update({ Templates.name eq templateInfo.name }) {
                        it[content] = templateInfo.content
                        it[description] = templateInfo.description
                        it[category] = templateInfo.category
                        it[updatedAt] = Instant.now()
                    }
                    println("  ✓ Updated template in database: ${templateInfo.name}")
                } else {
                    // Insert new template
                    Templates.insert {
                        it[name] = templateInfo.name
                        it[content] = templateInfo.content
                        it[description] = templateInfo.description
                        it[category] = templateInfo.category
                        it[createdAt] = Instant.now()
                        it[updatedAt] = Instant.now()
                    }
                    println("  ✓ Inserted template into database: ${templateInfo.name}")
                }
            }
            true
        } catch (e: Exception) {
            println("  ✗ Failed to save template: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Load template by name
     */
    fun loadTemplate(name: String): TemplateInfo? {
        return try {
            transaction {
                Templates.select { Templates.name eq name }.singleOrNull()?.let { row ->
                    TemplateInfo(
                        name = row[Templates.name],
                        content = row[Templates.content],
                        description = row[Templates.description],
                        category = row[Templates.category],
                        createdAt = row[Templates.createdAt],
                        updatedAt = row[Templates.updatedAt]
                    )
                }
            }
        } catch (e: Exception) {
            println("  Error loading template: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Load all templates from database
     */
    fun loadAllTemplates(): List<TemplateInfo> {
        return try {
            transaction {
                Templates.selectAll().map { row ->
                    TemplateInfo(
                        name = row[Templates.name],
                        content = row[Templates.content],
                        description = row[Templates.description],
                        category = row[Templates.category],
                        createdAt = row[Templates.createdAt],
                        updatedAt = row[Templates.updatedAt]
                    )
                }
            }
        } catch (e: Exception) {
            println("  Error loading templates: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Load templates by category
     */
    fun loadTemplatesByCategory(category: String): List<TemplateInfo> {
        return try {
            transaction {
                Templates.select { Templates.category eq category }.map { row ->
                    TemplateInfo(
                        name = row[Templates.name],
                        content = row[Templates.content],
                        description = row[Templates.description],
                        category = row[Templates.category],
                        createdAt = row[Templates.createdAt],
                        updatedAt = row[Templates.updatedAt]
                    )
                }
            }
        } catch (e: Exception) {
            println("  Error loading templates by category: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Delete template from database
     */
    fun deleteTemplate(name: String): Boolean {
        return try {
            transaction {
                Templates.deleteWhere { Templates.name eq name } > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Check if template exists
     */
    fun templateExists(name: String): Boolean {
        return try {
            transaction {
                Templates.select { Templates.name eq name }.count() > 0
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get template count
     */
    fun getTemplateCount(): Long {
        return try {
            transaction {
                Templates.selectAll().count()
            }
        } catch (e: Exception) {
            0
        }
    }
    
    /**
     * Get all categories
     */
    fun getAllCategories(): List<String> {
        return try {
            transaction {
                Templates.slice(Templates.category)
                    .selectAll()
                    .mapNotNull { it[Templates.category] }
                    .distinct()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
