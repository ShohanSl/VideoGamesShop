import { EntityFormPanel } from "@/features/admin/EntityFormPanel";

export function UsersAdmin(props) {
    const { users, detailsById, allGames, search, onSearch, formState, setFormState, editingEntity, relationState, setRelationState, onCreate, onUpdate, onReset, onEdit, onDelete, onAttach, onDetach } = props;
    const isEditing = Boolean(editingEntity);
    const filtered = users.filter((user) => user.username.toLowerCase().includes(search.toLowerCase()));

    return (
        <div className="split-layout">
            <section className="panel stack">
                <div className="section-head">
                    <div><h2>Users</h2><p className="muted">Manage user accounts and their game libraries.</p></div>
                    <span className="badge">User to Game</span>
                </div>
                <div className="field-group"><label>Filter users</label><input value={search} onChange={(event) => onSearch(event.target.value)} /></div>
                <div className="list">
                    {filtered.map((user) => {
                        const detail = detailsById[user.id];
                        return (
                            <div key={user.id} className="entity-row">
                                <div className="toolbar"><strong>{user.username}</strong><span className="muted">Library size: {detail?.games?.length ?? 0}</span></div>
                                <div className="pill-row">
                                    {(detail?.games || []).map((game) => (
                                        <span key={game.id} className="pill">{game.title}<button className="button-danger" type="button" onClick={() => onDetach(user.id, game.id)}>Remove</button></span>
                                    ))}
                                </div>
                                <div className="inline-actions">
                                    <select value={relationState[user.id] || ""} onChange={(event) => setRelationState({ ...relationState, [user.id]: event.target.value })}>
                                        <option value="">Add game to library</option>
                                        {allGames.map((game) => <option key={game.id} value={game.id}>{game.title}</option>)}
                                    </select>
                                    <button className="button-secondary" onClick={() => onAttach(user.id, Number(relationState[user.id]))} disabled={!relationState[user.id]}>Add game</button>
                                    <button className="button-secondary" onClick={() => onEdit(user)}>Edit</button>
                                    <button className="button-danger" onClick={() => onDelete(user.id)}>Delete</button>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </section>

            <EntityFormPanel title={isEditing ? "Edit user" : "Create user"} description="Admin creates and updates users; user mode only works with existing accounts." submitLabel={isEditing ? "Save changes" : "Create user"} onSubmit={isEditing ? onUpdate : onCreate} onReset={onReset} isEditing={isEditing}>
                <div className="field-group"><label>Username</label><input value={formState.username} onChange={(event) => setFormState({ ...formState, username: event.target.value })} required /></div>
            </EntityFormPanel>
        </div>
    );
}
