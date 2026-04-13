import { formatDate, formatPrice } from "@/utils/format";

export function GameCard({
    game,
    mode,
    activeUserId,
    activeUserLibraryIds,
    onAddToLibrary,
    onEdit,
    onDelete
}) {
    const inLibrary = activeUserLibraryIds.includes(game.id);

    return (
        <article className="game-card">
            <div>
                <div className="toolbar">
                    <h3>{game.title}</h3>
                    <span className="badge">{formatPrice(game.price)}</span>
                </div>
                <div className="meta-list">
                    <span>Developer: {game.developerName || "Unknown"}</span>
                    <span>Publisher: {game.publisherName || "Unknown"}</span>
                    <span>Release: {formatDate(game.releaseDate)}</span>
                </div>
            </div>

            {game.description ? <div className="muted">{game.description}</div> : null}

            <div className="pill-row">
                {(game.categories || []).map((category) => (
                    <span key={category.id} className="pill">{category.name}</span>
                ))}
            </div>

            <div className="inline-actions">
                {mode === "user" ? (
                    <button
                        className={inLibrary ? "button-secondary" : "button-accent"}
                        disabled={!activeUserId || inLibrary}
                        onClick={() => onAddToLibrary(game.id)}
                    >
                        {activeUserId ? (inLibrary ? "Already in library" : "Add to my library") : "Select a user"}
                    </button>
                ) : (
                    <>
                        <button className="button-secondary" onClick={() => onEdit(game)}>Edit</button>
                        <button className="button-danger" onClick={() => onDelete(game.id)}>Delete</button>
                    </>
                )}
            </div>
        </article>
    );
}
