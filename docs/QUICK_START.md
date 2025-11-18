# Quick Start Guide

Get started with JasperReports API Service in 5 minutes!

## 1. Start the Service

```bash
./gradlew run
```

The service will start on `http://localhost:8080`

## 2. Register a Google Font (Optional but Recommended)

```bash
# Register Roboto font for better rendering
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto
```

## 3. Generate Your First Report

```bash
# Generate a PDF report
curl -X POST "http://localhost:8080/api/reports/render/simple?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "My First Report"}' \
  -o my-report.pdf

# Open the PDF
open my-report.pdf  # macOS
# xdg-open my-report.pdf  # Linux
# start my-report.pdf  # Windows
```

## 4. Try Different Formats

```bash
# Excel
curl -X POST "http://localhost:8080/api/reports/render/simple?format=XLSX" \
  -H "Content-Type: application/json" \
  -d '{"title": "Excel Report"}' \
  -o report.xlsx

# PNG Image
curl -X POST "http://localhost:8080/api/reports/render/simple?format=PNG" \
  -H "Content-Type: application/json" \
  -d '{"title": "Image Report"}' \
  -o report.png

# HTML
curl -X POST "http://localhost:8080/api/reports/render/simple?format=HTML" \
  -H "Content-Type: application/json" \
  -d '{"title": "Web Report"}' \
  -o report.html
```

## 5. Upload Your Own Template

```bash
# Upload and render a custom JRXML template
curl -X POST "http://localhost:8080/api/reports/render" \
  -F "template=@my-template.jrxml" \
  -F "format=PDF" \
  -F "title=Custom Report" \
  -F "author=Your Name" \
  -o custom-report.pdf
```

## Common Use Cases

### Multilingual Reports

```bash
# 1. Register Noto Sans (best for multilingual)
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans"

# 2. Generate multilingual report
curl -X POST "http://localhost:8080/api/reports/render/utf8-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Global Report",
    "content": "English: Hello\nChinese: 你好\nArabic: مرحبا\nJapanese: こんにちは"
  }' \
  -o multilingual.pdf
```

### Custom Fonts

```bash
# Upload your own font
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=MyFont" \
  -F "normal=@my-font.ttf"

# Use in template: <font fontName="MyFont" size="12"/>
```

### Batch Reports

```bash
# Generate multiple reports
for i in {1..5}; do
  curl -X POST "http://localhost:8080/api/reports/render/simple?format=PDF" \
    -H "Content-Type: application/json" \
    -d "{\"title\": \"Report $i\"}" \
    -o "report-$i.pdf"
done
```

## API Endpoints Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/reports/health` | GET | Health check |
| `/api/reports/render` | POST | Upload and render template |
| `/api/reports/render/{name}` | POST | Render server template |
| `/api/google-fonts/quick-register/{font}` | POST | Quick register Google Font |
| `/api/google-fonts/downloaded` | GET | List downloaded fonts |
| `/api/fonts` | GET | List registered fonts |
| `/api/fonts/register` | POST | Upload custom font |

## Supported Formats

- **PDF** - Portable Document Format
- **XLSX** - Microsoft Excel
- **DOCX** - Microsoft Word
- **HTML** - Web page
- **PNG** - Image
- **JPEG** - Image
- **CSV** - Comma-separated values
- **XML** - XML format
- **RTF** - Rich Text Format
- **ODT** - OpenDocument Text
- **ODS** - OpenDocument Spreadsheet

## Next Steps

- 📖 Read [API_USAGE.md](API_USAGE.md) for detailed API documentation
- 🎨 Explore [GOOGLE_FONTS.md](GOOGLE_FONTS.md) for font options
- 🔧 Check [FONT_REGISTRY.md](FONT_REGISTRY.md) for advanced font management

## Troubleshooting

### Service won't start
```bash
# Check if port 8080 is in use
lsof -i :8080

# Kill the process if needed
kill -9 $(lsof -ti:8080)
```

### Font not working
```bash
# Verify font is registered
curl http://localhost:8080/api/fonts

# Re-register if needed
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto
```

### Report generation fails
```bash
# Check the error message
curl -X POST "http://localhost:8080/api/reports/render/simple?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test"}'

# Verify template exists
ls templates/
```

## Tips

1. **Use Google Fonts**: Quick and easy with one command
2. **Cache Fonts**: Downloaded fonts are cached automatically
3. **Test First**: Use the simple template to test before creating custom ones
4. **Check Health**: Use `/api/reports/health` to verify service is running
5. **Format Parameter**: Always specify the format in the URL or form data

## Example Templates

The service includes these templates:
- `simple` - Basic single-field template
- `utf8-demo` - Multilingual demonstration
- `roboto-demo` - Google Font example

Create your own templates in the `templates/` directory!

## Need Help?

- Check the error message in the response
- Verify your JRXML template is valid
- Ensure fonts are registered before use
- Test with the included sample templates first
