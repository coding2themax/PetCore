import type { Route } from "./+types/home";
import { Link, useNavigate } from "react-router";
import { getPets } from "../api/pets";
import { Species, PetStatus } from "../types/pet";
import type { PetResponse } from "../types/pet";

export async function loader({ request }: Route.LoaderArgs) {
  const url = new URL(request.url);
  const species = url.searchParams.get("species") ?? undefined;
  const status = url.searchParams.get("status") ?? undefined;

  try {
    const pets = await getPets({ species, status });
    return { pets, species: species ?? "", status: status ?? "", error: null };
  } catch {
    return { pets: [] as PetResponse[], species: "", status: "", error: "Failed to load pets. Is the backend running?" };
  }
}

export function meta() {
  return [
    { title: "PetCore - Pet Rescue" },
    { name: "description", content: "Manage pet intakes and adoptions." },
  ];
}

const STATUS_COLORS: Record<string, string> = {
  AVAILABLE: "bg-green-100 text-green-800",
  PENDING: "bg-yellow-100 text-yellow-800",
  ADOPTED: "bg-blue-100 text-blue-800",
  HOLD: "bg-orange-100 text-orange-800",
  FOSTER: "bg-purple-100 text-purple-800",
  MEDICAL_HOLD: "bg-red-100 text-red-800",
  UNAVAILABLE: "bg-gray-100 text-gray-700",
};

const SPECIES_EMOJI: Record<string, string> = {
  DOG: "🐶",
  CAT: "🐱",
  RABBIT: "🐰",
  BIRD: "🐦",
  OTHER: "🐾",
};

export default function Home({ loaderData }: Route.ComponentProps) {
  const { pets, species, status, error } = loaderData;
  const navigate = useNavigate();

  function handleFilter(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const form = new FormData(e.currentTarget);
    const params = new URLSearchParams();
    const s = form.get("species") as string;
    const st = form.get("status") as string;
    if (s) params.set("species", s);
    if (st) params.set("status", st);
    navigate(`/?${params.toString()}`);
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">PetCore</h1>
          <p className="text-sm text-gray-500">Pet Rescue Management</p>
        </div>
        <Link
          to="/pets/new"
          className="bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors"
        >
          + Add Pet
        </Link>
      </header>

      <main className="max-w-5xl mx-auto px-6 py-8">
        <form onSubmit={handleFilter} className="flex gap-4 mb-8 flex-wrap">
          <div className="flex flex-col gap-1">
            <label className="text-xs font-medium text-gray-600 uppercase tracking-wide">
              Species
            </label>
            <select
              name="species"
              defaultValue={species}
              className="border border-gray-300 rounded-lg px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="">All species</option>
              {Object.values(Species).map((s) => (
                <option key={s} value={s}>
                  {SPECIES_EMOJI[s]} {s.charAt(0) + s.slice(1).toLowerCase()}
                </option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-1">
            <label className="text-xs font-medium text-gray-600 uppercase tracking-wide">
              Status
            </label>
            <select
              name="status"
              defaultValue={status}
              className="border border-gray-300 rounded-lg px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="">All statuses</option>
              {Object.values(PetStatus).map((s) => (
                <option key={s} value={s}>
                  {s.replace(/_/g, " ")}
                </option>
              ))}
            </select>
          </div>

          <div className="flex items-end gap-2">
            <button
              type="submit"
              className="bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors"
            >
              Filter
            </button>
            {(species || status) && (
              <Link
                to="/"
                className="text-sm text-gray-500 hover:text-gray-700 px-4 py-2 rounded-lg border border-gray-300 bg-white transition-colors"
              >
                Clear
              </Link>
            )}
          </div>
        </form>

        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg text-sm">
            {error}
          </div>
        )}

        {pets.length === 0 && !error ? (
          <div className="text-center py-20 text-gray-400">
            <p className="text-4xl mb-3">🐾</p>
            <p className="text-lg font-medium">No pets found</p>
            <p className="text-sm mt-1">
              <Link to="/pets/new" className="text-indigo-600 hover:underline">
                Add the first one
              </Link>
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {pets.map((pet) => (
              <PetCard key={pet.petId} pet={pet} />
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

function PetCard({ pet }: { pet: PetResponse }) {
  const statusColor = STATUS_COLORS[pet.status] ?? "bg-gray-100 text-gray-700";
  const emoji = SPECIES_EMOJI[pet.species.toUpperCase()] ?? "🐾";

  return (
    <div className="bg-white rounded-xl border border-gray-200 p-5 shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-3">
        <div className="flex items-center gap-2">
          <span className="text-2xl">{emoji}</span>
          <div>
            <h3 className="font-semibold text-gray-900">{pet.name}</h3>
            <p className="text-sm text-gray-500 capitalize">
              {pet.species.charAt(0) + pet.species.slice(1).toLowerCase()}
              {pet.breed ? ` · ${pet.breed}` : ""}
            </p>
          </div>
        </div>
        <span
          className={`text-xs font-medium px-2 py-1 rounded-full ${statusColor}`}
        >
          {pet.status.replace(/_/g, " ")}
        </span>
      </div>
      <p className="text-xs text-gray-400 mt-2">
        Added{" "}
        {new Date(pet.createdAt).toLocaleDateString("en-US", {
          month: "short",
          day: "numeric",
          year: "numeric",
        })}
      </p>
    </div>
  );
}
