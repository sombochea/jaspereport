# JasperReports API Service - Usage Guide

## Overview
This service provides a REST API for rendering JasperReports templates to various formats including PDF, HTML, XML, CSV, Excel, Word, images, and more.

## Supported Export Formats
- **PDF** - Portable Document Format
- **HTML** - HTML document
- **XML** - XML format
- **CSV** - Comma-separated values
- **XLSX** - Microsoft Excel
- **DOCX** - Microsoft Word
- **RTF** - Rich Text Format
- **ODT** - OpenDocument Text
- **ODS** - OpenDocument Spreadsheet
- **PNG** - PNG image
- **JPEG** - JPEG image

## API Endpoints

### 1. Health Check
```bash
GET /api/reports/health
```

**Response:**
```json
{
  "status": "ok",
  "service": "jasper-reports"
}
```

### 2. Render Report from Uploaded Template
Upload a JRXML template file and render it to the desired format.

```bash
POST /api/reports/render
Content-Type: multipart/form-data
```

**Parameters:**
- `template` (file, required) - The JRXML template file
- `format` (string, optional) - Export format (default: PDF)
- Any additional form fields will be passed as report parameters

**Example using cURL:**
```bash
# Render to PDF
curl -X POST http://localhost:8080/api/reports/render \
  -F "template=@templates/sample-report.jrxml" \
  -F "format=PDF" \
  -F "title=My Report Title" \
  -F "author=John Doe" \
  -o output.pdf

# Render to Excel
curl -X POST http://localhost:8080/api/reports/render \
  -F "template=@templates/sample-report.jrxml" \
  -F "format=XLSX" \
  -F "title=Sales Report" \
  -F "author=Jane Smith" \
  -o output.xlsx

# Render to PNG image
curl -X POST http://localhost:8080/api/reports/render \
  -F "template=@templates/sample-report.jrxml" \
  -F "format=PNG" \
  -F "title=Chart Report" \
  -F "author=Admin" \
  -o output.png
```

**Example using Postman:**
1. Set method to POST
2. URL: `http://localhost:8080/api/reports/render`
3. Body → form-data:
   - Key: `template`, Type: File, Value: Select your .jrxml file
   - Key: `format`, Type: Text, Value: `PDF` (or any supported format)
   - Key: `title`, Type: Text, Value: `My Report`
   - Key: `author`, Type: Text, Value: `Your Name`

### 3. Render Report from Server Template
Render a report using a template stored on the server.

```bash
POST /api/reports/render/{templateName}?format={format}
Content-Type: application/json
```

**Parameters:**
- `templateName` (path, required) - Name of the template file (without .jrxml extension)
- `format` (query, optional) - Export format (default: PDF)
- Request body: JSON object with report parameters

**Example using cURL:**
```bash
# Render server-side template to PDF
curl -X POST "http://localhost:8080/api/reports/render/sample-report?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Monthly Sales Report",
    "author": "Sales Team"
  }' \
  -o report.pdf

# Render to DOCX
curl -X POST "http://localhost:8080/api/reports/render/sample-report?format=DOCX" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Annual Report",
    "author": "Management"
  }' \
  -o report.docx
```

## Running the Service

### Start the server:
```bash
./gradlew run
```

The service will start on `http://localhost:8080`

### Build the project:
```bash
./gradlew build
```

## Template Storage
Place your JRXML template files in the `templates/` directory at the project root. The service will look for templates in this location when using the `/api/reports/render/{templateName}` endpoint.

## Creating Custom Templates
1. Design your report using JasperSoft Studio or any JRXML editor
2. Define parameters in your template using `<parameter>` tags
3. Save the template as a `.jrxml` file
4. Either upload it via the API or place it in the `templates/` directory

## Example Template Parameters
In your JRXML template, define parameters like this:
```xml
<parameter name="title" class="java.lang.String"/>
<parameter name="author" class="java.lang.String"/>
<parameter name="date" class="java.util.Date"/>
<parameter name="amount" class="java.lang.Double"/>
```

Then pass them in your API request as form fields or JSON properties.

## Error Handling
The API returns appropriate HTTP status codes:
- `200 OK` - Report generated successfully
- `400 Bad Request` - Missing required parameters or invalid template
- `404 Not Found` - Template not found (for server-side templates)
- `500 Internal Server Error` - Report generation failed

Error responses include a JSON body with details:
```json
{
  "error": "Error message description"
}
```

## Google Fonts Integration 🎨

The easiest way to add custom fonts! Download and register Google Fonts with a single command.

See [GOOGLE_FONTS.md](GOOGLE_FONTS.md) for complete documentation.

**Quick Start:**
```bash
# Register Roboto font (one command!)
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto

# Register Open Sans
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Open%20Sans"

# Register Noto Sans (best for multilingual)
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans"

# List downloaded fonts
curl http://localhost:8080/api/google-fonts/downloaded
```

**Supported Popular Fonts:**
- Roboto
- Open Sans
- Noto Sans
- And more with Google Fonts API key!

## Font Registry

For advanced font management, upload custom fonts or register from file paths.

See [FONT_REGISTRY.md](FONT_REGISTRY.md) for complete documentation.

**Upload Custom Font:**
```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=CustomFont" \
  -F "normal=@/path/to/font-regular.ttf" \
  -F "bold=@/path/to/font-bold.ttf"
```

**Register from Path:**
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "CustomFont",
    "normalPath": "/path/to/font.ttf"
  }'
```

## UTF-8 Support

The service supports UTF-8 encoding for all formats:
- PDF exports work with standard fonts (SansSerif, Serif, Monospaced)
- For advanced Unicode support, register custom fonts via the Font Registry
- All text parameters support Unicode characters

**Note:** For full multilingual support (Chinese, Arabic, Japanese, etc.), you need to register fonts that support those character sets using the Font Registry API.

## Image Export (PNG/JPEG)

The service can export reports as images:
- Each page is rendered as a separate image
- Currently exports the first page only
- Supports both PNG and JPEG formats
- Image dimensions match the report page size

```bash
# Export as PNG
curl -X POST "http://localhost:8080/api/reports/render/simple?format=PNG" \
  -H "Content-Type: application/json" \
  -d '{"title": "Image Report"}' \
  -o report.png

# Export as JPEG
curl -X POST "http://localhost:8080/api/reports/render/simple?format=JPEG" \
  -H "Content-Type: application/json" \
  -d '{"title": "Image Report"}' \
  -o report.jpg
```

## Advanced Usage

### Using Data Sources
To pass data collections to your reports, you can extend the service to accept JSON data and convert it to `JRBeanCollectionDataSource`. The current implementation supports empty data sources and parameters.

### Custom Export Options
You can extend the `JasperReportService` to add custom export configurations for each format, such as:
- PDF encryption and permissions
- Excel sheet names and formulas
- Image resolution and quality
- HTML styling options

### Template UTF-8 Configuration
Add these properties to your JRXML templates for proper UTF-8 support:
```xml
<property name="net.sf.jasperreports.export.pdf.encoding" value="UTF-8"/>
<property name="net.sf.jasperreports.export.pdf.font.name" value="DejaVu Sans"/>
```

## Dependencies
- Ktor 3.3.2
- JasperReports 6.20.6
- Kotlin 2.2.20
