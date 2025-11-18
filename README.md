# JasperReports API Service

A production-ready REST API service built with Ktor and Kotlin for rendering JasperReports templates to multiple formats with advanced font management and multilingual support.

## ✨ Key Features

- 🚀 **Multi-Format Export** - PDF, Excel, Word, HTML, Images, and 7+ more formats
- 🎨 **Google Fonts Integration** - Download and use 1000+ fonts instantly
- 💾 **Font Persistence** - Fonts automatically saved and loaded across restarts
- 🌐 **Full Unicode Support** - Khmer, Chinese, Japanese, Arabic, and more
- ⚡ **High Performance** - Built with Ktor for speed and scalability
- 🔧 **Dynamic Parameters** - Customize reports with runtime parameters
- 📦 **Easy Deployment** - Single JAR with embedded database

## 🚀 Quick Start

### Prerequisites
- JDK 11 or higher
- Gradle (included via wrapper)

### Start the Server
```bash
./gradlew run
```

Server starts at `http://localhost:8080`

### Generate Your First Report
```bash
# Generate a PDF report
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "simple",
    "format": "PDF",
    "parameters": {"title": "My First Report"}
  }' \
  --output report.pdf
```

## 📚 Documentation

### Getting Started
- **[Quick Start Guide](docs/QUICK_START.md)** - Get up and running in 5 minutes
- **[API Usage](docs/API_USAGE.md)** - Complete API reference with examples
- **[Features Overview](docs/FEATURES.md)** - All features and capabilities

### Font Management
- **[Font Persistence Guide](docs/FONT_PERSISTENCE_GUIDE.md)** - Automatic font persistence system
- **[Font Registry](docs/FONT_REGISTRY.md)** - Dynamic font management
- **[Font Workflow](docs/FONT_WORKFLOW.md)** - Visual workflow guide
- **[Font Extensions](docs/FONT_EXTENSIONS.md)** - Automatic extension generation
- **[Google Fonts Integration](docs/GOOGLE_FONTS.md)** - Easy font downloads

### Template Development
- **[Template Management](docs/TEMPLATE_MANAGEMENT.md)** - Complete CRUD API for templates
- **[Using Fonts in Templates](docs/USING_FONTS_IN_TEMPLATES.md)** - JRXML font configuration

### Khmer Language Support
- **[Khmer Fonts Guide](docs/KHMER_FONTS.md)** - Complete Khmer support guide
- **[Khmer Fonts List](docs/KHMER_FONTS_LIST.md)** - 20+ available Khmer fonts
- **[Khmer Troubleshooting](docs/KHMER_FONTS_TROUBLESHOOTING.md)** - Common issues and solutions

### Deployment & Operations
- **[Docker Guide](docs/DOCKER.md)** - Docker deployment guide
- **[CI/CD Documentation](docs/CICD.md)** - GitHub Actions workflows

### Complete Reference
- **[Project Summary](docs/PROJECT_SUMMARY.md)** - Comprehensive project documentation

## 🎯 Supported Formats

| Format | Extension | Description |
|--------|-----------|-------------|
| PDF | .pdf | Portable Document Format |
| Excel | .xlsx, .xls | Microsoft Excel |
| Word | .docx | Microsoft Word |
| HTML | .html | Web page |
| CSV | .csv | Comma-separated values |
| RTF | .rtf | Rich Text Format |
| ODT | .odt | OpenDocument Text |
| ODS | .ods | OpenDocument Spreadsheet |
| PPTX | .pptx | PowerPoint |
| PNG | .png | Image (high quality) |
| JPEG | .jpg | Image (compressed) |

## 🌍 Language Support

Full Unicode support for all languages including:

- ✅ English, Spanish, French, German
- ✅ **Khmer (ភាសាខ្មែរ)** - Multiple fonts available
- ✅ Chinese (中文), Japanese (日本語), Korean (한국어)
- ✅ Arabic (العربية), Hebrew (עברית)
- ✅ Thai (ไทย), Vietnamese (Tiếng Việt)
- ✅ And many more!

