# JasperReports API Service - Complete Project Summary

## Overview

A production-ready REST API service built with Ktor and Kotlin that provides comprehensive JasperReports rendering capabilities with advanced font management, Google Fonts integration, and full Unicode/Khmer language support.

## Core Features

### 1. Multi-Format Report Generation
- **Supported Formats**: PDF, Excel (XLS/XLSX), Word (DOCX), CSV, HTML, RTF, ODT, ODS, PPTX, PNG, JPEG
- **UTF-8 Support**: Full Unicode rendering with proper encoding for all languages
- **Image Export**: High-quality PNG/JPEG rendering with configurable DPI
- **Template Compilation**: Dynamic JRXML template compilation and caching

### 2. Advanced Font Management System
- **Dynamic Font Registry**: Register and manage custom fonts at runtime
- **Font Persistence**: H2 database storage for fonts across application restarts
- **Automatic Extension Generation**: Creates JasperReports font configuration files automatically
- **Font Validation**: Validates font files and ensures static font compatibility
- **Multiple Font Formats**: Support for TTF, OTF, and other font formats

### 3. Google Fonts Integration
- **Easy Font Download**: Download and register Google Fonts with a single API call
- **Popular Fonts**: Pre-configured list of 50+ popular Google Fonts
- **Automatic Registration**: Downloaded fonts are automatically registered and persisted
- **Font Variants**: Support for different font weights and styles

### 4. Khmer Language Support
- **Multiple Khmer Fonts**: Hanuman, Kh-Content, KhmerOS, and more
- **Static Font Validation**: Ensures compatibility with JasperReports
- **Troubleshooting Guide**: Comprehensive documentation for Khmer font issues
- **Demo Templates**: Ready-to-use Khmer report templates

## Architecture

### Technology Stack
- **Framework**: Ktor 3.0.1
- **Language**: Kotlin 2.0.21
- **JasperReports**: 7.0.1
- **Database**: H2 (embedded)
- **Build Tool**: Gradle with Kotlin DSL
- **Serialization**: kotlinx.serialization

### Project Structure
```
src/main/kotlin/jasper/
├── Application.kt                    # Main application entry point
├── JasperReportService.kt           # Core report rendering service
├── JasperReportRoutes.kt            # Report generation API endpoints
├── FontRegistry.kt                   # Font management and registration
├── FontRoutes.kt                     # Font management API endpoints
├── GoogleFontsService.kt            # Google Fonts integration
├── JasperFontExtensionManager.kt    # Font extension file generator
└── FontPersistence.kt               # H2 database persistence layer

templates/
├── sample-report.jrxml              # Basic report template
├── khmer-demo.jrxml                 # Khmer language demo
├── utf8-demo.jrxml                  # UTF-8 character demo
├── roboto-demo.jrxml                # Google Fonts demo
└── custom-font-example.jrxml        # Custom font usage example

fonts/
├── Hanuman-Regular.ttf              # Khmer font
├── Kh-Content.ttf                   # Khmer font
├── KhmerOS.ttf                      # Khmer font
└── google-fonts/                    # Downloaded Google Fonts
```

## API Reference

### Report Generation Endpoints

#### Generate Report
```http
POST /api/reports/generate
Content-Type: application/json

{
  "templateName": "sample-report",
  "format": "PDF",
  "parameters": {
    "title": "My Report",
    "author": "John Doe"
  }
}
```

**Supported Formats**: `PDF`, `EXCEL`, `EXCEL_XLSX`, `WORD`, `CSV`, `HTML`, `RTF`, `ODT`, `ODS`, `PPTX`, `PNG`, `JPEG`

**Response**: Binary file with appropriate Content-Type header

#### List Templates
```http
GET /api/reports/templates
```

**Response**:
```json
{
  "templates": ["sample-report", "khmer-demo", "utf8-demo"]
}
```

### Font Management Endpoints

#### Register Custom Font
```http
POST /api/fonts/register
Content-Type: multipart/form-data

fontFile: <binary TTF/OTF file>
fontFamily: "My Custom Font"
```

**Response**:
```json
{
  "success": true,
  "message": "Font 'My Custom Font' registered successfully",
  "fontFamily": "My Custom Font",
  "fontPath": "/path/to/font.ttf"
}
```

#### List Registered Fonts
```http
GET /api/fonts/list
```

**Response**:
```json
{
  "fonts": [
    {
      "fontFamily": "Hanuman",
      "fontPath": "/fonts/Hanuman-Regular.ttf",
      "isEmbedded": true
    }
  ]
}
```

