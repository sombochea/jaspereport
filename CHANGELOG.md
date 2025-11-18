# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- **Template CRUD System** - Complete template management with database persistence
  - Create, read, update, delete templates via API
  - Database persistence using H2
  - File system synchronization
  - Category organization
  - Template upload support
  - Version tracking (created/updated timestamps)
  - Template statistics
  - Automatic import from file system on startup
- **Font Persistence System** - Automatic font persistence using H2 database
  - Fonts are saved to database on registration
  - Fonts are automatically loaded on application startup
  - Font validation on load (removes missing fonts)
  - Database stored in `data/fonts.mv.db`
- **Comprehensive Documentation** - Organized documentation in `docs/` folder
  - Font Persistence Guide
  - Project Summary
  - Documentation index (docs/README.md)
- **Utility Scripts** - Organized in `scripts/` folder
  - test-fonts.sh
  - test-khmer-fonts.sh
  - test-persistence.sh
  - setup-khmer-fonts.sh
- **.gitkeep files** - Preserve directory structure in git

### Changed
- **Documentation Organization** - Moved all .md files to `docs/` folder
- **Script Organization** - Moved all .sh files to `scripts/` folder
- **README.md** - Completely rewritten with better structure and navigation
- **.gitignore** - Enhanced with better patterns for generated files
- **Code Cleanup** - Removed unused imports from FontRegistry

### Fixed
- **H2 Database Compatibility** - Fixed REPLACE statement not supported in H2
  - Changed to INSERT/UPDATE logic
  - Fonts now properly persist across restarts

### Technical Details

#### Font Persistence Implementation
- Uses Exposed ORM for database operations
- H2 embedded database for zero-configuration
- Automatic schema creation on first run
- Transaction-based operations for data integrity

#### Code Refactoring
- Removed unused imports: `DefaultJasperReportsContext`, `FontFamily`, `SimpleFontFace`, `ExtensionsEnvironment`
- Improved error handling in font persistence
- Added validation logging for better debugging

#### Project Structure
```
├── docs/                    # All documentation (NEW)
│   ├── README.md           # Documentation index (NEW)
│   ├── FONT_PERSISTENCE_GUIDE.md (NEW)
│   └── ... (13 other docs)
├── scripts/                 # Utility scripts (NEW)
│   ├── test-fonts.sh
│   ├── test-khmer-fonts.sh
│   ├── test-persistence.sh
│   └── setup-khmer-fonts.sh
├── src/                     # Source code
├── templates/               # JRXML templates
├── fonts/                   # Font files
├── data/                    # Database files
├── README.md               # Main readme (UPDATED)
├── CHANGELOG.md            # This file (NEW)
└── .gitignore              # Enhanced (UPDATED)
```

## [Previous] - Before Organization

### Features
- Multi-format report generation (PDF, Excel, Word, etc.)
- Dynamic font registration
- Google Fonts integration
- Khmer language support
- UTF-8 support for all languages
- RESTful API
- JRXML template support

### Known Issues (Fixed in Unreleased)
- Fonts not persisting across restarts
- Documentation scattered in root directory
- Scripts mixed with project files
- H2 REPLACE statement compatibility issue

---

## Migration Guide

### For Existing Users

If you're upgrading from a previous version:

1. **Documentation**: All docs moved to `docs/` folder
   - Update any bookmarks or links
   - See `docs/README.md` for navigation

2. **Scripts**: All scripts moved to `scripts/` folder
   - Update any automation that calls scripts
   - Scripts are still executable

3. **Font Persistence**: Fonts now persist automatically
   - No action needed - works automatically
   - Database created in `data/fonts.mv.db`
   - Fonts registered once remain available forever

4. **No Breaking Changes**: All APIs remain the same
   - Existing integrations continue to work
   - No code changes required

### Testing After Upgrade

```bash
# 1. Build project
./gradlew build

# 2. Run tests
./scripts/test-fonts.sh
./scripts/test-persistence.sh

# 3. Start server
./gradlew run

# 4. Verify fonts persist
# Register a font, restart server, check it's still there
```

---

## Version History

### Current State
- **Status**: Production Ready
- **Stability**: Stable
- **Documentation**: Complete
- **Test Coverage**: Manual testing scripts provided

### Future Plans
- Automated unit tests
- Docker support enhancement
- Performance optimization
- Additional language support
- Template management API
- Report scheduling

---

**Note**: This project follows [Semantic Versioning](https://semver.org/).
