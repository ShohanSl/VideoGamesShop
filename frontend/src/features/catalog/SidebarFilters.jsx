export function SidebarFilters({
    filters,
    categories,
    publishers,
    onFilterChange,
    onApply,
    onReset
}) {
    const toggleCategory = (categoryId) => {
        const nextIds = filters.categoryIds.includes(categoryId)
            ? filters.categoryIds.filter((id) => id !== categoryId)
            : [...filters.categoryIds, categoryId];
        onFilterChange({ ...filters, categoryIds: nextIds });
    };

    return (
        <aside className="panel stack">
            <div>
                <h2>Catalog filters</h2>
                <p className="muted">
                    Filter games by category, publisher, and title. Category filtering calls the backend API.
                </p>
            </div>

            <div className="filters">
                <div className="field-group">
                    <label htmlFor="title-filter">Title contains</label>
                    <input
                        id="title-filter"
                        value={filters.title}
                        onChange={(event) => onFilterChange({ ...filters, title: event.target.value })}
                        placeholder="Search within the loaded catalog"
                    />
                </div>

                <div className="field-group">
                    <label htmlFor="publisher-filter">Publisher</label>
                    <select
                        id="publisher-filter"
                        value={filters.publisherName}
                        onChange={(event) => onFilterChange({ ...filters, publisherName: event.target.value })}
                    >
                        <option value="">All publishers</option>
                        {publishers.map((publisher) => (
                            <option key={publisher.id} value={publisher.name}>
                                {publisher.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="field-group">
                    <label>Categories</label>
                    <div className="checkbox-grid">
                        {categories.map((category) => (
                            <label key={category.id} className="checkbox-item">
                                <input
                                    type="checkbox"
                                    checked={filters.categoryIds.includes(category.id)}
                                    onChange={() => toggleCategory(category.id)}
                                />
                                <span>{category.name}</span>
                            </label>
                        ))}
                    </div>
                </div>
            </div>

            <div className="toolbar-actions">
                <button className="button-primary" onClick={onApply}>Apply filters</button>
                <button className="button-secondary" onClick={onReset}>Reset</button>
            </div>
        </aside>
    );
}
