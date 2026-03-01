#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

build_domain() {
  local domain="$1"
  local step="$2"
  local total="$3"
  local version="2.0.0-SNAPSHOT"
  echo ""
  echo "[${step}/${total}] Building ${domain} domain..."
  cd "${SCRIPT_DIR}/${domain}"
  "${SCRIPT_DIR}/mvnw" org.codehaus.mojo:versions-maven-plugin:2.7:set-property -Dproperty=revision -DnewVersion=${version} -DgenerateBackupPoms=false
  "${SCRIPT_DIR}/mvnw" org.codehaus.mojo:versions-maven-plugin:2.7:set -DnewVersion=${version} -DgenerateBackupPoms=false
  "${SCRIPT_DIR}/mvnw" clean install -Dmaven.test.skip=true
}

echo "========================================"
echo "Building CATools2 - All Domain Projects"
echo "========================================"

build_domain mcp       1 9
build_domain infra     2 9
build_domain common    3 9
build_domain reporting 4 9
build_domain media     5 9
build_domain web       6 9
build_domain ws        7 9
# build_domain pipeline  8 9
build_domain distrib   9 9

echo ""
echo "========================================"
echo "All domains built successfully!"
echo "========================================"
