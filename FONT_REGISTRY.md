# Font Registry API

The Font Registry allows you to dynamically register and manage custom fonts for JasperReports rendering.

## Font Registry Endpoints

### 1. List All Registered Fonts
```bash
GET /api/fonts
```

**Response:**
```json
{
  "fonts": [
    {
      "name": "CustomFont",
      "normalPath": "/path/to/font-regular.ttf",
      "boldPath": "/path/to/font-bold.ttf",
      "italicPath": "/path/to/font-italic.ttf",
      "boldItalicPath": "/path/to/font-bolditalic.ttf",
      "pdfEncoding": "Identity-H",
      "pdfEmbedded": true
    }
  ]
}
```

**Example:**
```bash
curl http://localhost:8080/api/fonts
```

### 2. Register Font from File Paths
Register a font using file paths (for server-side fonts).

```bash
POST /api/fonts/register-path
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "MyCustomFont",
  "normalPath": "/path/to/font-regular.ttf",
  "boldPath": "/path/to/font-bold.ttf",
  "italicPath": "/path/to/font-italic.ttf",
  "boldItalicPath": "/path/to/font-bolditalic.ttf",
  "pdfEncoding": "Identity-H",
  "pdfEmbedded": true
}
```

**Parameters:**
- `name` (required): Font family name
- `normalPath` (required): Path to regular font file
- `boldPath` (optional): Path to bold font file
- `italicPath` (optional): Path to italic font file
- `boldItalicPath` (optional): Path to bold italic font file
- `pdfEncoding` (optional): PDF encoding, default "Identity-H"
- `pdfEmbedded` (optional): Embed font in PDF, default true

**Example:**
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Roboto",
    "normalPath": "/usr/share/fonts/truetype/roboto/Roboto-Regular.ttf",
    "boldPath": "/usr/share/fonts/truetype/roboto/Roboto-Bold.ttf",
    "pdfEncoding": "Identity-H",
    "pdfEmbedded": true
  }'
```

### 3. Register Font by Uploading Files
Upload font files to register them.

```bash
POST /api/fonts/register
Content-Type: multipart/form-data
```

**Form Parameters:**
- `name` (required): Font family name
- `normal` (required): Regular font file (.ttf or .otf)
- `bold` (optional): Bold font file
- `italic` (optional): Italic font file
- `boldItalic` (optional): Bold italic font file
- `pdfEncoding` (optional): PDF encoding
- `pdfEmbedded` (optional): Embed font in PDF

**Example:**
```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=CustomFont" \
  -F "normal=@/path/to/font-regular.ttf" \
  -F "bold=@/path/to/font-bold.ttf" \
  -F "pdfEncoding=Identity-H" \
  -F "pdfEmbedded=true"
```

### 4. Check if Font is Registered
```bash
GET /api/fonts/{fontName}
```

**Response:**
```json
{
  "registered": true,
  "fontInfo": {
    "name": "CustomFont",
    "normalPath": "/path/to/font.ttf",
    ...
  }
}
```

**Example:**
```bash
curl http://localhost:8080/api/fonts/Roboto
```

### 5. Remove a Registered Font
```bash
DELETE /api/fonts/{fontName}
```

**Response:**
```json
{
  "message": "Font removed successfully"
}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/fonts/CustomFont
```

### 6. Clear All Fonts
```bash
DELETE /api/fonts
```

**Response:**
```json
{
  "message": "All fonts cleared"
}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/fonts
```

## Using Custom Fonts in Templates

After registering a font, use it in your JRXML templates:

```xml
<textField>
    <reportElement x="0" y="0" width="555" height="30"/>
    <textElement>
        <font fontName="CustomFont" size="18" isBold="true"/>
    </textElement>
    <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
</textField>
```

## UTF-8 Support

For proper UTF-8 support in PDFs:

1. Use fonts that support Unicode characters
2. Register fonts with `pdfEncoding="Identity-H"`
3. Set `pdfEmbedded=true` to embed fonts in PDF

**Example with multilingual support:**
```bash
# Register a Unicode font
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "NotoSans",
    "normalPath": "/path/to/NotoSans-Regular.ttf",
    "pdfEncoding": "Identity-H",
    "pdfEmbedded": true
  }'

# Use in template
# <font fontName="NotoSans" size="12"/>
```

## Common Font Locations

### macOS:
- System fonts: `/System/Library/Fonts/`
- User fonts: `~/Library/Fonts/`

### Linux:
- System fonts: `/usr/share/fonts/`
- User fonts: `~/.fonts/` or `~/.local/share/fonts/`

### Windows:
- System fonts: `C:\Windows\Fonts\`

## Best Practices

1. **Font File Formats**: Use TrueType (.ttf) or OpenType (.otf) fonts
2. **Unicode Support**: For multilingual content, use fonts like:
   - Noto Sans (Google)
   - DejaVu Sans
   - Arial Unicode MS
   - Roboto
3. **Font Embedding**: Always set `pdfEmbedded=true` for consistent rendering
4. **Encoding**: Use `Identity-H` for Unicode support
5. **Font Variants**: Register all variants (bold, italic) for proper styling

## Troubleshooting

### Font Not Found Error
- Verify the font file path exists
- Check file permissions
- Ensure the font file is a valid TrueType or OpenType font

### Characters Not Displaying
- Use a font that supports the required character set
- Verify `pdfEncoding` is set to "Identity-H"
- Ensure `pdfEmbedded` is true

### Font Not Applied in Report
- Check the font name matches exactly in the template
- Verify the font was successfully registered
- Use the GET endpoint to confirm registration

## Example Workflow

```bash
# 1. Register a custom font
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MyFont",
    "normalPath": "/path/to/font.ttf",
    "pdfEncoding": "Identity-H",
    "pdfEmbedded": true
  }'

# 2. Verify registration
curl http://localhost:8080/api/fonts/MyFont

# 3. Use in report (update your JRXML template)
# <font fontName="MyFont" size="12"/>

# 4. Generate report
curl -X POST "http://localhost:8080/api/reports/render/my-template?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test with Custom Font"}' \
  -o output.pdf

# 5. Remove font when done
curl -X DELETE http://localhost:8080/api/fonts/MyFont
```