#### Download Google Font
```http
POST /api/fonts/google/download
Content-Type: application/json

{
  "fontName": "Roboto"
}
```

**Response**:
```json
{
  "success": true,
  "message": "Font 'Roboto' downloaded and registered successfully",
  "fontFamily": "Roboto",
  "fontPath": "/fonts/google-fonts/Roboto-Regular.ttf"
}
```

#### List Available Google Fonts
```http
GET /api/fonts/google/list
```

**Response**:
```json
{
  "fonts": ["Roboto", "Open Sans", "Lato", "Montserrat", ...]
}
```

#### Regenerate Font Extensions
```http
POST /api/fonts/regenerate-extensions
```

**Response**:
```json
{
  "success": true,
  "message": "Font extensions regenerated successfully"
}
```

## Usage Examples

### Basic PDF Generation

```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "sample-report",
    "format": "PDF",
    "parameters": {
      "title": "Sales Report",
      "date": "2024-01-15"
    }
  }' \
  --output report.pdf
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

### Generate High-Resolution PNG

```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "sample-report",
    "format": "PNG",
    "parameters": {}
  }' \
  --output report.png
```

### Register Custom Font

```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "fontFile=@/path/to/MyFont.ttf" \
  -F "fontFamily=My Custom Font"
```

### Download and Use Google Font

```bash
# Download Roboto font
curl -X POST http://localhost:8080/api/fonts/google/download \
  -H "Content-Type: application/json" \
  -d '{"fontName": "Roboto"}'

# Generate report using Roboto
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "roboto-demo",
    "format": "PDF",
    "parameters": {}
  }' \
  --output roboto-report.pdf
```

### Khmer Language Report

```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "khmer-demo",
    "format": "PDF",
    "parameters": {
      "title": "របាយការណ៍ខ្មែរ"
    }
  }' \
  --output khmer-report.pdf
```

## Using Fonts in Templates

### JRXML Font Configuration

```xml
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd">
    
    <!-- Define font style -->
    <style name="KhmerFont" fontName="Hanuman" fontSize="12" pdfEncoding="Identity-H" isPdfEmbedded="true"/>
    
    <!-- Use in text field -->
    <textField>
        <reportElement x="0" y="0" width="200" height="30" style="KhmerFont"/>
        <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
    </textField>
</jasperReport>
```

### Important Font Attributes

- `fontName`: Must match registered font family name
- `pdfEncoding="Identity-H"`: Required for Unicode/Khmer fonts
- `isPdfEmbedded="true"`: Embeds font in PDF for portability
- `pdfFontName`: Optional, for specific font file selection

## Font Persistence System

### How It Works

1. **Startup**: Application loads all fonts from H2 database
2. **Registration**: New fonts are saved to database and registered with JasperReports
3. **Extension Generation**: Font configuration files are automatically created
4. **Validation**: Static fonts are validated for JasperReports compatibility

### Database Schema

```sql
CREATE TABLE fonts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    font_family VARCHAR(255) NOT NULL,
    font_path VARCHAR(500) NOT NULL,
    is_embedded BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

### Font Extension Files

The system automatically generates:

1. **fonts.xml**: JasperReports font family definitions
2. **jasperreports_extension.properties**: Extension configuration

These files are created in `src/main/resources/` and are automatically loaded by JasperReports.

## Troubleshooting

### Blank Text in PDF

**Problem**: Text appears blank or missing in generated PDFs

**Solutions**:
1. Ensure font is registered: `GET /api/fonts/list`
2. Verify font is static (not variable): Check font file properties
3. Use `pdfEncoding="Identity-H"` in JRXML
4. Set `isPdfEmbedded="true"` in JRXML
5. Regenerate extensions: `POST /api/fonts/regenerate-extensions`

### Khmer Fonts Not Working

**Problem**: Khmer text shows as boxes or blank spaces

**Solutions**:
1. Use static Khmer fonts (Hanuman, Kh-Content, KhmerOS)
2. Avoid variable fonts like Noto Sans Khmer
3. Check font registration: `GET /api/fonts/list`
4. Verify JRXML uses correct font family name
5. See `KHMER_FONTS_TROUBLESHOOTING.md` for detailed guide

### Image Export Issues

**Problem**: PNG/JPEG export fails or produces blank images

**Solutions**:
1. Ensure Java AWT is available
2. Check template dimensions are valid
3. Use appropriate DPI settings (default: 300)
4. Verify template compiles successfully

### Font Upload Fails

**Problem**: Custom font registration returns error

**Solutions**:
1. Verify font file is valid TTF/OTF format
2. Check font is static (not variable)
3. Ensure font file is not corrupted
4. Try different font family name

