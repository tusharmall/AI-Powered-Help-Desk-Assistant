docker compose up -d

if (-not $env:OPENAI_API_KEY) {
    Write-Host "Set OPENAI_API_KEY before starting the application." -ForegroundColor Yellow
    exit 1
}

./mvnw.cmd spring-boot:run