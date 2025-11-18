# Quick Reference Guide

Fast reference for common tasks in JasperReports API Service.

## 🚀 Getting Started

```bash
# Start server
./gradlew run

# Server runs at
http://localhost:8080
```

## 📊 Generate Reports

### PDF Report
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"templateName": "simple", "format": "PDF", "parameters": {}}' \
  --output report.pdf
```

### Excel Report
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"templateName": "simple", "format": "EXCEL_XLSX", "parameters": {}}' \
  --output report.xlsx
```

### Word Report
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"templateName": "simple", "format": "WORD", "parameters": {}}' \
  --output report.docx
```

## 🎨 Font Management

### List Fonts
```bash
curl http://localhost:8080/api/fonts
```

### Register Font (Path)
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{"name": "MyFont", "normalPath": "/path/to/font.ttf"}'
```

### Register Font (Upload)
```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=MyFont" \
  -F "normal=@/path/to/font.ttf"
```

### Remove Font
```bash
curl -X DELETE http://localhost:8080/api/fonts/MyFont
```

## 🌐 Google Fonts

### Download Font
```bash
curl -X POST http://localhost:8080/api/fonts/google/download \
  -H "Content-Type: application/json" \
  -d '{"fontFamily": "Roboto"}'
```

### List Downloaded
```bash
curl http://localhost:8080/api/fonts/google/downloaded
```

## 🇰🇭 Khmer Fonts

### Register Khmer Font
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{"name": "Hanuman", "normalPath": "/path/to/Hanuman-Regular.ttf"}'
```

### Generate Khmer Report
```bash
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{"templateName": "khmer-demo", "format": "PDF", "parameters": {"title": "របាយការណ៍"}}' \
  --output khmer.pdf
```

## 🧪 Testing

```bash
# Test fonts
./scripts/test-fonts.sh

# Test Khmer fonts
./scripts/test-khmer-fonts.sh

# Test persistence
./scripts/test-persistence.sh

# Setup Khmer fonts
./scripts/setup-khmer-fonts.sh
```

## 🛠️ Build Commands

```bash
# Run server
./gradlew run

# Build project
./gradlew build

# Build fat JAR
./gradlew buildFatJar

# Clean build
./gradlew clean

# Run tests
./gradlew test
```

## 📁 Important Paths

| Path | Description |
|------|-------------|
| `templates/` | JRXML template files |
| `fonts/` | Font files |
| `data/` | Database files |
| `docs/` | Documentation |
| `scripts/` | Utility scripts |
| `src/main/resources/fonts.xml` | Generated font config |

## 🔍 Troubleshooting

### Check Font Status
```bash
curl http://localhost:8080/api/fonts/extensions
```

### Regenerate Extensions
```bash
curl -X POST http://localhost:8080/api/fonts/extensions/regenerate
```

### Check Specific Font
```bash
curl http://localhost:8080/api/fonts/FontName
```

## 📖 Documentation

| Document | Purpose |
|----------|---------|
| [README.md](README.md) | Main readme |
| [docs/QUICK_START.md](docs/QUICK_START.md) | Getting started |
| [docs/API_USAGE.md](docs/API_USAGE.md) | API reference |
| [docs/FONT_PERSISTENCE_GUIDE.md](docs/FONT_PERSISTENCE_GUIDE.md) | Font persistence |
| [docs/KHMER_FONTS.md](docs/KHMER_FONTS.md) | Khmer support |
| [docs/README.md](docs/README.md) | Documentation index |

## 🎯 Common Formats

| Format | Value | Extension |
|--------|-------|-----------|
| PDF | `PDF` | .pdf |
| Excel | `EXCEL_XLSX` | .xlsx |
| Word | `WORD` | .docx |
| HTML | `HTML` | .html |
| CSV | `CSV` | .csv |
| PNG | `PNG` | .png |
| JPEG | `JPEG` | .jpg |

## 💾 Database

```bash
# Database location
data/fonts.mv.db

# Backup database
cp data/fonts.mv.db backups/fonts_$(date +%Y%m%d).mv.db

# Restore database
cp backups/fonts_20241118.mv.db data/fonts.mv.db
```

## 🔧 Configuration

### Server Port
Edit `src/main/resources/application.yaml`:
```yaml
ktor:
  deployment:
    port: 8080
```

### Database Path
Default: `data/fonts.mv.db`

Can be changed in `FontPersistence.kt`

## 📞 Getting Help

1. Check [docs/README.md](docs/README.md)
2. Review [docs/KHMER_FONTS_TROUBLESHOOTING.md](docs/KHMER_FONTS_TROUBLESHOOTING.md)
3. See [docs/API_USAGE.md](docs/API_USAGE.md)
4. Check [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)

## ⚡ Quick Tips

- **Fonts persist automatically** - Register once, use forever
- **Use absolute paths** - For font files to avoid issues
- **Static fonts only** - Variable fonts not supported
- **UTF-8 encoding** - Use `Identity-H` for Unicode
- **Embed fonts** - Set `pdfEmbedded: true` for portability

---

**Need more details?** See the [complete documentation](docs/README.md)