## Performance Considerations

### Template Caching
- Compiled templates are cached in memory
- Reduces compilation overhead for repeated requests
- Cache is cleared on application restart

### Font Loading
- Fonts are loaded once at startup
- Registered fonts persist across restarts
- Font validation happens during registration

### Database
- H2 embedded database for minimal overhead
- File-based storage in `data/` directory
- Automatic schema creation on first run

### Optimization Tips
1. Pre-compile frequently used templates
2. Register fonts once, use many times
3. Use appropriate image DPI (lower = faster)
4. Consider template complexity for large datasets

## Development and Testing

### Build Project
```bash
./gradlew build
```

### Run Application
```bash
./gradlew run
```

### Test Endpoints
```bash
# Test report generation
./test-fonts.sh

# Test Khmer fonts
./test-khmer-fonts.sh
```

### Add New Template
1. Create JRXML file in `templates/` directory
2. Use registered fonts in template
3. Test with API: `POST /api/reports/generate`

### Add New Font
1. Upload via API: `POST /api/fonts/register`
2. Or place in `fonts/` directory and register at startup
3. Use in templates with `fontName` attribute

## Configuration

### Application Settings

Edit `src/main/kotlin/jasper/Application.kt`:

```kotlin
// Server port
embeddedServer(Netty, port = 8080)

// Template directory
val templatesDir = File("templates")

// Font directory
val fontsDir = File("fonts")

// Database location
val dbUrl = "jdbc:h2:./data/fonts"
```

### Dependencies

Key dependencies in `build.gradle.kts`:
- Ktor 3.0.1 (web framework)
- JasperReports 7.0.1 (report engine)
- H2 2.2.224 (database)
- Apache POI 5.2.5 (Excel export)
- Batik 1.17 (SVG support)

## Security Considerations

### Input Validation
- Template names are validated against filesystem
- Font files are validated before registration
- Parameters are sanitized before use

### File Access
- Templates restricted to `templates/` directory
- Fonts restricted to `fonts/` directory
- No arbitrary file system access

### Recommendations
1. Add authentication/authorization for production
2. Implement rate limiting for API endpoints
3. Validate and sanitize all user inputs
4. Use HTTPS in production
5. Restrict file upload sizes
6. Implement proper error handling

## Future Enhancements

### Potential Features
- Template versioning and management
- Report scheduling and caching
- Async report generation with webhooks
- Template preview API
- Font subsetting for smaller PDFs
- Multi-language template support
- Report history and audit logging
- Template validation API
- Batch report generation
- Custom watermarks and headers

## Documentation Files

- `README.md`: Project overview and quick start
- `API_USAGE.md`: Detailed API documentation
- `FEATURES.md`: Feature list and capabilities
- `QUICK_START.md`: Getting started guide
- `FONT_REGISTRY.md`: Font management system
- `FONT_WORKFLOW.md`: Font registration workflow
- `FONT_EXTENSIONS.md`: Font extension generation
- `GOOGLE_FONTS.md`: Google Fonts integration
- `KHMER_FONTS.md`: Khmer language support
- `KHMER_FONTS_LIST.md`: Available Khmer fonts
- `KHMER_FONTS_TROUBLESHOOTING.md`: Khmer font issues
- `USING_FONTS_IN_TEMPLATES.md`: Template font usage

## Support and Resources

### JasperReports Documentation
- Official Docs: https://community.jaspersoft.com/documentation
- Font Configuration: https://community.jaspersoft.com/wiki/custom-font-font-extension

### Ktor Documentation
- Official Docs: https://ktor.io/docs/
- Routing: https://ktor.io/docs/routing-in-ktor.html

### Google Fonts
- Font Library: https://fonts.google.com/
- API: https://developers.google.com/fonts

## License

This project demonstrates JasperReports integration with Ktor. Ensure compliance with:
- JasperReports license (LGPL)
- Font licenses (varies by font)
- Third-party library licenses

## Conclusion

This JasperReports API service provides a complete, production-ready solution for generating reports in multiple formats with advanced font management capabilities. The system handles complex Unicode rendering, supports custom and Google Fonts, and includes comprehensive Khmer language support.

Key achievements:
- ✅ Multi-format report generation (11+ formats)
- ✅ Dynamic font management with persistence
- ✅ Google Fonts integration
- ✅ Full Unicode/Khmer support
- ✅ RESTful API with proper error handling
- ✅ Automatic font extension generation
- ✅ Comprehensive documentation
- ✅ Production-ready architecture

The service is ready for deployment and can be extended with additional features as needed.
