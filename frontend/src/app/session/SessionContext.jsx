import { createContext, useContext, useEffect, useMemo, useState } from "react";

const STORAGE_KEY = "video-games-shop-session";

const SessionContext = createContext(null);

function readSession() {
    if (typeof window === "undefined") {
        return { role: null, user: null, token: null };
    }

    try {
        const raw = window.localStorage.getItem(STORAGE_KEY);
        if (!raw) {
            return { role: null, user: null, token: null };
        }

        const parsed = JSON.parse(raw);
        return {
            role: parsed?.role || null,
            user: parsed?.user || null,
            token: parsed?.token || null
        };
    } catch {
        return { role: null, user: null, token: null };
    }
}

export function SessionProvider({ children }) {
    const [session, setSession] = useState(() => readSession());

    useEffect(() => {
        if (typeof window === "undefined") {
            return;
        }

        if (!session.role && !session.user && !session.token) {
            window.localStorage.removeItem(STORAGE_KEY);
            return;
        }

        window.localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
    }, [session]);

    const value = useMemo(() => ({
        role: session.role,
        currentUser: session.user,
        token: session.token,
        isAuthenticated: Boolean(session.token),
        isAdmin: session.role === "admin",
        isUser: session.role === "user",
        enterSession(auth) {
            setSession({
                role: auth.role?.toLowerCase() || null,
                user: auth.userId && auth.username
                    ? { id: auth.userId, username: auth.username }
                    : null,
                token: auth.token || null
            });
        },
        resetSession() {
            setSession({ role: null, user: null, token: null });
        }
    }), [session]);

    return <SessionContext.Provider value={value}>{children}</SessionContext.Provider>;
}

export function useSession() {
    const context = useContext(SessionContext);
    if (!context) {
        throw new Error("useSession must be used inside SessionProvider");
    }
    return context;
}
