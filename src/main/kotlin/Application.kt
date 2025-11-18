package com.cubis

import com.cubis.jasper.FontRegistry
import com.cubis.jasper.TemplateManager
import com.cubis.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Initialize font registry on startup
    FontRegistry.init()
    
    // Initialize template manager on startup
    TemplateManager.init()
    
    configureSerialization()
    configureRouting()
}
