package com.cubis

import com.cubis.jasper.FontRegistry
import com.cubis.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Initialize font registry on startup
    FontRegistry.init()
    
    configureSerialization()
    configureRouting()
}
