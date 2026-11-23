
import { useEffect, useState } from "react";
import Papa from "papaparse";
import AppHeader from "../components/AppHeader.jsx";
import { uploadCsvFile } from "../services/fileService.js";
import { Upload } from "lucide-react";

const API_BASE_URL =
  import.meta.env.VITE_IMEI_API_BASE_URL ??
  "http://localhost:8080/api/v1/imei";

export default function UploadPage() {
  const [csvFile, setCsvFile] = useState(null);
  const [rows, setRows] = useState([]);
  const [uploading, setUploading] = useState(false);
  const [uploadError, setUploadError] = useState("");
  const [updatingRow, setUpdatingRow] = useState(null);
  const [uploadSummary, setUploadSummary] = useState(null);

  function handleFileChange(event) {
    const file = event.target.files?.[0] ?? null;
    setCsvFile(file);
    setRows([]);
    setUploadError("");
    setUploadSummary(null);
  }

  async function handleUpload(event) {
    event.preventDefault();
    if (!csvFile) {
      setUploadError("Please select a CSV file.");
      return;
    }

    setUploading(true);
    setUploadError("");

    try {
      // Parse CSV for display in the table.
      const parsed = await new Promise((resolve, reject) => {
        Papa.parse(csvFile, {
          header: true,
          skipEmptyLines: true,
          transformHeader: (header) => header.trim(),
          complete: resolve,
          error: reject,
        });
      });

      if (parsed.errors?.length) {
        throw new Error(parsed.errors[0].message);
      }

      const csvRows = parsed.data.map((row, index) => ({
        rowNumber: index + 2,
        userName: row.userName ?? row.username ?? "",
        mobileModel: row.mobileModel ?? "",
        imeiNumber: row.imeiNumber ?? row.imeiNumber ?? "",
        telecomCompany: row.telecomCompany ?? "",
        action: String(row.action ?? "").trim(),
        status: "PENDING",
        reason: "",
      }));

      setRows(csvRows);

      // Send the original file to the existing Spring Boot file service.
      const result = await uploadCsvFile(csvFile);

      setUploadSummary({
        totalRows: result.totalRows ?? csvRows.length,
        successCount: result.successCount ?? 0,
        failureCount: result.failureCount ?? 0,
      });

      // Merge backend processing results using the CSV row number.
      const resultByRow = new Map(
        (result.results ?? []).map((item) => [
          Number(item.rowNumber),
          item,
        ])
      );

      setRows(
        csvRows.map((row) => {
          const processed = resultByRow.get(row.rowNumber);
          const success =
            String(processed?.status ?? "").toUpperCase() === "SUCCESS";

          return {
            ...row,
            status: processed?.status ?? "UNKNOWN",
            reason: processed?.reason ?? "",
            // Reflect the requested action only when processing succeeded.
            currentStatus: success
              ? row.action.toLowerCase() === "block"
                ? "BLOCKED"
                : row.action.toLowerCase() === "unblock"
                  ? "UNBLOCKED"
                  : "UNKNOWN"
              : "UNKNOWN",
          };
        })
      );
    } catch (err) {
      setUploadError(err.message || "Could not process the CSV file.");
    } finally {
      setUploading(false);
    }
  }

  async function toggleRow(row) {
    const currentlyBlocked = row.currentStatus === "BLOCKED";
    const action = currentlyBlocked ? "Unblock" : "Block";

    setUpdatingRow(row.rowNumber);
    setUploadError("");

    try {
      const response = await fetch(
        `${API_BASE_URL}/${action.toLowerCase()}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            userName: row.userName,
            mobileModel: row.mobileModel,
            imeiNumber: row.imeiNumber,
            telecomCompany: row.telecomCompany,
            action,
          }),
        }
      );

      const responseText = await response.text();

      if (!response.ok) {
        throw new Error(
          `Could not ${action.toLowerCase()} ${row.imeiNumber}: ` +
          `${response.status} ${responseText}`
        );
      }

      let data = {};
      if (responseText) {
        try {
          data = JSON.parse(responseText);
        } catch {
          // Some successful endpoints return plain text.
        }
      }

      setRows((previousRows) =>
        previousRows.map((item) =>
          item.rowNumber === row.rowNumber
            ? {
                ...item,
                action,
                currentStatus: String(
                  data.status ??
                  (action === "Block" ? "BLOCKED" : "UNBLOCKED")
                ).toUpperCase(),
                status: "SUCCESS",
                reason: data.message ?? `IMEI ${action.toLowerCase()}ed successfully`,
              }
            : item
        )
      );
    } catch (err) {
      setUploadError(err.message || "Could not update this IMEI.");
    } finally {
      setUpdatingRow(null);
    }
  }

  return (
    <main className="min-h-screen bg-slate-50 text-slate-900">
      <AppHeader />

      <section className="mx-auto max-w-6xl space-y-6 px-4 py-8 sm:px-6">
        <div>
          <p className="text-xs font-semibold uppercase tracking-widest text-indigo-600">
            Device Management
          </p>
          <h1 className="mt-2 text-2xl font-bold">Upload CSV records</h1>
          <p className="mt-2 text-sm text-slate-500">
            Upload a CSV file to process records and manage each IMEI.
          </p>
        </div>

        
        <form
        onSubmit={handleUpload}
        className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm sm:p-7"
        >
        <div className="rounded-2xl border-2 border-dashed border-slate-300 bg-slate-50 px-4 py-8 text-center transition hover:border-indigo-300">
            <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-indigo-100 text-indigo-600">
            <Upload size={26} />
            </div>

            <h2 className="mt-4 text-sm font-semibold text-slate-900">
            Choose a CSV file to upload
            </h2>

            <p className="mt-2 text-xs text-slate-500">
            CSV format only
            </p>

            <div className="mt-5 flex w-full justify-center">
            <input
                key={csvFile?.name ?? "empty"}
                type="file"
                accept=".csv,text/csv"
                onChange={handleFileChange}
                className="block w-fit max-w-full cursor-pointer text-xs text-slate-600
                file:mr-3 file:cursor-pointer file:rounded-lg file:border-0
                file:bg-white file:px-4 file:py-2.5 file:text-sm
                file:font-semibold file:text-slate-700 file:shadow-sm
                file:ring-1 file:ring-slate-200 hover:file:bg-slate-100"
            />
            </div>

            {csvFile && (
            <p className="mt-3 break-all text-sm font-medium text-slate-600">
                Selected: {csvFile.name}
            </p>
            )}

            <button
            type="submit"
            disabled={!csvFile || uploading}
            className="mt-5 rounded-lg bg-indigo-600 px-6 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-50"
            >
            {uploading ? "Processing..." : "Upload and process"}
            </button>
        </div>
        </form>


        {uploadError && (
          <div
            role="alert"
            className="break-words rounded-lg border border-red-200 bg-red-50 p-4 text-sm text-red-700"
          >
            {uploadError}
          </div>
        )}

        
        {uploadSummary && (
        <section className="space-y-4">
            <div>
            <h2 className="text-lg font-semibold text-slate-900">
                Upload Summary
            </h2>
            <p className="mt-1 text-sm text-slate-500">
                Results from processing your CSV file.
            </p>
            </div>

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
            <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
                <p className="text-sm font-medium text-slate-500">
                Total Records
                </p>
                <p className="mt-2 text-3xl font-bold text-slate-900">
                {uploadSummary.totalRows}
                </p>
            </div>

            <div className="rounded-2xl border border-emerald-200 bg-emerald-50 p-5">
                <p className="text-sm font-medium text-emerald-700">
                Successful
                </p>
                <p className="mt-2 text-3xl font-bold text-emerald-800">
                {uploadSummary.successCount}
                </p>
            </div>

            <div className="rounded-2xl border border-red-200 bg-red-50 p-5">
                <p className="text-sm font-medium text-red-700">
                Failed
                </p>
                <p className="mt-2 text-3xl font-bold text-red-800">
                {uploadSummary.failureCount}
                </p>
            </div>
            </div>
        </section>
        )}


        {rows.length > 0 && (
          <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">
            <div className="border-b border-slate-200 p-5">
              <h2 className="font-semibold">Uploaded device records</h2>
              <p className="mt-1 text-sm text-slate-500">
                {rows.length} records found in the CSV file.
              </p>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full min-w-[1050px] text-left text-sm">
                <thead className="bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
                  <tr>
                    <th className="px-4 py-3">Row</th>
                    <th className="px-4 py-3">Username</th>
                    <th className="px-4 py-3">Mobile model</th>
                    <th className="px-4 py-3">IMEI</th>
                    <th className="px-4 py-3">Telecom</th>
                    <th className="px-4 py-3">Result</th>
                    <th className="px-4 py-3">Current status</th>
                    <th className="px-4 py-3">Action</th>
                  </tr>
                </thead>

                <tbody className="divide-y divide-slate-100">
                  {rows.map((row) => (
                    <tr key={`${row.rowNumber}-${row.imeiNumber}`}>
                      <td className="px-4 py-3">{row.rowNumber}</td>
                      <td className="px-4 py-3">{row.userName}</td>
                      <td className="px-4 py-3">{row.mobileModel}</td>
                      <td className="px-4 py-3 font-mono">{row.imeiNumber}</td>
                      <td className="px-4 py-3">{row.telecomCompany}</td>
                      <td className="px-4 py-3">
                        <span className={
                          row.status === "SUCCESS"
                            ? "font-medium text-emerald-700"
                            : row.status === "FAILED"
                              ? "font-medium text-red-700"
                              : "text-slate-500"
                        }>
                          {row.status}
                        </span>
                        {row.reason && (
                          <p className="mt-1 max-w-xs whitespace-normal text-xs text-slate-500">
                            {row.reason}
                          </p>
                        )}
                      </td>
                      <td className="px-4 py-3">
                        {row.currentStatus ?? "—"}
                      </td>
                      <td className="px-4 py-3">
                        <button
                          type="button"
                          disabled={
                            updatingRow !== null ||
                            row.status !== "SUCCESS" ||
                            !["BLOCKED", "UNBLOCKED"].includes(row.currentStatus)
                          }
                          onClick={() => toggleRow(row)}
                          className="rounded-lg bg-slate-900 px-3 py-2 text-xs font-semibold text-white hover:bg-slate-700 disabled:cursor-not-allowed disabled:opacity-40"
                        >
                          {updatingRow === row.rowNumber
                            ? "Updating..."
                            : row.currentStatus === "BLOCKED"
                              ? "Unblock"
                              : "Block"}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </section>
    </main>
  );
}
