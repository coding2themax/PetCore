import type { Route } from "./+types/pets.new";
import { Form, Link, redirect, useNavigation } from "react-router";
import { createPet } from "../api/pets";
import { AgeUnit, IntakeType, PetStatus, Sex, Size, Species } from "../types/pet";
import type { PetIntakeRequest } from "../types/pet";
import Header from "../components/Header";
import Footer from "../components/Footer";

export async function action({ request }: Route.ActionArgs) {
  const fd = await request.formData();

  const pet: PetIntakeRequest = {
    name: String(fd.get("name") ?? ""),
    species: fd.get("species") as Species,
    breed: String(fd.get("breed") ?? "") || undefined,
    sex: fd.get("sex") as Sex,
    age: {
      value: Number(fd.get("ageValue")),
      unit: fd.get("ageUnit") as AgeUnit,
    },
    size: fd.get("size") as Size,
    intakeDate: String(fd.get("intakeDate") ?? ""),
    intakeType: fd.get("intakeType") as IntakeType,
    status: fd.get("status") as PetStatus,
  };

  const idempotencyKey = crypto.randomUUID();

  try {
    await createPet(pet, idempotencyKey);
  } catch (err) {
    return {
      error:
        err instanceof Error
          ? err.message
          : "Failed to add pet. Please try again.",
    };
  }

  return redirect("/pets");
}

export function meta() {
  return [{ title: "Add Pet — PetCore" }];
}

const FIELD_STYLE: React.CSSProperties = {
  width: "100%",
  padding: "0.7rem 1rem",
  border: "2px solid var(--border-input)",
  borderRadius: "var(--radius-lg)",
  fontSize: "0.92rem",
  fontFamily: "inherit",
  outline: "none",
  background: "var(--bg-card)",
  color: "var(--fg1)",
  transition: "border-color 0.15s ease",
};

const LABEL_STYLE: React.CSSProperties = {
  display: "block",
  fontSize: "0.85rem",
  fontWeight: 600,
  color: "var(--fg2)",
  marginBottom: "0.4rem",
};

const SECTION_TITLE_STYLE: React.CSSProperties = {
  fontSize: "0.78rem",
  fontWeight: 700,
  color: "var(--fg2)",
  textTransform: "uppercase",
  letterSpacing: "0.07em",
  margin: "0 0 1.25rem",
};

