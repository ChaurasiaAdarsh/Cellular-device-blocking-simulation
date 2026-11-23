const API_BASE_URL =
  import.meta.env.VITE_IMEI_API_BASE_URL ??
  "http://localhost:8080/api/v1/imei";

async function readErrorText(response) {
  try {
    return await response.text();
  } catch {
    return "";
  }
}

export async function fetchImeiRecord(imei) {
  const response = await fetch(`${API_BASE_URL}/${encodeURIComponent(imei)}`);

  if (!response.ok) {
    if (response.status === 404) {
      throw new Error("No device found for this IMEI number.");
    }
    throw new Error(`Request failed: ${response.status}`);
  }

  const data = await response.json();

  return {
    imei: data.imeiNumber ?? imei,
    model: data.mobileModel ?? "Unknown device",
    telecom: data.telecomCompany ?? "Unknown",
    username: data.userName ?? "Unknown",
    status: String(data.status ?? "").toUpperCase(),
  };
}

export async function updateImeiStatus(record) {
  const currentlyBlocked = record.status === "BLOCKED";
  const action = currentlyBlocked ? "Unblock" : "Block";

  const response = await fetch(`${API_BASE_URL}/${action.toLowerCase()}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      userName: record.username,
      mobileModel: record.model,
      imeiNumber: record.imei,
      telecomCompany: record.telecom,
      action,
    }),
  });

  const responseText = await readErrorText(response);

  if (!response.ok) {
    throw new Error(
      `Could not ${action.toLowerCase()} IMEI: ${response.status}. ${responseText}`
    );
  }

  const data = responseText ? JSON.parse(responseText) : {};

  return {
    action,
    record: {
      ...record,
      imei: data.imeiNumber ?? record.imei,
      username: data.userName ?? record.username,
      model: data.mobileModel ?? record.model,
      telecom: data.telecomCompany ?? record.telecom,
      status: String(
        data.status ?? (action === "Block" ? "BLOCKED" : "UNBLOCKED")
      ).toUpperCase(),
    },
  };
}
