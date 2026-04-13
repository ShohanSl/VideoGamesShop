export function formatPrice(value) {
    if (value === null || value === undefined || value === "") {
        return "No price";
    }

    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD"
    }).format(value);
}

export function formatDate(value) {
    if (!value) {
        return "Not specified";
    }

    return new Date(value).toLocaleDateString("en-GB");
}

export function buildQuery(params) {
    const query = new URLSearchParams();

    Object.entries(params).forEach(([key, value]) => {
        if (value === null || value === undefined || value === "") {
            return;
        }

        if (Array.isArray(value)) {
            value.forEach((item) => query.append(key, item));
            return;
        }

        query.set(key, value);
    });

    const queryString = query.toString();
    return queryString ? `?${queryString}` : "";
}
