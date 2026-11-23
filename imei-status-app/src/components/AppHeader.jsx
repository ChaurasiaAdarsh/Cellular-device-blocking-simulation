
import { Link, NavLink } from "react-router-dom";

export default function AppHeader() {
  return (
    <header className="sticky top-0 z-10 border-b border-slate-200 bg-white">
      <div className="mx-auto flex max-w-5xl flex-wrap items-center justify-between gap-3 px-5 py-4">
        <Link to="/" className="text-lg font-semibold text-slate-900">
          IMEI Status
          <span className="block text-sm font-normal text-slate-500">
            5G device block and unblock
          </span>
        </Link>

        <nav className="flex gap-2 text-sm font-medium">
          <NavLink
            to="/"
            end
            className={({ isActive }) =>
              `rounded-lg px-3 py-2 ${
                isActive
                  ? "bg-indigo-50 text-indigo-700"
                  : "text-slate-600 hover:bg-slate-100"
              }`
            }
          >
            Device Lookup
          </NavLink>

          <NavLink
            to="/upload"
            className={({ isActive }) =>
              `rounded-lg px-3 py-2 ${
                isActive
                  ? "bg-indigo-50 text-indigo-700"
                  : "text-slate-600 hover:bg-slate-100"
              }`
            }
          >
            Upload CSV
          </NavLink>
        </nav>
      </div>
    </header>
  );
}
