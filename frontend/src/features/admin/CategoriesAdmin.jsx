import { EntityFormPanel } from "@/features/admin/EntityFormPanel";

export function CategoriesAdmin(props) {
    const { categories, search, onSearch, formState, setFormState, editingEntity, onCreate, onUpdate, onReset, onEdit, onDelete } = props;
    const isEditing = Boolean(editingEntity);
    const filtered = categories.filter((category) => category.name.toLowerCase().includes(search.toLowerCase()));

    return (
        <div className="split-layout">
            <section className="panel stack">
                <div className="section-head">
                    <div><h2>Categories</h2><p className="muted">Manage the many-to-many side used by the catalog filters.</p></div>
                    <span className="badge">ManyToMany</span>
                </div>
                <div className="field-group"><label>Filter categories</label><input value={search} onChange={(event) => onSearch(event.target.value)} /></div>
                <div className="list">
                    {filtered.map((category) => (
                        <div key={category.id} className="entity-row">
                            <strong>{category.name}</strong>
                            <div className="inline-actions">
                                <button className="button-secondary" onClick={() => onEdit(category)}>Edit</button>
                                <button className="button-danger" onClick={() => onDelete(category.id)}>Delete</button>
                            </div>
                        </div>
                    ))}
                </div>
            </section>

            <EntityFormPanel title={isEditing ? "Edit category" : "Create category"} description="Short CRUD screen for category management." submitLabel={isEditing ? "Save changes" : "Create category"} onSubmit={isEditing ? onUpdate : onCreate} onReset={onReset} isEditing={isEditing}>
                <div className="field-group"><label>Name</label><input value={formState.name} onChange={(event) => setFormState({ ...formState, name: event.target.value })} required /></div>
            </EntityFormPanel>
        </div>
    );
}
