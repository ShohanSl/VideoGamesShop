import { EntityFormPanel } from "@/features/admin/EntityFormPanel";
import { formatDate } from "@/utils/format";

export function DevelopersAdmin(props) {
    const {
        developers,
        detailsById,
        allGames,
        formState,
        setFormState,
        editingEntity,
        relationState,
        setRelationState,
        onCreate,
        onUpdate,
        onReset,
        onEdit,
        onDelete,
        onAttach,
        onDetach
    } = props;

    const isEditing = Boolean(editingEntity);
    const filtered = developers.filter((developer) =>
        developer.name.toLowerCase().includes(formState.search.toLowerCase())
    );

    return (
        <div className="split-layout">
            <section className="panel stack">
                <div className="section-head">
                    <div><h2>Developers</h2><p className="muted">One-to-many relation: one developer can own many games.</p></div>
                    <span className="badge">OneToMany</span>
                </div>
                <div className="field-group"><label>Filter developers</label><input value={formState.search} onChange={(event) => setFormState({ ...formState, search: event.target.value })} placeholder="Filter by name" /></div>
                <div className="list">
                    {filtered.map((developer) => {
                        const detail = detailsById[developer.id];
                        return (
                            <div key={developer.id} className="entity-row">
                                <div className="toolbar"><strong>{developer.name}</strong><span className="muted">{developer.country}</span></div>
                                <div className="meta-list"><span>Founded: {formatDate(developer.foundedDate)}</span><span>Games: {detail?.games?.length ?? 0}</span></div>
                                <div className="pill-row">
                                    {(detail?.games || []).map((game) => (
                                        <span key={game.id} className="pill">{game.title}<button className="button-danger" type="button" onClick={() => onDetach(developer.id, game.id)}>Remove</button></span>
                                    ))}
                                </div>
                                <div className="inline-actions">
                                    <select value={relationState[developer.id] || ""} onChange={(event) => setRelationState({ ...relationState, [developer.id]: event.target.value })}>
                                        <option value="">Attach existing game</option>
                                        {allGames.map((game) => <option key={game.id} value={game.id}>{game.title}</option>)}
                                    </select>
                                    <button className="button-secondary" onClick={() => onAttach(developer.id, Number(relationState[developer.id]))} disabled={!relationState[developer.id]}>Attach game</button>
                                    <button className="button-secondary" onClick={() => onEdit(developer)}>Edit</button>
                                    <button className="button-danger" onClick={() => onDelete(developer.id)}>Delete</button>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </section>

            <EntityFormPanel title={isEditing ? "Edit developer" : "Create developer"} description="Create or update developer records that own the one-to-many game relation." submitLabel={isEditing ? "Save changes" : "Create developer"} onSubmit={isEditing ? onUpdate : onCreate} onReset={onReset} isEditing={isEditing}>
                <div className="field-group"><label>Name</label><input value={formState.name} onChange={(event) => setFormState({ ...formState, name: event.target.value })} required /></div>
                <div className="field-group"><label>Country</label><input value={formState.country} onChange={(event) => setFormState({ ...formState, country: event.target.value })} required /></div>
                <div className="field-group"><label>Founded date</label><input type="date" value={formState.foundedDate} onChange={(event) => setFormState({ ...formState, foundedDate: event.target.value })} required /></div>
            </EntityFormPanel>
        </div>
    );
}
