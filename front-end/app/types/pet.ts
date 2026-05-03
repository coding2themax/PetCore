export enum Species {
  DOG = "DOG",
  CAT = "CAT",
  RABBIT = "RABBIT",
  BIRD = "BIRD",
  OTHER = "OTHER",
}

export enum Sex {
  MALE = "MALE",
  FEMALE = "FEMALE",
}

export enum Size {
  SMALL = "SMALL",
  MEDIUM = "MEDIUM",
  LARGE = "LARGE",
}

export enum PetStatus {
  AVAILABLE = "AVAILABLE",
  PENDING = "PENDING",
  ADOPTED = "ADOPTED",
  HOLD = "HOLD",
  FOSTER = "FOSTER",
  MEDICAL_HOLD = "MEDICAL_HOLD",
  UNAVAILABLE = "UNAVAILABLE",
}

export enum IntakeType {
  STRAY = "STRAY",
  OWNER_SURRENDER = "OWNER_SURRENDER",
  TRANSFER = "TRANSFER",
  BORN_IN_SHELTER = "BORN_IN_SHELTER",
  CONFISCATED = "CONFISCATED",
}

export enum AgeUnit {
  MONTHS = "MONTHS",
  YEARS = "YEARS",
}

export interface Age {
  value: number;
  unit: AgeUnit;
}

export interface PetIntakeRequest {
  name: string;
  species: Species;
  breed?: string;
  sex: Sex;
  age: Age;
  size: Size;
  intakeDate: string;
  intakeType: IntakeType;
  status: PetStatus;
  externalReferenceId?: string;
}

export interface PetResponse {
  petId: string;
  name: string;
  species: string;
  breed?: string;
  status: string;
  createdAt: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}
