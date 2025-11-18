#!/bin/bash

echo "=== Testing Template CRUD Operations ==="
echo ""

BASE_URL="http://localhost:8080"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Test 1: List all templates
echo "1. Listing all templates..."
RESPONSE=$(curl -s "$BASE_URL/api/templates")
COUNT=$(echo $RESPONSE | jq -r '.total')
echo "   Found $COUNT templates"
echo ""

# Test 2: Create a new template
echo "2. Creating a new template..."
TEMPLATE_CONTENT='<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd"
              name="test-template" pageWidth="595" pageHeight="842">
    <parameter name="title" class="java.lang.String"/>
    <title>
        <band height="50">
            <textField>
                <reportElement x="0" y="0" width="555" height="30"/>
                <textElement textAlignment="Center">
                    <font size="20" isBold="true"/>
                </textElement>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
</jasperReport>'

CREATE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/templates" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"test-template\",
    \"content\": $(echo "$TEMPLATE_CONTENT" | jq -Rs .),
    \"description\": \"Test template for CRUD operations\",
    \"category\": \"test\"
  }")

if echo "$CREATE_RESPONSE" | jq -e '.message' > /dev/null; then
    echo -e "   ${GREEN}✓${NC} Template created successfully"
else
    echo -e "   ${RED}✗${NC} Failed to create template"
    echo "   Response: $CREATE_RESPONSE"
fi
echo ""

# Test 3: Get template by name
echo "3. Getting template by name..."
GET_RESPONSE=$(curl -s "$BASE_URL/api/templates/test-template")
TEMPLATE_NAME=$(echo $GET_RESPONSE | jq -r '.name')
if [ "$TEMPLATE_NAME" = "test-template" ]; then
    echo -e "   ${GREEN}✓${NC} Template retrieved successfully"
    echo "   Description: $(echo $GET_RESPONSE | jq -r '.description')"
    echo "   Category: $(echo $GET_RESPONSE | jq -r '.category')"
else
    echo -e "   ${RED}✗${NC} Failed to retrieve template"
fi
echo ""

# Test 4: Get template content
echo "4. Getting template content..."
CONTENT_RESPONSE=$(curl -s "$BASE_URL/api/templates/test-template/content")
if echo "$CONTENT_RESPONSE" | grep -q "jasperReport"; then
    echo -e "   ${GREEN}✓${NC} Template content retrieved successfully"
    echo "   Content length: ${#CONTENT_RESPONSE} bytes"
else
    echo -e "   ${RED}✗${NC} Failed to retrieve template content"
fi
echo ""

# Test 5: Update template
echo "5. Updating template..."
UPDATE_RESPONSE=$(curl -s -X PUT "$BASE_URL/api/templates/test-template" \
  -H "Content-Type: application/json" \
  -d "{
    \"content\": $(echo "$TEMPLATE_CONTENT" | jq -Rs .),
    \"description\": \"Updated test template\",
    \"category\": \"test-updated\"
  }")

if echo "$UPDATE_RESPONSE" | jq -e '.message' > /dev/null; then
    echo -e "   ${GREEN}✓${NC} Template updated successfully"
else
    echo -e "   ${RED}✗${NC} Failed to update template"
fi
echo ""

# Test 6: List templates by category
echo "6. Listing templates by category..."
CATEGORY_RESPONSE=$(curl -s "$BASE_URL/api/templates/category/test-updated")
CATEGORY_COUNT=$(echo $CATEGORY_RESPONSE | jq -r '.total')
echo "   Found $CATEGORY_COUNT templates in category 'test-updated'"
echo ""

# Test 7: Get all categories
echo "7. Getting all categories..."
CATEGORIES_RESPONSE=$(curl -s "$BASE_URL/api/templates/categories/list")
CATEGORIES=$(echo $CATEGORIES_RESPONSE | jq -r '.categories[]' | tr '\n' ', ')
echo "   Categories: $CATEGORIES"
echo ""

# Test 8: Get statistics
echo "8. Getting template statistics..."
STATS_RESPONSE=$(curl -s "$BASE_URL/api/templates/statistics")
TOTAL=$(echo $STATS_RESPONSE | jq -r '.totalTemplates')
CATEGORIES_COUNT=$(echo $STATS_RESPONSE | jq -r '.categories')
echo "   Total templates: $TOTAL"
echo "   Total categories: $CATEGORIES_COUNT"
echo ""

# Test 9: Upload template file
echo "9. Testing template file upload..."
# Create a temporary template file
TMP_FILE="/tmp/upload-test.jrxml"
echo "$TEMPLATE_CONTENT" > "$TMP_FILE"

UPLOAD_RESPONSE=$(curl -s -X POST "$BASE_URL/api/templates/upload" \
  -F "file=@$TMP_FILE" \
  -F "name=upload-test" \
  -F "description=Uploaded template" \
  -F "category=upload")

if echo "$UPLOAD_RESPONSE" | jq -e '.message' > /dev/null; then
    echo -e "   ${GREEN}✓${NC} Template uploaded successfully"
else
    echo -e "   ${RED}✗${NC} Failed to upload template"
fi
rm -f "$TMP_FILE"
echo ""

# Test 10: Delete template
echo "10. Deleting test templates..."
DELETE_RESPONSE1=$(curl -s -X DELETE "$BASE_URL/api/templates/test-template")
DELETE_RESPONSE2=$(curl -s -X DELETE "$BASE_URL/api/templates/upload-test")

if echo "$DELETE_RESPONSE1" | jq -e '.message' > /dev/null; then
    echo -e "   ${GREEN}✓${NC} test-template deleted successfully"
else
    echo -e "   ${RED}✗${NC} Failed to delete test-template"
fi

if echo "$DELETE_RESPONSE2" | jq -e '.message' > /dev/null; then
    echo -e "   ${GREEN}✓${NC} upload-test deleted successfully"
else
    echo -e "   ${RED}✗${NC} Failed to delete upload-test"
fi
echo ""

# Test 11: Verify deletion
echo "11. Verifying deletion..."
VERIFY_RESPONSE=$(curl -s "$BASE_URL/api/templates/test-template")
if echo "$VERIFY_RESPONSE" | jq -e '.error' > /dev/null; then
    echo -e "   ${GREEN}✓${NC} Template successfully deleted (not found)"
else
    echo -e "   ${RED}✗${NC} Template still exists after deletion"
fi
echo ""

# Final summary
echo "=== Test Summary ==="
FINAL_COUNT=$(curl -s "$BASE_URL/api/templates" | jq -r '.total')
echo "Total templates in system: $FINAL_COUNT"
echo ""
echo "=== Test Complete ==="
