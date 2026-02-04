# Carbon Footprint Calculator

A Progressive Web App (PWA) for calculating and tracking your carbon footprint.

## Features

- **Multi-category tracking**: Transportation, Energy, Food, and Waste
- **Real-time calculations**: Instant carbon footprint calculations based on scientific emission factors
- **History tracking**: SQLite database stores all your calculations
- **Summary dashboard**: View your total carbon footprint and recent activities
- **Progressive Web App**: Install on any device, works offline
- **Modern stack**: Kotlin Ktor backend, Svelte frontend

## Technology Stack

### Backend
- **Kotlin** with **Ktor** framework
- **SQLite** database (no ORM, pure JDBC)
- **Gradle** build system
- REST API with JSON serialization

### Frontend
- **Svelte** single-page application
- **Vite** build tool
- **PWA** capabilities with service worker
- Responsive design with dark mode support

### Deployment
- **Docker** with **Chainguard** images for security
- Multi-stage builds for optimization
- Docker Compose for easy deployment

## Getting Started

### Prerequisites
- JDK 17 or higher (for backend)
- Node.js 18+ (for frontend)
- Docker and Docker Compose (for containerized deployment)

### Running Locally

#### Backend
```bash
cd backend
gradle run
```

The backend will start on `http://localhost:8080`

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173` (or another port if 5173 is busy)

### Running with Docker

Build and run all services:
```bash
docker-compose up --build
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080

## API Endpoints

- `POST /api/calculate` - Calculate carbon footprint
- `GET /api/calculations` - Get calculation history
- `GET /api/summary` - Get summary with total carbon and recent calculations
- `GET /api/activity-types` - Get available activity types
- `GET /health` - Health check endpoint

## Carbon Calculation Categories

### Transportation (kg CO₂ per km)
- Petrol Car: 0.192
- Diesel Car: 0.171
- Electric Car: 0.053
- Bus: 0.089
- Train: 0.041
- Short Flight: 0.255
- Long Flight: 0.195

### Energy (kg CO₂ per kWh)
- Electricity: 0.475
- Natural Gas: 0.185
- Heating Oil: 0.265

### Food (kg CO₂ per kg)
- Beef: 27.0
- Lamb: 39.2
- Chicken: 6.9
- Vegetables: 2.0
- And more...

### Waste (kg CO₂ per kg)
- General: 0.5
- Plastic: 6.0
- Paper: 0.3

## Project Structure

```
.
├── backend/                 # Kotlin Ktor backend
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/
│   │       │   └── com/ybiyrit/carboncalc/
│   │       │       ├── Application.kt
│   │       │       ├── CarbonCalculator.kt
│   │       │       ├── database/
│   │       │       ├── models/
│   │       │       └── routes/
│   │       └── resources/
│   └── build.gradle.kts
├── frontend/               # Svelte frontend
│   ├── src/
│   │   ├── lib/           # Svelte components
│   │   ├── App.svelte
│   │   └── main.js
│   ├── public/            # Static assets
│   ├── package.json
│   └── vite.config.js
├── Dockerfile.backend     # Backend Docker image
├── Dockerfile.frontend    # Frontend Docker image
└── docker-compose.yml     # Docker Compose configuration
```

## Security

- Uses Chainguard images for minimal attack surface
- No ORM to prevent SQL injection (using prepared statements)
- CORS configured for cross-origin requests
- Input validation on both frontend and backend

## License

See [LICENSE](LICENSE) file for details.

## Maintainers

See [MAINTAINERS.md](MAINTAINERS.md) for project maintainers.
