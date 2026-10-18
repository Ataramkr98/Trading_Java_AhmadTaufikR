const API_BASE: string = (import.meta as { env?: Record<string, string> }).env?.VITE_API_BASE ?? "http://localhost:8080/api/v1";

export class ApiError extends Error {
  readonly status: number;
  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

async function parseError(response: Response): Promise<string> {
  try {
    const body = (await response.json()) as { detail?: string; error?: string; title?: string };
    return body.detail ?? body.error ?? body.title ?? `Request failed (${response.status})`;
  } catch {
    return `Request failed (${response.status})`;
  }
}

export async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, { headers: { Accept: "application/json" } });
  if (!response.ok) throw new ApiError(await parseError(response), response.status);
  return (await response.json()) as T;
}

export async function postJson<T>(path: string, body?: unknown): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (!response.ok) throw new ApiError(await parseError(response), response.status);
  if (response.status === 204 || response.headers.get("content-length") === "0") return undefined as T;
  return (await response.json()) as T;
}

export async function deleteJson<T>(path: string, body?: unknown): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    method: "DELETE",
    headers: { "Content-Type": "application/json", Accept: "application/json" },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (!response.ok) throw new ApiError(await parseError(response), response.status);
  if (response.status === 204 || response.headers.get("content-length") === "0") return undefined as T;
  return (await response.json()) as T;
}

/** Map UI timeframes (1m..1M) onto backend timeframes (1m, 15m, 1D). */
export function mapTimeframe(timeframe: string): "1m" | "15m" | "1D" {
  if (timeframe === "1m") return "1m";
  if (timeframe === "1D" || timeframe === "1W" || timeframe === "1M") return "1D";
  return "15m";
}
