# Google Fonts Integration

Easily download and register fonts from Google Fonts with automatic caching and management.

## Features

- 🚀 **Quick Register**: One-click registration of popular fonts
- 📥 **Automatic Download**: Fonts are downloaded and cached locally
- 💾 **Smart Caching**: Downloaded fonts are reused, no re-downloading
- 🎨 **Multiple Variants**: Supports regular, bold, italic, and bold-italic
- 🌐 **Popular Fonts**: Built-in support for Roboto, Open Sans, Noto Sans, and more

## Quick Start

### Register a Popular Font (Easiest)

```bash
# Register Roboto font (one command!)
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto

# Register Open Sans
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Open%20Sans"

# Register Noto Sans (great for multilingual support)
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans"
```

**Response:**
```json
{
  "message": "Font 'Roboto' downloaded and registered successfully",
  "fontFamily": "Roboto"
}
```

## API Endpoints

### 1. Quick Register Popular Fonts

The easiest way to register fonts. Works without API key for popular fonts.

```bash
POST /api/google-fonts/quick-register/{fontName}
```

**Supported Popular Fonts:**
- Roboto
- Open Sans
- Noto Sans
- Lato
- **Kantumruy Pro** (Khmer/ខ្មែរ)
- **Noto Sans Khmer** (Khmer/ខ្មែរ)

**Example:**
```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto
```

### 2. Download and Register with Custom Options

Download a font with custom variants and API key.

```bash
POST /api/google-fonts/download
Content-Type: application/json
```

**Request Body:**
```json
{
  "fontFamily": "Roboto",
  "apiKey": "YOUR_GOOGLE_FONTS_API_KEY",
  "variants": ["regular", "700", "italic", "700italic"]
}
```

**Parameters:**
- `fontFamily` (required): Font family name
- `apiKey` (optional): Google Fonts API key (for accessing full catalog)
- `variants` (optional): List of variants to download
  - Default: `["regular", "700", "italic", "700italic"]`
  - Common variants: `regular`, `700` (bold), `italic`, `700italic` (bold italic)

**Example:**
```bash
curl -X POST http://localhost:8080/api/google-fonts/download \
  -H "Content-Type: application/json" \
  -d '{
    "fontFamily": "Roboto",
    "variants": ["regular", "700"]
  }'
```

### 3. List Downloaded Fonts

See which Google Fonts have been downloaded and cached.

```bash
GET /api/google-fonts/downloaded
```

**Response:**
```json
{
  "fonts": [
    "Roboto",
    "Open-Sans",
    "Noto-Sans"
  ]
}
```

**Example:**
```bash
curl http://localhost:8080/api/google-fonts/downloaded
```

### 4. Clear Font Cache

Remove all downloaded Google Fonts to free up space.

```bash
DELETE /api/google-fonts/cache
```

**Response:**
```json
{
  "message": "Google Fonts cache cleared"
}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/google-fonts/cache
```

## Using Downloaded Fonts in Reports

After downloading a font, use it in your JRXML templates:

```xml
<textField>
    <reportElement x="0" y="0" width="555" height="30"/>
    <textElement>
        <font fontName="Roboto" size="18" isBold="true"/>
    </textElement>
    <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
</textField>
```

## Complete Workflow Example

```bash
# 1. Quick register Roboto font
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto

# 2. Verify it's registered
curl http://localhost:8080/api/fonts | grep Roboto

# 3. Create a template using Roboto (update your JRXML)
# <font fontName="Roboto" size="12"/>

# 4. Generate a report with the font
curl -X POST "http://localhost:8080/api/reports/render/my-template?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "Report with Roboto Font"}' \
  -o report.pdf

# 5. View downloaded fonts
curl http://localhost:8080/api/google-fonts/downloaded
```

## Multilingual Support

For multilingual reports, use Noto Sans which supports many languages:

```bash
# Register Noto Sans
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans"

# Use in template
# <font fontName="Noto Sans" size="12"/>

# Generate multilingual report
curl -X POST "http://localhost:8080/api/reports/render/utf8-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Multilingual Report",
    "content": "English: Hello\nChinese: 你好\nJapanese: こんにちは\nKorean: 안녕하세요"
  }' \
  -o multilingual.pdf
```

## Font Storage

Downloaded fonts are stored in:
```
fonts/google-fonts/{FontName}/
  ├── {FontName}-regular.ttf
  ├── {FontName}-700.ttf
  ├── {FontName}-italic.ttf
  └── {FontName}-700italic.ttf
```

## Google Fonts API Key (Optional)

For access to the full Google Fonts catalog (1000+ fonts):

1. Get an API key from [Google Cloud Console](https://console.cloud.google.com/)
2. Enable the Google Fonts API
3. Set environment variable:
   ```bash
   export GOOGLE_FONTS_API_KEY=your_api_key_here
   ```
4. Or pass it in the request:
   ```bash
   curl -X POST http://localhost:8080/api/google-fonts/download \
     -H "Content-Type: application/json" \
     -d '{
       "fontFamily": "Montserrat",
       "apiKey": "your_api_key_here"
     }'
   ```

**Note:** Popular fonts work without an API key using direct GitHub links.

## Recommended Fonts

### For Body Text:
- **Roboto**: Modern, clean, highly readable
- **Open Sans**: Friendly, neutral, professional
- **Noto Sans**: Excellent multilingual support

### For Headings:
- **Roboto**: Bold variants work great
- **Open Sans**: Strong presence

### For Multilingual Content:
- **Noto Sans**: Best Unicode coverage
- Supports: Latin, Cyrillic, Greek, Arabic, Hebrew, Chinese, Japanese, Korean, and more

### For Khmer Language (ភាសាខ្មែរ):
- **Kantumruy Pro**: Modern, professional Khmer font (recommended)
- **Noto Sans Khmer**: Universal Khmer font
- See [KHMER_FONTS.md](KHMER_FONTS.md) for complete Khmer support guide

## Troubleshooting

### Font Download Failed
- Check your internet connection
- Verify the font name is correct
- Try using the quick-register endpoint for popular fonts

### Font Not Appearing in Report
- Verify the font is registered: `curl http://localhost:8080/api/fonts`
- Check the font name matches exactly in your JRXML template
- Ensure the font was downloaded successfully

### Characters Not Displaying
- Use Noto Sans for multilingual content
- Verify the font supports the required character set
- Check that `pdfEmbedded` is true in the font registration

## Cache Management

Fonts are cached locally to avoid re-downloading:

```bash
# Check cache size
du -sh fonts/google-fonts/

# Clear cache to free space
curl -X DELETE http://localhost:8080/api/google-fonts/cache

# Re-register fonts as needed
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto
```

## Best Practices

1. **Use Quick Register**: For popular fonts, use the quick-register endpoint
2. **Cache Fonts**: Downloaded fonts are cached automatically
3. **Multilingual**: Use Noto Sans for international content
4. **Variants**: Download only the variants you need to save space
5. **Testing**: Test fonts with sample reports before production use

## Integration with Font Registry

Google Fonts automatically register with the Font Registry:

```bash
# Download from Google Fonts
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto

# Font is now available in Font Registry
curl http://localhost:8080/api/fonts

# Can be managed like any other font
curl -X DELETE http://localhost:8080/api/fonts/Roboto
```
