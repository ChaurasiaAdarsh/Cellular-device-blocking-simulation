# IMEI Status

A React + Vite frontend for uploading device CSV files, checking an IMEI, and blocking/unblocking a device.

## Requirements

- Node.js 18 or newer
- The IMEI API running at `http://localhost:8080`
- The CSV upload API running at `http://localhost:8081`

## Run locally

```bash
npm install
cp .env.example .env
npm run dev
```

Open the local URL printed by Vite.

## API configuration

The app uses these environment variables (optional; defaults are shown):

- `VITE_IMEI_API_BASE_URL` — `http://localhost:8080/api/v1/imei`
- `VITE_CSV_UPLOAD_ENDPOINT` — `http://localhost:8081/api/v1/file/upload`

If your backend uses different URLs, update `.env` and restart the Vite dev server.

## Project structure

```text
imei-status-app/
├── public/
├── src/
│   ├── components/
│   │   ├── AppHeader.jsx
│   │   ├── csv/
│   │   │   ├── CsvUpload.jsx
│   │   │   └── UploadResult.jsx
│   │   └── imei/
│   │       ├── DeviceDetails.jsx
│   │       └── ImeiLookup.jsx
│   ├── pages/
│   │   ├── Home.jsx
│   │   └── UploadPage.jsx
│   ├── services/
│   │   ├── fileService.js
│   │   └── imeiService.js
│   ├── utils/
│   │   └── validation.js
│   ├── App.jsx
│   ├── index.css
│   └── main.jsx
├── .env.example
├── index.html
├── package.json
├── postcss.config.js
├── tailwind.config.js
└── vite.config.js
```
