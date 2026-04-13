export function MessageBanner({ error, success }) {
    if (error) {
        return <div className="alert alert-error">{error}</div>;
    }

    if (success) {
        return <div className="alert alert-success">{success}</div>;
    }

    return null;
}
