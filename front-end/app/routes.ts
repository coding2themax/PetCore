import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  index("routes/home.tsx"),
  route("pets", "routes/pets.tsx"),
  route("pets/new", "routes/pets.new.tsx"),
  route("guidance", "routes/guidance.tsx"),
] satisfies RouteConfig;
