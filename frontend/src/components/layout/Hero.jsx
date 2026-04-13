export function Hero({ mode, onModeChange, stats }) {
    return (
        <section className="hero">
            <div className="hero-top">
                <div>
                    <span className="badge">React + Vite client for the Spring API</span>
                    <h1>Video Games Shop Control Center</h1>
                    <p>
                        Production-style SPA architecture with a dedicated frontend module,
                        structured components, central API layer, and separate user and admin flows.
                    </p>
                </div>
                <div className="mode-switch">
                    <button
                        className={mode === "user" ? "active" : ""}
                        onClick={() => onModeChange("user")}
                    >
                        User mode
                    </button>
                    <button
                        className={mode === "admin" ? "active" : ""}
                        onClick={() => onModeChange("admin")}
                    >
                        Admin mode
                    </button>
                </div>
            </div>
            <div className="hero-stats">
                <div className="stat-card">
                    <span>Games</span>
                    <strong>{stats.games}</strong>
                </div>
                <div className="stat-card">
                    <span>Developers</span>
                    <strong>{stats.developers}</strong>
                </div>
                <div className="stat-card">
                    <span>Publishers</span>
                    <strong>{stats.publishers}</strong>
                </div>
                <div className="stat-card">
                    <span>Categories</span>
                    <strong>{stats.categories}</strong>
                </div>
                <div className="stat-card">
                    <span>Users</span>
                    <strong>{stats.users}</strong>
                </div>
            </div>
        </section>
    );
}
