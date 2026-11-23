# Sample Data for IMEI Simulation

This directory contains test CSV files for trying out the batch file ingestion and verification workflow in the IMEI Block and Unblock Simulation system.

## Files

1. **`sample_imei_records.csv`**
   - 12 well-formed records covering both `BLOCK` and `UNBLOCK` operations.
   - Spans 10+ rows to demonstrate asynchronous batch chunking (default chunk size: 10).
   - All rows should succeed and be reflected in the registry.

2. **`sample_with_validation_errors.csv`**
   - Demonstrates row-level validation and error reporting.
   - Contains a mix of valid rows and invalid rows (e.g., short IMEI, blank fields, unsupported action verb).
   - Allows verifying that valid rows proceed while invalid rows report precise row-numbered errors without aborting the entire batch.

## CSV Format Specification

The CSV file must contain a header row with exactly 5 column names:

```csv
userName,mobileModel,imeiNumber,telecomCompany,action
```

| Field Name | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `userName` | String | Non-empty | Subscriber / device owner name |
| `mobileModel` | String | Non-empty | Device make & model (e.g., "Galaxy S24") |
| `imeiNumber` | String | Exactly 15 digits (`^\d{15}$`) | 15-digit International Mobile Equipment Identity |
| `telecomCompany` | String | Non-empty | Simulated carrier network name |
| `action` | String | `BLOCK` or `UNBLOCK` (case-insensitive) | Desired status transition |
