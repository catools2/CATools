#!/bin/bash
set -e

echo "========================================"
echo "Building CATools2 - All Domain Projects"
echo "========================================"

# Build infrastructure first (catools.parent → catools.hibernate.parent → catools.bom)
echo ""
echo "[1/8] Building infra domain..."
./mvnw clean install -f infra/pom.xml

# Build common domain (dependency for others)
echo ""
echo "[2/8] Building common domain..."
./mvnw clean install -f common/pom.xml

# Build other domains
echo ""
echo "[3/8] Building web domain..."
./mvnw clean install -f web/pom.xml

echo ""
echo "[4/8] Building mcp domain..."
./mvnw clean install -f mcp/pom.xml

echo ""
echo "[5/8] Building ws domain..."
./mvnw clean install -f ws/pom.xml

echo ""
echo "[6/8] Building reporting domain..."
./mvnw clean install -f reporting/pom.xml

echo ""
echo "[7/8] Building pipeline domain..."
./mvnw clean install -f pipeline/pom.xml

echo ""
echo "[8/8] Building media domain..."
./mvnw clean install -f media/pom.xml

echo ""
echo "========================================"
echo "✅ All domains built successfully!"
echo "========================================"
