import { Link, useParams } from "react-router-dom";
import { useAppState } from "@/app/state/AppStateProvider";
import { formatDate, formatPrice } from "@/utils/format";

export function GameDetailsPage({ role }) {
    const { gameId } = useParams();
    const { games, activeUser, addToLibrary } = useAppState();
    const game = games.find((item) => item.id === Number(gameId));
    const basePath = role === "admin" ? "/admin" : "/user";
    const isInLibrary = (activeUser?.games || []).some((item) => item.id === Number(gameId));

    if (!game) {
        return <section className="content-panel"><div className="empty-box">Игра не найдена.</div></section>;
    }

    return (
        <section className="content-panel">
            <div className="details-header">
                <div>
                    <span className="brand-kicker">Карточка игры</span>
                    <h2>{game.title}</h2>
                </div>
                <strong className="details-price">{formatPrice(game.price)}</strong>
            </div>
            <div className="details-grid">
                <div className="details-card">
                    <h3>Основная информация</h3>
                    <p>{game.description}</p>
                    <div className="details-list">
                        <span>Дата релиза: {formatDate(game.releaseDate)}</span>
                        <span>Разработчик: <Link to={`${basePath}/developers/${game.developerId}`}>{game.developerName}</Link></span>
                        <span>Издатель: <Link to={`${basePath}/publishers/${game.publisherId}`}>{game.publisherName}</Link></span>
                    </div>
                </div>
                <div className="details-card">
                    <h3>Категории</h3>
                    <div className="tag-row">
                        {(game.categories || []).map((category) => (
                            <span key={category.id} className="tag">{category.name}</span>
                        ))}
                    </div>
                    {role === "user" ? (
                        <div className="details-actions">
                            {isInLibrary
                                ? <span className="library-badge">в библиотеке</span>
                                : <button className="primary-button" onClick={() => addToLibrary(game.id)}>Добавить в библиотеку</button>}
                        </div>
                    ) : null}
                </div>
            </div>
        </section>
    );
}

export function DeveloperDetailsPage({ role }) {
    const { developerId } = useParams();
    const { developerDetails } = useAppState();
    const developer = developerDetails[Number(developerId)];
    const basePath = role === "admin" ? "/admin" : "/user";

    if (!developer) {
        return <section className="content-panel"><div className="empty-box">Разработчик не найден.</div></section>;
    }

    return (
        <section className="content-panel">
            <div className="details-header">
                <div>
                    <span className="brand-kicker">Разработчик</span>
                    <h2>{developer.name}</h2>
                </div>
            </div>
            <div className="details-grid">
                <div className="details-card">
                    <h3>Информация</h3>
                    <div className="details-list">
                        <span>Страна: {developer.country}</span>
                        <span>Дата основания: {formatDate(developer.foundedDate)}</span>
                    </div>
                </div>
                <div className="details-card">
                    <h3>Игры разработчика</h3>
                    <div className="related-list">
                        {(developer.games || []).map((game) => (
                            <Link key={game.id} className="related-link" to={`${basePath}/games/${game.id}`}>{game.title}</Link>
                        ))}
                    </div>
                </div>
            </div>
        </section>
    );
}

export function PublisherDetailsPage({ role }) {
    const { publisherId } = useParams();
    const { publisherDetails } = useAppState();
    const publisher = publisherDetails[Number(publisherId)];
    const basePath = role === "admin" ? "/admin" : "/user";

    if (!publisher) {
        return <section className="content-panel"><div className="empty-box">Издатель не найден.</div></section>;
    }

    return (
        <section className="content-panel">
            <div className="details-header">
                <div>
                    <span className="brand-kicker">Издатель</span>
                    <h2>{publisher.name}</h2>
                </div>
            </div>
            <div className="details-grid">
                <div className="details-card">
                    <h3>Информация</h3>
                    <div className="details-list">
                        <span>Страна: {publisher.country}</span>
                        <span>Дата основания: {formatDate(publisher.foundedDate)}</span>
                    </div>
                </div>
                <div className="details-card">
                    <h3>Игры издателя</h3>
                    <div className="related-list">
                        {(publisher.games || []).map((game) => (
                            <Link key={game.id} className="related-link" to={`${basePath}/games/${game.id}`}>{game.title}</Link>
                        ))}
                    </div>
                </div>
            </div>
        </section>
    );
}
