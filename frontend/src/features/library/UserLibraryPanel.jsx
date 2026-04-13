export function UserLibraryPanel({ users, activeUserId, activeUser, onSelectUser }) {
    return (
        <section className="panel stack">
            <div>
                <h2>My library</h2>
                <p className="muted">
                    User mode exposes only the catalog and the selected user library.
                </p>
            </div>

            <div className="field-group">
                <label htmlFor="active-user">Current user</label>
                <select
                    id="active-user"
                    value={activeUserId || ""}
                    onChange={(event) => onSelectUser(event.target.value ? Number(event.target.value) : null)}
                >
                    <option value="">Select user</option>
                    {users.map((user) => (
                        <option key={user.id} value={user.id}>{user.username}</option>
                    ))}
                </select>
            </div>

            {!activeUserId ? (
                <div className="empty-state">Ask the admin to create a user, then select it here.</div>
            ) : activeUser?.games?.length ? (
                <div className="list">
                    {activeUser.games.map((game) => (
                        <div key={game.id} className="entity-row">
                            <strong>{game.title}</strong>
                            <span className="muted">Stored in the user-game many-to-many relation.</span>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="empty-state">This library is still empty.</div>
            )}
        </section>
    );
}
