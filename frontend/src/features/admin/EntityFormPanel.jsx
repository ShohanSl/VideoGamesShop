export function EntityFormPanel({
    title,
    description,
    children,
    submitLabel,
    onSubmit,
    onReset,
    isEditing
}) {
    return (
        <section className="panel stack">
            <div>
                <h2>{title}</h2>
                <p className="muted">{description}</p>
            </div>
            <form
                className="stack"
                onSubmit={(event) => {
                    event.preventDefault();
                    onSubmit();
                }}
            >
                {children}
                <div className="toolbar-actions">
                    <button type="submit" className="button-primary">{submitLabel}</button>
                    {isEditing ? (
                        <button type="button" className="button-secondary" onClick={onReset}>
                            Cancel edit
                        </button>
                    ) : null}
                </div>
            </form>
        </section>
    );
}
