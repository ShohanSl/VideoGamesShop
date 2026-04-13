import { useState } from "react";
import { Link } from "react-router-dom";
import { shopApi } from "@/api/shopApi";
import { useAppState } from "@/app/state/AppStateProvider";

function SectionCard({ title, children }) {
    return (
        <section className="content-panel">
            <div className="content-header">
                <div><h2>{title}</h2></div>
            </div>
            {children}
        </section>
    );
}

function CrudLayout({ list, form }) {
    return <div className="crud-layout">{list}{form}</div>;
}

function createBulkGame() {
    return {
        title: "",
        price: "",
        releaseDate: "",
        description: "",
        publisherId: "",
        categoryIds: []
    };
}

export function AdminGamesPage() {
    const { games, developers, publishers, categories, gameForm, setGameForm, editingIds, startEdit, cancelEdit, saveGame, deleteGame } = useAppState();

    return (
        <SectionCard title="Управление играми">
            <CrudLayout
                list={(
                    <div className="crud-list">
                        {games.map((game) => (
                            <div key={game.id} className="crud-row">
                                <Link className="entity-link" to={`/admin/games/${game.id}`}>{game.title}</Link>
                                <div className="inline-actions">
                                    <button className="ghost-button" onClick={() => startEdit("game", game)}>Изменить</button>
                                    <button className="danger-button" onClick={() => deleteGame(game.id)}>Удалить</button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
                form={(
                    <form className="crud-form" onSubmit={(event) => { event.preventDefault(); void saveGame(); }}>
                        <h3>{editingIds.game ? "Редактировать игру" : "Добавить игру"}</h3>
                        <input value={gameForm.title} onChange={(event) => setGameForm({ ...gameForm, title: event.target.value })} placeholder="Название" required />
                        <input type="number" step="0.01" value={gameForm.price} onChange={(event) => setGameForm({ ...gameForm, price: event.target.value })} placeholder="Цена" required />
                        <input type="date" value={gameForm.releaseDate} onChange={(event) => setGameForm({ ...gameForm, releaseDate: event.target.value })} required />
                        <textarea value={gameForm.description} onChange={(event) => setGameForm({ ...gameForm, description: event.target.value })} placeholder="Описание" required />
                        <select value={gameForm.developerId} disabled={Boolean(editingIds.game)} onChange={(event) => setGameForm({ ...gameForm, developerId: event.target.value })} required>
                            <option value="">Разработчик</option>
                            {developers.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
                        </select>
                        <select value={gameForm.publisherId} disabled={Boolean(editingIds.game)} onChange={(event) => setGameForm({ ...gameForm, publisherId: event.target.value })} required>
                            <option value="">Издатель</option>
                            {publishers.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
                        </select>
                        <div className="checkbox-panel">
                            {categories.map((category) => (
                                <label key={category.id} className="checkbox-item dark">
                                    <input
                                        type="checkbox"
                                        checked={gameForm.categoryIds.includes(category.id)}
                                        onChange={() => setGameForm({
                                            ...gameForm,
                                            categoryIds: gameForm.categoryIds.includes(category.id)
                                                ? gameForm.categoryIds.filter((id) => id !== category.id)
                                                : [...gameForm.categoryIds, category.id]
                                        })}
                                    />
                                    <span>{category.name}</span>
                                </label>
                            ))}
                        </div>
                        <div className="inline-actions">
                            <button className="primary-button" type="submit">Сохранить</button>
                            <button className="ghost-button" type="button" onClick={() => cancelEdit("game")}>Сбросить</button>
                        </div>
                    </form>
                )}
            />
        </SectionCard>
    );
}

export function AdminDevelopersPage() {
    const {
        developers,
        publishers,
        categories,
        developerForm,
        setDeveloperForm,
        editingIds,
        startEdit,
        cancelEdit,
        saveDeveloper,
        deleteDeveloper,
        refreshAll
    } = useAppState();

    const [creationMode, setCreationMode] = useState("simple");
    const [bulkDeveloper, setBulkDeveloper] = useState({ name: "", country: "", foundedDate: "" });
    const [bulkGames, setBulkGames] = useState([createBulkGame()]);
    const [bulkError, setBulkError] = useState("");

    function updateBulkGame(index, key, value) {
        setBulkGames((current) => current.map((game, gameIndex) => (
            gameIndex === index ? { ...game, [key]: value } : game
        )));
    }

    function toggleBulkGameCategory(index, categoryId) {
        setBulkGames((current) => current.map((game, gameIndex) => {
            if (gameIndex !== index) {
                return game;
            }

            return {
                ...game,
                categoryIds: game.categoryIds.includes(categoryId)
                    ? game.categoryIds.filter((id) => id !== categoryId)
                    : [...game.categoryIds, categoryId]
            };
        }));
    }

    function resetBulkForm() {
        setBulkDeveloper({ name: "", country: "", foundedDate: "" });
        setBulkGames([createBulkGame()]);
        setBulkError("");
    }

    async function handleBulkCreate(event) {
        event.preventDefault();
        setBulkError("");

        try {
            await shopApi.createDeveloperWithGamesTx({
                developer: bulkDeveloper,
                games: bulkGames.map((game) => ({
                    title: game.title,
                    price: Number(game.price),
                    releaseDate: game.releaseDate,
                    description: game.description,
                    publisherId: Number(game.publisherId),
                    categoryIds: game.categoryIds
                }))
            });

            resetBulkForm();
            await refreshAll();
        } catch (requestError) {
            setBulkError(requestError.message);
        }
    }

    return (
        <SectionCard title="Управление разработчиками">
            <CrudLayout
                list={(
                    <div className="crud-list">
                        {developers.map((developer) => (
                            <div key={developer.id} className="crud-row">
                                <Link className="entity-link" to={`/admin/developers/${developer.id}`}>{developer.name}</Link>
                                <div className="inline-actions">
                                    <button className="ghost-button" onClick={() => startEdit("developer", developer)}>Изменить</button>
                                    <button className="danger-button" onClick={() => deleteDeveloper(developer.id)}>Удалить</button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
                form={(
                    <div className="crud-form">
                        <div className="mode-toggle">
                            <button
                                className={creationMode === "simple" ? "primary-button small-button" : "ghost-button small-button"}
                                type="button"
                                onClick={() => setCreationMode("simple")}
                            >
                                без игр
                            </button>
                            <button
                                className={creationMode === "bulk" ? "primary-button small-button" : "ghost-button small-button"}
                                type="button"
                                onClick={() => setCreationMode("bulk")}
                            >
                                с играми
                            </button>
                        </div>

                        {creationMode === "simple" ? (
                            <form className="stack-form" onSubmit={(event) => { event.preventDefault(); void saveDeveloper(); }}>
                                <h3>{editingIds.developer ? "Редактировать разработчика" : "Добавить разработчика"}</h3>
                                <input value={developerForm.name} onChange={(event) => setDeveloperForm({ ...developerForm, name: event.target.value })} placeholder="Название" required />
                                <input value={developerForm.country} onChange={(event) => setDeveloperForm({ ...developerForm, country: event.target.value })} placeholder="Страна" required />
                                <input type="date" value={developerForm.foundedDate} onChange={(event) => setDeveloperForm({ ...developerForm, foundedDate: event.target.value })} required />
                                <div className="inline-actions">
                                    <button className="primary-button" type="submit">Сохранить</button>
                                    <button className="ghost-button" type="button" onClick={() => cancelEdit("developer")}>Сбросить</button>
                                </div>
                            </form>
                        ) : (
                            <form className="stack-form" onSubmit={handleBulkCreate}>
                                <h3>Добавить разработчика с играми</h3>
                                <div className="bulk-grid">
                                    <input value={bulkDeveloper.name} onChange={(event) => setBulkDeveloper({ ...bulkDeveloper, name: event.target.value })} placeholder="Название разработчика" required />
                                    <input value={bulkDeveloper.country} onChange={(event) => setBulkDeveloper({ ...bulkDeveloper, country: event.target.value })} placeholder="Страна" required />
                                    <input type="date" value={bulkDeveloper.foundedDate} onChange={(event) => setBulkDeveloper({ ...bulkDeveloper, foundedDate: event.target.value })} required />
                                </div>

                                <div className="bulk-games-list">
                                    {bulkGames.map((game, index) => (
                                        <div key={index} className="bulk-game-card">
                                            <div className="bulk-game-header">
                                                <h3>Игра {index + 1}</h3>
                                                {bulkGames.length > 1 ? (
                                                    <button
                                                        className="ghost-button small-button"
                                                        type="button"
                                                        onClick={() => setBulkGames((current) => current.filter((_, gameIndex) => gameIndex !== index))}
                                                    >
                                                        Убрать
                                                    </button>
                                                ) : null}
                                            </div>
                                            <input value={game.title} onChange={(event) => updateBulkGame(index, "title", event.target.value)} placeholder="Название игры" required />
                                            <input type="number" step="0.01" value={game.price} onChange={(event) => updateBulkGame(index, "price", event.target.value)} placeholder="Цена" required />
                                            <input type="date" value={game.releaseDate} onChange={(event) => updateBulkGame(index, "releaseDate", event.target.value)} required />
                                            <textarea value={game.description} onChange={(event) => updateBulkGame(index, "description", event.target.value)} placeholder="Описание" required />
                                            <select value={game.publisherId} onChange={(event) => updateBulkGame(index, "publisherId", event.target.value)} required>
                                                <option value="">Издатель</option>
                                                {publishers.map((publisher) => <option key={publisher.id} value={publisher.id}>{publisher.name}</option>)}
                                            </select>

                                            <div className="checkbox-panel">
                                                {categories.map((category) => (
                                                    <label key={category.id} className="checkbox-item dark">
                                                        <input
                                                            type="checkbox"
                                                            checked={game.categoryIds.includes(category.id)}
                                                            onChange={() => toggleBulkGameCategory(index, category.id)}
                                                        />
                                                        <span>{category.name}</span>
                                                    </label>
                                                ))}
                                            </div>
                                        </div>
                                    ))}
                                </div>

                                <div className="inline-actions">
                                    <button className="ghost-button" type="button" onClick={() => setBulkGames((current) => [...current, createBulkGame()])}>
                                        Добавить ещё игру
                                    </button>
                                    <button className="primary-button" type="submit">Создать разработчика с играми</button>
                                    <button className="ghost-button" type="button" onClick={resetBulkForm}>Сбросить</button>
                                </div>

                                {bulkError ? <div className="report-error">{bulkError}</div> : null}
                            </form>
                        )}
                    </div>
                )}
            />
        </SectionCard>
    );
}

export function AdminPublishersPage() {
    const { publishers, publisherForm, setPublisherForm, editingIds, startEdit, cancelEdit, savePublisher, deletePublisher } = useAppState();
    return (
        <SimpleAdminEntityPage
            title="Управление издателями"
            items={publishers}
            itemLinkPrefix="/admin/publishers"
            editingId={editingIds.publisher}
            startEdit={(item) => startEdit("publisher", item)}
            cancelEdit={() => cancelEdit("publisher")}
            save={savePublisher}
            remove={deletePublisher}
            form={publisherForm}
            setForm={setPublisherForm}
            fields={[["name", "Название"], ["country", "Страна"], ["foundedDate", "Дата основания", "date"]]}
        />
    );
}

export function AdminCategoriesPage() {
    const { categories, categoryForm, setCategoryForm, editingIds, startEdit, cancelEdit, saveCategory, deleteCategory } = useAppState();
    return (
        <SimpleAdminEntityPage
            title="Управление категориями"
            items={categories}
            editingId={editingIds.category}
            startEdit={(item) => startEdit("category", item)}
            cancelEdit={() => cancelEdit("category")}
            save={saveCategory}
            remove={deleteCategory}
            form={categoryForm}
            setForm={setCategoryForm}
            fields={[["name", "Название"]]}
        />
    );
}

export function AdminUsersPage() {
    const { users, userForm, setUserForm, editingIds, startEdit, cancelEdit, saveUser, deleteUser } = useAppState();
    return (
        <SimpleAdminEntityPage
            title="Управление пользователями"
            items={users}
            editingId={editingIds.user}
            startEdit={(item) => startEdit("user", item)}
            cancelEdit={() => cancelEdit("user")}
            save={saveUser}
            remove={deleteUser}
            form={userForm}
            setForm={setUserForm}
            fields={[["username", "Имя пользователя"]]}
        />
    );
}

function SimpleAdminEntityPage({ title, items, itemLinkPrefix, editingId, startEdit, cancelEdit, save, remove, form, setForm, fields }) {
    return (
        <SectionCard title={title}>
            <CrudLayout
                list={(
                    <div className="crud-list">
                        {items.map((item) => (
                            <div key={item.id} className="crud-row">
                                {itemLinkPrefix ? (
                                    <Link className="entity-link" to={`${itemLinkPrefix}/${item.id}`}>{item.title || item.name || item.username}</Link>
                                ) : (
                                    <strong>{item.title || item.name || item.username}</strong>
                                )}
                                <div className="inline-actions">
                                    <button className="ghost-button" onClick={() => startEdit(item)}>Изменить</button>
                                    <button className="danger-button" onClick={() => remove(item.id)}>Удалить</button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
                form={(
                    <form className="crud-form" onSubmit={(event) => { event.preventDefault(); void save(); }}>
                        <h3>{editingId ? "Редактирование" : "Добавление"}</h3>
                        {fields.map(([key, label, type]) => (
                            <input
                                key={key}
                                type={type || "text"}
                                value={form[key] || ""}
                                onChange={(event) => setForm({ ...form, [key]: event.target.value })}
                                placeholder={label}
                                required
                            />
                        ))}
                        <div className="inline-actions">
                            <button className="primary-button" type="submit">Сохранить</button>
                            <button className="ghost-button" type="button" onClick={cancelEdit}>Сбросить</button>
                        </div>
                    </form>
                )}
            />
        </SectionCard>
    );
}
