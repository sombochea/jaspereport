# JasperReports API Service

A powerful REST API service built with Ktor and Kotlin for rendering JasperReports templates to multiple formats.

## Features

- 🚀 Render JasperReports templates to 12+ formats (PDF, Excel, Word, Images, etc.)
- 📤 Upload JRXML templates via API or use server-side templates
- 🔧 Pass dynamic parameters to customize reports
- ⚡ Built with Ktor for high performance
- 🎯 Simple REST API interface

## Quick Start

### Prerequisites
- JDK 11 or higher
- Gradle

### Run the service
```bash
./gradlew run
```

The service will start on `http://localhost:8080`

### Test the API
```bash
# Health check
curl http://localhost:8080/api/reports/health

# Render sample report to PDF
curl -X POST "http://localhost:8080/api/reports/render/sample-report?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test Report", "author": "Admin"}' \
  -o report.pdf
```

## Supported Formats

- PDF, HTML, XML, CSV
- Excel (XLSX)
- Word (DOCX), RTF
- OpenDocument (ODT, ODS)
- Images (PNG, JPEG)

## Documentation

- [API_USAGE.md](API_USAGE.md) - Complete API documentation and examples
- [FONT_REGISTRY.md](FONT_REGISTRY.md) - Font management and UTF-8 support

## Project Structure

```
├── src/main/kotlin/
│   ├── Application.kt              # Main application entry point
│   ├── Routing.kt                  # Route configuration
│   ├── jasper/
│   │   ├── JasperReportService.kt  # Core JasperReports service
│   │   └── JasperReportRoutes.kt   # API endpoints
│   └── plugins/
│       └── Serialization.kt        # JSON serialization config
├── templates/                       # JRXML template files
│   └── sample-report.jrxml         # Sample template
└── API_USAGE.md                    # API documentation
```

## License

MIT

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                               | Description                                                 |
| ----------------------------------------------------|------------------------------------------------------------- |
| [Routing](https://start.ktor.io/p/routing-default) | Allows to define structured routes and associated handlers. |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                                    | Description                                                          |
| -----------------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`                        | Run the tests                                                        |
| `./gradlew build`                       | Build everything                                                     |
| `./gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `./gradlew buildImage`                  | Build the docker image to use with the fat JAR                       |
| `./gradlew publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `./gradlew run`                         | Run the server                                                       |
| `./gradlew runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

