export function isValidImei(value) {
  return /^\d{15}$/.test(value);
}
