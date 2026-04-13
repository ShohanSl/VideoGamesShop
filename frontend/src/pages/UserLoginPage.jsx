import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppState } from "@/app/state/AppStateProvider";

export function UserLoginPage() {
    const navigate = useNavigate();
    const { loginUser } = useAppState();
    const [username, setUsername] = useState("");

    return (
        <div className="landing-page">
            <div className="landing-card narrow">
                <button className="back-button" type="button" onClick={() => navigate("/")}>←</button>
                <span className="brand-kicker">Пользователь</span>
                <h1>Введите имя пользователя</h1>
                <form
                    className="login-form"
                    onSubmit={async (event) => {
                        event.preventDefault();
                        const ok = await loginUser(username);
                        if (ok) {
                            navigate("/user/catalog");
                        }
                    }}
                >
                    <input value={username} onChange={(event) => setUsername(event.target.value)} placeholder="Например, player_one" />
                    <button className="primary-button" type="submit">Войти</button>
                </form>
            </div>
        </div>
    );
}
