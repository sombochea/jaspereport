# Docker Guide

Complete guide for running JasperReports API Service with Docker.

## Quick Start

### Pull and Run

```bash
# Pull the image
docker pull ghcr.io/sombochea/jaspereport:latest

# Run the container
docker run -d -p 8080:8080 ghcr.io/sombochea/jaspereport:latest

# Access the API
curl http://localhost:8080/
```

### Using Docker Compose

```bash
# Copy environment file
cp .env.example .env

# Edit .env with your settings
nano .env

# Start the service
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the service
docker-compose down
```

## Docker Images

### Available Tags

| Tag | Description | Architectures |
|-----|-------------|---------------|
| `latest` | Latest main branch build | amd64, arm64 |
| `main` | Main branch builds | amd64, arm64 |
| `v1.0.0` | Specific version | amd64, arm64 |
| `v1.0` | Major.minor version | amd64, arm64 |
| `v1` | Major version | amd64, arm64 |

### Image Details

**Base Image:** `eclipse-temurin:17-jre-alpine`

**Size:** ~200-250 MB (compressed)

**Layers:**
- JRE 17
- Application JAR
- Font packages
- System utilities

## Running the Container

### Basic Run

```bash
docker run -d \
  --name jasperreports \
  -p 8080:8080 \
  ghcr.io/sombochea/jaspereport:latest
```

### With Volume Mounts

```bash
docker run -d \
  --name jasperreports \
  -p 8080:8080 \
  -v $(pwd)/templates:/app/templates \
  -v $(pwd)/fonts:/app/fonts \
  -v $(pwd)/data:/app/data \
  ghcr.io/sombochea/jaspereport:latest
```

### With Environment Variables

```bash
docker run -d \
  --name jasperreports \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  -e TZ="America/New_York" \
  ghcr.io/sombochea/jaspereport:latest
```

### With Custom Port

```bash
docker run -d \
  --name jasperreports \
  -p 9090:8080 \
  ghcr.io/sombochea/jaspereport:latest
```

### With Restart Policy

```bash
docker run -d \
  --name jasperreports \
  -p 8080:8080 \
  --restart unless-stopped \
  ghcr.io/sombochea/jaspereport:latest
```

## Docker Compose

### Basic Configuration

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
    restart: unless-stopped
```

### Production Configuration

```yaml
version: '3.8'

services:
  jasperreports:
    image: ghcr.io/sombochea/jaspereport:v1.0.0
    container_name: jasperreports-api
    ports:
      - "8080:8080"
    volumes:
      - ./templates:/app/templates:ro
      - ./fonts:/app/fonts:ro
      - jasperreports-data:/app/data
    environment:
      - JAVA_OPTS=-Xmx2g -Xms1g
      - TZ=UTC
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 40s
    networks:
      - jasperreports-network
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"

volumes:
  jasperreports-data:
    driver: local

networks:
  jasperreports-network:
    driver: bridge
```

### With Nginx Reverse Proxy

```yaml
version: '3.8'

services:
  jasperreports:
    image: ghcr.io/sombochea/jaspereport:latest
    expose:
      - "8080"
    volumes:
      - ./templates:/app/templates
      - ./fonts:/app/fonts
      - ./data:/app/data
    networks:
      - jasperreports-network
    restart: unless-stopped

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - ./ssl:/etc/nginx/ssl:ro
    depends_on:
      - jasperreports
    networks:
      - jasperreports-network
    restart: unless-stopped

networks:
  jasperreports-network:
    driver: bridge
```

## Volume Mounts

### Templates Directory

Mount your JRXML templates:

```bash
-v $(pwd)/templates:/app/templates
```

**Purpose:** Store and manage report templates

**Permissions:** Read-only recommended (`:ro`)

### Fonts Directory

Mount custom fonts:

```bash
-v $(pwd)/fonts:/app/fonts
```

**Purpose:** Store custom font files

**Permissions:** Read-only recommended (`:ro`)

### Data Directory

Mount database storage:

```bash
-v $(pwd)/data:/app/data
```

**Purpose:** Persist font and template metadata

**Permissions:** Read-write required

## Environment Variables

### JAVA_OPTS

Configure JVM options:

```bash
-e JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC"
```

**Common Options:**
- `-Xmx` - Maximum heap size
- `-Xms` - Initial heap size
- `-XX:+UseG1GC` - Use G1 garbage collector
- `-XX:MaxMetaspaceSize` - Metaspace limit

### TZ

Set timezone:

```bash
-e TZ="America/New_York"
```

**Examples:**
- `UTC`
- `America/New_York`
- `Europe/London`
- `Asia/Tokyo`

## Health Checks

### Docker Health Check

Built-in health check:

```bash
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1
```

### Manual Health Check

```bash
# Check container health
docker inspect --format='{{.State.Health.Status}}' jasperreports

# View health check logs
docker inspect --format='{{json .State.Health}}' jasperreports | jq
```

## Logging

### View Logs

```bash
# Follow logs
docker logs -f jasperreports

# Last 100 lines
docker logs --tail 100 jasperreports

# Since timestamp
docker logs --since 2024-01-01T00:00:00 jasperreports
```

### Configure Logging

```yaml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

