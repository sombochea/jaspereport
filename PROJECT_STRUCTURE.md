# Project Structure

Complete overview of the JasperReports API Service project structure.

## Directory Tree

```
jaspereport/
├── src/
│   └── main/
│       ├── kotlin/
│       │   ├── Application.kt                    # Main entry point
│       │   ├── Routing.kt                        # Route configuration
│       │   ├── jasper/                           # JasperReports integration
│       │   │   ├── JasperReportService.kt        # Report rendering service
│       │   │   ├── JasperReportRoutes.kt         # Report API endpoints
│       │   │   ├── FontRegistry.kt               # Font management
│       │   │   ├── FontRoutes.kt                 # Font API endpoints
│       │   │   ├── FontPersistence.kt            # Database persistence
│       │   │   ├── GoogleFontsService.kt         # Google Fonts integration
│       │   │   └── JasperFontExtensionManager.kt # Extension generation
│       │   └── plugins/
│       │       └── Serialization.kt              # JSON serialization
│       └── resources/
│           ├── application.yaml                  # Server configuration
│           ├── logback.xml                       # Logging configuration
│           ├── fonts.xml                         # Generated font config
│           └── jasperreports_extension.properties # Generated JR config
│
├── templates/                                     # JRXML templates
│   ├── simple.jrxml                              # Simple template
│   ├── sample-report.jrxml                       # Sample report
│   ├── khmer-demo.jrxml                          # Khmer demo
│   ├── utf8-demo.jrxml                           # UTF-8 demo
│   ├── roboto-demo.jrxml                         # Google Fonts demo
│   └── custom-font-example.jrxml                 # Custom font example
│
├── fonts/                                         # Font files
│   ├── .gitkeep                                  # Preserve directory
│   ├── Hanuman-Regular.ttf                       # Khmer font
│   ├── Kh-Content.ttf                            # Khmer font
│   ├── KhmerOS.ttf                               # Khmer font
│   └── google-fonts/                             # Downloaded Google Fonts
│
├── data/                                          # Database files
│   ├── .gitkeep                                  # Preserve directory
│   ├── fonts.mv.db                               # H2 database (gitignored)
│   └── fonts.trace.db                            # H2 trace log (gitignored)
│
├── docs/                                          # Documentation
│   ├── README.md                                 # Documentation index
│   ├── QUICK_START.md                            # Quick start guide
│   ├── API_USAGE.md                              # API reference
│   ├── FEATURES.md                               # Features overview
│   ├── PROJECT_SUMMARY.md                        # Complete summary
│   ├── FONT_PERSISTENCE_GUIDE.md                 # Font persistence
│   ├── FONT_REGISTRY.md                          # Font management
│   ├── FONT_WORKFLOW.md                          # Font workflow
│   ├── FONT_EXTENSIONS.md                        # Extension generation
│   ├── GOOGLE_FONTS.md                           # Google Fonts
│   ├── USING_FONTS_IN_TEMPLATES.md               # Template fonts
│   ├── KHMER_FONTS.md                            # Khmer support
│   ├── KHMER_FONTS_LIST.md                       # Khmer fonts list
│   └── KHMER_FONTS_TROUBLESHOOTING.md            # Khmer troubleshooting
│
├── scripts/                                       # Utility scripts
│   ├── test-fonts.sh                             # Test font functionality
│   ├── test-khmer-fonts.sh                       # Test Khmer fonts
│   ├── test-persistence.sh                       # Test persistence
│   └── setup-khmer-fonts.sh                      # Setup Khmer fonts
│
├── gradle/                                        # Gradle wrapper
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
│
├── build/                                         # Build output (gitignored)
├── .gradle/                                       # Gradle cache (gitignored)
├── .idea/                                         # IntelliJ IDEA (gitignored)
│
├── README.md                                      # Main readme
├── CHANGELOG.md                                   # Change log
├── PROJECT_STRUCTURE.md                           # This file
├── build.gradle.kts                               # Build configuration
├── settings.gradle.kts                            # Project settings
├── gradle.properties                              # Gradle properties
├── gradlew                                        # Gradle wrapper (Unix)
├── gradlew.bat                                    # Gradle wrapper (Windows)
├── .gitignore                                     # Git ignore rules
└── khmer-report.pdf                               # Sample Khmer report
```

