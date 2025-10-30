# workshop-02-bg
Currency Exchange Rates Provider Service

## Description

A Spring Boot application that fetches and stores currency exchange rates from external sources (fixer.io and exchangeratesapi.io). The application runs scheduled tasks to periodically retrieve exchange rates and stores them in a PostgreSQL database.

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven**
- **PostgreSQL 16**
- **Liquibase** (Database schema management)
- **Docker & Docker Compose**

## Features

- Scheduled fetching of exchange rates from multiple providers:
  - fixer.io
  - exchangeratesapi.io
- REST API for accessing stored exchange rates
- PostgreSQL database for persistent storage
- Database schema management with Liquibase
- Fully containerized with Docker

## API Endpoints

### Get All Exchange Rates
```
GET /api/exchange-rates
```

### Get Exchange Rates for Currency Pair
```
GET /api/exchange-rates/{baseCurrency}/{targetCurrency}
```
Example: `GET /api/exchange-rates/EUR/USD`

### Manually Trigger Rate Fetch
```
POST /api/exchange-rates/fetch/{baseCurrency}
```
Example: `POST /api/exchange-rates/fetch/EUR`

## Configuration

The application can be configured via environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host | localhost |
| `DB_PORT` | PostgreSQL port | 5432 |
| `DB_NAME` | Database name | exchangerates |
| `DB_USER` | Database user | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `FIXER_API_KEY` | API key for fixer.io | (empty) |

## Building and Running

### Local Build

```bash
# Build the application
mvn clean package

# Run the application (requires PostgreSQL running)
java -jar target/exchange-rates-service-1.0.0.jar
```

### Docker Compose (Recommended)

Start both the application and PostgreSQL database:

```bash
docker-compose up --build
```

This will:
- Build the application Docker image
- Start a PostgreSQL container
- Start the application container
- Expose the application on port 8080

### Accessing the Application

Once running, the application is available at:
```
http://localhost:8080
```

## Development

### Using Makefile

A Makefile is provided with convenient shortcuts for common development tasks:

```bash
# Show all available commands
make help

# Start services (PostgreSQL + Application)
make up

# Stop and remove all services
make down

# View logs
make logs

# List running containers
make ps

# Open database shell
make db-shell

# Open application shell
make app-shell
```

### Running Tests

Run tests using Maven directly:
```bash
mvn test
```

Or use the Makefile targets for containerized test execution:

```bash
# Run tests with code coverage
make coverage

# Generate HTML coverage report
make coverage-html
```

The coverage reports are generated using JaCoCo and will be available at:
- **Console report**: Displayed in terminal after running `make coverage`
- **HTML report**: `target/site/jacoco/index.html` (after running `make coverage-html`)

**Note for Windows users:**
- Make sure you have `make` installed (e.g., via Chocolatey: `choco install make`)
- To view HTML report on Windows: `start target\site\jacoco\index.html`
- If tests require database access, start services first: `make up`

### Database Migrations

Database schema is managed by Liquibase. Migrations are located in:
```
src/main/resources/db/changelog/
```

Migrations are automatically applied on application startup.

## Scheduled Tasks

The application automatically fetches exchange rates every hour (configurable via `exchange.rates.fetch.interval` property in milliseconds).

## License

This project is for educational purposes.

