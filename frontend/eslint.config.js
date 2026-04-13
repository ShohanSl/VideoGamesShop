import js from "@eslint/js";
import globals from "globals";
import react from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";

export default [
    {
        ignores: ["dist", "node_modules", "../src/main/resources/static"]
    },
    {
        files: ["vite.config.js"],
        languageOptions: {
            globals: globals.node
        }
    },
    js.configs.recommended,
    react.configs.flat.recommended,
    {
        files: ["src/**/*.{js,jsx}"],
        languageOptions: {
            ecmaVersion: 2024,
            globals: globals.browser,
            parserOptions: {
                ecmaFeatures: {
                    jsx: true
                },
                sourceType: "module"
            }
        },
        plugins: {
            react,
            "react-hooks": reactHooks,
            "react-refresh": reactRefresh
        },
        settings: {
            react: {
                version: "detect"
            }
        },
        rules: {
            ...reactHooks.configs.recommended.rules,
            "react/prop-types": "off",
            "react/react-in-jsx-scope": "off",
            "react/jsx-uses-react": "off",
            "react-hooks/exhaustive-deps": "off",
            "react-refresh/only-export-components": ["warn", { allowConstantExport: true }]
        }
    }
];
