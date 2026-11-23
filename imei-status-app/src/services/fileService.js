const CSV_UPLOAD_ENDPOINT =
  import.meta.env.VITE_CSV_UPLOAD_ENDPOINT ??
  "http://localhost:8081/api/v1/file/upload";

export async function uploadCsvFile(file) {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(CSV_UPLOAD_ENDPOINT, {
    method: "POST",
    body: formData,
  });

  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.message || `Upload failed: ${response.status}`);
  }

  return data;
}
