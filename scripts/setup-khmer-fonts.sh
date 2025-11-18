#!/bin/bash

# Khmer Fonts Setup Script
# This script helps you set up Khmer fonts for JasperReports

echo "🇰🇭 Khmer Fonts Setup for JasperReports"
echo "========================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}⚠️  IMPORTANT INFORMATION${NC}"
echo ""
echo "Google Fonts provides VARIABLE FONTS which don't work with JasperReports."
echo "Khmer text will appear BLANK if you use variable fonts."
echo ""
echo "You need to use STATIC FONT FILES instead."
echo ""

echo -e "${BLUE}📥 Recommended Khmer Fonts (Static):${NC}"
echo ""
echo "1. Khmer OS Fonts (Most Popular)"
echo "   Download: https://www.khmeros.info/download.php"
echo "   - Khmer OS"
echo "   - Khmer OS Battambang"
echo "   - Khmer OS Content"
echo "   - Khmer OS Siemreap"
echo ""
echo "2. Noto Sans Khmer (Static version)"
echo "   Download: https://fonts.google.com/noto/specimen/Noto+Sans+Khmer"
echo "   Click 'Download family' and extract the static fonts"
echo ""
echo "3. Limon Fonts"
echo "   Search: 'Limon Khmer fonts download'"
echo ""

echo -e "${BLUE}📝 Setup Instructions:${NC}"
echo ""
echo "Step 1: Download Khmer OS fonts"
echo "  Visit: https://www.khmeros.info/download.php"
echo "  Download: 'Khmer OS System Fonts'"
echo "  Extract the ZIP file"
echo ""
echo "Step 2: Upload fonts to the service"
echo "  Run these commands (replace paths with your actual font files):"
echo ""
echo -e "${GREEN}  curl -X POST http://localhost:8080/api/fonts/register \\"
echo "    -F \"name=Khmer OS\" \\"
echo "    -F \"normal=@KhmerOS.ttf\"${NC}"
echo ""
echo "Step 3: Update your template"
echo "  Use: <font fontName=\"Khmer OS\" size=\"12\"/>"
echo ""
echo "Step 4: Generate report"
echo -e "${GREEN}  curl -X POST \"http://localhost:8080/api/reports/render/khmer-demo?format=PDF\" \\"
echo "    -H \"Content-Type: application/json\" \\"
echo "    -d '{\"title\": \"របាយការណ៍ប្រចាំខែ\"}' \\"
echo "    -o khmer-report.pdf${NC}"
echo ""

echo -e "${BLUE}🔧 Quick Test:${NC}"
echo ""
echo "If you already have Khmer OS font file:"
echo ""
read -p "Do you have KhmerOS.ttf file? (y/n): " has_font

if [ "$has_font" = "y" ] || [ "$has_font" = "Y" ]; then
    read -p "Enter the path to KhmerOS.ttf: " font_path
    
    if [ -f "$font_path" ]; then
        echo ""
        echo "Uploading font..."
        response=$(curl -s -X POST http://localhost:8080/api/fonts/register \
          -F "name=Khmer OS" \
          -F "normal=@$font_path")
        
        echo "$response"
        
        if echo "$response" | grep -q "successfully"; then
            echo ""
            echo -e "${GREEN}✓ Font uploaded successfully!${NC}"
            echo ""
            echo "Now you can use it in your templates:"
            echo "  <font fontName=\"Khmer OS\" size=\"12\"/>"
        else
            echo ""
            echo -e "${RED}✗ Failed to upload font${NC}"
            echo "Make sure the service is running: ./gradlew run"
        fi
    else
        echo -e "${RED}File not found: $font_path${NC}"
    fi
else
    echo ""
    echo "Please download Khmer OS fonts first:"
    echo "  https://www.khmeros.info/download.php"
fi

echo ""
echo -e "${BLUE}📚 Documentation:${NC}"
echo "  - KHMER_FONTS_TROUBLESHOOTING.md - Detailed troubleshooting guide"
echo "  - KHMER_FONTS.md - Complete Khmer font guide"
echo "  - KHMER_FONTS_LIST.md - List of available fonts"
echo ""

echo -e "${YELLOW}💡 Why Variable Fonts Don't Work:${NC}"
echo ""
echo "Variable fonts contain multiple font weights in a single file using"
echo "special technology. JasperReports doesn't support this format and"
echo "requires traditional static font files (.ttf) with one weight per file."
echo ""
echo "Google Fonts now provides mostly variable fonts, so you need to:"
echo "  1. Download fonts from alternative sources (like Khmer OS)"
echo "  2. Or convert variable fonts to static fonts"
echo "  3. Or extract static instances from variable fonts"
echo ""

echo "=========================================="
echo "For more help, see: KHMER_FONTS_TROUBLESHOOTING.md"
echo ""
