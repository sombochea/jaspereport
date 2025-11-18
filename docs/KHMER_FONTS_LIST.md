# Complete List of Khmer Fonts

All available Khmer fonts that can be registered with one command.

## 📋 Available Khmer Fonts

### Recommended Fonts

| Font Name | Style | Best For | Register Command |
|-----------|-------|----------|------------------|
| **Hanuman** | Traditional | Official documents, books | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Hanuman` |
| **Battambang** | Modern | Business documents | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Battambang` |
| **Content** | Clean | Body text, articles | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Content` |
| **Suwannaphum** | Professional | Reports, presentations | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Suwannaphum` |
| **Noto Sans Khmer** | Universal | Multilingual documents | `curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans%20Khmer"` |
| **Noto Serif Khmer** | Serif | Formal documents | `curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Serif%20Khmer"` |

### Display & Decorative Fonts

| Font Name | Style | Best For | Register Command |
|-----------|-------|----------|------------------|
| **Bayon** | Bold Display | Headers, titles | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Bayon` |
| **Bokor** | Decorative | Artistic designs | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Bokor` |
| **Koulen** | Heavy Display | Posters, banners | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Koulen` |
| **Moul** | Ornamental | Traditional designs | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Moul` |
| **Moulpali** | Decorative | Artistic headers | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Moulpali` |
| **Metal** | Bold | Impact text | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Metal` |

### Handwriting & Script Fonts

| Font Name | Style | Best For | Register Command |
|-----------|-------|----------|------------------|
| **Fasthand** | Handwritten | Informal documents | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Fasthand` |
| **Freehand** | Casual Script | Personal notes | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Freehand` |

### Traditional & Cultural Fonts

| Font Name | Style | Best For | Register Command |
|-----------|-------|----------|------------------|
| **Chenla** | Traditional | Cultural documents | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Chenla` |
| **Dangrek** | Classic | Traditional texts | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Dangrek` |
| **Khmer** | Standard | General use | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Khmer` |
| **Odor Mean Chey** | Traditional | Historical documents | `curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Odor%20Mean%20Chey"` |
| **Preahvihear** | Classic | Traditional content | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Preahvihear` |
| **Siemreap** | Traditional | Cultural texts | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Siemreap` |
| **Taprom** | Ancient Style | Historical documents | `curl -X POST http://localhost:8080/api/google-fonts/quick-register/Taprom` |

## 🎯 Font Selection Guide

### For Business Documents
```bash
# Best choices:
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Hanuman
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Battambang
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Content
```

### For Official Reports
```bash
# Professional fonts:
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Hanuman
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Suwannaphum
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Serif%20Khmer"
```

### For Invoices & Receipts
```bash
# Clear, readable fonts:
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Hanuman
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Content
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans%20Khmer"
```

### For Headers & Titles
```bash
# Bold, impactful fonts:
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Bayon
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Koulen
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Moul
```

## 📝 Usage Examples

### Example 1: Business Report with Hanuman

```bash
# 1. Register font
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Hanuman

# 2. Generate report
curl -X POST "http://localhost:8080/api/reports/render/khmer-demo?format=PDF" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "របាយការណ៍ប្រចាំខែ",
    "subtitle": "ខែមករា ឆ្នាំ២០២៤",
    "content": "នេះជារបាយការណ៍អាជីវកម្មជាមួយពុម្ពអក្សរ Hanuman។",
    "footer": "ក្រុមហ៊ុន ABC"
  }' \
  -o business-report.pdf
```

### Example 2: Invoice with Content Font

```bash
# 1. Register font
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Content

# 2. Update template to use Content font
# <font fontName="Content" size="12"/>

# 3. Generate invoice
curl -X POST "http://localhost:8080/api/reports/render/your-template?format=PDF" \
  -d '{"invoice_number": "INV-001"}' \
  -o invoice.pdf
```

### Example 3: Poster with Koulen Font

```bash
# 1. Register bold display font
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Koulen

# 2. Use in template for headers
# <font fontName="Koulen" size="36"/>
```

## 🔄 Register Multiple Fonts

```bash
# Register all recommended fonts at once
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Hanuman
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Battambang
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Content
curl -X POST http://localhost:8080/api/google-fonts/quick-register/Suwannaphum
curl -X POST "http://localhost:8080/api/google-fonts/quick-register/Noto%20Sans%20Khmer"
```

## 📊 Font Comparison

| Feature | Hanuman | Battambang | Content | Noto Sans Khmer |
|---------|---------|------------|---------|-----------------|
| Readability | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Professional | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Traditional | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ |
| Modern | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Bold Available | ❌ | ✅ | ✅ | ✅ |

## 🧪 Test All Fonts

```bash
# Test script to try different fonts
for font in "Hanuman" "Battambang" "Content" "Bayon" "Koulen"; do
  echo "Testing $font..."
  curl -X POST http://localhost:8080/api/google-fonts/quick-register/$font
  sleep 2
done

# List all registered fonts
curl http://localhost:8080/api/fonts
```

## 💡 Tips

1. **Start with Hanuman** - It's the most versatile and professional
2. **Use Battambang for modern look** - Great for contemporary documents
3. **Content for body text** - Very readable for long documents
4. **Noto Sans Khmer for compatibility** - Works everywhere
5. **Display fonts for headers only** - Bayon, Koulen, Moul are best for titles

## 🔗 See Also

- [KHMER_FONTS.md](KHMER_FONTS.md) - Complete Khmer font guide
- [USING_FONTS_IN_TEMPLATES.md](USING_FONTS_IN_TEMPLATES.md) - How to use fonts in templates
- [GOOGLE_FONTS.md](GOOGLE_FONTS.md) - Google Fonts integration

---

**ជ្រើសរើសពុម្ពអក្សរដែលសមស្របបំផុតសម្រាប់គម្រោងរបស់អ្នក!**
*Choose the font that best suits your project!*
