# Template Management Guide

Complete guide for managing JRXML templates with the Template CRUD API.

## Overview

The Template Management system provides a complete CRUD (Create, Read, Update, Delete) API for managing JasperReports JRXML templates with database persistence and file system synchronization.

## Features

✅ **Full CRUD Operations** - Create, read, update, and delete templates  
✅ **Database Persistence** - Templates stored in H2 database  
✅ **File System Sync** - Automatic synchronization with templates directory  
✅ **Category Organization** - Organize templates by category  
✅ **Version Tracking** - Created and updated timestamps  
✅ **File Upload** - Upload JRXML files via multipart form data  
✅ **Content Validation** - Basic JRXML validation  
✅ **Statistics** - Template usage statistics  

## Architecture

### Components

1. **TemplatePersistence** - Database layer using Exposed ORM
2. **TemplateManager** - Business logic and caching
3. **TemplateRoutes** - REST API endpoints

### Data Flow

```
API Request → TemplateRoutes → TemplateManager → TemplatePersistence → H2 Database
                                      ↓
                                File System (templates/)
```

### Database Schema

```sql
CREATE TABLE templates (
    name VARCHAR(255) PRIMARY KEY,
    content TEXT NOT NULL,
    description VARCHAR(500),
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

## API Reference

### Base URL

```
http://localhost:8080/api/templates
```

### Endpoints

#### 1. List All Templates

```http
GET /api/templates
```

**Response:**
```json
{
  "templates": [
    {
      "name": "sample-report",
      "description": "Sample report template",
      "category": "reports",
      "createdAt": "2024-11-18T10:00:00Z",
      "updatedAt": "2024-11-18T10:00:00Z",
      "contentLength": 2048
    }
  ],
  "total": 1
}
```

**Example:**
```bash
curl http://localhost:8080/api/templates
```

---

#### 2. Get Template by Name

```http
GET /api/templates/{name}
```

**Response:**
```json
{
  "name": "sample-report",
  "content": "<?xml version=\"1.0\"...",
  "description": "Sample report template",
  "category": "reports",
  "createdAt": "2024-11-18T10:00:00Z",
  "updatedAt": "2024-11-18T10:00:00Z"
}
```

**Example:**
```bash
curl http://localhost:8080/api/templates/sample-report
```

---

#### 3. Get Template Content Only

```http
GET /api/templates/{name}/content
```

**Response:** Raw JRXML content (Content-Type: application/xml)

**Example:**
```bash
curl http://localhost:8080/api/templates/sample-report/content
```

---

#### 4. Create Template

```http
POST /api/templates
Content-Type: application/json

{
  "name": "my-template",
  "content": "<?xml version=\"1.0\"...",
  "description": "My custom template",
  "category": "custom"
}
```

**Response:**
```json
{
  "message": "Template created successfully",
  "name": "my-template"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/templates \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-template",
    "content": "<?xml version=\"1.0\" encoding=\"UTF-8\"?><jasperReport>...</jasperReport>",
    "description": "My custom template",
    "category": "custom"
  }'
```

---

#### 5. Upload Template File

```http
POST /api/templates/upload
Content-Type: multipart/form-data

