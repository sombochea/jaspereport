# Khmer Fonts Troubleshooting

## Issue: Khmer Text Shows as Blank

If Khmer text appears blank in your reports, it's because the font files are variable fonts which don't work well with JasperReports. Here are the solutions:

## Solution 1: Use Noto Sans Khmer (Recommended)

Download and register Noto Sans Khmer manually:

### Step 1: Download Static Font Files

Download these static font files:
- [Noto Sans Khmer Regular](https://fonts.google.com/download?family=Noto%20Sans%20Khmer)
- Extract the ZIP file

### Step 2: Upload to Service

```bash
# Upload the regular font
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Noto Sans Khmer" \
  -F "normal=@NotoSansKhmer-Regular.ttf" \
  -F "bold=@NotoSansKhmer-Bold.ttf"
```

### Step 3: Use in Template

```xml
<font fontName="Noto Sans Khmer" size="12"/>
```

## Solution 2: Use Khmer OS Fonts (Best Compatibility)

Khmer OS fonts are specifically designed for Khmer script and work perfectly with JasperReports.

### Download Khmer OS Fonts

1. Visit: https://www.khmeros.info/download.php
2. Download: Khmer OS System Fonts
3. Extract the fonts

### Register Khmer OS Fonts

```bash
# Khmer OS (most popular)
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS" \
  -F "normal=@KhmerOS.ttf"

# Khmer OS Battambang
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Battambang" \
  -F "normal=@KhmerOS_battambang.ttf"

# Khmer OS Bokor
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Bokor" \
  -F "normal=@KhmerOS_bokor.ttf"

# Khmer OS Content
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Content" \
  -F "normal=@KhmerOS_content.ttf"

# Khmer OS Fasthand
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Fasthand" \
  -F "normal=@KhmerOS_fasthand.ttf"

# Khmer OS Freehand
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Freehand" \
  -F "normal=@KhmerOS_freehand.ttf"

# Khmer OS Metal Chrieng
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Metal Chrieng" \
  -F "normal=@KhmerOS_metalchrieng.ttf"

# Khmer OS Muol
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Muol" \
  -F "normal=@KhmerOS_muol.ttf"

# Khmer OS Siemreap
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS Siemreap" \
  -F "normal=@KhmerOS_siemreap.ttf"
```

### Use in Template

```xml
<font fontName="Khmer OS" size="12"/>
```

## Solution 3: Use Limon Fonts

Limon fonts are another excellent option for Khmer script.

### Download Limon Fonts

1. Search for "Limon Khmer fonts" online
2. Download the font files
3. Upload to the service

```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Limon S1" \
  -F "normal=@limons1.ttf"
```

## Solution 4: Convert Variable Fonts to Static

If you want to use Google Fonts, you need to convert them to static fonts first.

### Using FontForge (Advanced)

```bash
# Install FontForge
brew install fontforge  # macOS
# or
sudo apt-get install fontforge  # Linux

# Convert variable font to static
fontforge -lang=py -c 'import fontforge; f=fontforge.open("Hanuman[wght].ttf"); f.generate("Hanuman-Regular.ttf")'
```

### Using Online Tools

1. Visit: https://everythingfonts.com/ttf-to-ttf
2. Upload your variable font
3. Download the converted static font
4. Upload to the service

## Complete Working Example

### 1. Download Khmer OS

```bash
# Download from https://www.khmeros.info/download.php
wget https://sourceforge.net/projects/khmer/files/Khmer%20OS%20Fonts/5.0/KhmerOSfonts.zip
unzip KhmerOSfonts.zip
```

### 2. Register the Font

```bash
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS" \
  -F "normal=@KhmerOS.ttf"
```

### 3. Update Your Template

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport ...>
    <property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
    
    <parameter name="title" class="java.lang.String"/>
    
    <title>
        <band height="50">
            <textField isBlankWhenNull="true">
                <reportElement x="0" y="0" width="555" height="30"/>
                <textElement>
                    <font fontName="Khmer OS" size="18"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
</jasperReport>
```

### 4. Generate Report

```bash
curl -X POST "http://localhost:8080/api/reports/render/your-template?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "របាយការណ៍ប្រចាំខែ"}' \
  -o khmer-report.pdf
```

## Verification Steps

### 1. Check Font Registration

```bash
curl http://localhost:8080/api/fonts | grep "Khmer OS"
```

### 2. Test with Simple Text

```bash
curl -X POST "http://localhost:8080/api/reports/render/simple?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{"title": "សួស្តី"}' \
  -o test.pdf
```

### 3. Check PDF

```bash
# macOS
open test.pdf

# Linux
xdg-open test.pdf

# Check if Khmer text is visible
```

## Common Issues

### Issue: Font file not found
**Solution:** Make sure the font file path is correct when uploading

### Issue: Font name mismatch
**Solution:** Use the exact font name in your template that you used when registering

### Issue: Still showing blank
**Solution:** 
1. Verify the font file is a valid TrueType font
2. Check that the font supports Khmer Unicode range (U+1780 to U+17FF)
3. Try a different Khmer font

## Recommended Fonts (Tested & Working)

### Best for JasperReports:
1. **Khmer OS** - Most reliable, widely used
2. **Khmer OS Battambang** - Modern, clean
3. **Khmer OS Content** - Great for body text
4. **Limon S1** - Traditional style

### Where to Get Them:
- **Khmer OS Fonts**: https://www.khmeros.info/
- **Limon Fonts**: Search "Limon Khmer fonts download"
- **Noto Sans Khmer (static)**: https://fonts.google.com/noto/specimen/Noto+Sans+Khmer

## Quick Fix Script

```bash
#!/bin/bash
# Download and register Khmer OS font

# Download
wget -O khmer-fonts.zip https://sourceforge.net/projects/khmer/files/Khmer%20OS%20Fonts/5.0/KhmerOSfonts.zip/download
unzip khmer-fonts.zip

# Register main font
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS" \
  -F "normal=@KhmerOS.ttf"

# Verify
curl http://localhost:8080/api/fonts | grep "Khmer OS"

echo "Khmer OS font registered successfully!"
```

## Alternative: Use System Fonts

If you have Khmer fonts installed on your system:

```bash
# macOS
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Khmer MN",
    "normalPath": "/System/Library/Fonts/KhmerMN.ttc"
  }'

# Linux (if Khmer fonts are installed)
curl -X POST http://localhost:8080/api/fonts/register-path \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Khmer OS",
    "normalPath": "/usr/share/fonts/truetype/khmeros/KhmerOS.ttf"
  }'
```

## Need Help?

If you're still having issues:

1. Check the font file is valid: `file your-font.ttf`
2. Verify it's a TrueType font (not variable font)
3. Test with a simple English text first
4. Check the server logs for errors
5. Try a different Khmer font

## Summary

**The key issue:** Google Fonts provides variable fonts which don't work with JasperReports.

**The solution:** Use static font files from:
- Khmer OS (recommended)
- Limon fonts
- Noto Sans Khmer (download and extract static files)

**Quick command:**
```bash
# Upload your downloaded Khmer OS font
curl -X POST http://localhost:8080/api/fonts/register \
  -F "name=Khmer OS" \
  -F "normal=@KhmerOS.ttf"
```

Then use `<font fontName="Khmer OS" size="12"/>` in your templates.
