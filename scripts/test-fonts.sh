#!/bin/bash

# Test script for JasperReports Font Integration
# This script demonstrates the complete workflow

echo "🎨 JasperReports Font Integration Test"
echo "========================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

# Check if service is running
echo -e "${BLUE}1. Checking if service is running...${NC}"
if curl -s "$BASE_URL/api/reports/health" > /dev/null; then
    echo -e "${GREEN}✓ Service is running${NC}"
else
    echo -e "${YELLOW}✗ Service is not running. Please start it with: ./gradlew run${NC}"
    exit 1
fi
echo ""

# Register Roboto font
echo -e "${BLUE}2. Registering Roboto font from Google Fonts...${NC}"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/google-fonts/quick-register/Roboto")
echo "$RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE"
echo ""

# Wait a moment for font to be registered
sleep 2

# List registered fonts
echo -e "${BLUE}3. Listing registered fonts...${NC}"
curl -s "$BASE_URL/api/fonts" | python3 -m json.tool 2>/dev/null | head -20
echo ""

# Generate PDF with custom font
echo -e "${BLUE}4. Generating PDF report with Roboto font...${NC}"
curl -s -X POST "$BASE_URL/api/reports/render/custom-font-example?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Custom Font Report",
    "subtitle": "Using Roboto from Google Fonts",
    "content": "This report demonstrates the use of custom fonts in JasperReports. The Roboto font was downloaded from Google Fonts and registered with a single API call. You can use different font styles like bold, italic, and regular throughout your report.",
    "author": "JasperReports API"
  }' \
  -o custom-font-report.pdf

if [ -f "custom-font-report.pdf" ]; then
    echo -e "${GREEN}✓ PDF generated: custom-font-report.pdf${NC}"
    ls -lh custom-font-report.pdf
else
    echo -e "${YELLOW}✗ Failed to generate PDF${NC}"
fi
echo ""

# Generate PNG with custom font
echo -e "${BLUE}5. Generating PNG image with Roboto font...${NC}"
curl -s -X POST "$BASE_URL/api/reports/render/custom-font-example?format=PNG" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "PNG Report",
    "subtitle": "Image Export with Custom Font",
    "content": "This is a PNG image export using the Roboto font.",
    "author": "JasperReports API"
  }' \
  -o custom-font-report.png

if [ -f "custom-font-report.png" ]; then
    echo -e "${GREEN}✓ PNG generated: custom-font-report.png${NC}"
    ls -lh custom-font-report.png
else
    echo -e "${YELLOW}✗ Failed to generate PNG${NC}"
fi
echo ""

# Test multilingual with Noto Sans
echo -e "${BLUE}6. Registering Noto Sans for multilingual support...${NC}"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/google-fonts/quick-register/Noto%20Sans")
echo "$RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE"
echo ""

sleep 2

# Generate multilingual report
echo -e "${BLUE}7. Generating multilingual report...${NC}"
curl -s -X POST "$BASE_URL/api/reports/render/utf8-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Multilingual Report",
    "content": "English: Hello World\nChinese: 你好世界\nJapanese: こんにちは世界\nKorean: 안녕하세요 세계\nArabic: مرحبا العالم"
  }' \
  -o multilingual-report.pdf

if [ -f "multilingual-report.pdf" ]; then
    echo -e "${GREEN}✓ Multilingual PDF generated: multilingual-report.pdf${NC}"
    ls -lh multilingual-report.pdf
else
    echo -e "${YELLOW}✗ Failed to generate multilingual PDF${NC}"
fi
echo ""

# Summary
echo -e "${GREEN}========================================"
echo "✓ Font Integration Test Complete!"
echo "========================================${NC}"
echo ""
echo "Generated files:"
echo "  - custom-font-report.pdf"
echo "  - custom-font-report.png"
echo "  - multilingual-report.pdf"
echo ""
echo "To view the PDFs:"
echo "  macOS:   open custom-font-report.pdf"
echo "  Linux:   xdg-open custom-font-report.pdf"
echo "  Windows: start custom-font-report.pdf"
echo ""
echo "Registered fonts:"
curl -s "$BASE_URL/api/fonts" | python3 -c "import sys, json; fonts = json.load(sys.stdin)['fonts']; print('\n'.join(['  - ' + f['name'] for f in fonts]))" 2>/dev/null
echo ""
