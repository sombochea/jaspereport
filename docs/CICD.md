# CI/CD Documentation

Complete guide for the GitHub Actions CI/CD pipeline.

## Overview

The project uses GitHub Actions for continuous integration and deployment with three main workflows:

1. **CI Workflow** - Testing and code quality checks
2. **Docker Publish** - Build and publish multi-arch Docker images
3. **Build and Publish** - Complete build, test, and release pipeline

## Workflows

### 1. CI Workflow (`.github/workflows/ci.yml`)

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop`
- Manual workflow dispatch

**Jobs:**

#### Test
- Runs on: Ubuntu, macOS, Windows
- Java version: 17
- Executes: `./gradlew test`
- Uploads test results and reports

#### Build
- Runs on: Ubuntu
- Builds application and Fat JAR
- Uploads build artifacts
- Reports build sizes

#### Lint
- Code quality checks
- Runs detekt (if configured)
- Checks code formatting with ktlint (if configured)

#### Security
- Trivy vulnerability scanner
- Uploads results to GitHub Security

#### Dependency Check
- Checks for dependency updates
- Generates dependency report

### 2. Docker Publish Workflow (`.github/workflows/docker-publish.yml`)

**Triggers:**
- Push to `main` branch
- Tags matching `v*`
- Manual workflow dispatch

**Features:**
- Multi-architecture builds (linux/amd64, linux/arm64)
- Publishes to GitHub Container Registry (ghcr.io)
- Automatic tagging based on branch/tag
- Build provenance and SBOM generation
- Image attestation

**Image Tags:**
- `latest` - Latest main branch build
- `main` - Main branch builds
- `v1.0.0` - Semantic version tags
- `v1.0` - Major.minor tags
- `v1` - Major version tags
- `main-<sha>` - Commit SHA tags

### 3. Build and Publish Workflow (`.github/workflows/build-and-publish.yml`)

**Triggers:**
- Push to `main` or `develop` branches
- Tags matching `v*`
- Pull requests
- Manual workflow dispatch

**Jobs:**

#### Test
- Runs all tests
- Uploads test results

#### Build
- Builds application
- Uploads artifacts

#### Docker Build and Push
- Multi-arch Docker image build
- Pushes to GitHub Container Registry
- Generates attestations

#### Docker Test
- Pulls and tests the published image
- Verifies API endpoints
- Ensures container health

#### Release
- Creates GitHub release for version tags
- Includes Docker pull instructions
- Links to changelog

## Docker Images

### Registry

Images are published to GitHub Container Registry:

```
ghcr.io/sombochea/jaspereport
```

### Supported Architectures

- `linux/amd64` - x86_64 architecture
- `linux/arm64` - ARM64 architecture (Apple Silicon, ARM servers)

### Image Details

**Base Image:** `eclipse-temurin:17-jre-alpine`

**Installed Packages:**
- fontconfig
- ttf-dejavu
- ttf-liberation
- bash
- curl

**Directories:**
- `/app` - Application directory
- `/app/data` - Database storage
- `/app/templates` - JRXML templates
- `/app/fonts` - Font files

**Port:** 8080

**Health Check:** HTTP GET to `http://localhost:8080/`

**User:** Non-root user `appuser` (UID 1000)

## Usage

### Pull Image

```bash
# Pull latest
docker pull ghcr.io/sombochea/jaspereport:latest

# Pull specific version
docker pull ghcr.io/sombochea/jaspereport:v1.0.0

# Pull specific architecture
docker pull --platform linux/amd64 ghcr.io/sombochea/jaspereport:latest
docker pull --platform linux/arm64 ghcr.io/sombochea/jaspereport:latest
```

### Run Container

```bash
# Basic run
docker run -d -p 8080:8080 ghcr.io/sombochea/jaspereport:latest

# With volume mounts
docker run -d -p 8080:8080 \
  -v $(pwd)/templates:/app/templates \
  -v $(pwd)/fonts:/app/fonts \
  -v $(pwd)/data:/app/data \
  ghcr.io/sombochea/jaspereport:latest

# With environment variables
docker run -d -p 8080:8080 \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  ghcr.io/sombochea/jaspereport:latest

# With custom port
docker run -d -p 9090:8080 \
  ghcr.io/sombochea/jaspereport:latest
```

### Docker Compose

```yaml
version: '3.8'

services:
  jasperreports:
    image: ghcr.io/sombochea/jaspereport:latest
    ports:
      - "8080:8080"
    volumes:
      - ./templates:/app/templates
      - ./fonts:/app/fonts
      - ./data:/app/data
    environment:
      - JAVA_OPTS=-Xmx1g -Xms512m
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 40s
```

## GitHub Actions Secrets

### Required Secrets

**GITHUB_TOKEN** - Automatically provided by GitHub Actions
- Used for: Publishing to GitHub Container Registry
- Permissions: `packages: write`, `contents: read`

### Optional Secrets

For additional registries or features:

**DOCKER_USERNAME** - Docker Hub username
**DOCKER_PASSWORD** - Docker Hub password/token

## Permissions

The workflows require the following permissions:

