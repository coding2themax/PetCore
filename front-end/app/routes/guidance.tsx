import { useState } from "react";
import { Link } from "react-router";
import Header from "../components/Header";
import Footer from "../components/Footer";

export function meta() {
  return [
    { title: "Expert Guidance — PetCore" },
    { name: "description", content: "Help potential adopters find the right pet." },
  ];
}

const CATEGORIES = [
  {
    title: "🏠 Lifestyle Assessment",
    items: [
      "Activity level compatibility",
      "Living space requirements",
      "Time commitment needed",
      "Allergy considerations",
    ],
  },
  {
    title: "💰 Budget Planning",
    items: [
      "Initial adoption cost",
      "Ongoing food and supplies",
      "Veterinary care expenses",
      "Grooming and training costs",
    ],
  },
  {
    title: "🌱 Long-term Commitment",
    items: [
      "Average lifespan by species",
      "Life stage care changes",
      "Travel and boarding needs",
      "Emotional bonding expectations",
    ],
  },
  {
    title: "👨‍👩‍👧 Family Compatibility",
    items: [
      "Pet temperament with children",
      "Multi-pet household dynamics",
      "Age-appropriate pets",
      "Training and socialization",
    ],
  },
];

const PET_TYPES = [
  { icon: "🐕", title: "Dogs",    commitment: "High",    space: "Medium–Large", time: "2–3 hrs/day" },
  { icon: "🐱", title: "Cats",    commitment: "Medium",  space: "Any",          time: "30 min/day" },
  { icon: "🐦", title: "Birds",   commitment: "Medium",  space: "Small",        time: "1–2 hrs/day" },
  { icon: "🐰", title: "Rabbits", commitment: "Medium",  space: "Small–Med",    time: "1 hr/day" },
  { icon: "🐾", title: "Other",   commitment: "Varies",  space: "Varies",       time: "Varies" },
];

export default function Guidance() {
  const [hovCard, setHovCard] = useState<number | null>(null);

  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <Header />

      <main style={{ flex: 1, background: "var(--bg-page)", padding: "2rem 0 3rem" }}>
        <div className="container">
          {/* Hero banner */}
          <div
            style={{
              background: "var(--gradient-primary)",
              color: "white",
              borderRadius: "var(--radius-xl)",
              padding: "2.5rem 2rem",
              textAlign: "center",
              marginBottom: "3rem",
            }}
          >
            <h1
              style={{
                fontSize: "2.4rem",
                margin: "0 0 0.75rem",
                fontWeight: 700,
                color: "white",
              }}
            >
              💡 Expert Guidance
            </h1>
            <p
              style={{
                fontSize: "1.1rem",
                opacity: 0.92,
                maxWidth: 560,
                margin: "0 auto",
                color: "white",
              }}
            >
              Helping potential adopters choose the perfect companion for their
              lifestyle and home.
            </p>
          </div>

          {/* Category cards */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(2,1fr)",
              gap: "1.5rem",
              marginBottom: "3rem",
            }}
          >
            {CATEGORIES.map((cat) => (
              <div
                key={cat.title}
                style={{
                  background: "var(--bg-card)",
                  padding: "1.75rem",
                  borderRadius: "var(--radius-xl)",
                  boxShadow: "var(--shadow-card)",
                  borderLeft: "4px solid var(--color-primary)",
                  border: "1px solid var(--border-default)",
                  borderLeftWidth: 4,
                  borderLeftColor: "var(--color-primary)",
                }}
              >
                <h3
                  style={{
                    color: "var(--fg1)",
                    fontSize: "1.1rem",
                    marginTop: 0,
                    marginBottom: "1rem",
                    fontWeight: 600,
                  }}
                >
                  {cat.title}
                </h3>
                <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
                  {cat.items.map((item) => (
                    <li
                      key={item}
                      style={{
                        color: "var(--fg2)",
                        padding: "0.38rem 0 0.38rem 1.5rem",
                        position: "relative",
                        lineHeight: 1.5,
                        fontSize: "0.92rem",
                      }}
                    >
                      <span
                        style={{
                          position: "absolute",
                          left: 0,
                          color: "var(--color-primary)",
                          fontWeight: "bold",
                        }}
                      >
                        ✓
                      </span>
                      {item}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>

          {/* Pet types at a glance */}
          <h2
            style={{
              textAlign: "center",
              fontSize: "1.9rem",
              color: "var(--fg1)",
              marginBottom: "1.5rem",
            }}
          >
            Pet Types at a Glance
          </h2>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(5,1fr)",
              gap: "1.25rem",
              marginBottom: "3rem",
            }}
          >
            {PET_TYPES.map((pt, i) => (
              <div
                key={pt.title}
                style={{
                  background: "var(--bg-card)",
                  padding: "1.5rem",
                  borderRadius: "var(--radius-xl)",
                  boxShadow:
                    hovCard === i ? "var(--shadow-hover)" : "var(--shadow-sm)",
                  transition: "var(--transition-default)",
                  transform: hovCard === i ? "translateY(-3px)" : "none",
                  border: "1px solid var(--border-default)",
                  textAlign: "center",
                }}
                onMouseEnter={() => setHovCard(i)}
                onMouseLeave={() => setHovCard(null)}
              >
                <div style={{ fontSize: "2.5rem", marginBottom: "0.75rem" }}>
                  {pt.icon}
                </div>
                <h3
                  style={{
                    fontSize: "1rem",
                    color: "var(--fg1)",
                    margin: "0 0 0.75rem",
                    fontWeight: 600,
                  }}
                >
                  {pt.title}
                </h3>
                {[
                  ["Commitment", pt.commitment],
                  ["Space", pt.space],
                  ["Daily time", pt.time],
                ].map(([k, v]) => (
                  <p
                    key={k}
                    style={{
                      color: "var(--fg2)",
                      fontSize: "0.82rem",
                      margin: "0 0 3px",
                      textAlign: "left",
                    }}
                  >
                    <strong style={{ color: "var(--fg1)" }}>{k}:</strong> {v}
                  </p>
                ))}
              </div>
            ))}
          </div>

          {/* CTA */}
          <div
            style={{
              background: "var(--color-gray-50)",
              padding: "2.5rem 2rem",
              borderRadius: "var(--radius-xl)",
              textAlign: "center",
              border: "1px solid var(--border-default)",
            }}
          >
            <h2 style={{ color: "var(--fg1)", fontSize: "1.75rem", marginTop: 0, marginBottom: "0.75rem" }}>
              Ready to Find a Match?
            </h2>
            <p
              style={{
                color: "var(--fg2)",
                fontSize: "1rem",
                maxWidth: 500,
                margin: "0 auto 1.75rem",
              }}
            >
              Browse our available animals and use filters to find the ideal companion.
            </p>
            <div style={{ display: "flex", gap: "1rem", justifyContent: "center", flexWrap: "wrap" }}>
              <Link
                to="/pets"
                style={{
                  padding: "0.85rem 2.25rem",
                  background: "var(--gradient-primary)",
                  color: "white",
                  border: "none",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 600,
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
                  padding: "0.85rem 2.25rem",
                  background: "transparent",
                  color: "var(--color-primary)",
                  border: "2px solid var(--color-primary)",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 600,
                  fontSize: "1rem",
                  textDecoration: "none",
                  transition: "var(--transition-default)",
                }}
              >
                + Add a Pet
              </Link>
            </div>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}
