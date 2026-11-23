import { RefreshCw, Search, ShieldAlert, ShieldCheck, Smartphone } from "lucide-react";

export default function DeviceDetails({ record, updating, onToggleStatus }) {
  const isBlocked = record?.status === "BLOCKED";

  if (!record) {
    return (
      <div className="mx-auto mb-8 w-[calc(100%-1.5rem)] max-w-5xl rounded-2xl border border-dashed border-slate-300 bg-white px-4 py-10 text-center sm:mb-10 sm:w-[calc(100%-2.5rem)] sm:px-6 sm:py-12">
        <div className="mx-auto flex h-11 w-11 items-center justify-center rounded-full bg-slate-100 text-slate-500">
          <Search size={20} />
        </div>
        <p className="mt-3 text-sm font-semibold text-slate-700">No device selected</p>
        <p className="mx-auto mt-1 max-w-sm text-sm leading-5 text-slate-500">
          IMEI details and status will appear here.
        </p>
      </div>
    );
  }

  return (
    <section className="mx-auto mb-8 w-[calc(100%-1.5rem)] max-w-5xl min-w-0 overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm sm:mb-10 sm:w-[calc(100%-2.5rem)]">
      <div className="flex min-w-0 flex-col gap-4 border-b border-slate-200 p-4 sm:p-5 md:flex-row md:items-center md:justify-between">
        <div className="flex min-w-0 items-start gap-3">
          <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-indigo-50 text-indigo-600">
            <Smartphone size={21} />
          </div>
          <div className="min-w-0 flex-1">
            <h3 className="break-words text-base font-semibold text-slate-900 sm:text-lg">
              {record.model}
            </h3>
            <p className="mt-1 break-all text-sm text-slate-500">IMEI: {record.imei}</p>
          </div>
        </div>

        <div
          className={`inline-flex w-fit max-w-full shrink-0 items-center gap-2 rounded-full px-3 py-1.5 text-sm font-semibold ${
            isBlocked ? "bg-red-50 text-red-700" : "bg-emerald-50 text-emerald-700"
          }`}
        >
          {isBlocked ? (
            <ShieldAlert size={16} className="shrink-0" />
          ) : (
            <ShieldCheck size={16} className="shrink-0" />
          )}
          {isBlocked ? "Blocked" : "Unblocked"}
        </div>
      </div>

      <div className="grid grid-cols-1 gap-5 p-4 sm:grid-cols-2 sm:p-5 md:gap-6">
        <InfoField label="Username" value={record.username} />
        <InfoField label="Telecom company" value={record.telecom} />
      </div>

      <div className="flex flex-col gap-4 border-t border-slate-200 bg-slate-50 p-4 sm:p-5 md:flex-row md:items-center md:justify-between">
        <p className="min-w-0 text-sm leading-6 text-slate-600">
          {isBlocked
            ? "This device is currently blocked on the network."
            : "This device is currently allowed on the network."}
        </p>
        <button
          type="button"
          onClick={onToggleStatus}
          disabled={updating}
          className={`inline-flex min-h-11 w-full shrink-0 items-center justify-center gap-2 rounded-lg px-5 py-3 text-sm font-semibold text-white shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-60 md:w-auto ${
            isBlocked
              ? "bg-emerald-700 hover:bg-emerald-800 focus:ring-emerald-500"
              : "bg-red-700 hover:bg-red-800 focus:ring-red-500"
          }`}
        >
          {updating && <RefreshCw size={16} className="animate-spin" />}
          {updating ? "Updating..." : isBlocked ? "Unblock device" : "Block device"}
        </button>
      </div>
    </section>
  );
}

function InfoField({ label, value }) {
  return (
    <div className="min-w-0">
      <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">{label}</p>
      <p className="mt-1 break-words text-sm font-medium text-slate-800">{value}</p>
    </div>
  );
}
