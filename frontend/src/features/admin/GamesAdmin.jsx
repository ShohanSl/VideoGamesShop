import { EntityFormPanel } from "@/features/admin/EntityFormPanel";
import { formatPrice } from "@/utils/format";

export function GamesAdmin(props) {
    const {
        gamesPage,
        developers,
        publishers,
        categories,
        formState,
        setFormState,
        editingEntity,
        onCreate,
        onUpdate,
        onReset,
        onEdit,
        onDelete
    } = props;

    const isEditing = Boolean(editingEntity);

    return (
        <div className="split-layout">
            <section className="panel stack">
                <div className="section-head">
                    <div>
                        <h2>Games</h2>
                        <p className="muted">CRUD for the main catalog entity. Category links are managed here.</p>
                    </div>
                    <span className="badge">Game to Categories</span>
                </div>
                <div className="list">
                    {gamesPage.content.map((game) => (
                        <div key={game.id} className="entity-row">
                            <div className="toolbar">
                                <strong>{game.title}</strong>
                                <span>{formatPrice(game.price)}</span>
                            </div>
                            <div className="pill-row">
                                {(game.categories || []).map((category) => (
                                    <span key={category.id} className="pill">{category.name}</span>
                                ))}
                            </div>
                            <div className="inline-actions">
                                <button className="button-secondary" onClick={() => onEdit(game.id)}>Edit</button>
                                <button className="button-danger" onClick={() => onDelete(game.id)}>Delete</button>
                            </div>
                        </div>
                    ))}
                </div>
            </section>

            <EntityFormPanel
                title={isEditing ? "Edit game" : "Create game"}
                description={isEditing
                    ? "Update game information and category links."
                    : "Create a game and attach one developer, one publisher, and multiple categories."}
                submitLabel={isEditing ? "Save changes" : "Create game"}
                onSubmit={isEditing ? onUpdate : onCreate}
                onReset={onReset}
                isEditing={isEditing}
            >
                <div className="field-group"><label>Title</label><input value={formState.title} onChange={(event) => setFormState({ ...formState, title: event.target.value })} required /></div>
                <div className="field-group"><label>Price</label><input type="number" min="0" step="0.01" value={formState.price} onChange={(event) => setFormState({ ...formState, price: event.target.value })} required /></div>
                <div className="field-group"><label>Release date</label><input type="date" value={formState.releaseDate} onChange={(event) => setFormState({ ...formState, releaseDate: event.target.value })} required /></div>
                <div className="field-group"><label>Description</label><textarea value={formState.description} onChange={(event) => setFormState({ ...formState, description: event.target.value })} required /></div>
                <div className="field-group">
                    <label>Developer</label>
                    <select value={formState.developerId} disabled={isEditing} onChange={(event) => setFormState({ ...formState, developerId: event.target.value })} required>
                        <option value="">Select developer</option>
                        {developers.map((developer) => <option key={developer.id} value={developer.id}>{developer.name}</option>)}
                    </select>
                </div>
                <div className="field-group">
                    <label>Publisher</label>
                    <select value={formState.publisherId} disabled={isEditing} onChange={(event) => setFormState({ ...formState, publisherId: event.target.value })} required>
                        <option value="">Select publisher</option>
                        {publishers.map((publisher) => <option key={publisher.id} value={publisher.id}>{publisher.name}</option>)}
                    </select>
                </div>
                <div className="field-group">
                    <label>Categories</label>
                    <div className="checkbox-grid">
                        {categories.map((category) => (
                            <label key={category.id} className="checkbox-item">
                                <input
                                    type="checkbox"
                                    checked={formState.categoryIds.includes(category.id)}
                                    onChange={() => {
                                        const nextCategoryIds = formState.categoryIds.includes(category.id)
                                            ? formState.categoryIds.filter((id) => id !== category.id)
                                            : [...formState.categoryIds, category.id];
                                        setFormState({ ...formState, categoryIds: nextCategoryIds });
                                    }}
                                />
                                <span>{category.name}</span>
                            </label>
                        ))}
                    </div>
                </div>
            </EntityFormPanel>
        </div>
    );
}