export default function NewPet({ actionData }: Route.ComponentProps) {
  const navigation = useNavigation();
  const submitting = navigation.state === "submitting";
  const today = new Date().toISOString().split("T")[0];

  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <Header />

      <main
        style={{
          flex: 1,
          background: "linear-gradient(160deg, var(--bg-page) 0%, var(--color-gray-100) 100%)",
          padding: "2.5rem 0 3rem",
        }}
      >
        <div style={{ maxWidth: 680, margin: "0 auto", padding: "0 20px" }}>
          {/* Breadcrumb */}
          <div style={{ marginBottom: "1.5rem" }}>
            <Link
              to="/pets"
              style={{
                color: "var(--fg2)",
                fontSize: "0.9rem",
                textDecoration: "none",
                display: "inline-flex",
                alignItems: "center",
                gap: "0.3rem",
              }}
            >
              ← Back to Pets
            </Link>
          </div>

          <div style={{ marginBottom: "1.75rem" }}>
            <h1 style={{ fontSize: "2rem", color: "var(--fg1)", marginBottom: "0.4rem" }}>
              Add Pet
            </h1>
            <p style={{ color: "var(--fg2)", margin: 0 }}>
              Register a new animal intake record
            </p>
          </div>

          {actionData?.error && (
            <div
              style={{
                marginBottom: "1.5rem",
                padding: "1rem 1.25rem",
                background: "#fef2f2",
                border: "1px solid #fecaca",
                color: "#991b1b",
                borderRadius: "var(--radius-lg)",
                fontSize: "0.9rem",
              }}
            >
              {actionData.error}
            </div>
          )}

          <Form
            method="post"
            style={{
              background: "var(--bg-card)",
              borderRadius: "var(--radius-xl)",
              border: "1px solid var(--border-default)",
              boxShadow: "var(--shadow-card)",
              overflow: "hidden",
            }}
          >
            {/* Basic Info section */}
            <div style={{ padding: "1.75rem 2rem" }}>
              <h2 style={SECTION_TITLE_STYLE}>Basic Info</h2>
              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "1fr 1fr",
                  gap: "1.25rem",
                }}
              >
                <div>
                  <label htmlFor="name" style={LABEL_STYLE}>
                    Name <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <input
                    id="name"
                    name="name"
                    type="text"
                    required
                    placeholder="e.g. Buddy"
                    style={FIELD_STYLE}
                  />
                </div>

                <div>
                  <label htmlFor="species" style={LABEL_STYLE}>
                    Species <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <select id="species" name="species" required style={FIELD_STYLE}>
                    <option value="">Select species</option>
                    {Object.values(Species).map((s) => (
                      <option key={s} value={s}>
                        {s.charAt(0) + s.slice(1).toLowerCase()}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label htmlFor="breed" style={LABEL_STYLE}>
                    Breed
                  </label>
                  <input
                    id="breed"
                    name="breed"
                    type="text"
                    placeholder="e.g. Golden Retriever"
                    style={FIELD_STYLE}
                  />
                </div>

                <div>
                  <label htmlFor="sex" style={LABEL_STYLE}>
                    Sex <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <select id="sex" name="sex" required style={FIELD_STYLE}>
                    <option value="">Select sex</option>
                    {Object.values(Sex).map((s) => (
                      <option key={s} value={s}>
                        {s.charAt(0) + s.slice(1).toLowerCase()}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label style={LABEL_STYLE}>
                    Age <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <div style={{ display: "flex", gap: "0.6rem" }}>
                    <input
                      name="ageValue"
                      type="number"
                      min="0"
                      required
                      placeholder="0"
                      style={{ ...FIELD_STYLE, flex: 1 }}
                    />
                    <select
                      name="ageUnit"
                      required
                      style={{ ...FIELD_STYLE, width: 120, flex: "none" }}
                    >
                      {Object.values(AgeUnit).map((u) => (
                        <option key={u} value={u}>
                          {u.charAt(0) + u.slice(1).toLowerCase()}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div>
                  <label htmlFor="size" style={LABEL_STYLE}>
                    Size <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <select id="size" name="size" required style={FIELD_STYLE}>
                    <option value="">Select size</option>
                    {Object.values(Size).map((s) => (
                      <option key={s} value={s}>
                        {s.charAt(0) + s.slice(1).toLowerCase()}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>

            <hr style={{ margin: 0, border: "none", borderTop: "1px solid var(--border-default)" }} />

            {/* Intake Details section */}
            <div style={{ padding: "1.75rem 2rem" }}>
              <h2 style={SECTION_TITLE_STYLE}>Intake Details</h2>
              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "1fr 1fr",
                  gap: "1.25rem",
                }}
              >
                <div>
                  <label htmlFor="intakeDate" style={LABEL_STYLE}>
                    Intake Date <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <input
                    id="intakeDate"
                    name="intakeDate"
                    type="date"
                    required
                    defaultValue={today}
                    style={FIELD_STYLE}
                  />
                </div>

                <div>
                  <label htmlFor="intakeType" style={LABEL_STYLE}>
                    Intake Type <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <select id="intakeType" name="intakeType" required style={FIELD_STYLE}>
                    <option value="">Select type</option>
                    {Object.values(IntakeType).map((t) => (
                      <option key={t} value={t}>
                        {t.replace(/_/g, " ").charAt(0) +
                          t.replace(/_/g, " ").slice(1).toLowerCase()}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label htmlFor="status" style={LABEL_STYLE}>
                    Status <span style={{ color: "var(--color-danger)" }}>*</span>
                  </label>
                  <select
                    id="status"
                    name="status"
                    required
                    defaultValue="AVAILABLE"
                    style={FIELD_STYLE}
                  >
                    {Object.values(PetStatus).map((s) => (
                      <option key={s} value={s}>
                        {s.replace(/_/g, " ").charAt(0) +
                          s.replace(/_/g, " ").slice(1).toLowerCase()}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>

            {/* Actions */}
            <div
              style={{
                padding: "1.25rem 2rem",
                background: "var(--color-gray-50)",
                borderTop: "1px solid var(--border-default)",
                display: "flex",
                gap: "0.75rem",
                justifyContent: "flex-end",
              }}
            >
              <Link
                to="/pets"
                style={{
                  padding: "0.7rem 1.5rem",
                  background: "transparent",
                  color: "var(--fg2)",
                  border: "2px solid var(--border-default)",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 500,
                  fontSize: "0.92rem",
                  textDecoration: "none",
                  transition: "var(--transition-default)",
                }}
              >
                Cancel
              </Link>
              <button
                type="submit"
                disabled={submitting}
                style={{
                  padding: "0.7rem 2rem",
                  background: submitting ? "var(--color-gray-400)" : "var(--gradient-primary)",
                  color: "white",
                  border: "none",
                  borderRadius: "var(--radius-lg)",
                  fontWeight: 600,
                  fontSize: "0.92rem",
                  cursor: submitting ? "not-allowed" : "pointer",
                  fontFamily: "inherit",
                  transition: "var(--transition-default)",
                  boxShadow: submitting ? "none" : "var(--shadow-sm)",
                }}
              >
                {submitting ? "Saving…" : "Add Pet"}
              </button>
            </div>
          </Form>
        </div>
      </main>

      <Footer />
    </div>
  );
}
