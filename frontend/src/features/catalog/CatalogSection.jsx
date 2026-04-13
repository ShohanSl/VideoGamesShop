import { useMemo } from "react";
import { GameCard } from "@/features/catalog/GameCard";

export function CatalogSection({
    gamesPage,
    loading,
    mode,
    activeUserId,
    activeUserLibraryIds,
    filters,
    onPageChange,
    onAddToLibrary,
    onEditGame,
    onDeleteGame
}) {
    const visibleGames = useMemo(() => {
        const title = filters.title.trim().toLowerCase();

        return gamesPage.content.filter((game) => {
            const matchesTitle = !title || game.title.toLowerCase().includes(title);
            const matchesCategories =
                !filters.categoryIds.length ||
                filters.categoryIds.every((categoryId) =>
                    (game.categories || []).some((category) => category.id === categoryId)
                );

            return matchesTitle && matchesCategories;
        });
    }, [gamesPage.content, filters.title, filters.categoryIds]);

    return (
        <section className="panel stack">
            <div className="section-head">
                <div>
                    <h2>{mode === "user" ? "Games catalog" : "Games management"}</h2>
                    <p className="muted">
                        {mode === "user"
                            ? "Browse the available games and add them to your own library."
                            : "Manage the game catalog, including category relations and core metadata."}
                    </p>
                </div>
                <span className="badge">{gamesPage.totalElements} items</span>
            </div>

            {loading ? (
                <div className="loading">Loading games...</div>
            ) : visibleGames.length ? (
                <>
                    <div className="card-grid">
                        {visibleGames.map((game) => (
                            <GameCard
                                key={game.id}
                                game={game}
                                mode={mode}
                                activeUserId={activeUserId}
                                activeUserLibraryIds={activeUserLibraryIds}
                                onAddToLibrary={onAddToLibrary}
                                onEdit={onEditGame}
                                onDelete={onDeleteGame}
                            />
                        ))}
                    </div>

                    {!filters.publisherName && (
                        <div className="pagination">
                            <button
                                className="button-secondary"
                                disabled={gamesPage.number <= 0}
                                onClick={() => onPageChange(gamesPage.number - 1)}
                            >
                                Previous
                            </button>
                            <span>
                                Page {gamesPage.number + 1} of {Math.max(gamesPage.totalPages, 1)}
                            </span>
                            <button
                                className="button-secondary"
                                disabled={gamesPage.number >= gamesPage.totalPages - 1}
                                onClick={() => onPageChange(gamesPage.number + 1)}
                            >
                                Next
                            </button>
                        </div>
                    )}
                </>
            ) : (
                <div className="empty-state">No games found for the selected filters.</div>
            )}
        </section>
    );
}
