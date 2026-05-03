import { Link, useLocation } from "react-router";

const NAV_LINKS = [
  { label: "Home", to: "/" },
  { label: "Pets", to: "/pets" },
  { label: "Guidance", to: "/guidance" },
];

export default function Header() {
  const { pathname } = useLocation();

  const isActive = (to: string) =>
    to === "/" ? pathname === "/" : pathname.startsWith(to);

  return (
    <header
      style={{
        background: "var(--gradient-primary)",
        boxShadow: "var(--shadow-header)",
        position: "sticky",
        top: 0,
        zIndex: "var(--z-sticky)",
      }}
    >
      <div className="container">
        <div
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            gap: "1rem",
            padding: "1rem 0",
            flexWrap: "wrap",
          }}
        >
          <Link to="/" style={{ textDecoration: "none" }}>
            <svg
              width="210"
              height="42"
              viewBox="0 0 290 64"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <rect
                width="64"
                height="64"
                rx="14"
                fill="rgba(255,255,255,0.18)"
              />
              <circle cx="27" cy="11" r="5.5" fill="white" />
              <circle cx="36" cy="10" r="5.5" fill="white" />
              <circle cx="32" cy="8" r="6" fill="white" />
              <circle cx="22" cy="13" r="4.5" fill="white" />
              <circle cx="41" cy="13" r="4.5" fill="white" />
              <circle cx="32" cy="27" r="12.5" fill="white" />
              <circle cx="13" cy="25" r="7" fill="white" />
              <circle cx="10" cy="32" r="6" fill="white" />
              <circle cx="14" cy="38" r="5.5" fill="white" />
              <circle cx="51" cy="25" r="7" fill="white" />
              <circle cx="54" cy="32" r="6" fill="white" />
              <circle cx="50" cy="38" r="5.5" fill="white" />
              <ellipse cx="32" cy="36" rx="7" ry="5.5" fill="white" />
              <circle cx="32" cy="34" r="2.5" fill="rgba(102,126,234,0.7)" />
              <circle cx="26" cy="24" r="2" fill="rgba(102,126,234,0.7)" />
              <circle cx="38" cy="24" r="2" fill="rgba(102,126,234,0.7)" />
              <path
                d="M27,39 Q32,43 37,39"
                stroke="rgba(102,126,234,0.7)"
                strokeWidth="1.6"
                fill="none"
                strokeLinecap="round"
              />
              <text
                fontFamily="'Inter','Helvetica Neue',Arial,sans-serif"
                fontSize="34"
                letterSpacing="-1"
              >
                <tspan x="76" y="46" fontWeight="700" fill="white">
                  Pet
                </tspan>
                <tspan fontWeight="300" fill="rgba(255,255,255,0.78)">
                  Core
                </tspan>
              </text>
            </svg>
            <p
              style={{
                fontSize: "0.75rem",
                margin: "2px 0 0",
                opacity: 0.8,
                fontWeight: 300,
                color: "white",
              }}
            >
              Your Trusted Pet Companion
            </p>
          </Link>

          <nav>
            <ul
              style={{
                display: "flex",
                listStyle: "none",
                margin: 0,
                padding: 0,
                gap: "0.5rem",
              }}
            >
              {NAV_LINKS.map(({ label, to }) => {
                const active = isActive(to);
                return (
                  <li key={to}>
                    <Link
                      to={to}
                      style={{
                        color: "white",
                        fontWeight: active ? 700 : 500,
                        padding: "0.5rem 1rem",
                        borderRadius: "var(--radius-md)",
                        transition: "var(--transition-default)",
                        background: active
                          ? "rgba(255,255,255,0.22)"
                          : "transparent",
                        fontSize: "0.95rem",
                        textDecoration: "none",
                        display: "block",
                      }}
                      onMouseEnter={(e) => {
                        if (!active)
                          (e.currentTarget as HTMLElement).style.background =
                            "rgba(255,255,255,0.12)";
                      }}
                      onMouseLeave={(e) => {
                        if (!active)
                          (e.currentTarget as HTMLElement).style.background =
                            "transparent";
                      }}
                    >
                      {label}
                    </Link>
                  </li>
                );
              })}
            </ul>
          </nav>

          <Link
            to="/pets/new"
            style={{
              background: "rgba(255,255,255,0.15)",
              border: "1.5px solid rgba(255,255,255,0.4)",
              color: "white",
              padding: "0.55rem 1.1rem",
              borderRadius: "var(--radius-lg)",
              fontWeight: 600,
              fontSize: "0.9rem",
              textDecoration: "none",
              transition: "var(--transition-default)",
              whiteSpace: "nowrap",
            }}
            onMouseEnter={(e) => {
              (e.currentTarget as HTMLElement).style.background =
                "rgba(255,255,255,0.25)";
            }}
            onMouseLeave={(e) => {
              (e.currentTarget as HTMLElement).style.background =
                "rgba(255,255,255,0.15)";
            }}
          >
            + Add Pet
          </Link>
        </div>
      </div>
    </header>
  );
}
