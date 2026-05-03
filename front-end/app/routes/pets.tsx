import type { Route } from "./+types/pets";
import { Link, useNavigate } from "react-router";
import { useState } from "react";
import { getPets } from "../api/pets";
import { Species, PetStatus } from "../types/pet";
import type { PetResponse } from "../types/pet";
import Header from "../components/Header";
import Footer from "../components/Footer";

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
    { title: "Browse Pets — PetCore" },
    { name: "description", content: "Browse and filter pets available for adoption." },
  ];
}

const STATUS_COLORS: Record<string, { bg: string; text: string }> = {
  AVAILABLE:    { bg: "var(--color-success-light)", text: "var(--color-success-text)" },
  PENDING:      { bg: "var(--color-warning-light)", text: "var(--color-warning-text)" },
  ADOPTED:      { bg: "var(--color-info-light)",    text: "var(--color-info-text)" },
  FOSTER:       { bg: "#ede9fe",                    text: "#5b21b6" },
  HOLD:         { bg: "#fff7ed",                    text: "#9a3412" },
  MEDICAL_HOLD: { bg: "#fee2e2",                    text: "#991b1b" },
  UNAVAILABLE:  { bg: "var(--color-gray-100)",      text: "var(--color-gray-600)" },
};

const SPECIES_EMOJI: Record<string, string> = {
  DOG: "🐶", CAT: "🐱", RABBIT: "🐰", BIRD: "🐦", OTHER: "🐾",
};

const SELECT_STYLE: React.CSSProperties = {
  padding: "0.7rem 1rem",
  border: "2px solid var(--border-input)",
  borderRadius: "var(--radius-lg)",
  fontSize: "0.92rem",
  fontFamily: "inherit",
  outline: "none",
  background: "var(--bg-card)",
  color: "var(--fg1)",
  width: "100%",
  cursor: "pointer",
};

