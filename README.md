# Mobile Device IMEI Blocking & Unblocking Simulation

An architectural simulation and demonstration platform for mobile device IMEI lifecycle management (Blocking and Unblocking) across 4G and 5G cellular network concepts.

> **Note:** This project is an independent educational simulation and proof-of-concept created solely for learning and exploring systems architecture. It does not interface with live telecommunications carriers, cellular core networks (EIR/HSS/UDM), or physical radio devices. All device states are simulated in an in-memory datastore.

---

## 1. System Overview & Architecture

In mobile telecommunications networks, an **IMEI (International Mobile Equipment Identity)** is a unique 15-digit identifier allocated to mobile equipment. Equipment Identity Registers (EIR) maintain lists (white, grey, black) to permit or deny network access (e.g., when a device is reported lost or stolen).

This simulation models the end-to-end workflow:
1. **Interactive Device Lookup & Toggle**: Real-time querying and immediate status switching for individual devices.
2. **Bulk File Ingestion**: Uploading structured CSV files, performing row-level validation, chunking records into batches, and asynchronously applying status updates.
3. **Registry Store**: Central in-memory registry maintaining device records and transaction audit timestamps.

### System Diagram

```
+--------------------------------------------------------+
|             imei-status-app (React + Vite)             |
|  - Device Lookup & Status Toggle                       |
|  - Batch CSV Upload with Live Result Table             |
+---------------------------+----------------------------+
                            |
             +--------------+--------------+
             |                             |
      Direct Lookup / Update         CSV Upload File
             |                             |
             v                             v
+--------------------------+  +--------------------------+
|      imei-registry       |  |      file-ingestion      |
|  (Spring Boot :8080)     |  |  (Spring Boot :8081)     |
|  - RESTful Endpoints     |  |  - CSV Parser & Validator|
|  - In-Memory H2 DB       |<----+ Async Batch Chunks    |
|  - Audit Timestamps      |  |  - Result Aggregator     |
+--------------------------+  +--------------------------+
```

---

## 2. Project Modules

| Directory | Technology | Default Port | Description |
| :--- | :--- | :--- | :--- |
| `imei-registry` | Java 21, Spring Boot 4, Spring Data JPA, H2 | `8080` | Stores simulated device status (`Blocked` / `Unblocked`), history, and batch operations. |
| `file-ingestion` | Java 21, Spring Boot 4, CompletableFuture | `8081` | Ingests CSV files, validates format and IMEI constraints, and streams batch chunks to the registry. |
| `imei-status-app` | React 18, Vite 6, Tailwind CSS, Lucide | `5173` | Modern web interface for checking device state and uploading batch spreadsheets. |
| `sample-data` | CSV | N/A | Ready-to-use sample CSV files for testing valid ingestions and error scenarios. |

---

## 3. Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher
- **Node.js**: Version 18.x or higher
- **npm**: Version 9.x or higher
- **Maven**: (Optional; included `mvnw` wrapper can be used)

---

## 4. Getting Started

Run each service in a separate terminal window in the order listed below.

### Step 1: Start IMEI Registry Service (Port 8080)

```bash
cd imei-registry
# On Linux/macOS:
./mvnw spring-boot:run
# On Windows:
.\mvnw.cmd spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Database Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:test`
  - User: `sa`
  - Password: *(leave blank)*

### Step 2: Start File Ingestion Service (Port 8081)

```bash
cd file-ingestion
# On Linux/macOS:
./mvnw spring-boot:run
# On Windows:
.\mvnw.cmd spring-boot:run
```

- Swagger UI: http://localhost:8081/swagger-ui.html

### Step 3: Start the Web UI (Port 5173)

```bash
cd imei-status-app
npm install
npm run dev
```

Open your browser to the URL printed by Vite (typically http://localhost:5173).

---

## 5. API Reference

### IMEI Registry Service (`http://localhost:8080`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/imei/{imei}` | Retrieve full device record by 15-digit IMEI. |
| `GET` | `/api/v1/imei/{imei}/status` | Retrieve only current status (`Blocked` or `Unblocked`). |
| `POST` | `/api/v1/imei/block` | Mark device status as `Blocked`. |
| `POST` | `/api/v1/imei/unblock` | Mark device status as `Unblocked`. |
| `POST` | `/api/v1/imei/batch` | Process an array of block/unblock actions. |

#### Request Payload (`/api/v1/imei/block` or `/unblock`)

```json
{
  "userName": "Asha Sharma",
  "mobileModel": "Galaxy S24 Ultra",
  "imeiNumber": "356938035643809",
  "telecomCompany": "AeroTel 5G",
  "action": "BLOCK"
}
```

### File Ingestion Service (`http://localhost:8081`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/file/upload` | Multipart form upload (`file` key) accepting a `.csv` file. |

---

## 6. CSV Format & Sample Testing

A valid CSV file must include the following header and schema:

```csv
userName,mobileModel,imeiNumber,telecomCompany,action
Asha Sharma,Galaxy S24 Ultra,356938035643809,AeroTel 5G,BLOCK
Ravi Verma,iPhone 15 Pro,490154203237518,NovaCom,UNBLOCK
```

- **Validation Rules**:
  - `userName`, `mobileModel`, and `telecomCompany` must not be blank.
  - `imeiNumber` must be exactly 15 numeric digits.
  - `action` must be either `BLOCK` or `UNBLOCK` (case-insensitive).

Pre-configured sample files are available in the [`sample-data/`](sample-data/) folder:
- `sample_imei_records.csv`: Full valid set to verify multi-batch processing.
- `sample_with_validation_errors.csv`: Includes deliberate errors to test error reporting.

---

## 7. Configuration

Frontend configuration can be adjusted via `imei-status-app/.env`:

```env
VITE_IMEI_API_BASE_URL=http://localhost:8080/api/v1/imei
VITE_CSV_UPLOAD_ENDPOINT=http://localhost:8081/api/v1/file/upload
```

Backend service properties can be modified in:
- `imei-registry/src/main/resources/application.properties`
- `file-ingestion/src/main/resources/application.properties` (e.g. `registry.batch-size=10`)

---

## 8. Repository Structure

```
Blocking app/
├── .github/                      # GitHub templates & workflows
├── file-ingestion/               # Batch file ingestion microservice
│   ├── src/main/java/org/dep/fileingestion/
│   │   ├── client/               # HTTP client communicating with registry
│   │   ├── config/               # Thread pool and RestTemplate configuration
│   │   ├── controller/           # REST controller for CSV upload
│   │   ├── dto/                  # Data transfer objects
│   │   ├── exception/            # Exception handlers
│   │   └── service/              # CSV parser and data validator
│   └── pom.xml
├── imei-registry/                # Core registry microservice
│   ├── src/main/java/org/dep/imeiregistry/
│   │   ├── controller/           # Lookup and block/unblock endpoints
│   │   ├── dto/                  # Request/response DTOs
│   │   ├── exception/            # Exception handlers
│   │   ├── model/                # JPA entity with audit hooks
│   │   ├── repository/           # Spring Data JPA repository
│   │   └── service/              # Business logic implementation
│   └── pom.xml
├── imei-status-app/              # React frontend application
│   ├── src/
│   │   ├── components/           # Reusable UI widgets
│   │   ├── pages/                # Home and Upload pages
│   │   └── services/             # API integration clients
│   ├── package.json
│   └── vite.config.js
├── sample-data/                  # Test CSV data files
├── .gitattributes
├── .gitignore
├── CONTRIBUTING.md
└── README.md
```

---

## 9. License

This repository is distributed under the [MIT License](LICENSE).