## 📖 API Examples

### Register a Font
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Roboto",
    "normalPath": "/path/to/Roboto-Regular.ttf"
  }'
```

### Download Google Font
```bash
curl -X POST http://localhost:8080/api/fonts/google/download \
  -H "Content-Type: application/json" \
  -d '{"fontFamily": "Roboto"}'
```

### List Registered Fonts
```bash
curl http://localhost:8080/api/fonts
```

### Generate Excel Report
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "sample-report",
    "format": "EXCEL_XLSX",
    "parameters": {}
  }' \
  --output report.xlsx
```

## 🏗️ Project Structure

```
├── src/main/kotlin/
│   ├── Application.kt                    # Main entry point
│   ├── Routing.kt                        # Route configuration
│   └── jasper/
│       ├── JasperReportService.kt        # Report rendering
│       ├── JasperReportRoutes.kt         # Report API
│       ├── FontRegistry.kt               # Font management
│       ├── FontRoutes.kt                 # Font API
│       ├── FontPersistence.kt            # Database persistence
│       ├── GoogleFontsService.kt         # Google Fonts
│       └── JasperFontExtensionManager.kt # Extension generation
├── templates/                             # JRXML templates
├── fonts/                                 # Font files
├── data/                                  # H2 database
├── docs/                                  # Documentation
└── scripts/                               # Utility scripts
```

## 🛠️ Building & Running

| Command | Description |
|---------|-------------|
| `./gradlew run` | Run the server |
| `./gradlew build` | Build the project |
| `./gradlew test` | Run tests |
| `./gradlew buildFatJar` | Build executable JAR |

## 🧪 Testing

Run the test scripts:

```bash
# Test font functionality
./scripts/test-fonts.sh

# Test Khmer fonts
./scripts/test-khmer-fonts.sh

# Test font persistence
./scripts/test-persistence.sh

# Setup Khmer fonts
./scripts/setup-khmer-fonts.sh
```

## 🔧 Configuration

### Application Settings

Edit `src/main/resources/application.yaml`:

```yaml
ktor:
  application:
    modules:
      - com.cubis.ApplicationKt.module
  deployment:
    port: 8080
```

### Database Location

Fonts are persisted in: `data/fonts.mv.db`

### Template Directory

Templates are loaded from: `templates/`

### Font Directory

Font files are stored in: `fonts/`

## 🚢 Deployment

### Docker (Recommended)

Multi-architecture Docker images are available on GitHub Container Registry:

```bash
# Pull the image
docker pull ghcr.io/sombochea/jaspereport:latest

# Run with Docker
docker run -d -p 8080:8080 \
  -v $(pwd)/templates:/app/templates \
  -v $(pwd)/fonts:/app/fonts \
  -v $(pwd)/data:/app/data \
  ghcr.io/sombochea/jaspereport:latest

# Or use Docker Compose
docker-compose up -d
```

**Supported Architectures:**
- linux/amd64 (x86_64)
- linux/arm64 (ARM64, Apple Silicon)

See [Docker Guide](docs/DOCKER.md) for complete documentation.

### Build Fat JAR

```bash
./gradlew buildFatJar
```

Output: `build/libs/jaspereport-all.jar`

### Run Fat JAR

```bash
java -jar build/libs/jaspereport-all.jar
```

### Build Docker Image Locally

```bash
./gradlew buildImage
./gradlew runDocker
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

MIT License - see LICENSE file for details

## 🔗 Resources

- [Ktor Documentation](https://ktor.io/docs/)
- [JasperReports Documentation](https://community.jaspersoft.com/documentation)
- [Google Fonts](https://fonts.google.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

## 💡 Support

For issues and questions:
1. Check the [documentation](docs/)
2. Review [troubleshooting guides](docs/KHMER_FONTS_TROUBLESHOOTING.md)
3. Open an issue on GitHub

---

Built with ❤️ using Ktor and Kotlin
