#!/usr/bin/env bash
set -euo pipefail

docker compose up -d

if [[ -z "${OPENAI_API_KEY:-}" ]]; then
  echo "Set OPENAI_API_KEY before starting the application."
  exit 1
fi

./mvnw spring-boot:run