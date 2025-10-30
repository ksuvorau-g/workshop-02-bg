# Makefile - convenient shortcuts for docker-compose tasks
# Usage: make <target>

DC ?= docker compose
COMPOSE_FILE ?= docker-compose.yml
DC_CMD := $(DC) -f $(COMPOSE_FILE)

.PHONY: help up down build logs ps restart db-shell app-shell ps-version coverage coverage-html

help:
	@echo "Available targets:"
	@echo "  make up            - Build and start services (detached)"
	@echo "  make down          - Stop and remove containers, networks, volumes"
	@echo "  make build         - Build images (no cache)"
	@echo "  make logs          - Follow logs (Ctrl+C to exit)"
	@echo "  make ps            - List containers managed by compose"
	@echo "  make restart       - Restart all services (down then up)"
	@echo "  make db-shell      - Open psql shell inside the postgres container"
	@echo "  make app-shell     - Open a shell inside the app container"
	@echo "  make ps-version    - Show docker compose version"
	@echo "  make coverage      - Run tests with coverage inside app container"
	@echo "  make coverage-html - Generate HTML coverage report"

up:
	$(DC_CMD) up -d --build

down:
	$(DC_CMD) down

build:
	$(DC_CMD) build --no-cache

logs:
	$(DC_CMD) logs -f --tail=200

ps:
	$(DC_CMD) ps

restart: down up

db-shell:
	# Non-interactive (-T) so it works from CI and different shells
	$(DC_CMD) exec -T postgres psql -U postgres -d exchangerates

app-shell:
	# Interactive shell for the app service. Use from a terminal that supports TTY.
	$(DC_CMD) exec -it app /bin/sh

ps-version:
	$(DC) version

# Run tests with coverage using Maven in a temporary container
# Uses Maven with JaCoCo plugin to generate coverage reports
# Note: Tests will be run inside a Maven container with access to the repository source
# If tests require database access, start services first with: make up
coverage:
	@echo "Running tests with coverage using Maven..."
	@echo "Note: This runs tests in an isolated Maven container"
	docker run --rm \
		-v $(PWD):/app \
		-w /app \
		maven:3.9-eclipse-temurin-17-alpine \
		mvn test

# Generate HTML coverage report
# HTML report will be available at: target/site/jacoco/index.html
# On Windows cmd.exe: Open target\site\jacoco\index.html in your browser
coverage-html:
	@echo "Generating HTML coverage report..."
	docker run --rm \
		-v $(PWD):/app \
		-w /app \
		maven:3.9-eclipse-temurin-17-alpine \
		mvn test
	@echo ""
	@echo "=========================================="
	@echo "HTML coverage report generated successfully!"
	@echo "Report location: target/site/jacoco/index.html"
	@echo ""
	@echo "To view the report:"
	@echo "  - Linux/Mac: open target/site/jacoco/index.html"
	@echo "  - Windows:   start target\\site\\jacoco\\index.html"
	@echo "=========================================="
