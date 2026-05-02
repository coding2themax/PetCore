import type { Route } from "./+types/pets.new";
import { Form, Link, redirect, useNavigation } from "react-router";
import { createPet } from "../api/pets";
import {
  AgeUnit,
  IntakeType,
  PetStatus,
  Sex,
  Size,
  Species,
} from "../types/pet";
import type { PetIntakeRequest } from "../types/pet";

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
      error: err instanceof Error ? err.message : "Failed to add pet. Please try again.",
    };
  }

  return redirect("/");
}

export function meta() {
  return [{ title: "Add Pet — PetCore" }];
}

const FIELD_CLASS =
  "w-full border border-gray-300 rounded-lg px-3 py-2 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500";

const LABEL_CLASS = "block text-sm font-medium text-gray-700 mb-1";

export default function NewPet({ actionData }: Route.ComponentProps) {
  const navigation = useNavigation();
  const submitting = navigation.state === "submitting";
  const today = new Date().toISOString().split("T")[0];

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 px-6 py-4 flex items-center gap-4">
        <Link
          to="/"
          className="text-sm text-gray-500 hover:text-gray-700 flex items-center gap-1"
        >
          ← Back
        </Link>
        <div>
          <h1 className="text-xl font-bold text-gray-900">Add Pet</h1>
          <p className="text-sm text-gray-500">New intake record</p>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-6 py-8">
        {actionData?.error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg text-sm">
            {actionData.error}
          </div>
        )}

        <Form method="post" className="bg-white rounded-xl border border-gray-200 p-6 shadow-sm space-y-5">
          <section>
            <h2 className="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-4">
              Basic Info
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label htmlFor="name" className={LABEL_CLASS}>
                  Name <span className="text-red-500">*</span>
                </label>
                <input
                  id="name"
                  name="name"
                  type="text"
                  required
                  placeholder="e.g. Buddy"
                  className={FIELD_CLASS}
                />
              </div>

              <div>
                <label htmlFor="species" className={LABEL_CLASS}>
                  Species <span className="text-red-500">*</span>
                </label>
                <select id="species" name="species" required className={FIELD_CLASS}>
                  <option value="">Select species</option>
                  {Object.values(Species).map((s) => (
                    <option key={s} value={s}>
                      {s.charAt(0) + s.slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label htmlFor="breed" className={LABEL_CLASS}>
                  Breed
                </label>
                <input
                  id="breed"
                  name="breed"
                  type="text"
                  placeholder="e.g. Golden Retriever"
                  className={FIELD_CLASS}
                />
              </div>

              <div>
                <label htmlFor="sex" className={LABEL_CLASS}>
                  Sex <span className="text-red-500">*</span>
                </label>
                <select id="sex" name="sex" required className={FIELD_CLASS}>
                  <option value="">Select sex</option>
                  {Object.values(Sex).map((s) => (
                    <option key={s} value={s}>
                      {s.charAt(0) + s.slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className={LABEL_CLASS}>
                  Age <span className="text-red-500">*</span>
                </label>
                <div className="flex gap-2">
                  <input
                    name="ageValue"
                    type="number"
                    min="0"
                    required
                    placeholder="0"
                    className={`${FIELD_CLASS} flex-1`}
                  />
                  <select name="ageUnit" required className={`${FIELD_CLASS} w-32`}>
                    {Object.values(AgeUnit).map((u) => (
                      <option key={u} value={u}>
                        {u.charAt(0) + u.slice(1).toLowerCase()}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div>
                <label htmlFor="size" className={LABEL_CLASS}>
                  Size <span className="text-red-500">*</span>
                </label>
                <select id="size" name="size" required className={FIELD_CLASS}>
                  <option value="">Select size</option>
                  {Object.values(Size).map((s) => (
                    <option key={s} value={s}>
                      {s.charAt(0) + s.slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </section>

          <hr className="border-gray-100" />

          <section>
            <h2 className="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-4">
              Intake Details
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label htmlFor="intakeDate" className={LABEL_CLASS}>
                  Intake Date <span className="text-red-500">*</span>
                </label>
                <input
                  id="intakeDate"
                  name="intakeDate"
                  type="date"
                  required
                  defaultValue={today}
                  className={FIELD_CLASS}
                />
              </div>

              <div>
                <label htmlFor="intakeType" className={LABEL_CLASS}>
                  Intake Type <span className="text-red-500">*</span>
                </label>
                <select id="intakeType" name="intakeType" required className={FIELD_CLASS}>
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
                <label htmlFor="status" className={LABEL_CLASS}>
                  Status <span className="text-red-500">*</span>
                </label>
                <select id="status" name="status" required defaultValue="AVAILABLE" className={FIELD_CLASS}>
                  {Object.values(PetStatus).map((s) => (
                    <option key={s} value={s}>
                      {s.replace(/_/g, " ").charAt(0) +
                        s.replace(/_/g, " ").slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </section>

          <div className="pt-2 flex gap-3 justify-end">
            <Link
              to="/"
              className="text-sm text-gray-600 hover:text-gray-800 px-4 py-2 rounded-lg border border-gray-300 bg-white transition-colors"
            >
              Cancel
            </Link>
            <button
              type="submit"
              disabled={submitting}
              className="bg-indigo-600 hover:bg-indigo-700 disabled:opacity-60 text-white text-sm font-medium px-6 py-2 rounded-lg transition-colors"
            >
              {submitting ? "Saving..." : "Add Pet"}
            </button>
          </div>
        </Form>
      </main>
    </div>
  );
}
