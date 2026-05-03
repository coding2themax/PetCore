import type { Route } from "./+types/home";
import { Link } from "react-router";
import { useState } from "react";
import { getPets } from "../api/pets";
import type { PetResponse } from "../types/pet";
import Header from "../components/Header";
import Footer from "../components/Footer";

export async function loader() {
  try {
    const pets = await getPets();
    return { pets: pets.slice(0, 6), error: null };
  } catch {
    return { pets: [] as PetResponse[], error: "Backend unavailable" };
  }
}

export function meta() {
  return [
    { title: "PetCore — Pet Rescue Management" },
    { name: "description", content: "Open-source pet rescue management platform." },
  ];
}

const FEATURES = [
  {
    icon: "🐕",
    title: "Pet Intake",
    desc: "Register stray, surrendered, and transferred animals with full intake records and idempotency guarantees.",
    to: "/pets/new",
  },
  {
    icon: "🔍",
    title: "Browse Pets",
    desc: "Filter and search available animals by species, status, and more. Real-time data from the shelter database.",
    to: "/pets",
  },
  {
    icon: "🤝",
    title: "Matching",
    desc: "Rule-based compatibility scoring helps match pets with the right adopters based on lifestyle and preferences.",
    to: "/pets",
  },
  {
    icon: "💡",
    title: "Expert Guidance",
    desc: "Help potential adopters choose the right pet for their home, budget, and lifestyle with structured guidance.",
    to: "/guidance",
  },
];

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

export default function Home({ loaderData }: Route.ComponentProps) {
  const { pets, error } = loaderData;
  const [hovCard, setHovCard] = useState<number | null>(null);

  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <Header />

      <main style={{ flex: 1, background: "var(--bg-page)" }}>
        {/* Hero */}
        <section
          style={{
            background: "var(--gradient-primary)",
            color: "white",
            padding: "5rem 0 4rem",
            textAlign: "center",
          }}
        >
          <div className="container">
            <h1
              style={{
                fontSize: "3rem",
                fontWeight: 700,
                color: "white",
                marginBottom: "1.25rem",
                lineHeight: 1.15,
              }}
            >
              Modern Pet Rescue Management
            </h1>
            <p
              style={{
                fontSize: "1.2rem",
                opacity: 0.9,
                maxWidth: 580,
                margin: "0 auto 2.5rem",
                lineHeight: 1.65,
                color: "white",
              }}
            >
              Domain-driven, rule-first platform for managing pet intakes,
              profiles, matching, and adoption — built for shelters that need
              reliability over magic.
            </p>
            <div style={{ display: "flex", gap: "1rem", justifyContent: "center", flexWrap: "wrap" }}>
              <Link
                to="/pets"
                style={{
                  background: "white",
                  color: "var(--color-primary)",
                  padding: "0.85rem 2rem",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 700,
                  fontSize: "1rem",
                  textDecoration: "none",
                  transition: "var(--transition-default)",
                  boxShadow: "var(--shadow-md)",
                }}
              >
                Browse Pets
              </Link>
              <Link
                to="/pets/new"
                style={{
                  background: "rgba(255,255,255,0.15)",
                  border: "2px solid rgba(255,255,255,0.5)",
                  color: "white",
                  padding: "0.85rem 2rem",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 600,
                  fontSize: "1rem",
                  textDecoration: "none",
                  transition: "var(--transition-default)",
                }}
              >
                + Add Pet
              </Link>
            </div>
          </div>
        </section>

        {/* Features */}
        <section style={{ padding: "4rem 0", background: "var(--color-gray-50)" }}>
          <div className="container">
            <h2
              style={{
                textAlign: "center",
                fontSize: "1.9rem",
                marginBottom: "0.5rem",
                color: "var(--fg1)",
              }}
            >
              Everything a Shelter Needs
            </h2>
            <p
              style={{
                textAlign: "center",
                color: "var(--fg2)",
                marginBottom: "2.5rem",
                fontSize: "1rem",
              }}
            >
              Deterministic, explainable logic — AI is optional, never critical.
            </p>
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(4,1fr)",
                gap: "1.5rem",
              }}
            >
              {FEATURES.map((f, i) => (
                <Link
                  key={f.title}
                  to={f.to}
                  style={{
                    background: hovCard === i ? "white" : "var(--bg-card)",
                    padding: "1.75rem",
                    borderRadius: "var(--radius-xl)",
                    boxShadow:
                      hovCard === i ? "var(--shadow-hover)" : "var(--shadow-card)",
                    transition: "var(--transition-default)",
                    cursor: "pointer",
                    transform: hovCard === i ? "translateY(-5px)" : "none",
                    border: "1px solid var(--border-default)",
                    textDecoration: "none",
                    display: "block",
                  }}
                  onMouseEnter={() => setHovCard(i)}
                  onMouseLeave={() => setHovCard(null)}
                >
                  <div style={{ fontSize: "2.2rem", marginBottom: "0.75rem" }}>
                    {f.icon}
                  </div>
                  <h3
                    style={{
                      fontSize: "1.05rem",
                      marginBottom: "0.6rem",
                      color: "var(--fg1)",
                      fontWeight: 600,
                    }}
                  >
                    {f.title}
                  </h3>
                  <p
                    style={{
                      color: "var(--fg2)",
                      lineHeight: 1.6,
                      margin: 0,
                      fontSize: "0.88rem",
                    }}
                  >
                    {f.desc}
                  </p>
                </Link>
              ))}
            </div>
          </div>
        </section>

        {/* Featured Pets */}
        <section style={{ padding: "4rem 0" }}>
          <div className="container">
            <div style={{ textAlign: "center", marginBottom: "2rem" }}>
              <h2 style={{ fontSize: "1.9rem", color: "var(--fg1)", marginBottom: "0.5rem" }}>
                Recently Added Pets
              </h2>
              <p style={{ color: "var(--fg2)", fontSize: "1rem", margin: 0 }}>
                Latest animals in the shelter system
              </p>
            </div>

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
                {error} — is the backend running on port 3030?
              </div>
            )}

            {pets.length === 0 && !error ? (
              <div style={{ textAlign: "center", padding: "3rem", color: "var(--fg3)" }}>
                <p style={{ fontSize: "3rem", margin: "0 0 0.75rem" }}>🐾</p>
                <p style={{ fontSize: "1.1rem", fontWeight: 500, color: "var(--fg2)" }}>
                  No pets yet
                </p>
                <Link
                  to="/pets/new"
                  style={{ color: "var(--color-primary)", fontSize: "0.95rem" }}
                >
                  Add the first one →
                </Link>
              </div>
            ) : (
              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "repeat(3,1fr)",
                  gap: "1.5rem",
                }}
              >
                {pets.map((pet) => (
                  <PetCard key={pet.petId} pet={pet} />
                ))}
              </div>
            )}

            {pets.length > 0 && (
              <div style={{ textAlign: "center", marginTop: "2.5rem" }}>
                <Link
                  to="/pets"
                  style={{
                    padding: "0.85rem 2.5rem",
                    background: "transparent",
                    color: "var(--color-primary)",
                    border: "2px solid var(--color-primary)",
                    borderRadius: "var(--radius-lg)",
                    fontWeight: 600,
                    fontSize: "1rem",
                    textDecoration: "none",
                    transition: "var(--transition-default)",
                    display: "inline-block",
                  }}
                >
                  View All Pets →
                </Link>
              </div>
            )}
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
}

