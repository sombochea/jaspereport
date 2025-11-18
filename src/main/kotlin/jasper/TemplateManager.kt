package com.cubis.jasper

import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Template management service with file system and database persistence
 */
object TemplateManager {
    private val templatesDir = File("templates")
    private val templateCache = ConcurrentHashMap<String, TemplatePersistence.TemplateInfo>()
    private var initialized = false
    
    /**
     * Initialize template manager
     */
    fun init() {
        if (initialized) return
        
        try {
            // Ensure templates directory exists
            if (!templatesDir.exists()) {
                templatesDir.mkdirs()
                println("✓ Created templates directory")
            }
            
            // Initialize persistence
            TemplatePersistence.init()
            
            // Load templates from database
            val dbTemplates = TemplatePersistence.loadAllTemplates()
            println("Loading ${dbTemplates.size} templates from database...")
            
            dbTemplates.forEach { template ->
                templateCache[template.name] = template
                // Sync to file system
                syncTemplateToFile(template)
                println("  ✓ Loaded: ${template.name}")
            }
            
            // Load templates from file system (not in database)
            loadTemplatesFromFileSystem()
            
            initialized = true
            println("✓ Template manager initialized with ${templateCache.size} templates")
        } catch (e: Exception) {
            println("✗ Failed to initialize template manager: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * Load templates from file system that aren't in database
     */
    private fun loadTemplatesFromFileSystem() {
        templatesDir.listFiles { file -> file.extension == "jrxml" }?.forEach { file ->
            val templateName = file.nameWithoutExtension
            if (!templateCache.containsKey(templateName)) {
                try {
                    val content = file.readText()
                    val template = TemplatePersistence.TemplateInfo(
                        name = templateName,
                        content = content,
                        description = "Imported from file system"
                    )
                    templateCache[templateName] = template
                    // Save to database
                    TemplatePersistence.saveTemplate(template)
                    println("  ✓ Imported from file: $templateName")
                } catch (e: Exception) {
                    println("  ✗ Failed to import: $templateName - ${e.message}")
                }
            }
        }
    }
    
    /**
     * Sync template to file system
     */
    private fun syncTemplateToFile(template: TemplatePersistence.TemplateInfo) {
        try {
            val file = File(templatesDir, "${template.name}.jrxml")
            file.writeText(template.content)
        } catch (e: Exception) {
            println("  ✗ Failed to sync template to file: ${template.name}")
        }
    }
    
    /**
     * Create or update template
     */
    fun saveTemplate(
        name: String,
        content: String,
        description: String? = null,
        category: String? = null
    ): Boolean {
        return try {
            // Validate JRXML content
            if (!isValidJRXML(content)) {
                throw IllegalArgumentException("Invalid JRXML content")
            }
            
            val template = TemplatePersistence.TemplateInfo(
                name = name,
                content = content,
                description = description,
                category = category
            )
            
            // Save to database
            if (!TemplatePersistence.saveTemplate(template)) {
                return false
            }
            
            // Update cache
            templateCache[name] = template
            
            // Sync to file system
            syncTemplateToFile(template)
            
            println("✓ Saved template: $name")
            true
        } catch (e: Exception) {
            println("✗ Failed to save template: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get template by name
     */
    fun getTemplate(name: String): TemplatePersistence.TemplateInfo? {
        return templateCache[name] ?: TemplatePersistence.loadTemplate(name)?.also {
            templateCache[name] = it
        }
    }
    
    /**
     * Get template content
     */
    fun getTemplateContent(name: String): String? {
        return getTemplate(name)?.content
    }
    
    /**
     * Get template file path
     */
    fun getTemplateFile(name: String): File? {
        val file = File(templatesDir, "$name.jrxml")
        return if (file.exists()) file else null
    }
    
    /**
     * List all templates
     */
    fun listTemplates(): List<TemplatePersistence.TemplateInfo> {
        return templateCache.values.toList()
    }
    
    /**
     * List templates by category
     */
    fun listTemplatesByCategory(category: String): List<TemplatePersistence.TemplateInfo> {
        return templateCache.values.filter { it.category == category }
    }
    
    /**
     * Get all categories
     */
    fun getCategories(): List<String> {
        return templateCache.values.mapNotNull { it.category }.distinct().sorted()
    }
    
    /**
     * Delete template
     */
    fun deleteTemplate(name: String): Boolean {
        return try {
            // Remove from database
            if (!TemplatePersistence.deleteTemplate(name)) {
                return false
            }
            
            // Remove from cache
            templateCache.remove(name)
            
            // Remove file
            val file = File(templatesDir, "$name.jrxml")
            if (file.exists()) {
                file.delete()
            }
            
            // Remove compiled template
            val compiledFile = File(templatesDir, "$name.jasper")
            if (compiledFile.exists()) {
                compiledFile.delete()
            }
            
            println("✓ Deleted template: $name")
            true
        } catch (e: Exception) {
            println("✗ Failed to delete template: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Check if template exists
     */
    fun templateExists(name: String): Boolean {
        return templateCache.containsKey(name) || TemplatePersistence.templateExists(name)
    }
    
    /**
     * Validate JRXML content
     */
    private fun isValidJRXML(content: String): Boolean {
        return try {
            // Basic validation - check for jasperReport root element
            content.contains("<jasperReport") && content.contains("</jasperReport>")
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get template statistics
     */
    fun getStatistics(): Map<String, Any> {
        return mapOf(
            "totalTemplates" to templateCache.size,
            "categories" to getCategories().size,
            "categoryCounts" to getCategories().associateWith { category ->
                listTemplatesByCategory(category).size
            }
        )
    }
    
    /**
     * Reload templates from database
     */
    fun reload() {
        templateCache.clear()
        val templates = TemplatePersistence.loadAllTemplates()
        templates.forEach { template ->
            templateCache[template.name] = template
            syncTemplateToFile(template)
        }
        println("✓ Reloaded ${templates.size} templates")
    }
}
