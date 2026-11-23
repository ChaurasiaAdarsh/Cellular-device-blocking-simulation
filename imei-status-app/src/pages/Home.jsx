import { useState } from "react";
import AppHeader from "../components/AppHeader.jsx";
import DeviceDetails from "../components/imei/DeviceDetails.jsx";
import ImeiLookup from "../components/imei/ImeiLookup.jsx";
import { fetchImeiRecord, updateImeiStatus } from "../services/imeiService.js";
import { isValidImei } from "../utils/validation.js";

export default function Home() {
  const [imeiInput, setImeiInput] = useState("");
  const [record, setRecord] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [updating, setUpdating] = useState(false);

  async function handleImeiLookup(event) {
    event.preventDefault();
    const imei = imeiInput.trim();
    setRecord(null);
    setError("");

    if (!isValidImei(imei)) {
      setError("Please enter a valid 15-digit IMEI number.");
      return;
    }

    setLoading(true);
    try {
      const data = await fetchImeiRecord(imei);
      setRecord(data);
    } catch (err) {
      setError(err.message || "Could not fetch IMEI details.");
    } finally {
      setLoading(false);
    }
  }

  async function handleToggleStatus() {
    if (!record) return;
    setUpdating(true);
    setError("");

    try {
      const result = await updateImeiStatus(record);
      setRecord(result.record);
    } catch (err) {
      setError(err.message || "Status could not be updated.");
    } finally {
      setUpdating(false);
    }
  }

  return (
    <main className="min-h-screen bg-slate-50 text-slate-900">
      <AppHeader />
      <ImeiLookup
        imeiInput={imeiInput}
        setImeiInput={setImeiInput}
        loading={loading}
        error={error}
        onSubmit={handleImeiLookup}
      />
      <DeviceDetails
        record={record}
        updating={updating}
        onToggleStatus={handleToggleStatus}
      />
    </main>
  );
}