function PetCard({ pet }: { pet: PetResponse }) {
  const [hov, setHov] = useState(false);
  const statusStyle = STATUS_COLORS[pet.status] ?? STATUS_COLORS.UNAVAILABLE;
  const emoji = SPECIES_EMOJI[pet.species.toUpperCase()] ?? "🐾";

  return (
    <div
      style={{
        background: "var(--bg-card)",
        borderRadius: "var(--radius-2xl)",
        boxShadow: hov ? "var(--shadow-hover)" : "var(--shadow-card)",
        overflow: "hidden",
        transition: "var(--transition-default)",
        transform: hov ? "translateY(-4px)" : "none",
        border: "1px solid var(--border-default)",
      }}
      onMouseEnter={() => setHov(true)}
      onMouseLeave={() => setHov(false)}
    >
      {/* Species banner */}
      <div
        style={{
          background: "var(--gradient-primary)",
          height: 80,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontSize: "2.75rem",
        }}
      >
        {emoji}
      </div>

      <div style={{ padding: "1.25rem" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "flex-start",
            marginBottom: "0.5rem",
          }}
        >
          <h3 style={{ fontSize: "1.2rem", fontWeight: 700, color: "var(--fg1)", margin: 0 }}>
            {pet.name}
          </h3>
          <span
            style={{
              background: statusStyle.bg,
              color: statusStyle.text,
              padding: "3px 10px",
              borderRadius: "var(--radius-pill)",
              fontSize: "0.75rem",
              fontWeight: 600,
              whiteSpace: "nowrap",
            }}
          >
            {pet.status.replace(/_/g, " ")}
          </span>
        </div>

        <p style={{ fontSize: "0.9rem", color: "var(--color-primary)", fontWeight: 600, margin: "0 0 0.5rem" }}>
          {pet.species.charAt(0) + pet.species.slice(1).toLowerCase()}
          {pet.breed ? ` · ${pet.breed}` : ""}
        </p>

        <p style={{ fontSize: "0.8rem", color: "var(--fg3)", margin: 0 }}>
          Added{" "}
          {new Date(pet.createdAt).toLocaleDateString("en-US", {
            month: "short",
            day: "numeric",
            year: "numeric",
          })}
        </p>
      </div>
    </div>
  );
}
