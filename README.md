# IMEI Block and Unblock (Simulation)

A local demo of blocking and unblocking a 4G or 5G phone by its IMEI. It was built to understand that workflow before working on the real company system.

This project does not talk to a mobile network, a carrier, or a physical phone. A block or unblock only changes a record in an in-memory database. Restarting the registry clears every record.

## How the pieces fit

```text
imei-status-app  →  file-ingestion  →  imei-registry
   (browser)         (CSV upload)       (IMEI records)
```

| Folder | What it does | Address |
| --- | --- | --- |
| `imei-registry` | Stores each IMEI and its status (`Blocked` or `Unblocked`) | http://localhost:8080 |
| `file-ingestion` | Reads a CSV, checks each row, and sends batches to the registry | http://localhost:8081 |
| `imei-status-app` | Lookup page and CSV upload page | http://localhost:5173 |

## Requirements

- Java 21
- Maven (or the `mvnw` wrapper in each Java module)
- Node.js 18 or newer

## Run it

Start the registry first, then file ingestion, then the UI. Use a separate terminal for each.

```bash
cd imei-registry
./mvnw spring-boot:run
```

```bash
cd file-ingestion
./mvnw spring-boot:run
```

```bash
cd imei-status-app
npm install
cp .env.example .env
npm run dev
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

Open the URL printed by Vite. API docs for the Java services are at:

- http://localhost:8080/swagger-ui.html
- http://localhost:8081/swagger-ui.html

The H2 console for the registry is at http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:test`, user `sa`, empty password).

## Single IMEI

Look up a 15-digit IMEI on the home page, then block or unblock it.

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/api/v1/imei/{imei}` | Full record |
| `GET` | `/api/v1/imei/{imei}/status` | Status only |
| `POST` | `/api/v1/imei/block` | Set status to `Blocked` |
| `POST` | `/api/v1/imei/unblock` | Set status to `Unblocked` |
| `POST` | `/api/v1/imei/batch` | Apply a list of block or unblock rows |

A block or unblock body looks like this:

```json
{
  "userName": "Asha",
  "mobileModel": "Phone X",
  "imeiNumber": "356938035643809",
  "telecomCompany": "Demo Telecom",
  "action": "BLOCK"
}
```

`action` is `BLOCK` or `UNBLOCK`. The IMEI must be exactly 15 digits. If the IMEI is new, the registry creates the record. If it already exists, the registry updates the status.

## CSV upload

`POST /api/v1/file/upload` accepts a multipart file named `file`. The first row is the header. Every data row has five columns:

```csv
userName,mobileModel,imeiNumber,telecomCompany,action
Asha,Phone X,356938035643809,Demo Telecom,BLOCK
Ravi,Phone Y,490154203237518,Demo Telecom,UNBLOCK
```

File ingestion checks that each row is complete, the IMEI is 15 digits, and the action is `BLOCK` or `UNBLOCK`. Valid rows are sent to the registry in batches of 10. The response lists how many rows succeeded and why any row failed.

## UI configuration

`imei-status-app/.env` can override the API addresses. Defaults:

- `VITE_IMEI_API_BASE_URL=http://localhost:8080/api/v1/imei`
- `VITE_CSV_UPLOAD_ENDPOINT=http://localhost:8081/api/v1/file/upload`

Restart the Vite dev server after changing `.env`.
