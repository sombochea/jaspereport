package com.cubis.jasper

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateTemplateRequest(
    val name: String,
    val content: String,
    val description: String? = null,
    val category: String? = null
)

@Serializable
data class UpdateTemplateRequest(
    val content: String,
    val description: String? = null,
    val category: String? = null
)

@Serializable
data class TemplateResponse(
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val contentLength: Int
)

fun Route.templateRoutes() {
    route("/api/templates") {
        
        // List all templates
        get {
            val templates = TemplateManager.listTemplates()
            call.respond(mapOf(
                "templates" to templates.map { template ->
                    TemplateResponse(
                        name = template.name,
                        description = template.description,
                        category = template.category,
                        createdAt = template.createdAt.toString(),
                        updatedAt = template.updatedAt.toString(),
                        contentLength = template.content.length
                    )
                },
                "total" to templates.size
            ))
        }
        
        // Get template by name
        get("/{name}") {
            val name = call.parameters["name"] ?: run {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template name is required"))
                return@get
            }
            
            val template = TemplateManager.getTemplate(name)
            if (template != null) {
                call.respond(mapOf(
                    "name" to template.name,
                    "content" to template.content,
                    "description" to template.description,
                    "category" to template.category,
                    "createdAt" to template.createdAt.toString(),
                    "updatedAt" to template.updatedAt.toString()
                ))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Template not found"))
            }
        }
        
        // Get template content only
        get("/{name}/content") {
            val name = call.parameters["name"] ?: run {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template name is required"))
                return@get
            }
            
            val content = TemplateManager.getTemplateContent(name)
            if (content != null) {
                call.respondText(content, ContentType.Application.Xml)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Template not found"))
            }
        }
        
        // Create new template
        post {
            val request = call.receive<CreateTemplateRequest>()
            
            if (request.name.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template name is required"))
                return@post
            }
            
            if (request.content.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template content is required"))
                return@post
            }
            
            if (TemplateManager.templateExists(request.name)) {
                call.respond(HttpStatusCode.Conflict, mapOf("error" to "Template already exists"))
                return@post
            }
            
            val success = TemplateManager.saveTemplate(
                name = request.name,
                content = request.content,
                description = request.description,
                category = request.category
            )
            
            if (success) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf(
                        "message" to "Template created successfully",
                        "name" to request.name
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to create template")
                )
            }
        }
        
        // Upload template file
        post("/upload") {
            val multipart = call.receiveMultipart()
            var templateName: String? = null
            var description: String? = null
            var category: String? = null
            var content: String? = null
            
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        if (part.name == "file") {
                            val fileName = part.originalFileName ?: "template.jrxml"
                            templateName = fileName.removeSuffix(".jrxml")
                            @Suppress("DEPRECATION")
                            content = part.streamProvider().readBytes().toString(Charsets.UTF_8)
                        }
                    }
                    is PartData.FormItem -> {
                        when (part.name) {
                            "name" -> templateName = part.value
                            "description" -> description = part.value
                            "category" -> category = part.value
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }
            
            if (templateName == null || content == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Template name and file are required")
                )
                return@post
            }
            
            val success = TemplateManager.saveTemplate(
                name = templateName!!,
                content = content!!,
                description = description,
                category = category
            )
            
            if (success) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf(
                        "message" to "Template uploaded successfully",
                        "name" to templateName
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to upload template")
                )
            }
        }
        
        // Update template
        put("/{name}") {
            val name = call.parameters["name"] ?: run {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template name is required"))
                return@put
            }
            
            if (!TemplateManager.templateExists(name)) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Template not found"))
                return@put
            }
            
            val request = call.receive<UpdateTemplateRequest>()
            
            val success = TemplateManager.saveTemplate(
                name = name,
                content = request.content,
                description = request.description,
                category = request.category
            )
            
            if (success) {
                call.respond(mapOf(
                    "message" to "Template updated successfully",
                    "name" to name
                ))
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to update template")
                )
            }
        }
        
        // Delete template
        delete("/{name}") {
            val name = call.parameters["name"] ?: run {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Template name is required"))
                return@delete
            }
            
            if (!TemplateManager.templateExists(name)) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Template not found"))
                return@delete
            }
            
            val success = TemplateManager.deleteTemplate(name)
            
            if (success) {
                call.respond(mapOf("message" to "Template deleted successfully"))
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to delete template")
                )
            }
        }
        
        // List templates by category
        get("/category/{category}") {
            val category = call.parameters["category"] ?: run {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Category is required"))
                return@get
            }
            
            val templates = TemplateManager.listTemplatesByCategory(category)
            call.respond(mapOf(
                "category" to category,
                "templates" to templates.map { template ->
                    TemplateResponse(
                        name = template.name,
                        description = template.description,
                        category = template.category,
                        createdAt = template.createdAt.toString(),
                        updatedAt = template.updatedAt.toString(),
                        contentLength = template.content.length
                    )
                },
                "total" to templates.size
            ))
        }
        
        // Get all categories
        get("/categories/list") {
            val categories = TemplateManager.getCategories()
            call.respond(mapOf(
                "categories" to categories,
                "total" to categories.size
            ))
        }
        
        // Get statistics
        get("/statistics") {
            val stats = TemplateManager.getStatistics()
            call.respond(stats)
        }
        
        // Reload templates
        post("/reload") {
            TemplateManager.reload()
            call.respond(mapOf(
                "message" to "Templates reloaded successfully",
                "total" to TemplateManager.listTemplates().size
            ))
        }
    }
}
