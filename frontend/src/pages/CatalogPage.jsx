import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { useAppState } from "@/app/state/AppStateProvider";
import { formatPrice } from "@/utils/format";

function paginate(items, page, pageSize) {
    const start = page * pageSize;
    return items.slice(start, start + pageSize);
}

export function CatalogPage({ role, libraryOnly = false }) {
    const { games, categories, publishers, activeUser, addToLibrary, removeFromLibrary } = useAppState();
    const [selectedCategory, setSelectedCategory] = useState("all");
    const [selectedPublisher, setSelectedPublisher] = useState("all");
    const [filtersOpen, setFiltersOpen] = useState(false);
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const pageSize = 10;
    const basePath = role === "admin" ? "/admin" : "/user";
    const libraryIds = new Set((activeUser?.games || []).map((item) => item.id));

    const sourceGames = libraryOnly
        ? games.filter((game) => activeUser?.games?.some((item) => item.id === game.id))
        : games;

    const filteredGames = useMemo(() => {
        return sourceGames.filter((game) => {
            const matchesCategory = libraryOnly || selectedCategory === "all"
                ? true
                : game.categories?.some((category) => String(category.id) === selectedCategory);
            const matchesPublisher = libraryOnly || selectedPublisher === "all"
                ? true
                : String(game.publisherId) === selectedPublisher;
            const matchesSearch = game.title.toLowerCase().includes(search.trim().toLowerCase());

            return matchesCategory && matchesPublisher && matchesSearch;
        });
    }, [libraryOnly, search, selectedCategory, selectedPublisher, sourceGames]);

    const totalPages = Math.max(1, Math.ceil(filteredGames.length / pageSize));
    const visibleGames = paginate(filteredGames, page, pageSize);

    function resetFilters() {
        setSelectedCategory("all");
        setSelectedPublisher("all");
        setPage(0);
    }

    return (
        <section className="content-panel">
            <div className="content-header">
                <div>
                    <h2>{libraryOnly ? "Библиотека" : "Каталог"}</h2>
                </div>
                <div className="catalog-toolbar">
                    <input
                        className="dark-select search-input"
                        value={search}
                        onChange={(event) => {
                            setSearch(event.target.value);
                            setPage(0);
                        }}
                        placeholder="Поиск по названию"
                    />
                    {!libraryOnly ? (
                        <div className="filters-dropdown">
                            <button
                                className="ghost-button"
                                type="button"
                                onClick={() => setFiltersOpen((current) => !current)}
                            >
                                Фильтры
                            </button>
                            {filtersOpen ? (
                                <div className="filters-menu">
                                    <label className="filters-field">
                                        <span>Категория</span>
                                        <select
                                            className="dark-select"
                                            value={selectedCategory}
                                            onChange={(event) => {
                                                setSelectedCategory(event.target.value);
                                                setPage(0);
                                            }}
                                        >
                                            <option value="all">Все</option>
                                            {categories.map((category) => (
                                                <option key={category.id} value={category.id}>{category.name}</option>
                                            ))}
                                        </select>
                                    </label>

                                    <label className="filters-field">
                                        <span>Издатель</span>
                                        <select
                                            className="dark-select"
                                            value={selectedPublisher}
                                            onChange={(event) => {
                                                setSelectedPublisher(event.target.value);
                                                setPage(0);
                                            }}
                                        >
                                            <option value="all">Все</option>
                                            {publishers.map((publisher) => (
                                                <option key={publisher.id} value={publisher.id}>{publisher.name}</option>
                                            ))}
                                        </select>
                                    </label>

                                    <button className="ghost-button small-button" type="button" onClick={resetFilters}>
                                        Сбросить
                                    </button>
                                </div>
                            ) : null}
                        </div>
                    ) : null}
                </div>
            </div>

            <div className="items-list">
                {visibleGames.map((game) => (
                    <article key={game.id} className="list-card">
                        <div>
                            <Link className="game-link" to={`${basePath}/games/${game.id}`}>{game.title}</Link>
                            <div className="card-meta">{game.developerName} • {game.publisherName}</div>
                            <div className="tag-row">
                                {(game.categories || []).map((category) => (
                                    <span key={category.id} className="tag">{category.name}</span>
                                ))}
                            </div>
                        </div>
                        <div className="card-side">
                            <strong>{formatPrice(game.price)}</strong>
                            {role === "user" && !libraryOnly ? (
                                libraryIds.has(game.id)
                                    ? <span className="library-badge">в библиотеке</span>
                                    : <button className="primary-button" onClick={() => addToLibrary(game.id)}>Добавить</button>
                            ) : null}
                            {role === "user" && libraryOnly ? (
                                <button className="danger-button" onClick={() => removeFromLibrary(game.id)}>Удалить</button>
                            ) : null}
                        </div>
                    </article>
                ))}
                {!visibleGames.length ? <div className="empty-box">Список пуст.</div> : null}
            </div>

            <div className="pager">
                <button className="ghost-button" disabled={page <= 0} onClick={() => setPage((current) => current - 1)}>Назад</button>
                <span>Страница {page + 1} из {totalPages}</span>
                <button className="ghost-button" disabled={page >= totalPages - 1} onClick={() => setPage((current) => current + 1)}>Вперёд</button>
            </div>
        </section>
    );
}
