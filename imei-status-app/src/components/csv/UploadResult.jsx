import { CheckCircle2 } from "lucide-react";

export default function UploadResult({ result }) {
  if (!result) return null;

  return (
    <section className="mx-auto mt-5 w-full max-w-4xl rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:mt-6 sm:p-6 md:p-8">
      <div className="flex min-w-0 items-start gap-3">
        <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-emerald-100 text-emerald-700">
          <CheckCircle2 size={21} />
        </div>
        <div className="min-w-0">
          <h3 className="text-base font-semibold text-slate-900 sm:text-lg">
            Processing completed
          </h3>
          <p className="mt-1 text-sm leading-5 text-slate-500">
            Here is the summary of your uploaded device records.
          </p>
        </div>
      </div>

      <div className="mt-5 grid grid-cols-1 gap-3 sm:mt-6 sm:grid-cols-2 sm:gap-4 lg:grid-cols-3">
        <StatCard
          label="Total rows"
          value={result.totalRows}
          description="Records processed"
          className="border-slate-200 bg-slate-50"
          labelClassName="text-slate-500"
          valueClassName="text-slate-900"
          descriptionClassName="text-slate-500"
        />
        <StatCard
          label="Successful"
          value={result.successCount}
          description="Records completed successfully"
          className="border-emerald-200 bg-emerald-50/70"
          labelClassName="text-emerald-700"
          valueClassName="text-emerald-700"
          descriptionClassName="text-emerald-700"
        />
        <StatCard
          label="Failed"
          value={result.failureCount}
          description="Records requiring attention"
          className="border-red-200 bg-red-50/70"
          labelClassName="text-red-700"
          valueClassName="text-red-700"
          descriptionClassName="text-red-700"
        />
      </div>
    </section>
  );
}

function StatCard({
  label,
  value,
  description,
  className,
  labelClassName,
  valueClassName,
  descriptionClassName,
}) {
  return (
    <div className={`min-w-0 rounded-xl border p-4 sm:p-5 ${className}`}>
      <p className={`text-sm font-medium ${labelClassName}`}>{label}</p>
      <p className={`mt-2 break-words text-2xl font-bold tracking-tight sm:text-3xl ${valueClassName}`}>
        {value ?? 0}
      </p>
      <p className={`mt-1 text-xs leading-5 ${descriptionClassName}`}>{description}</p>
    </div>
  );
}
