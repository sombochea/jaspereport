# JasperReports API Service - Documentation

Complete documentation for the JasperReports API Service.

## 📖 Table of Contents

### Getting Started
1. **[Quick Start Guide](QUICK_START.md)** - Get up and running in 5 minutes
2. **[Features Overview](FEATURES.md)** - All features and capabilities
3. **[API Usage](API_USAGE.md)** - Complete API reference with examples

### Font Management
4. **[Font Persistence Guide](FONT_PERSISTENCE_GUIDE.md)** - Automatic font persistence system ⭐
5. **[Font Registry](FONT_REGISTRY.md)** - Dynamic font management
6. **[Font Workflow](FONT_WORKFLOW.md)** - Visual workflow guide
7. **[Font Extensions](FONT_EXTENSIONS.md)** - Automatic extension generation
8. **[Google Fonts Integration](GOOGLE_FONTS.md)** - Easy font downloads

### Template Development
9. **[Template Management](TEMPLATE_MANAGEMENT.md)** - Complete CRUD API for templates ⭐
10. **[Using Fonts in Templates](USING_FONTS_IN_TEMPLATES.md)** - JRXML font configuration

### Khmer Language Support
11. **[Khmer Fonts Guide](KHMER_FONTS.md)** - Complete Khmer support guide 🇰🇭
12. **[Khmer Fonts List](KHMER_FONTS_LIST.md)** - 20+ available Khmer fonts
13. **[Khmer Troubleshooting](KHMER_FONTS_TROUBLESHOOTING.md)** - Common issues and solutions

### Complete Reference
14. **[Project Summary](PROJECT_SUMMARY.md)** - Comprehensive project documentation

---

## 🚀 Quick Navigation

### I want to...

#### Get Started
- **Generate my first report** → [Quick Start Guide](QUICK_START.md)
- **Understand all features** → [Features Overview](FEATURES.md)
- **Learn the API** → [API Usage](API_USAGE.md)

#### Work with Fonts
- **Register custom fonts** → [Font Registry](FONT_REGISTRY.md)
- **Use Google Fonts** → [Google Fonts Integration](GOOGLE_FONTS.md)
- **Understand font persistence** → [Font Persistence Guide](FONT_PERSISTENCE_GUIDE.md)
- **Use fonts in templates** → [Using Fonts in Templates](USING_FONTS_IN_TEMPLATES.md)

#### Support Khmer Language
- **Setup Khmer fonts** → [Khmer Fonts Guide](KHMER_FONTS.md)
- **Find Khmer fonts** → [Khmer Fonts List](KHMER_FONTS_LIST.md)
- **Fix Khmer issues** → [Khmer Troubleshooting](KHMER_FONTS_TROUBLESHOOTING.md)

#### Understand the System
- **See the big picture** → [Project Summary](PROJECT_SUMMARY.md)
- **Understand font workflow** → [Font Workflow](FONT_WORKFLOW.md)
- **Learn about extensions** → [Font Extensions](FONT_EXTENSIONS.md)

---

## 📚 Documentation by Topic

### Core Functionality

#### Report Generation
- Supported formats: PDF, Excel, Word, HTML, Images, and more
- Dynamic parameter passing
- Template compilation and caching
- See: [API Usage](API_USAGE.md)

#### Font Management
- Dynamic font registration
- Automatic persistence across restarts
- Google Fonts integration
- Font validation and verification
- See: [Font Persistence Guide](FONT_PERSISTENCE_GUIDE.md)

#### Multilingual Support
- Full Unicode support
- Khmer language support
- CJK (Chinese, Japanese, Korean) support
- Right-to-left language support (Arabic, Hebrew)
- See: [Khmer Fonts Guide](KHMER_FONTS.md)

### Advanced Topics

#### Font Persistence System
The font persistence system automatically saves registered fonts to an H2 database and loads them on startup. This ensures fonts remain available across server restarts.

**Key Features:**
- Automatic database persistence
- Font validation on load
- Dynamic extension generation
- Zero-configuration startup

**Learn more:** [Font Persistence Guide](FONT_PERSISTENCE_GUIDE.md)

#### Font Extension Generation
JasperReports requires font configuration files (`fonts.xml` and `jasperreports_extension.properties`). This system automatically generates these files whenever fonts are registered or removed.

**Benefits:**
- No manual configuration needed
- Always in sync with registered fonts
- Automatic regeneration on changes

**Learn more:** [Font Extensions](FONT_EXTENSIONS.md)

#### Google Fonts Integration
Download and register Google Fonts with a single API call. The system handles downloading, validation, and registration automatically.

**Supported:**
- 1000+ Google Fonts
- Multiple font variants
- Automatic registration
- Persistent storage

**Learn more:** [Google Fonts Integration](GOOGLE_FONTS.md)

---

## 🎯 Common Use Cases

### Use Case 1: Generate PDF Reports
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "sample-report",
    "format": "PDF",
    "parameters": {"title": "Monthly Report"}
  }' \
  --output report.pdf
```
**See:** [API Usage](API_USAGE.md)

### Use Case 2: Register Custom Font
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MyFont",
    "normalPath": "/path/to/font.ttf"
  }'
```
**See:** [Font Registry](FONT_REGISTRY.md)

### Use Case 3: Use Google Fonts
```bash
curl -X POST http://localhost:8080/api/fonts/google/download \
  -H "Content-Type: application/json" \
  -d '{"fontFamily": "Roboto"}'
```
**See:** [Google Fonts Integration](GOOGLE_FONTS.md)

### Use Case 4: Generate Khmer Report
```bash
# 1. Register Khmer font
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Hanuman",
    "normalPath": "/path/to/Hanuman-Regular.ttf"
  }'

# 2. Generate report
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "khmer-demo",
    "format": "PDF",
    "parameters": {"title": "របាយការណ៍ប្រចាំខែ"}
  }' \
  --output khmer-report.pdf
```
**See:** [Khmer Fonts Guide](KHMER_FONTS.md)

---

## 🔍 Troubleshooting

### Common Issues

#### Fonts not appearing in PDF
→ See [Khmer Troubleshooting](KHMER_FONTS_TROUBLESHOOTING.md)

#### Fonts not persisting across restarts
→ See [Font Persistence Guide](FONT_PERSISTENCE_GUIDE.md)

#### Variable fonts not working
→ See [Font Registry](FONT_REGISTRY.md) - Only static fonts are supported

#### Google Fonts download fails
→ See [Google Fonts Integration](GOOGLE_FONTS.md)

---

## 📞 Getting Help

1. **Check the documentation** - Most questions are answered here
2. **Review troubleshooting guides** - Common issues and solutions
3. **Check the API reference** - Complete API documentation
4. **Open an issue** - For bugs or feature requests

---

## 🔄 Documentation Updates

This documentation is continuously updated. Last major update: November 2024

### Recent Additions
- Font Persistence Guide (NEW)
- Project Summary (UPDATED)
- Khmer Fonts Troubleshooting (ENHANCED)

---

**Need help?** Start with the [Quick Start Guide](QUICK_START.md) or jump to the [API Usage](API_USAGE.md) for detailed examples.
