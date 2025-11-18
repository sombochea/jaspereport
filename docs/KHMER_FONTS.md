# Khmer Font Support (ការគាំទ្រពុម្ពអក្សរខ្មែរ)

Complete guide for using Khmer fonts in JasperReports.

## 🇰🇭 Overview

This service supports Khmer language with two excellent fonts:
- **Kantumruy Pro** - Modern, clean design (recommended)
- **Noto Sans Khmer** - Google's universal Khmer font

## Quick Start

### 1. Register Kantumruy Pro Font

```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Kantumruy%20Pro
```

**Response:**
```json
{
  "message": "Font 'Kantumruy Pro' downloaded and registered successfully",
  "fontFamily": "Kantumruy Pro"
}
```

### 2. Use in JRXML Template

```xml
<textField>
    <reportElement x="0" y="0" width="555" height="30"/>
    <textElement>
        <font fontName="Kantumruy Pro" size="18" isBold="true"/>
    </textElement>
    <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
</textField>
```

### 3. Generate Khmer Report

```bash
curl -X POST "http://localhost:8080/api/reports/render/khmer-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "របាយការណ៍ប្រចាំខែ",
    "subtitle": "ខែមករា ឆ្នាំ២០២៤",
    "content": "សូមស្វាគមន៍មកកាន់របាយការណ៍ជាភាសាខ្មែរ។",
    "footer": "បង្កើតដោយ JasperReports"
  }' \
  -o khmer-report.pdf
```

## Available Khmer Fonts

### Kantumruy Pro (Recommended)

**Best for:**
- Official documents (ឯកសារផ្លូវការ)
- Business reports (របាយការណ៍អាជីវកម្ម)
- Invoices (វិក្កយបត្រ)
- Modern designs

**Variants:**
- Regular
- Bold (ដិត)
- Italic (ទ្រេត)
- Bold Italic

**Register:**
```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Kantumruy%20Pro
```

### Noto Sans Khmer

**Best for:**
- Universal compatibility
- Web documents
- Mixed language content

**Variants:**
- Regular
- Bold

**Register:**
```bash
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans%20Khmer"
```

## Complete Template Example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" 
              name="khmer-report" 
              pageWidth="595" 
              pageHeight="842" 
              columnWidth="555" 
              leftMargin="20" 
              rightMargin="20" 
              topMargin="20" 
              bottomMargin="20">
    
    <property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
    
    <parameter name="title" class="java.lang.String"/>
    <parameter name="content" class="java.lang.String"/>
    
    <title>
        <band height="60">
            <!-- Khmer Title -->
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="10" width="555" height="40"/>
                <textElement textAlignment="Center">
                    <font fontName="Kantumruy Pro" size="24" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
    
    <detail>
        <band height="150">
            <!-- Khmer Content -->
            <textField isBlankWhenNull="true">
                <reportElement x="30" y="20" width="495" height="100"/>
                <textElement textAlignment="Justified">
                    <font fontName="Kantumruy Pro" size="12"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{content}]]></textFieldExpression>
            </textField>
        </band>
    </detail>
</jasperReport>
```

## Common Khmer Text Examples

### Titles (ចំណងជើង)
```json
{
  "title": "របាយការណ៍ប្រចាំខែ"
}
```

### Dates (កាលបរិច្ឆេទ)
```json
{
  "date": "ថ្ងៃទី ១៥ ខែមករា ឆ្នាំ២០២៤"
}
```

### Business Terms (ពាក្យអាជីវកម្ម)
```json
{
  "invoice": "វិក្កយបត្រ",
  "receipt": "បង្កាន់ដៃ",
  "report": "របាយការណ៍",
  "total": "សរុប",
  "amount": "ចំនួនទឹកប្រាក់"
}
```

### Greetings (ការស្វាគមន៍)
```json
{
  "welcome": "សូមស្វាគមន៍",
  "thank_you": "អរគុណ",
  "regards": "សូមគោរព"
}
```

## Bilingual Reports (Khmer + English)

```bash
curl -X POST "http://localhost:8080/api/reports/render/khmer-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "របាយការណ៍ / Report",
    "subtitle": "ខ្មែរ និង អង់គ្លេស / Khmer and English",
    "content": "ភាសាខ្មែរ:\nសូមស្វាគមន៍មកកាន់របាយការណ៍នេះ។\n\nEnglish:\nWelcome to this report.",
    "footer": "Created with JasperReports / បង្កើតដោយ JasperReports"
  }' \
  -o bilingual-report.pdf
