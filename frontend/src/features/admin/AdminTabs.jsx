const ENTITY_TABS = [
    { id: "games", label: "Games" },
    { id: "developers", label: "Developers" },
    { id: "publishers", label: "Publishers" },
    { id: "categories", label: "Categories" },
    { id: "users", label: "Users" }
];

export function AdminTabs({ activeTab, onChange }) {
    return (
        <div className="tabs">
            {ENTITY_TABS.map((tab) => (
                <button
                    key={tab.id}
                    className={activeTab === tab.id ? "active" : ""}
                    onClick={() => onChange(tab.id)}
                >
                    {tab.label}
                </button>
            ))}
        </div>
    );
}