export default function Pets({ loaderData }: Route.ComponentProps) {
  const { pets, species, status, error } = loaderData;
  const navigate = useNavigate();
  const [hov, setHov] = useState<string | null>(null);

  function handleFilter(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    const form = new FormData(e.currentTarget);
    const params = new URLSearchParams();
    const s = form.get("species") as string;
    const st = form.get("status") as string;
    if (s) params.set("species", s);
    if (st) params.set("status", st);
    navigate(`/pets?${params.toString()}`);
  }

  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <Header />

      <main
        style={{
          flex: 1,
          background: "linear-gradient(160deg, var(--bg-page) 0%, var(--color-gray-100) 100%)",
          padding: "2rem 0 3rem",
        }}
      >
        <div className="container">
          {/* Page heading */}
          <div style={{ textAlign: "center", marginBottom: "2.5rem" }}>
            <h1
              style={{
                fontSize: "2.5rem",
                color: "var(--fg1)",
                marginBottom: "0.6rem",
                marginTop: 0,
              }}
            >
              Find Your Perfect Pet
            </h1>
            <p style={{ fontSize: "1.05rem", color: "var(--fg2)", maxWidth: 560, margin: "0 auto" }}>
              Browse our shelter animals and filter by species or status
            </p>
          </div>

          {/* Filter panel */}
          <form
            onSubmit={handleFilter}
            style={{
              background: "var(--bg-card)",
              borderRadius: "var(--radius-xl)",
              padding: "1.75rem 2rem",
              marginBottom: "2.5rem",
              boxShadow: "var(--shadow-card)",
              border: "1px solid var(--border-default)",
            }}
          >
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr auto auto",
                gap: "1.25rem",
                alignItems: "end",
              }}
            >
              <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
                <label style={{ fontSize: "0.85rem", fontWeight: 600, color: "var(--fg2)" }}>
                  Species
                </label>
                <select name="species" defaultValue={species} style={SELECT_STYLE}>
                  <option value="">All species</option>
                  {Object.values(Species).map((s) => (
                    <option key={s} value={s}>
                      {SPECIES_EMOJI[s]} {s.charAt(0) + s.slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>

              <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
                <label style={{ fontSize: "0.85rem", fontWeight: 600, color: "var(--fg2)" }}>
                  Status
                </label>
                <select name="status" defaultValue={status} style={SELECT_STYLE}>
                  <option value="">All statuses</option>
                  {Object.values(PetStatus).map((s) => (
                    <option key={s} value={s}>
                      {s.replace(/_/g, " ")}
                    </option>
                  ))}
                </select>
              </div>

              <button
                type="submit"
                style={{
                  padding: "0.7rem 1.75rem",
                  background: "var(--gradient-primary)",
                  color: "white",
                  border: "none",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 600,
                  fontSize: "0.92rem",
                  cursor: "pointer",
                  fontFamily: "inherit",
                  whiteSpace: "nowrap",
                  alignSelf: "end",
                }}
              >
                Filter
              </button>

              {(species || status) && (
                <Link
                  to="/pets"
                  style={{
                    padding: "0.7rem 1.25rem",
                    background: "transparent",
                    color: "var(--fg2)",
                    border: "2px solid var(--border-default)",
                    borderRadius: "var(--radius-lg)",
                    fontWeight: 500,
                    fontSize: "0.88rem",
                    textDecoration: "none",
                    textAlign: "center",
                    whiteSpace: "nowrap",
                    alignSelf: "end",
                  }}
                >
                  Clear
                </Link>
              )}
            </div>
          </form>

          {/* Results */}
          <h2 style={{ fontSize: "1.3rem", color: "var(--fg1)", marginBottom: "1.5rem" }}>
            {pets.length} Pet{pets.length !== 1 ? "s" : ""} Found
          </h2>

          {error && (
            <div
              style={{
                padding: "1rem 1.5rem",
                background: "#fef2f2",
                border: "1px solid #fecaca",
                color: "#991b1b",
                borderRadius: "var(--radius-lg)",
                marginBottom: "1.5rem",
                fontSize: "0.9rem",
              }}
            >
              {error}
            </div>
          )}

          {pets.length === 0 && !error ? (
            <div
              style={{
                textAlign: "center",
                padding: "3.5rem",
                background: "var(--bg-card)",
                borderRadius: "var(--radius-xl)",
                boxShadow: "var(--shadow-card)",
                border: "1px solid var(--border-default)",
              }}
            >
              <p style={{ fontSize: "2.5rem", margin: "0 0 0.75rem" }}>🐾</p>
              <p style={{ fontSize: "1.1rem", color: "var(--fg1)", fontWeight: 500, margin: "0 0 0.4rem" }}>
                No pets match your filters
              </p>
              <p style={{ color: "var(--fg2)", fontSize: "0.9rem", margin: 0 }}>
                Try clearing your filters or{" "}
                <Link to="/pets/new" style={{ color: "var(--color-primary)" }}>
                  add a new pet
                </Link>
                .
              </p>
            </div>
          ) : (
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(3,1fr)",
                gap: "1.75rem",
              }}
            >
              {pets.map((pet) => (
                <PetCard
                  key={pet.petId}
                  pet={pet}
                  hovered={hov === pet.petId}
                  onHover={setHov}
                />
              ))}
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}

function PetCard({
  pet,
  hovered,
  onHover,
}: {
  pet: PetResponse;
  hovered: boolean;
  onHover: (id: string | null) => void;
}) {
  const statusStyle = STATUS_COLORS[pet.status] ?? STATUS_COLORS.UNAVAILABLE;
  const emoji = SPECIES_EMOJI[pet.species.toUpperCase()] ?? "🐾";
  const unavailable = pet.status === "ADOPTED" || pet.status === "UNAVAILABLE";

  return (
    <div
      style={{
        background: "var(--bg-card)",
        borderRadius: "var(--radius-2xl)",
        boxShadow: hovered && !unavailable ? "var(--shadow-hover)" : "var(--shadow-card)",
        overflow: "hidden",
        transition: "var(--transition-default)",
        transform: hovered && !unavailable ? "translateY(-4px)" : "none",
        display: "flex",
        flexDirection: "column",
        opacity: unavailable ? 0.72 : 1,
        border: "1px solid var(--border-default)",
      }}
      onMouseEnter={() => onHover(pet.petId)}
      onMouseLeave={() => onHover(null)}
    >
      {/* Banner */}
      <div
        style={{
          background: "var(--gradient-primary)",
          height: 120,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontSize: "3.5rem",
          position: "relative",
        }}
      >
        {emoji}
        {unavailable && (
          <div
            style={{
              position: "absolute",
              inset: 0,
              background: "rgba(0,0,0,0.45)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              color: "white",
              fontWeight: 700,
              fontSize: "1rem",
              letterSpacing: "0.02em",
            }}
          >
            {pet.status.replace(/_/g, " ")}
          </div>
        )}
      </div>

      <div style={{ padding: "1.25rem", flex: 1, display: "flex", flexDirection: "column" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "flex-start",
            marginBottom: "0.5rem",
          }}
        >
          <h3 style={{ fontSize: "1.3rem", fontWeight: 700, color: "var(--fg1)", margin: 0 }}>
            {pet.name}
          </h3>
          {!unavailable && (
            <span
              style={{
                background: statusStyle.bg,
                color: statusStyle.text,
                padding: "3px 10px",
                borderRadius: "var(--radius-pill)",
                fontSize: "0.72rem",
                fontWeight: 600,
                whiteSpace: "nowrap",
              }}
            >
              {pet.status.replace(/_/g, " ")}
            </span>
          )}
        </div>

        <p
          style={{
            fontSize: "0.95rem",
            color: "var(--color-primary)",
            fontWeight: 600,
            margin: "0 0 0.75rem",
          }}
        >
          {pet.species.charAt(0) + pet.species.slice(1).toLowerCase()}
          {pet.breed ? ` · ${pet.breed}` : ""}
        </p>

        <p style={{ fontSize: "0.8rem", color: "var(--fg3)", margin: "auto 0 1rem" }}>
          Added{" "}
          {new Date(pet.createdAt).toLocaleDateString("en-US", {
            month: "short",
            day: "numeric",
            year: "numeric",
          })}
        </p>

        <Link
          to="/pets/new"
          style={{
            display: "block",
            textAlign: "center",
            padding: "0.6rem",
            background: "transparent",
            color: "var(--color-primary)",
            border: "2px solid var(--color-primary)",
            borderRadius: "var(--radius-lg)",
            fontWeight: 600,
            fontSize: "0.88rem",
            textDecoration: "none",
            transition: "var(--transition-default)",
          }}
        >
          View Details
        </Link>
      </div>
    </div>
  );
}
