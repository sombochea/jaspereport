package com.cubis

import com.cubis.jasper.fontRoutes
import com.cubis.jasper.jasperReportRoutes
import com.cubis.jasper.templateRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("JasperReports API Service")
        }
        
        jasperReportRoutes()
        fontRoutes()
        templateRoutes()
    }
}
