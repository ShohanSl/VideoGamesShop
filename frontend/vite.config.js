import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));

const apiPaths = [
    "/games",
    "/developers",
    "/publishers",
    "/categories",
    "/users",
    "/async-jobs",
    "/api-docs",
    "/swagger-ui.html",
    "/swagger-ui"
];

export default defineConfig({
    plugins: [react()],
    resolve: {
        alias: {
            "@": path.resolve(__dirname, "./src")
        }
    },
    server: {
        port: 5173,
        proxy: apiPaths.reduce((acc, route) => {
            acc[route] = {
                target: "http://localhost:8080",
                changeOrigin: true
            };
            return acc;
        }, {})
    },
    build: {
        outDir: "../src/main/resources/static",
        emptyOutDir: true
    }
});
