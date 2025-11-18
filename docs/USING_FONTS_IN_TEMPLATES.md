# Using Fonts in JasperReports Templates

Complete guide on how to use registered fonts in your JRXML templates.

## Quick Example

```xml
<textField>
    <reportElement x="0" y="0" width="555" height="30"/>
    <textElement>
        <font fontName="Roboto" size="18" isBold="true"/>
    </textElement>
    <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
</textField>
```

## Step-by-Step Guide

### Step 1: Register a Font

First, register the font using one of these methods:

#### Option A: Quick Register Google Font (Easiest!)
```bash
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto
```

#### Option B: Upload Custom Font
```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=MyCustomFont" \
  -F "normal=@my-font-regular.ttf" \
  -F "bold=@my-font-bold.ttf"
```

#### Option C: Register from File Path
```bash
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MyFont",
    "normalPath": "/path/to/font.ttf"
  }'
```

### Step 2: Verify Font Registration

```bash
curl http://localhost:8080/api/fonts
```

You should see your font in the list:
```json
{
  "fonts": [
    {
      "name": "Roboto",
      "normalPath": "/path/to/Roboto-regular.ttf",
      ...
    }
  ]
}
```

### Step 3: Use Font in JRXML Template

Create or update your JRXML template to use the registered font:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" 
              name="my-report" 
              pageWidth="595" 
              pageHeight="842" 
              columnWidth="555" 
              leftMargin="20" 
              rightMargin="20" 
              topMargin="20" 
              bottomMargin="20">
    
    <!-- Add this property to ignore missing fonts gracefully -->
    <property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
    
    <parameter name="title" class="java.lang.String"/>
    
    <title>
        <band height="50">
            <textField>
                <reportElement x="0" y="0" width="555" height="30"/>
                <textElement>
                    <!-- Use your registered font here -->
                    <font fontName="Roboto" size="18" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
</jasperReport>
```

### Step 4: Generate Report

```bash
curl -X POST "http://localhost:8080/api/reports/render/my-report?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "Report with Custom Font"}' \
  -o output.pdf
```

## Font Element Attributes

### Basic Font Attributes

```xml
<font 
    fontName="Roboto"           <!-- Font family name (must match registered name) -->
    size="12"                   <!-- Font size in points -->
    isBold="true"               <!-- Bold style -->
    isItalic="false"            <!-- Italic style -->
    isUnderline="false"         <!-- Underline -->
    isStrikeThrough="false"     <!-- Strikethrough -->
/>
```

### Advanced Font Attributes (for PDF)

```xml
<font 
    fontName="Roboto"
    size="12"
    pdfFontName="Roboto"        <!-- PDF font name -->
    pdfEncoding="Identity-H"    <!-- Unicode encoding for PDF -->
    isPdfEmbedded="true"        <!-- Embed font in PDF -->
/>
```

## Complete Template Examples

### Example 1: Simple Report with Custom Font

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" 
              name="custom-font-report" 
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
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="10" width="555" height="40"/>
                <textElement textAlignment="Center">
                    <font fontName="Roboto" size="24" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
    
    <detail>
        <band height="100">
            <textField isBlankWhenNull="true">
                <reportElement x="20" y="20" width="515" height="60"/>
                <textElement>
                    <font fontName="Roboto" size="12"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{content}]]></textFieldExpression>
            </textField>
        </band>
    </detail>
</jasperReport>
```

### Example 2: Multilingual Report with Noto Sans

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" 
              name="multilingual-report" 
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
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="10" width="555" height="40"/>
                <textElement textAlignment="Center">
                    <!-- Noto Sans supports many languages -->
                    <font fontName="Noto Sans" size="20" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
    
    <detail>
        <band height="150">
            <textField isBlankWhenNull="true">
                <reportElement x="20" y="20" width="515" height="100"/>
                <textElement>
                    <font fontName="Noto Sans" size="12"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{content}]]></textFieldExpression>
            </textField>
        </band>
    </detail>
</jasperReport>
```

### Example 3: Mixed Fonts in One Report

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" 
              name="mixed-fonts-report" 
              pageWidth="595" 
              pageHeight="842" 
              columnWidth="555" 
              leftMargin="20" 
              rightMargin="20" 
              topMargin="20" 
              bottomMargin="20">
    
    <property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
    
    <parameter name="title" class="java.lang.String"/>
    <parameter name="subtitle" class="java.lang.String"/>
    <parameter name="content" class="java.lang.String"/>
    
    <title>
        <band height="100">
            <!-- Title with Roboto -->
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="10" width="555" height="30"/>
                <textElement textAlignment="Center">
                    <font fontName="Roboto" size="24" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
            
            <!-- Subtitle with Open Sans -->
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="50" width="555" height="20"/>
                <textElement textAlignment="Center">
                    <font fontName="Open Sans" size="14" isItalic="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{subtitle}]]></textFieldExpression>
            </textField>
        </band>
    </title>
    
    <detail>
        <band height="100">
            <!-- Content with SansSerif (system font) -->
            <textField isBlankWhenNull="true">
                <reportElement x="20" y="20" width="515" height="60"/>
                <textElement>
                    <font fontName="SansSerif" size="12"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{content}]]></textFieldExpression>
            </textField>
        </band>
    </detail>
</jasperReport>
```