**Drivers:**
- `json-file` - JSON file logging
- `syslog` - Syslog logging
- `journald` - Systemd journal
- `gelf` - Graylog Extended Log Format

## Networking

### Bridge Network

Default networking:

```bash
docker network create jasperreports-network
docker run --network jasperreports-network ...
```

### Host Network

Use host networking:

```bash
docker run --network host ...
```

### Custom Network

```yaml
networks:
  jasperreports-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

## Security

### Non-Root User

Container runs as non-root user `appuser` (UID 1000)

### Read-Only Volumes

Mount volumes as read-only when possible:

```bash
-v $(pwd)/templates:/app/templates:ro
```

### Security Options

```bash
docker run \
  --security-opt=no-new-privileges:true \
  --cap-drop=ALL \
  --read-only \
  --tmpfs /tmp \
  ...
```

### Secrets Management

Use Docker secrets:

```yaml
secrets:
  api_key:
    external: true

services:
  jasperreports:
    secrets:
      - api_key
```

## Multi-Architecture

### Pull Specific Architecture

```bash
# AMD64
docker pull --platform linux/amd64 ghcr.io/sombochea/jaspereport:latest

# ARM64
docker pull --platform linux/arm64 ghcr.io/sombochea/jaspereport:latest
```

### Build for Multiple Architectures

```bash
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t myimage:latest \
  --push .
```

## Performance Tuning

### Memory Limits

```bash
docker run \
  --memory="1g" \
  --memory-swap="2g" \
  --memory-reservation="512m" \
  ...
```

### CPU Limits

```bash
docker run \
  --cpus="2.0" \
  --cpu-shares=1024 \
  ...
```

### Compose Limits

```yaml
services:
  jasperreports:
    deploy:
      resources:
        limits:
          cpus: '2.0'
          memory: 1G
        reservations:
          cpus: '1.0'
          memory: 512M
```

## Troubleshooting

### Container Won't Start

**Check logs:**
```bash
docker logs jasperreports
```

**Check health:**
```bash
docker inspect jasperreports
```

**Common issues:**
- Port already in use
- Volume mount permissions
- Insufficient memory

### Application Not Responding

**Check if running:**
```bash
docker ps | grep jasperreports
```

**Check health endpoint:**
```bash
curl http://localhost:8080/
```

**Restart container:**
```bash
docker restart jasperreports
```

### Permission Errors

**Fix volume permissions:**
```bash
sudo chown -R 1000:1000 data/
```

**Run with user:**
```bash
docker run --user 1000:1000 ...
```

### Out of Memory

**Increase memory:**
```bash
docker run -e JAVA_OPTS="-Xmx2g" ...
```

**Check memory usage:**
```bash
docker stats jasperreports
```

## Maintenance

### Update Image

```bash
# Pull latest
docker pull ghcr.io/sombochea/jaspereport:latest

# Stop old container
docker stop jasperreports
docker rm jasperreports

# Start new container
docker run -d --name jasperreports ...
```

### Backup Data

```bash
# Backup data directory
docker run --rm \
  -v jasperreports-data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/data-backup.tar.gz /data

# Restore data
docker run --rm \
  -v jasperreports-data:/data \
  -v $(pwd):/backup \
  alpine tar xzf /backup/data-backup.tar.gz -C /
```

### Clean Up

```bash
# Remove stopped containers
docker container prune

# Remove unused images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything
docker system prune -a
```

## Best Practices

### 1. Use Specific Tags

```bash
# Good
docker pull ghcr.io/sombochea/jaspereport:v1.0.0

# Avoid in production
docker pull ghcr.io/sombochea/jaspereport:latest
```

### 2. Use Volume Mounts

Always mount data, templates, and fonts directories

### 3. Set Resource Limits

Prevent resource exhaustion

### 4. Use Health Checks

Monitor container health

### 5. Configure Logging

Manage log size and rotation

### 6. Use Restart Policies

Ensure high availability

### 7. Regular Updates

Keep images updated with security patches

### 8. Monitor Performance

Use `docker stats` to monitor resource usage

## Examples

### Development Setup

```bash
docker run -d \
  --name jasperreports-dev \
  -p 8080:8080 \
  -v $(pwd)/templates:/app/templates \
  -v $(pwd)/fonts:/app/fonts \
  -v $(pwd)/data:/app/data \
  -e JAVA_OPTS="-Xmx512m" \
  ghcr.io/sombochea/jaspereport:latest
```

### Production Setup

```bash
docker run -d \
  --name jasperreports-prod \
  -p 8080:8080 \
  -v /opt/jasperreports/templates:/app/templates:ro \
  -v /opt/jasperreports/fonts:/app/fonts:ro \
  -v /opt/jasperreports/data:/app/data \
  -e JAVA_OPTS="-Xmx2g -Xms1g" \
  --memory="2g" \
  --cpus="2.0" \
  --restart unless-stopped \
  --health-cmd="curl -f http://localhost:8080/ || exit 1" \
  --health-interval=30s \
  --health-timeout=3s \
  --health-retries=3 \
  ghcr.io/sombochea/jaspereport:v1.0.0
```

---

**Need help?** See the [CI/CD Documentation](CICD.md) or [main README](../README.md).
