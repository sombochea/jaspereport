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

## Advanced Usage

### Using Data Sources
To pass data collections to your reports, you can extend the service to accept JSON data and convert it to `JRBeanCollectionDataSource`. The current implementation supports empty data sources and parameters.

### Custom Export Options
You can extend the `JasperReportService` to add custom export configurations for each format, such as:
- PDF encryption and permissions
- Excel sheet names and formulas
- Image resolution and quality
- HTML styling options

## Dependencies
- Ktor 3.3.2
- JasperReports 6.20.6
- Kotlin 2.2.20