```yaml
permissions:
  contents: read        # Read repository contents
  packages: write       # Publish to GitHub Packages
  attestations: write   # Generate attestations
  id-token: write       # OIDC token for attestations
  security-events: write # Upload security scan results
```

## Triggering Workflows

### Automatic Triggers

**On Push:**
```bash
git push origin main
```

**On Tag:**
```bash
git tag v1.0.0
git push origin v1.0.0
```

**On Pull Request:**
```bash
# Create PR to main or develop branch
```

### Manual Trigger

1. Go to Actions tab in GitHub
2. Select workflow
3. Click "Run workflow"
4. Choose branch
5. Click "Run workflow" button

## Release Process

### Creating a Release

1. **Update Version**
   ```bash
   # Update version in build.gradle.kts
   version = "1.0.0"
   ```

2. **Update Changelog**
   ```bash
   # Add changes to CHANGELOG.md
   ```

3. **Commit Changes**
   ```bash
   git add .
   git commit -m "Release v1.0.0"
   git push origin main
   ```

4. **Create Tag**
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```

5. **Workflow Runs**
   - CI tests run
   - Docker image builds for both architectures
   - Image published to GitHub Container Registry
   - GitHub release created automatically

### Release Artifacts

- Docker images (multi-arch)
- Build artifacts (JAR files)
- Test results
- Release notes

## Monitoring

### Workflow Status

Check workflow status:
- GitHub Actions tab
- Commit status checks
- Pull request checks

### Build Logs

View detailed logs:
1. Go to Actions tab
2. Click on workflow run
3. Click on job
4. View step logs

### Image Verification

Verify published images:

```bash
# List tags
docker pull ghcr.io/sombochea/jaspereport --all-tags

# Inspect image
docker inspect ghcr.io/sombochea/jaspereport:latest

# Check platforms
docker manifest inspect ghcr.io/sombochea/jaspereport:latest
```

## Troubleshooting

### Build Failures

**Problem:** Build fails on specific OS

**Solution:**
- Check OS-specific logs
- Verify Gradle wrapper compatibility
- Check Java version compatibility

### Docker Build Failures

**Problem:** Multi-arch build fails

**Solution:**
- Verify QEMU setup
- Check Buildx configuration
- Review platform-specific dependencies

### Permission Errors

**Problem:** Cannot push to registry

**Solution:**
- Verify GITHUB_TOKEN permissions
- Check repository settings
- Ensure packages write permission

### Test Failures

**Problem:** Tests fail in CI but pass locally

**Solution:**
- Check environment differences
- Verify test dependencies
- Review test isolation

## Best Practices

### 1. Version Tagging

Use semantic versioning:
```bash
v1.0.0  # Major.Minor.Patch
v1.0    # Major.Minor
v1      # Major
```

### 2. Branch Strategy

- `main` - Production-ready code
- `develop` - Development branch
- `feature/*` - Feature branches
- `hotfix/*` - Hotfix branches

### 3. Commit Messages

Follow conventional commits:
```
feat: add new feature
fix: fix bug
docs: update documentation
chore: update dependencies
```

### 4. Pull Requests

- Always create PR for changes
- Wait for CI checks to pass
- Require code review
- Squash and merge

### 5. Security

- Keep dependencies updated
- Review security scan results
- Use non-root containers
- Scan images regularly

## Performance Optimization

### Caching

Workflows use caching for:
- Gradle dependencies
- Docker layers
- Build artifacts

### Parallel Execution

- Tests run in parallel across OS
- Docker builds use layer caching
- Independent jobs run concurrently

### Resource Limits

Configure resource limits:

```yaml
env:
  GRADLE_OPTS: "-Xmx2g -XX:MaxMetaspaceSize=512m"
  JAVA_OPTS: "-Xmx1g -Xms512m"
```

## Advanced Configuration

### Custom Dockerfile

Modify Dockerfile in workflow:

```yaml
- name: Create Dockerfile
  run: |
    cat > Dockerfile << 'EOF'
    # Your custom Dockerfile
    EOF
```

### Additional Registries

Push to multiple registries:

```yaml
- name: Push to Docker Hub
  uses: docker/build-push-action@v5
  with:
    push: true
    tags: dockerhub/image:tag
```

### Custom Tests

Add custom test steps:

```yaml
- name: Integration Tests
  run: ./scripts/integration-tests.sh
```

## Maintenance

### Regular Tasks

1. **Update Dependencies**
   ```bash
   ./gradlew dependencyUpdates
   ```

2. **Update Actions**
   - Check for action updates
   - Review changelogs
   - Test in feature branch

3. **Review Security Scans**
   - Check Trivy results
   - Update vulnerable dependencies
   - Apply security patches

4. **Clean Up Old Images**
   - Remove unused tags
   - Archive old versions
   - Maintain registry size

## Support

### Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com/)
- [Gradle Documentation](https://docs.gradle.org/)

### Getting Help

1. Check workflow logs
2. Review documentation
3. Search GitHub issues
4. Open new issue with details

---

**Last Updated:** November 2024

For more information, see the [main README](../README.md).
