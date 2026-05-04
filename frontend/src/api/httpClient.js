import axios from "axios";

const SESSION_STORAGE_KEY = "video-games-shop-session";

export const httpClient = axios.create({
    baseURL: "/api",
    headers: {
        "Content-Type": "application/json"
    }
});

httpClient.interceptors.request.use((config) => {
    if (typeof window === "undefined") {
        return config;
    }

    const raw = window.localStorage.getItem(SESSION_STORAGE_KEY);
    if (!raw) {
        return config;
    }

    try {
        const session = JSON.parse(raw);
        if (session?.token) {
            config.headers.Authorization = `Bearer ${session.token}`;
        }
    } catch {
        // Ignore invalid local storage payloads.
    }

    return config;
});

httpClient.interceptors.response.use(
    (response) => response,
    (error) => {
        const details = error.response?.data?.details
            ?.map((item) => `${item.field}: ${item.message}`)
            ?.join("; ");

        const message =
            details ||
            error.response?.data?.message ||
            error.message ||
            "Request failed";

        return Promise.reject(new Error(message));
    }
);