file: <JRXML file>
name: template-name (optional, uses filename if not provided)
description: Template description (optional)
category: Template category (optional)
```

**Response:**
```json
{
  "message": "Template uploaded successfully",
  "name": "my-template"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/templates/upload \
  -F "file=@my-template.jrxml" \
  -F "name=my-template" \
  -F "description=Uploaded template" \
  -F "category=custom"
```

---

#### 6. Update Template

```http
PUT /api/templates/{name}
Content-Type: application/json

{
  "content": "<?xml version=\"1.0\"...",
  "description": "Updated description",
  "category": "updated-category"
}
```

**Response:**
```json
{
  "message": "Template updated successfully",
  "name": "my-template"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/templates/my-template \
  -H "Content-Type: application/json" \
  -d '{
    "content": "<?xml version=\"1.0\" encoding=\"UTF-8\"?><jasperReport>...</jasperReport>",
    "description": "Updated template",
    "category": "updated"
  }'
```

---

#### 7. Delete Template

```http
DELETE /api/templates/{name}
```

**Response:**
```json
{
  "message": "Template deleted successfully"
}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/templates/my-template
```

---

#### 8. List Templates by Category

```http
GET /api/templates/category/{category}
```

**Response:**
```json
{
  "category": "reports",
  "templates": [...],
  "total": 5
}
```

**Example:**
```bash
curl http://localhost:8080/api/templates/category/reports
```

---

#### 9. Get All Categories

```http
GET /api/templates/categories/list
```

**Response:**
```json
{
  "categories": ["reports", "invoices", "custom"],
  "total": 3
}
```

**Example:**
```bash
curl http://localhost:8080/api/templates/categories/list
```

---

#### 10. Get Statistics

```http
GET /api/templates/statistics
```

**Response:**
```json
{
  "totalTemplates": 10,
  "categories": 3,
  "categoryCounts": {
    "reports": 5,
    "invoices": 3,
    "custom": 2
  }
}
```

**Example:**
```bash
curl http://localhost:8080/api/templates/statistics
```

---

#### 11. Reload Templates

```http
POST /api/templates/reload
```

**Response:**
```json
{
  "message": "Templates reloaded successfully",
  "total": 10
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/templates/reload
```

## Usage Examples

### Example 1: Create a Simple Template

```bash
curl -X POST http://localhost:8080/api/templates \
  -H "Content-Type: application/json" \
  -d '{
    "name": "hello-world",
    "content": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\"\n              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n              xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports \n              http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\"\n              name=\"hello-world\" pageWidth=\"595\" pageHeight=\"842\">\n    <title>\n        <band height=\"50\">\n            <staticText>\n                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"30\"/>\n                <text><![CDATA[Hello World!]]></text>\n            </staticText>\n        </band>\n    </title>\n</jasperReport>",
    "description": "Simple Hello World template",
    "category": "examples"
  }'
```

### Example 2: Upload Template from File

```bash
# Create a template file
cat > my-report.jrxml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              name="my-report" pageWidth="595" pageHeight="842">
    <parameter name="title" class="java.lang.String"/>
    <title>
        <band height="50">
            <textField>
                <reportElement x="0" y="0" width="555" height="30"/>
                <textFieldExpression><![CDATA[$P{title}]]></textFieldExpression>
            </textField>
        </band>
    </title>
</jasperReport>
EOF

# Upload it
curl -X POST http://localhost:8080/api/templates/upload \
  -F "file=@my-report.jrxml" \
  -F "description=My custom report" \
  -F "category=custom"
```

### Example 3: Update Template Description

```bash
# Get current template
TEMPLATE=$(curl -s http://localhost:8080/api/templates/my-report)
CONTENT=$(echo $TEMPLATE | jq -r '.content')

# Update with new description
curl -X PUT http://localhost:8080/api/templates/my-report \
  -H "Content-Type: application/json" \
  -d "{
    \"content\": $(echo "$CONTENT" | jq -Rs .),
    \"description\": \"Updated description\",
    \"category\": \"reports\"
  }"
```

### Example 4: Organize Templates by Category

```bash
# Create templates in different categories
curl -X POST http://localhost:8080/api/templates \
  -H "Content-Type: application/json" \
  -d '{"name": "invoice-1", "content": "...", "category": "invoices"}'

curl -X POST http://localhost:8080/api/templates \
  -H "Content-Type: application/json" \
  -d '{"name": "report-1", "content": "...", "category": "reports"}'

# List by category
curl http://localhost:8080/api/templates/category/invoices
curl http://localhost:8080/api/templates/category/reports
```

## File System Integration

### Automatic Synchronization

Templates are automatically synchronized between database and file system:

1. **On Create/Update**: Template is saved to `templates/{name}.jrxml`
2. **On Delete**: Template file is removed from `templates/` directory
3. **On Startup**: Templates from file system are imported to database

### Directory Structure

```
templates/
├── sample-report.jrxml
├── invoice-template.jrxml
├── custom-report.jrxml
└── ...
```

### Manual File Import

Place JRXML files in `templates/` directory and restart the server or call the reload endpoint:

```bash
# Copy template file
cp my-template.jrxml templates/

# Reload templates
curl -X POST http://localhost:8080/api/templates/reload
```

## Template Validation

### Basic Validation

The system performs basic JRXML validation:

- Must contain `<jasperReport>` root element
- Must be valid XML
- Must have closing `</jasperReport>` tag

### Advanced Validation

For production use, consider:

1. **JasperReports Compilation**: Compile template to verify syntax
2. **Schema Validation**: Validate against JasperReports XSD
3. **Parameter Validation**: Check required parameters exist

## Best Practices

### 1. Naming Conventions

```bash
# Good names
invoice-template
monthly-report
customer-statement

# Avoid
template1
temp
test
```

### 2. Use Categories

Organize templates by purpose:

```bash
# Categories
invoices/
reports/
statements/
labels/
custom/
```

### 3. Add Descriptions

Always include meaningful descriptions:

```json
{
  "name": "monthly-sales-report",
  "description": "Monthly sales report with charts and summaries",
  "category": "reports"
}
```

### 4. Version Control

Consider versioning templates:

```bash
# Template names with versions
invoice-v1
invoice-v2
monthly-report-2024
```

### 5. Backup Templates

Regular backups:

```bash
# Backup database
cp data/fonts.mv.db backups/templates_$(date +%Y%m%d).mv.db

# Backup template files
tar -czf templates_backup_$(date +%Y%m%d).tar.gz templates/
```

## Troubleshooting

### Template Not Found

**Problem**: GET request returns 404

**Solutions**:
1. Check template name (case-sensitive)
2. List all templates to verify name
3. Check if template was deleted
4. Reload templates from file system

### Invalid JRXML Content

**Problem**: Template creation fails with validation error

**Solutions**:
1. Verify XML is well-formed
2. Check for `<jasperReport>` root element
3. Validate against JasperReports XSD
4. Test template in JasperSoft Studio first

### File System Sync Issues

**Problem**: Template in database but not in file system

**Solutions**:
1. Check `templates/` directory permissions
2. Verify disk space available
3. Check application logs for errors
4. Manually sync with reload endpoint

### Database Errors

**Problem**: Database operation fails

**Solutions**:
1. Check database file exists: `data/fonts.mv.db`
2. Verify database permissions
3. Check disk space
4. Restart application

## Performance Considerations

### Caching

- Templates are cached in memory after first load
- Cache is updated on create/update/delete operations
- Reload endpoint clears and rebuilds cache

### Large Templates

For templates with large content:

- Consider compression for storage
- Use pagination for list operations
- Implement lazy loading for content

### Concurrent Access

- Thread-safe operations using ConcurrentHashMap
- Database transactions for consistency
- File system operations are synchronized

## Security Considerations

### Input Validation

- Template names are validated
- JRXML content is validated
- File uploads are restricted to .jrxml extension

### Access Control

Consider implementing:

1. **Authentication**: Require API keys or tokens
2. **Authorization**: Role-based access control
3. **Rate Limiting**: Prevent abuse
4. **Audit Logging**: Track template changes

### Best Practices

1. **Sanitize Input**: Validate all user input
2. **Limit File Size**: Restrict upload sizes
3. **Scan Content**: Check for malicious code
4. **Backup Regularly**: Maintain backups

## Integration with Report Generation

### Using Managed Templates

```bash
# 1. Create template
curl -X POST http://localhost:8080/api/templates \
  -H "Content-Type: application/json" \
  -d '{"name": "my-report", "content": "...", "category": "reports"}'

# 2. Generate report using template
curl -X POST http://localhost:8080/api/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "my-report",
    "format": "PDF",
    "parameters": {"title": "My Report"}
  }' \
  --output report.pdf
```

## Testing

### Run Test Script

```bash
./scripts/test-templates.sh
```

### Manual Testing

```bash
# Create
curl -X POST http://localhost:8080/api/templates \
  -H "Content-Type: application/json" \
  -d '{"name": "test", "content": "<?xml version=\"1.0\"?><jasperReport></jasperReport>"}'

# Read
curl http://localhost:8080/api/templates/test

# Update
curl -X PUT http://localhost:8080/api/templates/test \
  -H "Content-Type: application/json" \
  -d '{"content": "<?xml version=\"1.0\"?><jasperReport></jasperReport>", "description": "Updated"}'

# Delete
curl -X DELETE http://localhost:8080/api/templates/test
```

## Summary

The Template Management system provides:

✅ Complete CRUD operations for templates  
✅ Database persistence with H2  
✅ File system synchronization  
✅ Category organization  
✅ Version tracking  
✅ RESTful API  
✅ Production-ready reliability  

Templates are managed efficiently with automatic persistence, making it easy to maintain and organize your JasperReports templates.

---

**Need help?** See the [API Usage Guide](API_USAGE.md) or [Quick Reference](../QUICK_REFERENCE.md).