## Key Directories

### `/src/main/kotlin/`
**Purpose**: Application source code

**Key Files**:
- `Application.kt` - Main entry point, initializes font registry
- `Routing.kt` - Configures all API routes
- `jasper/` - JasperReports integration package

**Package Structure**:
```
com.cubis
├── Application.kt
├── Routing.kt
├── jasper/
│   ├── JasperReportService.kt
│   ├── JasperReportRoutes.kt
│   ├── FontRegistry.kt
│   ├── FontRoutes.kt
│   ├── FontPersistence.kt
│   ├── GoogleFontsService.kt
│   └── JasperFontExtensionManager.kt
└── plugins/
    └── Serialization.kt
```

### `/src/main/resources/`
**Purpose**: Application resources and configuration

**Key Files**:
- `application.yaml` - Server configuration (port, modules)
- `logback.xml` - Logging configuration
- `fonts.xml` - Generated font definitions (auto-generated)
- `jasperreports_extension.properties` - JasperReports config (auto-generated)

### `/templates/`
**Purpose**: JRXML template files

**Templates**:
- `simple.jrxml` - Basic template
- `sample-report.jrxml` - Sample with parameters
- `khmer-demo.jrxml` - Khmer language demo
- `utf8-demo.jrxml` - UTF-8 character demo
- `roboto-demo.jrxml` - Google Fonts demo
- `custom-font-example.jrxml` - Custom font usage

### `/fonts/`
**Purpose**: Font file storage

**Contents**:
- Static font files (TTF, OTF, TTC)
- `google-fonts/` subdirectory for downloaded fonts
- `.gitkeep` to preserve directory in git

**Note**: Font files are gitignored, only directory structure is tracked

### `/data/`
**Purpose**: Database storage

**Contents**:
- `fonts.mv.db` - H2 database file (gitignored)
- `fonts.trace.db` - H2 trace log (gitignored)
- `.gitkeep` to preserve directory in git

**Note**: Database files are gitignored, created automatically on first run

### `/docs/`
**Purpose**: Project documentation

**Organization**:
- Getting Started guides
- API documentation
- Font management guides
- Khmer language support
- Troubleshooting guides

**Index**: See `docs/README.md` for complete navigation

### `/scripts/`
**Purpose**: Utility scripts for testing and setup

**Scripts**:
- `test-fonts.sh` - Test font registration and usage
- `test-khmer-fonts.sh` - Test Khmer font rendering
- `test-persistence.sh` - Test font persistence
- `setup-khmer-fonts.sh` - Setup Khmer fonts automatically

**Usage**: All scripts are executable and can be run from project root

## File Types

### Source Files
- `.kt` - Kotlin source files
- `.yaml` - YAML configuration files
- `.xml` - XML configuration files
- `.properties` - Java properties files

### Template Files
- `.jrxml` - JasperReports XML template files
- `.jasper` - Compiled JasperReports templates (gitignored)

### Font Files
- `.ttf` - TrueType Font files
- `.otf` - OpenType Font files
- `.ttc` - TrueType Collection files

### Documentation Files
- `.md` - Markdown documentation files

### Script Files
- `.sh` - Shell scripts (Unix/Linux/macOS)

### Build Files
- `.gradle.kts` - Kotlin Gradle build scripts
- `.properties` - Gradle properties

## Generated Files

These files are automatically generated and should not be edited manually:

### At Build Time
- `build/` - Compiled classes and build artifacts
- `.gradle/` - Gradle cache

### At Runtime
- `src/main/resources/fonts.xml` - Font definitions
- `src/main/resources/jasperreports_extension.properties` - JasperReports config
- `data/fonts.mv.db` - H2 database
- `templates/*.jasper` - Compiled templates

## Configuration Files

### Build Configuration
- `build.gradle.kts` - Main build script
  - Dependencies
  - Plugins
  - Build tasks
- `settings.gradle.kts` - Project settings
  - Project name
  - Module configuration
- `gradle.properties` - Gradle properties
  - JVM settings
  - Build options

