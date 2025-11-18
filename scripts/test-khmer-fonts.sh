#!/bin/bash

# Test script for Khmer Font (Kantumruy Pro) Integration
# This script demonstrates Khmer language support in JasperReports

echo "🇰🇭 Khmer Font Integration Test"
echo "================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
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

# Register Kantumruy Pro font
echo -e "${BLUE}2. Registering Kantumruy Pro (Khmer font) from Google Fonts...${NC}"
echo -e "${CYAN}   This font is specifically designed for Khmer script${NC}"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/google-fonts/quick-register/Kantumruy%20Pro")
echo "$RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE"
echo ""

# Wait for font registration
sleep 3

# Verify font registration
echo -e "${BLUE}3. Verifying Kantumruy Pro registration...${NC}"
curl -s "$BASE_URL/api/fonts/Kantumruy%20Pro" | python3 -m json.tool 2>/dev/null
echo ""

# Generate Khmer PDF report
echo -e "${BLUE}4. Generating Khmer language PDF report...${NC}"
curl -s -X POST "$BASE_URL/api/reports/render/khmer-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "របាយការណ៍ប្រចាំខែ",
    "subtitle": "ខែមករា ឆ្នាំ២០២៤",
    "content": "សូមស្វាគមន៍មកកាន់របាយការណ៍ជាភាសាខ្មែរ។ នេះជាឧទាហរណ៍នៃការប្រើប្រាស់ពុម្ពអក្សរ Kantumruy Pro ក្នុងការបង្កើតឯកសារ PDF។\n\nពុម្ពអក្សរនេះត្រូវបានរចនាឡើងជាពិសេសសម្រាប់អក្សរខ្មែរ ហើយផ្តល់នូវការអានងាយស្រួល និងស្អាតស្អំ។\n\nអ្នកអាចប្រើពុម្ពអក្សរនេះសម្រាប់៖\n• របាយការណ៍ជាផ្លូវការ\n• ឯកសារអាជីវកម្ម\n• វិក្កយបត្រ\n• និងឯកសារផ្សេងៗទៀត",
    "footer": "ឯកសារនេះត្រូវបានបង្កើតដោយប្រើ JasperReports API"
  }' \
  -o khmer-report.pdf

if [ -f "khmer-report.pdf" ]; then
    echo -e "${GREEN}✓ Khmer PDF generated: khmer-report.pdf${NC}"
    ls -lh khmer-report.pdf
else
    echo -e "${YELLOW}✗ Failed to generate Khmer PDF${NC}"
fi
echo ""

# Generate Khmer PNG report
echo -e "${BLUE}5. Generating Khmer language PNG image...${NC}"
curl -s -X POST "$BASE_URL/api/reports/render/khmer-demo?format=PNG" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "រូបភាពជាភាសាខ្មែរ",
    "subtitle": "ឧទាហរណ៍ PNG",
    "content": "នេះជារូបភាព PNG ដែលប្រើពុម្ពអក្សរខ្មែរ Kantumruy Pro។",
    "footer": "បង្កើតដោយ JasperReports"
  }' \
  -o khmer-report.png

if [ -f "khmer-report.png" ]; then
    echo -e "${GREEN}✓ Khmer PNG generated: khmer-report.png${NC}"
    ls -lh khmer-report.png
else
    echo -e "${YELLOW}✗ Failed to generate Khmer PNG${NC}"
fi
echo ""

# Test bilingual report (Khmer + English)
echo -e "${BLUE}6. Generating bilingual report (Khmer + English)...${NC}"
curl -s -X POST "$BASE_URL/api/reports/render/khmer-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "របាយការណ៍ពហុភាសា / Multilingual Report",
    "subtitle": "ខ្មែរ និង អង់គ្លេស / Khmer and English",
    "content": "ភាសាខ្មែរ (Khmer Language):\nសូមស្វាគមន៍! នេះជាឧទាហរណ៍នៃរបាយការណ៍ពហុភាសា។\n\nEnglish:\nWelcome! This is an example of a multilingual report.\n\nKantumruy Pro font supports both Khmer script and Latin characters, making it perfect for bilingual documents.\n\nពុម្ពអក្សរ Kantumruy Pro គាំទ្រទាំងអក្សរខ្មែរ និងអក្សរឡាតាំង ធ្វើឱ្យវាល្អឥតខ្ចោះសម្រាប់ឯកសារពីរភាសា។",
    "footer": "Created with JasperReports API / បង្កើតដោយ JasperReports API"
  }' \
  -o khmer-bilingual-report.pdf

if [ -f "khmer-bilingual-report.pdf" ]; then
    echo -e "${GREEN}✓ Bilingual PDF generated: khmer-bilingual-report.pdf${NC}"
    ls -lh khmer-bilingual-report.pdf
else
    echo -e "${YELLOW}✗ Failed to generate bilingual PDF${NC}"
fi
echo ""

# Optional: Register Noto Sans Khmer as alternative
echo -e "${BLUE}7. Registering Noto Sans Khmer (alternative font)...${NC}"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/google-fonts/quick-register/Noto%20Sans%20Khmer")
echo "$RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE"
echo ""

# Summary
echo -e "${GREEN}========================================"
echo "✓ Khmer Font Integration Test Complete!"
echo "========================================${NC}"
echo ""
echo "Generated files:"
echo "  📄 khmer-report.pdf - Khmer language PDF"
echo "  🖼️  khmer-report.png - Khmer language PNG"
echo "  📄 khmer-bilingual-report.pdf - Bilingual (Khmer + English)"
echo ""
echo "To view the PDFs:"
echo "  macOS:   open khmer-report.pdf"
echo "  Linux:   xdg-open khmer-report.pdf"
echo "  Windows: start khmer-report.pdf"
echo ""
echo "Registered Khmer fonts:"
curl -s "$BASE_URL/api/fonts" | python3 -c "
import sys, json
try:
    fonts = json.load(sys.stdin)['fonts']
    khmer_fonts = [f['name'] for f in fonts if 'Khmer' in f['name'] or 'Kantumruy' in f['name']]
    if khmer_fonts:
        for font in khmer_fonts:
            print(f'  ✓ {font}')
    else:
        print('  (No Khmer fonts found)')
except:
    pass
" 2>/dev/null
echo ""
echo -e "${CYAN}Tip: You can now use 'Kantumruy Pro' in any JRXML template!${NC}"
echo -e "${CYAN}Example: <font fontName=\"Kantumruy Pro\" size=\"12\"/>${NC}"
echo ""
