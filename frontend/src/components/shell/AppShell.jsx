import { NavLink, Outlet } from "react-router-dom";
import { useAppState } from "@/app/state/AppStateProvider";
import { AdminReportPanel } from "@/components/shell/AdminReportPanel";

const USER_MENU = [
    { to: "/user/catalog", label: "Каталог" },
    { to: "/user/library", label: "Библиотека" }
];

const ADMIN_MENU = [
    { to: "/admin/catalog", label: "Каталог" },
    { to: "/admin/manage/games", label: "Игры" },
    { to: "/admin/manage/developers", label: "Разработчики" },
    { to: "/admin/manage/publishers", label: "Издатели" },
    { to: "/admin/manage/categories", label: "Категории" },
    { to: "/admin/manage/users", label: "Пользователи" }
];

export function AppShell({ role }) {
    const { activeUsername, logoutUser } = useAppState();
    const menu = role === "admin" ? ADMIN_MENU : USER_MENU;

    return (
        <div className="shell-page">
            <button
                className="mode-switch-floating"
                type="button"
                title="Сменить режим"
                onClick={() => {
                    logoutUser();
                    window.location.href = "/";
                }}
            >
                ←
            </button>

            <aside className="shell-sidebar">
                <div className="brand-block">
                    <span className="brand-kicker">Video Games Shop</span>
                    <h1>{role === "admin" ? "Администратор" : activeUsername || "Пользователь"}</h1>
                </div>

                <nav className="shell-nav">
                    {menu.map((item) => (
                        <NavLink key={item.to} to={item.to} className={({ isActive }) => isActive ? "nav-link active" : "nav-link"}>
                            {item.label}
                        </NavLink>
                    ))}
                </nav>

                {role === "admin" ? <AdminReportPanel /> : null}
            </aside>

            <main className="shell-content">
                <Outlet />
            </main>
        </div>
    );
}
