import axios from "axios";

export const httpClient = axios.create({
    headers: {
        "Content-Type": "application/json"
    }
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
