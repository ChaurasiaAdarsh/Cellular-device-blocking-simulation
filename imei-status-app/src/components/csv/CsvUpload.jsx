import { AlertCircle, FileText, RefreshCw, Upload } from "lucide-react";

export default function CsvUpload({
  csvFile,
  setCsvFile,
  uploading,
  uploadError,
  setUploadError,
  onSubmit,
}) {
  return (
    <>
      <div className="mx-auto w-full max-w-4xl space-y-5 px-3 sm:space-y-6 sm:px-5 lg:px-0">
        <div className="mt-4 space-y-2 sm:mt-[18px]">
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-indigo-600">
            Device Management
          </p>
          <h2 className="text-xl font-bold tracking-tight text-slate-900 sm:text-2xl md:text-3xl">
            Upload device records
          </h2>
          <p className="max-w-2xl text-sm leading-6 text-slate-500 sm:text-base">
            Upload a CSV file to process device block and unblock requests.
          </p>
        </div>

        <form
          onSubmit={onSubmit}
          className="min-w-0 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6 md:p-8"
        >
          <div className="mb-5 sm:mb-6">
            <h3 className="text-base font-semibold text-slate-900">Import CSV file</h3>
            <p className="mt-1 text-sm leading-5 text-slate-500">
              Select a CSV file containing your device records.
            </p>
          </div>

          <div className="rounded-xl border-2 border-dashed border-slate-300 bg-slate-50 p-4 transition-colors hover:border-indigo-400 hover:bg-indigo-50/30 sm:p-6 md:p-8">
            <div className="flex min-w-0 flex-col items-center text-center">
              <div className="mb-4 flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-indigo-100 text-indigo-600">
                <Upload size={24} />
              </div>
              <p className="text-sm font-semibold text-slate-800">Choose a CSV file to upload</p>
              <p className="mt-1 text-xs text-slate-500">CSV format only</p>

             <div className="mt-5 flex w-full justify-center">
                <input
                  key={csvFile?.name ?? "empty"}
                  type="file"
                  accept=".csv,text/csv"
                  onChange={(event) => {
                    setCsvFile(event.target.files?.[0] || null);
                    setUploadError("");
                  }}
                  className="block w-fit max-w-full cursor-pointer text-xs text-slate-600
                    file:mr-3 file:cursor-pointer file:rounded-lg file:border-0
                    file:bg-white file:px-4 file:py-2.5 file:text-sm
                    file:font-semibold file:text-slate-700 file:shadow-sm
                    file:ring-1 file:ring-slate-200 hover:file:bg-slate-100"
                />
             </div>

              {csvFile && (
                <div className="mt-4 flex w-full max-w-md min-w-0 items-start gap-2 rounded-lg border border-indigo-100 bg-white px-3 py-2">
                  <FileText size={18} className="mt-0.5 shrink-0 text-indigo-600" />
                  <span className="min-w-0 break-all text-left text-sm font-medium text-slate-700">
                    {csvFile.name}
                  </span>
                </div>
              )}
            </div>
          </div>

          <div className="mt-5 flex flex-col gap-4 border-t border-slate-100 pt-5 sm:mt-6 sm:flex-row sm:items-center sm:justify-between">
            <p className="text-xs leading-5 text-slate-500 sm:max-w-xs">
              Ensure your CSV contains valid device records.
            </p>
            <button
              type="submit"
              disabled={!csvFile || uploading}
              className="inline-flex min-h-11 w-full shrink-0 items-center justify-center gap-2 rounded-lg bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-sm transition-colors hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 disabled:cursor-not-allowed disabled:bg-slate-300 disabled:shadow-none sm:w-auto"
            >
              {uploading ? (
                <RefreshCw size={16} className="animate-spin" />
              ) : (
                <Upload size={16} />
              )}
              {uploading ? "Uploading..." : "Upload CSV"}
            </button>
          </div>
        </form>

        {uploadError && (
          <div
            role="alert"
            className="flex min-w-0 items-start gap-3 rounded-xl border border-red-200 bg-red-50 p-3 sm:p-4"
          >
            <AlertCircle size={20} className="mt-0.5 shrink-0 text-red-600" />
            <div className="min-w-0 flex-1">
              <p className="text-sm font-semibold text-red-800">Upload failed</p>
              <p className="mt-1 break-words text-sm leading-5 text-red-700 [overflow-wrap:anywhere]">
                {uploadError}
              </p>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
