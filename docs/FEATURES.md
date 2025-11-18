# Features Overview

## 🚀 Report Generation

### Multiple Output Formats
- **PDF** - High-quality portable documents
- **Excel (XLSX)** - Spreadsheets with data
- **Word (DOCX)** - Editable documents
- **HTML** - Web-ready reports
- **Images (PNG, JPEG)** - Visual exports
- **CSV, XML, RTF, ODT, ODS** - Additional formats

### Template Options
- Upload JRXML templates via API
- Store templates on server
- Dynamic parameters at runtime
- Multi-page reports supported

## 🎨 Google Fonts Integration

### One-Command Font Registration
```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto
```

### Features
- ✅ Automatic download and caching
- ✅ No API key needed for popular fonts
- ✅ Multiple font variants (regular, bold, italic)
- ✅ 1000+ fonts available with API key
- ✅ Smart caching - download once, use forever

## 💾 Font Registry

### Dynamic Font Management
- Register fonts from file paths
- Upload custom font files
- Remove fonts when not needed
- List all registered fonts

### Font Upload
```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=CustomFont" \
  -F "normal=@font.ttf"
```

## 🌐 UTF-8 & Multilingual Support

Full Unicode support for:
- Chinese (中文)
- Japanese (日本語)
- Korean (한국어)
- Arabic (العربية)
- Cyrillic (Русский)
- And more!

## 💡 Why Choose This Service?

1. **Easy to Use** - Start in minutes
2. **Google Fonts** - 1000+ fonts with one command
3. **Multilingual** - Full UTF-8 support
4. **Flexible** - Multiple formats
5. **Modern** - Kotlin + Ktor
6. **Well-Documented** - Complete guides
7. **Production-Ready** - Tested and reliable