### Application Configuration
- `src/main/resources/application.yaml` - Server config
  - Port: 8080
  - Modules
  - Deployment settings
- `src/main/resources/logback.xml` - Logging config
  - Log levels
  - Output format
  - File appenders

### Git Configuration
- `.gitignore` - Files to ignore in git
  - Build artifacts
  - IDE files
  - Generated files
  - Database files
  - Font files

## Dependencies

### Main Dependencies
- **Ktor 3.0.1** - Web framework
- **Kotlin 2.0.21** - Programming language
- **JasperReports 7.0.1** - Report engine
- **Exposed** - ORM framework
- **H2 2.2.224** - Embedded database
- **Jackson** - JSON serialization
- **Logback** - Logging

### Build Dependencies
- **Gradle 9.1.0** - Build tool
- **Kotlin Gradle Plugin** - Kotlin support
- **Ktor Gradle Plugin** - Ktor support

## API Endpoints

### Report Endpoints
- `POST /api/reports/generate` - Generate report
- `GET /api/reports/templates` - List templates

### Font Endpoints
- `GET /api/fonts` - List fonts
- `POST /api/fonts/register` - Register font (upload)
- `POST /api/fonts/register-path` - Register font (path)
- `DELETE /api/fonts/{name}` - Remove font
- `GET /api/fonts/extensions` - Get extensions
- `POST /api/fonts/extensions/regenerate` - Regenerate extensions

### Google Fonts Endpoints
- `POST /api/fonts/google/download` - Download font
- `GET /api/fonts/google/downloaded` - List downloaded
- `DELETE /api/fonts/google/cache` - Clear cache

## Database Schema

### Table: registered_fonts
```sql
CREATE TABLE registered_fonts (
    name VARCHAR(255) PRIMARY KEY,
    normal_path VARCHAR(1024) NOT NULL,
    bold_path VARCHAR(1024),
    italic_path VARCHAR(1024),
    bold_italic_path VARCHAR(1024),
    pdf_encoding VARCHAR(50) DEFAULT 'Identity-H',
    pdf_embedded BOOLEAN DEFAULT TRUE
)
```

## Build Artifacts

### JAR Files
- `build/libs/jaspereport-{version}.jar` - Standard JAR
- `build/libs/jaspereport-all.jar` - Fat JAR (with dependencies)

### Docker Images
- `jaspereport:latest` - Docker image (if built)

## Environment Variables

### Optional Configuration
- `PORT` - Server port (default: 8080)
- `DATABASE_PATH` - Database location (default: ./data/fonts)
- `TEMPLATES_DIR` - Templates directory (default: ./templates)
- `FONTS_DIR` - Fonts directory (default: ./fonts)

## Development Workflow

### 1. Development
```bash
./gradlew run                    # Run in development mode
./gradlew build                  # Build project
./gradlew test                   # Run tests
```

### 2. Testing
```bash
./scripts/test-fonts.sh          # Test fonts
./scripts/test-persistence.sh    # Test persistence
./scripts/test-khmer-fonts.sh    # Test Khmer
```

### 3. Deployment
```bash
./gradlew buildFatJar            # Build fat JAR
java -jar build/libs/jaspereport-all.jar  # Run
```

## Maintenance

### Regular Tasks
- **Backup database**: `cp data/fonts.mv.db backups/`
- **Clean build**: `./gradlew clean`
- **Update dependencies**: Edit `build.gradle.kts`
- **Add templates**: Place `.jrxml` files in `templates/`
- **Add fonts**: Use API or place in `fonts/`

### Troubleshooting
- **Check logs**: Console output or log files
- **Verify database**: Check `data/fonts.mv.db` exists
- **Test fonts**: Run `./scripts/test-fonts.sh`
- **Check docs**: See `docs/` for guides

## Best Practices

### Code Organization
- Keep services in `jasper/` package
- Use data classes for DTOs
- Follow Kotlin conventions
- Document public APIs

### File Management
- Don't commit generated files
- Keep templates organized
- Backup database regularly
- Use meaningful names

### Documentation
- Update docs when adding features
- Keep README.md current
- Document API changes
- Add examples for new features

---

**Last Updated**: November 2024

For more information, see the [main README](README.md) or [documentation index](docs/README.md).