```

## Font Styles

### Regular (ធម្មតា)
```xml
<font fontName="Kantumruy Pro" size="12"/>
```

### Bold (ដិត)
```xml
<font fontName="Kantumruy Pro" size="12" isBold="true"/>
```

### Italic (ទ្រេត)
```xml
<font fontName="Kantumruy Pro" size="12" isItalic="true"/>
```

### Bold Italic (ដិត និង ទ្រេត)
```xml
<font fontName="Kantumruy Pro" size="12" isBold="true" isItalic="true"/>
```

## Testing

Run the Khmer font test script:

```bash
./test-khmer-fonts.sh
```

This will:
1. ✓ Register Kantumruy Pro font
2. ✓ Generate Khmer PDF report
3. ✓ Generate Khmer PNG image
4. ✓ Generate bilingual report
5. ✓ Register Noto Sans Khmer

## Output Formats

All formats support Khmer text:

```bash
# PDF
curl -X POST ".../khmer-demo?format=PDF" -d '{...}' -o output.pdf

# PNG
curl -X POST ".../khmer-demo?format=PNG" -d '{...}' -o output.png

# DOCX
curl -X POST ".../khmer-demo?format=DOCX" -d '{...}' -o output.docx

# HTML
curl -X POST ".../khmer-demo?format=HTML" -d '{...}' -o output.html

# XLSX
curl -X POST ".../khmer-demo?format=XLSX" -d '{...}' -o output.xlsx
```

## Best Practices

### 1. Use Kantumruy Pro for Professional Documents
```xml
<font fontName="Kantumruy Pro" size="12"/>
```

### 2. Set Proper Text Alignment
```xml
<textElement textAlignment="Justified">
    <font fontName="Kantumruy Pro" size="12"/>
</textElement>
```

### 3. Use Line Spacing for Readability
```xml
<textElement>
    <font fontName="Kantumruy Pro" size="12"/>
    <paragraph lineSpacing="1_1_2"/>
</textElement>
```

### 4. Always Include Font Fallback Property
```xml
<property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
```

## Common Use Cases

### Invoice (វិក្កយបត្រ)
```json
{
  "title": "វិក្កយបត្រ",
  "invoice_number": "លេខវិក្កយបត្រ: INV-001",
  "date": "កាលបរិច្ឆេទ: ១៥/០១/២០២៤",
  "customer": "អតិថិជន: ...",
  "total": "សរុប: $100.00"
}
```

### Receipt (បង្កាន់ដៃ)
```json
{
  "title": "បង្កាន់ដៃ",
  "receipt_number": "លេខបង្កាន់ដៃ: REC-001",
  "amount_received": "ទឹកប្រាក់បានទទួល: $50.00",
  "thank_you": "អរគុណ!"
}
```

### Monthly Report (របាយការណ៍ប្រចាំខែ)
```json
{
  "title": "របាយការណ៍ប្រចាំខែ",
  "month": "ខែមករា ឆ្នាំ២០២៤",
  "summary": "សង្ខេប: ...",
  "details": "ព័ត៌មានលម្អិត: ..."
}
```

## Troubleshooting

### Characters Not Displaying

**Solution:** Ensure Kantumruy Pro is registered
```bash
curl http://localhost:8080/api/fonts | grep "Kantumruy"
```

If not found, register it:
```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Kantumruy%20Pro
```

### Font Name Mismatch

**Correct:**
```xml
<font fontName="Kantumruy Pro" size="12"/>
```

**Incorrect:**
```xml
<font fontName="kantumruy pro" size="12"/>  <!-- Wrong case -->
<font fontName="KantumruyPro" size="12"/>   <!-- Wrong spacing -->
```

### Bold/Italic Not Working

Ensure you registered the font (it includes all variants):
```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Kantumruy%20Pro
```

## Resources

- **Font Source**: [Google Fonts - Kantumruy Pro](https://fonts.google.com/specimen/Kantumruy+Pro)
- **Khmer Unicode**: U+1780 to U+17FF
- **Template**: `templates/khmer-demo.jrxml`
- **Test Script**: `test-khmer-fonts.sh`

## Example Workflow

```bash
# 1. Start the service
./gradlew run

# 2. Register Kantumruy Pro
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Kantumruy%20Pro

# 3. Generate Khmer report
curl -X POST "http://localhost:8080/api/reports/render/khmer-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "របាយការណ៍របស់ខ្ញុំ",
    "subtitle": "ឧទាហរណ៍",
    "content": "នេះជាឧទាហរណ៍នៃរបាយការណ៍ជាភាសាខ្មែរ។",
    "footer": "អរគុណ"
  }' \
  -o my-khmer-report.pdf

# 4. View the report
open my-khmer-report.pdf  # macOS
```

## Support

For more information:
- [USING_FONTS_IN_TEMPLATES.md](USING_FONTS_IN_TEMPLATES.md) - General font usage
- [GOOGLE_FONTS.md](GOOGLE_FONTS.md) - Google Fonts integration
- [API_USAGE.md](API_USAGE.md) - Complete API documentation

---

**សូមរីករាយក្នុងការប្រើប្រាស់! (Enjoy using it!)**
