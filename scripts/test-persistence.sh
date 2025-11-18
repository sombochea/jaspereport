#!/bin/bash

echo "=== Testing Font Persistence ==="
echo ""

echo "1. Checking registered fonts..."
curl -s http://localhost:8080/api/fonts | jq '.fonts | length'
echo ""

echo "2. Listing font names..."
curl -s http://localhost:8080/api/fonts | jq '.fonts[].name'
echo ""

echo "3. Checking font extensions..."
curl -s http://localhost:8080/api/fonts/extensions | jq '{hasExtensions, registeredFontsCount}'
echo ""

echo "4. Generating simple report..."
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "simple",
    "format": "PDF",
    "parameters": {}
  }' \
  --output test-simple.pdf

if [ -s test-simple.pdf ]; then
    echo "✓ Simple report generated successfully ($(ls -lh test-simple.pdf | awk '{print $5}'))"
else
    echo "✗ Simple report generation failed"
fi
echo ""

echo "5. Testing font in report..."
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "khmer-demo",
    "format": "PDF",
    "parameters": {
      "title": "Font Persistence Test"
    }
  }' \
  --output test-khmer-persistence.pdf

if [ -s test-khmer-persistence.pdf ]; then
    echo "✓ Khmer report generated successfully ($(ls -lh test-khmer-persistence.pdf | awk '{print $5}'))"
else
    echo "✗ Khmer report generation failed"
fi

echo ""
echo "=== Test Complete ==="
