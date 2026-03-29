# Euro Denomination App

Monorepo mit:
- **Backend**: Spring Boot (`euro-denomination-backend`)
- **Frontend**: Angular (`euro-denomination-frontend`)
- **Container Setup**: Docker Compose

## Projektstruktur

```text
  docker-compose.yml
  euro-denomination-backend/
  euro-denomination-frontend/
```

## Architektur

- Frontend lokal auf `http://localhost:4200`
- Backend lokal auf `http://localhost:8080`
- API-Aufrufe im Frontend über `/api/*`
- **Lokal (ng serve):** Weiterleitung über `euro-denomination-frontend/proxy.conf.json` nach `http://localhost:8080`
- **Docker:** Nginx im Frontend-Container leitet `/api/*` intern an `backend:8080` weiter (`euro-denomination-frontend/nginx.conf`)

## Voraussetzungen

- Java 21
- Node.js + npm
- Docker Desktop (optional, für den Container-Start)

## Schnellstart (lokal, ohne Docker)

### 1) Backend starten im Backend Verzeichnis "euro-denomination-backend"

```powershell
.\mvnw.cmd spring-boot:run
```

### 2) Frontend starten (neues Terminal) im Frontend Verzeichnis "euro-denomination-frontend"

```powershell
npm install
npm start
```

Danach im Browser öffnen: `http://localhost:4200`

## Start mit Docker Compose

Im Root-Verzeichnis:

```powershell
docker compose up --build
```

- Frontend: `http://localhost:4200`
- Backend: `http://localhost:8080`

## Tests

### Backend-Tests

```powershell
.\mvnw.cmd test
```

### Frontend-Tests

```powershell
npm test -- --watch=false --browsers=ChromeHeadless
```
