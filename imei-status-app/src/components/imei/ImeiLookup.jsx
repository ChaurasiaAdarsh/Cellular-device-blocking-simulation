import { RefreshCw, Search } from "lucide-react";

export default function ImeiLookup({
  imeiInput,
  setImeiInput,
  loading,
  error,
  onSubmit,
}) {
  return (
    <section className="mx-auto w-full max-w-5xl space-y-5 px-3 py-6 sm:space-y-6 sm:px-5 sm:py-8 md:py-10">
      <div className="space-y-2">
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-indigo-600">
          Device Lookup
        </p>
        <h2 className="text-xl font-bold tracking-tight text-slate-900 sm:text-2xl md:text-3xl">
          Check IMEI status
        </h2>
        <p className="max-w-xl text-sm leading-6 text-slate-500 sm:text-base">
          Enter a 15-digit IMEI to view the registered device.
        </p>
      </div>

      <form
        onSubmit={onSubmit}
        className="flex min-w-0 flex-col gap-3 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-5 md:flex-row md:items-center"
      >
        <label className="sr-only" htmlFor="imei">IMEI number</label>
        <div className="relative min-w-0 flex-1">
          <Search
            className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
            size={18}
          />
          <input
            id="imei"
            value={imeiInput}
            onChange={(event) => setImeiInput(event.target.value.replace(/\D/g, "").slice(0, 15))}
            inputMode="numeric"
            autoComplete="off"
            maxLength={15}
            placeholder="Enter 15-digit IMEI number"
            className="min-h-11 w-full min-w-0 rounded-lg border border-slate-300 bg-white py-3 pl-10 pr-3 text-sm outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100"
          />
        </div>
        <button
          type="submit"
          disabled={loading}
          className="inline-flex min-h-11 w-full shrink-0 items-center justify-center gap-2 rounded-lg bg-indigo-600 px-5 py-3 text-sm font-semibold text-white shadow-sm transition-colors hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 disabled:cursor-not-allowed disabled:bg-slate-300 disabled:shadow-none md:w-auto"
        >
          {loading && <RefreshCw className="animate-spin" size={16} />}
          {loading ? "Checking..." : "Check IMEI"}
        </button>
      </form>

      {error && (
        <div
          role="alert"
          className="min-w-0 break-words rounded-xl border border-red-200 bg-red-50 p-3 text-sm leading-5 text-red-700 [overflow-wrap:anywhere] sm:p-4"
        >
          {error}
        </div>
      )}
    </section>
  );
}
