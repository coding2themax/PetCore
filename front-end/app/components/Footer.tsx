const QUICK_LINKS = ["Home", "Available Pets", "Expert Guidance", "Add a Pet"];
const SERVICES = ["Pet Intake", "Profile Management", "Matching", "Search"];
const CONTACT = [
  ["📍", "123 Rescue Lane, Shelter City"],
  ["📞", "(555) PET-CORE"],
  ["✉️", "hello@petcore.example"],
  ["🕒", "Mon–Sun: 8AM – 6PM"],
];

export default function Footer() {
  return (
    <footer style={{ background: "var(--color-gray-800)", color: "white" }}>
      <div className="container">
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(4,1fr)",
            gap: "2rem",
            padding: "2.5rem 0 1.5rem",
          }}
        >
          <div>
            <svg
              width="160"
              height="32"
              viewBox="0 0 290 64"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              style={{ marginBottom: "0.75rem", display: "block" }}
            >
              <rect width="64" height="64" rx="14" fill="var(--color-secondary)" />
              <circle cx="27" cy="11" r="5.5" fill="var(--color-gray-800)" />
              <circle cx="36" cy="10" r="5.5" fill="var(--color-gray-800)" />
              <circle cx="32" cy="8" r="6" fill="var(--color-gray-800)" />
              <circle cx="22" cy="13" r="4.5" fill="var(--color-gray-800)" />
              <circle cx="41" cy="13" r="4.5" fill="var(--color-gray-800)" />
              <circle cx="32" cy="27" r="12.5" fill="var(--color-gray-800)" />
              <circle cx="13" cy="25" r="7" fill="var(--color-gray-800)" />
              <circle cx="10" cy="32" r="6" fill="var(--color-gray-800)" />
              <circle cx="14" cy="38" r="5.5" fill="var(--color-gray-800)" />
              <circle cx="51" cy="25" r="7" fill="var(--color-gray-800)" />
              <circle cx="54" cy="32" r="6" fill="var(--color-gray-800)" />
              <circle cx="50" cy="38" r="5.5" fill="var(--color-gray-800)" />
              <ellipse cx="32" cy="36" rx="7" ry="5.5" fill="var(--color-gray-800)" />
              <circle cx="32" cy="34" r="2.5" fill="var(--color-secondary)" />
              <circle cx="26" cy="24" r="2" fill="var(--color-secondary)" />
              <circle cx="38" cy="24" r="2" fill="var(--color-secondary)" />
              <path
                d="M27,39 Q32,43 37,39"
                stroke="var(--color-secondary)"
                strokeWidth="1.6"
                fill="none"
                strokeLinecap="round"
              />
              <text
                fontFamily="'Inter','Helvetica Neue',Arial,sans-serif"
                fontSize="34"
                letterSpacing="-1"
              >
                <tspan x="76" y="46" fontWeight="700" fill="#e2e8f0">
                  Pet
                </tspan>
                <tspan fontWeight="300" fill="var(--color-secondary)">
                  Core
                </tspan>
              </text>
            </svg>
            <p
              style={{
                color: "var(--color-gray-500)",
                lineHeight: 1.6,
                fontSize: "0.9rem",
                marginBottom: "0.75rem",
              }}
            >
              Open-source pet rescue management — domain-driven, built for
              shelters.
            </p>
            <div style={{ display: "flex", gap: "0.6rem", marginTop: "0.75rem" }}>
              {["📘", "🐦", "💻", "📬"].map((icon) => (
                <span
                  key={icon}
                  style={{
                    display: "inline-flex",
                    alignItems: "center",
                    justifyContent: "center",
                    width: 36,
                    height: 36,
                    background: "rgba(255,255,255,0.08)",
                    borderRadius: "50%",
                    fontSize: "1rem",
                    cursor: "pointer",
                  }}
                >
                  {icon}
                </span>
              ))}
            </div>
          </div>

          {[
            { head: "Quick Links", links: QUICK_LINKS },
            { head: "Features", links: SERVICES },
          ].map((col) => (
            <div key={col.head}>
              <h4
                style={{
                  color: "#e2e8f0",
                  fontSize: "1rem",
                  marginTop: 0,
                  marginBottom: "0.75rem",
                  fontWeight: 600,
                }}
              >
                {col.head}
              </h4>
              <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
                {col.links.map((l) => (
                  <li key={l} style={{ marginBottom: "0.4rem" }}>
                    <span
                      style={{
                        color: "var(--color-gray-500)",
                        fontSize: "0.88rem",
                        cursor: "pointer",
                      }}
                    >
                      {l}
                    </span>
                  </li>
                ))}
              </ul>
            </div>
          ))}

          <div>
            <h4
              style={{
                color: "#e2e8f0",
                fontSize: "1rem",
                marginTop: 0,
                marginBottom: "0.75rem",
                fontWeight: 600,
              }}
            >
              Contact
            </h4>
            {CONTACT.map(([icon, text]) => (
              <p
                key={text}
                style={{
                  color: "var(--color-gray-500)",
                  marginBottom: "0.5rem",
                  display: "flex",
                  alignItems: "center",
                  gap: "0.5rem",
                  fontSize: "0.88rem",
                }}
              >
                <span>{icon}</span>
                {text}
              </p>
            ))}
          </div>
        </div>

        <div
          style={{
            borderTop: "1px solid var(--color-gray-700)",
            padding: "1.25rem 0",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            flexWrap: "wrap",
            gap: "0.5rem",
          }}
        >
          <p style={{ margin: 0, color: "var(--color-gray-500)", fontSize: "0.85rem" }}>
            © 2026 PetCore — MIT License
          </p>
          <p
            style={{
              margin: 0,
              fontStyle: "italic",
              color: "var(--color-gray-600)",
              fontSize: "0.85rem",
            }}
          >
            Always adopt from local shelters 🐕🐈
          </p>
        </div>
      </div>
    </footer>
  );
}
