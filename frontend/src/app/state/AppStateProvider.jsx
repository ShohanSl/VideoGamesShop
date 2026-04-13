import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { shopApi } from "@/api/shopApi";

const STORAGE_KEYS = {
    mode: "vgs-mode",
    username: "vgs-username"
};

const AppStateContext = createContext(null);

function readStorage(key, fallback = "") {
    return window.localStorage.getItem(key) ?? fallback;
}

function clearStoredSession() {
    window.localStorage.removeItem(STORAGE_KEYS.mode);
    window.localStorage.removeItem(STORAGE_KEYS.username);
}

function emptyGameForm() {
    return {
        title: "",
        price: "",
        releaseDate: "",
        description: "",
        developerId: "",
        publisherId: "",
        categoryIds: []
    };
}

function emptyDeveloperForm() {
    return { name: "", country: "", foundedDate: "" };
}

function emptyPublisherForm() {
    return { name: "", country: "", foundedDate: "" };
}

function emptyCategoryForm() {
    return { name: "" };
}

function emptyUserForm() {
    return { username: "" };
}

export function AppStateProvider({ children }) {
    const [mode, setMode] = useState(readStorage(STORAGE_KEYS.mode, ""));
    const [activeUsername, setActiveUsername] = useState(readStorage(STORAGE_KEYS.username, ""));
    const [categories, setCategories] = useState([]);
    const [developers, setDevelopers] = useState([]);
    const [developerDetails, setDeveloperDetails] = useState({});
    const [publishers, setPublishers] = useState([]);
    const [publisherDetails, setPublisherDetails] = useState({});
    const [users, setUsers] = useState([]);
    const [activeUser, setActiveUser] = useState(null);
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState({ type: "", text: "" });
    const [gameForm, setGameForm] = useState(emptyGameForm());
    const [developerForm, setDeveloperForm] = useState(emptyDeveloperForm());
    const [publisherForm, setPublisherForm] = useState(emptyPublisherForm());
    const [categoryForm, setCategoryForm] = useState(emptyCategoryForm());
    const [userForm, setUserForm] = useState(emptyUserForm());
    const [editingIds, setEditingIds] = useState({
        game: null,
        developer: null,
        publisher: null,
        category: null,
        user: null
    });

    useEffect(() => {
        window.localStorage.setItem(STORAGE_KEYS.mode, mode);
    }, [mode]);

    useEffect(() => {
        if (activeUsername) {
            window.localStorage.setItem(STORAGE_KEYS.username, activeUsername);
        } else {
            window.localStorage.removeItem(STORAGE_KEYS.username);
        }
    }, [activeUsername]);

    useEffect(() => {
        void bootstrap();
    }, []);

    async function bootstrap() {
        setLoading(true);
        try {
            await refreshAll();
        } finally {
            setLoading(false);
        }
    }

    function showMessage(type, text) {
        setMessage({ type, text });
    }

    function clearMessage() {
        setMessage({ type: "", text: "" });
    }

    async function fetchGames() {
        const page = await shopApi.getGames({}, 0, 500);
        const results = await Promise.allSettled((page.content || []).map((item) => shopApi.getGame(item.id)));
        return results
            .filter((result) => result.status === "fulfilled")
            .map((result) => result.value);
    }

    async function refreshAll() {
        const [categoriesData, developersData, publishersData, usersData, gamesData] = await Promise.all([
            shopApi.getCategories(),
            shopApi.getDevelopers(),
            shopApi.getPublishers(),
            shopApi.getUsers(),
            fetchGames()
        ]);

        setCategories(categoriesData);
        setDevelopers(developersData);
        setPublishers(publishersData);
        setUsers(usersData);
        setGames(gamesData);

        const developerFull = await Promise.all(developersData.map((item) => shopApi.getDeveloper(item.id)));
        setDeveloperDetails(Object.fromEntries(developerFull.map((item) => [item.id, item])));

        const publisherFull = await Promise.all(publishersData.map((item) => shopApi.getPublisher(item.id)));
        setPublisherDetails(Object.fromEntries(publisherFull.map((item) => [item.id, item])));

        if (activeUsername) {
            const matchedUser = usersData.find((item) => item.username.toLowerCase() === activeUsername.toLowerCase());
            if (matchedUser) {
                const fullUser = await shopApi.getUser(matchedUser.id);
                setActiveUser(fullUser);
                setActiveUsername(fullUser.username);
            } else {
                setActiveUser(null);
                setActiveUsername("");
            }
        }
    }

    async function enterMode(nextMode) {
        clearMessage();
        setLoading(true);
        try {
            setMode(nextMode);
            if (nextMode === "admin") {
                setActiveUsername("");
                setActiveUser(null);
            }
            await refreshAll();
        } finally {
            setLoading(false);
        }
    }

    async function loginUser(username) {
        clearMessage();
        setLoading(true);
        try {
            await refreshAll();
            const candidate = users.find((item) => item.username.toLowerCase() === username.trim().toLowerCase());
            if (!candidate) {
                return false;
            }
            const fullUser = await shopApi.getUser(candidate.id);
            setMode("user");
            setActiveUsername(fullUser.username);
            setActiveUser(fullUser);
            return true;
        } finally {
            setLoading(false);
        }
    }

    function logoutUser() {
        clearStoredSession();
        setActiveUsername("");
        setActiveUser(null);
        setMode("");
        clearMessage();
    }

    async function reloadActiveUser() {
        if (!activeUser?.id) {
            return;
        }
        const fullUser = await shopApi.getUser(activeUser.id);
        setActiveUser(fullUser);
    }

    async function addToLibrary(gameId) {
        if (!activeUser?.id) {
            return;
        }
        await shopApi.addGameToUser(activeUser.id, gameId);
        await reloadActiveUser();
    }

    async function removeFromLibrary(gameId) {
        if (!activeUser?.id) {
            return;
        }
        await shopApi.removeGameFromUser(activeUser.id, gameId);
        await reloadActiveUser();
    }

    function startEdit(entity, value) {
        setEditingIds((current) => ({ ...current, [entity]: value.id }));
        if (entity === "game") {
            setGameForm({
                title: value.title,
                price: value.price,
                releaseDate: value.releaseDate,
                description: value.description,
                developerId: value.developerId,
                publisherId: value.publisherId,
                categoryIds: (value.categories || []).map((item) => item.id)
            });
        }
        if (entity === "developer") {
            setDeveloperForm({ name: value.name, country: value.country, foundedDate: value.foundedDate });
        }
        if (entity === "publisher") {
            setPublisherForm({ name: value.name, country: value.country, foundedDate: value.foundedDate });
        }
        if (entity === "category") {
            setCategoryForm({ name: value.name });
        }
        if (entity === "user") {
            setUserForm({ username: value.username });
        }
    }

    function cancelEdit(entity) {
        setEditingIds((current) => ({ ...current, [entity]: null }));
        if (entity === "game") setGameForm(emptyGameForm());
        if (entity === "developer") setDeveloperForm(emptyDeveloperForm());
        if (entity === "publisher") setPublisherForm(emptyPublisherForm());
        if (entity === "category") setCategoryForm(emptyCategoryForm());
        if (entity === "user") setUserForm(emptyUserForm());
    }

    async function saveGame() {
        if (editingIds.game) {
            await shopApi.updateGame(editingIds.game, {
                title: gameForm.title,
                price: Number(gameForm.price),
                releaseDate: gameForm.releaseDate,
                description: gameForm.description,
                categoryIds: gameForm.categoryIds
            });
            showMessage("success", "Game updated.");
        } else {
            await shopApi.createGame({
                title: gameForm.title,
                price: Number(gameForm.price),
                releaseDate: gameForm.releaseDate,
                description: gameForm.description,
                developerId: Number(gameForm.developerId),
                publisherId: Number(gameForm.publisherId),
                categoryIds: gameForm.categoryIds
            });
            showMessage("success", "Game created.");
        }
        cancelEdit("game");
        await refreshAll();
    }

    async function deleteGame(id) {
        await shopApi.deleteGame(id);
        await refreshAll();
        showMessage("success", "Game deleted.");
    }

    async function saveDeveloper() {
        if (editingIds.developer) {
            await shopApi.updateDeveloper(editingIds.developer, developerForm);
            showMessage("success", "Developer updated.");
        } else {
            await shopApi.createDeveloper(developerForm);
            showMessage("success", "Developer created.");
        }
        cancelEdit("developer");
        await refreshAll();
    }

    async function deleteDeveloper(id) {
        await shopApi.deleteDeveloper(id);
        await refreshAll();
        showMessage("success", "Developer deleted.");
    }

    async function savePublisher() {
        if (editingIds.publisher) {
            await shopApi.updatePublisher(editingIds.publisher, publisherForm);
            showMessage("success", "Publisher updated.");
        } else {
            await shopApi.createPublisher(publisherForm);
            showMessage("success", "Publisher created.");
        }
        cancelEdit("publisher");
        await refreshAll();
    }

    async function deletePublisher(id) {
        await shopApi.deletePublisher(id);
        await refreshAll();
        showMessage("success", "Publisher deleted.");
    }

    async function saveCategory() {
        if (editingIds.category) {
            await shopApi.updateCategory(editingIds.category, categoryForm);
            showMessage("success", "Category updated.");
        } else {
            await shopApi.createCategory(categoryForm);
            showMessage("success", "Category created.");
        }
        cancelEdit("category");
        await refreshAll();
    }

    async function deleteCategory(id) {
        await shopApi.deleteCategory(id);
        await refreshAll();
        showMessage("success", "Category deleted.");
    }

    async function saveUser() {
        if (editingIds.user) {
            await shopApi.updateUser(editingIds.user, userForm);
            showMessage("success", "User updated.");
        } else {
            await shopApi.createUser(userForm);
            showMessage("success", "User created.");
        }
        cancelEdit("user");
        await refreshAll();
    }

    async function deleteUser(id) {
        await shopApi.deleteUser(id);
        if (activeUser?.id === id) {
            logoutUser();
        }
        await refreshAll();
        showMessage("success", "User deleted.");
    }

    const value = useMemo(() => ({
        mode,
        activeUsername,
        activeUser,
        categories,
        developers,
        developerDetails,
        publishers,
        publisherDetails,
        users,
        games,
        loading,
        message,
        setMessage,
        clearMessage,
        enterMode,
        loginUser,
        logoutUser,
        addToLibrary,
        removeFromLibrary,
        refreshAll,
        gameForm,
        setGameForm,
        developerForm,
        setDeveloperForm,
        publisherForm,
        setPublisherForm,
        categoryForm,
        setCategoryForm,
        userForm,
        setUserForm,
        editingIds,
        startEdit,
        cancelEdit,
        saveGame,
        deleteGame,
        saveDeveloper,
        deleteDeveloper,
        savePublisher,
        deletePublisher,
        saveCategory,
        deleteCategory,
        saveUser,
        deleteUser
    }), [
        mode,
        activeUsername,
        activeUser,
        categories,
        developers,
        developerDetails,
        publishers,
        publisherDetails,
        users,
        games,
        loading,
        message,
        gameForm,
        developerForm,
        publisherForm,
        categoryForm,
        userForm,
        editingIds
    ]);

    return <AppStateContext.Provider value={value}>{children}</AppStateContext.Provider>;
}

export function useAppState() {
    const context = useContext(AppStateContext);
    if (!context) {
        throw new Error("useAppState must be used inside AppStateProvider");
    }
    return context;
}
