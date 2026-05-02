import type { PetIntakeRequest, PetResponse } from "../types/pet";

const API_BASE =
  (typeof process !== "undefined" && process.env.API_BASE_URL) ||
  "http://localhost:3030";

export async function getPets(params?: {
  species?: string;
  status?: string;
}): Promise<PetResponse[]> {
  const url = new URL(`${API_BASE}/api/v1/pets`);
  if (params?.species) url.searchParams.set("species", params.species);
  if (params?.status) url.searchParams.set("status", params.status);

  const res = await fetch(url.toString());
  if (!res.ok) throw new Error(`Failed to fetch pets: ${res.status}`);
  return res.json();
}

export async function getPetById(id: string): Promise<PetResponse> {
  const res = await fetch(`${API_BASE}/api/v1/pets/${id}`);
  if (!res.ok) throw new Error(`Pet not found: ${res.status}`);
  return res.json();
}

export async function createPet(
  pet: PetIntakeRequest,
  idempotencyKey: string
): Promise<PetResponse> {
  const res = await fetch(`${API_BASE}/api/v1/pets`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-Idempotency-Key": idempotencyKey,
    },
    body: JSON.stringify(pet),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message ?? `Failed to create pet: ${res.status}`);
  }

  return res.json();
}