## Font Styles and Variants

### Using Bold
```xml
<font fontName="Roboto" size="12" isBold="true"/>
```

### Using Italic
```xml
<font fontName="Roboto" size="12" isItalic="true"/>
```

### Using Bold Italic
```xml
<font fontName="Roboto" size="12" isBold="true" isItalic="true"/>
```

**Note:** The font must have the corresponding variant registered (bold, italic, bold-italic) for these to work properly.

## Built-in System Fonts

You can also use these system fonts without registration:

- **SansSerif** - Default sans-serif font
- **Serif** - Default serif font
- **Monospaced** - Default monospace font

```xml
<font fontName="SansSerif" size="12"/>
```

## Best Practices

### 1. Always Add the Ignore Missing Font Property

```xml
<property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
```

This prevents errors if a font is not found and falls back to a default font.

### 2. Use isBlankWhenNull for Text Fields

```xml
<textField isBlankWhenNull="true">
    ...
</textField>
```

This prevents "null" from appearing in your reports.

### 3. Register All Font Variants

If you use bold or italic, register those variants:

```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=MyFont" \
  -F "normal=@font-regular.ttf" \
  -F "bold=@font-bold.ttf" \
  -F "italic=@font-italic.ttf" \
  -F "boldItalic=@font-bolditalic.ttf"
```

### 4. Test with Different Formats

Test your font with multiple output formats:

```bash
# PDF
curl -X POST "http://localhost:8080/api/reports/render/my-report?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test"}' -o test.pdf

# PNG
curl -X POST "http://localhost:8080/api/reports/render/my-report?format=PNG" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test"}' -o test.png

# HTML
curl -X POST "http://localhost:8080/api/reports/render/my-report?format=HTML" \
  -H "Content-Type: application/json" \
  -d '{"title": "Test"}' -o test.html
```

### 5. Use Appropriate Fonts for Content Type

- **Body Text**: Roboto, Open Sans
- **Headings**: Roboto Bold, Open Sans Bold
- **Multilingual**: Noto Sans
- **Code/Data**: Monospaced

## Troubleshooting

### Font Not Appearing

**Check if font is registered:**
```bash
curl http://localhost:8080/api/fonts | grep "MyFont"
```

**Verify font name matches exactly:**
```xml
<!-- Font name must match exactly (case-sensitive) -->
<font fontName="Roboto" size="12"/>  <!-- ✓ Correct -->
<font fontName="roboto" size="12"/>  <!-- ✗ Wrong -->
```

### Characters Not Displaying

**Use a font that supports the character set:**
```bash
# For multilingual content, use Noto Sans
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans"
```

```xml
<font fontName="Noto Sans" size="12"/>
```

### Bold/Italic Not Working

**Register the font variants:**
```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=MyFont" \
  -F "normal=@font-regular.ttf" \
  -F "bold=@font-bold.ttf"
```

## Complete Workflow

```bash
# 1. Register Google Font
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto

# 2. Create template (save as templates/my-report.jrxml)
cat > templates/my-report.jrxml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" 
              name="my-report" pageWidth="595" pageHeight="842" 
              columnWidth="555" leftMargin="20" rightMargin="20" 
              topMargin="20" bottomMargin="20">
    <property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
    <parameter name="title" class="java.lang.String"/>
    <title>
        <band height="50">
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="0" width="555" height="30"/>
                <textElement>
                    <font fontName="Roboto" size="18" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
</jasperReport>
EOF

# 3. Generate report
curl -X POST "http://localhost:8080/api/reports/render/my-report?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "My Custom Font Report"}' \
  -o output.pdf

# 4. Open the report
open output.pdf  # macOS
```

## Quick Reference

| Task | Command |
|------|---------|
| Register Google Font | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Roboto` |
| Upload Custom Font | `curl -X POST http://localhost:8080/api/fonts/register -F "name=Font" -F "normal=@font.ttf"` |
| List Fonts | `curl http://localhost:8080/api/fonts` |
| Use in Template | `<font fontName="Roboto" size="12"/>` |
| Generate Report | `curl -X POST "http://localhost:8080/api/reports/render/template?format=PDF" -d '{"param":"value"}' -o out.pdf` |

## Next Steps

- See [GOOGLE_FONTS.md](GOOGLE_FONTS.md) for more font options
- See [FONT_REGISTRY.md](FONT_REGISTRY.md) for advanced font management
- See [API_USAGE.md](API_USAGE.md) for complete API documentation
