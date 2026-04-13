import { useNavigate } from "react-router-dom";
import { useAppState } from "@/app/state/AppStateProvider";

export function LandingPage() {
    const navigate = useNavigate();
    const { enterMode } = useAppState();

    return (
        <div className="landing-page">
            <div className="landing-card">
                <span className="brand-kicker">Video Games Shop</span>
                <h1>Выберите режим работы</h1>
                <p>Сначала определяем роль, затем открываем подходящий интерфейс с навигацией по функциям.</p>
                <div className="role-grid">
                    <button
                        className="role-card"
                        onClick={async () => {
                            await enterMode("user");
                            navigate("/user/login");
                        }}
                    >
                        <strong>Пользователь</strong>
                        <span>Каталог и личная библиотека</span>
                    </button>
                    <button
                        className="role-card"
                        onClick={async () => {
                            await enterMode("admin");
                            navigate("/admin/catalog");
                        }}
                    >
                        <strong>Администратор</strong>
                        <span>Каталог и управление всеми сущностями</span>
                    </button>
                </div>
            </div>
        </div>
    );
}
