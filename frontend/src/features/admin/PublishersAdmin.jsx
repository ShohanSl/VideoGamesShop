import { EntityFormPanel } from "@/features/admin/EntityFormPanel";
import { formatDate } from "@/utils/format";

export function PublishersAdmin(props) {
    const { publishers, search, onSearch, formState, setFormState, editingEntity, onCreate, onUpdate, onReset, onEdit, onDelete } = props;
    const isEditing = Boolean(editingEntity);
    const filtered = publishers.filter((publisher) => publisher.name.toLowerCase().includes(search.toLowerCase()));

    return (
        <div className="split-layout">
            <section className="panel stack">
                <div className="section-head">
                    <div><h2>Publishers</h2><p className="muted">Publishers also expose a one-to-many relation to games.</p></div>
                    <span className="badge">Publisher to Games</span>
                </div>
                <div className="field-group"><label>Filter publishers</label><input value={search} onChange={(event) => onSearch(event.target.value)} /></div>
                <div className="list">
                    {filtered.map((publisher) => (
                        <div key={publisher.id} className="entity-row">
                            <div className="toolbar"><strong>{publisher.name}</strong><span className="muted">{publisher.country}</span></div>
                            <div className="meta-list"><span>Founded: {formatDate(publisher.foundedDate)}</span><span>Games: {publisher.games?.length || 0}</span></div>
                            <div className="pill-row">{(publisher.games || []).map((game) => <span key={game.id} className="pill">{game.title}</span>)}</div>
                            <div className="inline-actions">
                                <button className="button-secondary" onClick={() => onEdit(publisher)}>Edit</button>
                                <button className="button-danger" onClick={() => onDelete(publisher.id)}>Delete</button>
                            </div>
                        </div>
                    ))}
                </div>
            </section>

            <EntityFormPanel title={isEditing ? "Edit publisher" : "Create publisher"} description="Manage publishers and inspect their linked games." submitLabel={isEditing ? "Save changes" : "Create publisher"} onSubmit={isEditing ? onUpdate : onCreate} onReset={onReset} isEditing={isEditing}>
                <div className="field-group"><label>Name</label><input value={formState.name} onChange={(event) => setFormState({ ...formState, name: event.target.value })} required /></div>
                <div className="field-group"><label>Country</label><input value={formState.country} onChange={(event) => setFormState({ ...formState, country: event.target.value })} required /></div>
                <div className="field-group"><label>Founded date</label><input type="date" value={formState.foundedDate} onChange={(event) => setFormState({ ...formState, foundedDate: event.target.value })} required /></div>
            </EntityFormPanel>
        </div>
    );
}
