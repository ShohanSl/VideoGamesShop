import { useQueryClient } from "@tanstack/react-query";
import { shopApi } from "@/api/shopApi";
import { shopKeys } from "@/api/shopQueries";
import { AppStateContext } from "@/app/state/appStateContext";

async function invalidateQueries(queryClient, keys) {
    await Promise.all(keys.map((queryKey) => queryClient.invalidateQueries({ queryKey })));
}

export function AppStateProvider({ children }) {
    const queryClient = useQueryClient();

    async function createGame(payload) {
        const data = await shopApi.createGame(payload);
        await invalidateQueries(queryClient, [
            ["catalog"],
            ["management-games"],
            ["game"],
            ["developer"],
            ["publisher"],
            ["user"]
        ]);
        return data;
    }

    async function updateGame(gameId, payload) {
        const data = await shopApi.updateGame(gameId, payload);
        await invalidateQueries(queryClient, [
            ["catalog"],
            ["management-games"],
            shopKeys.game(gameId),
            ["developer"],
            ["publisher"],
            ["user"]
        ]);
        return data;
    }

    async function deleteGame(gameId) {
        await shopApi.deleteGame(gameId);
        await invalidateQueries(queryClient, [
            ["catalog"],
            ["management-games"],
            ["game"],
            ["developer"],
            ["publisher"],
            ["user"]
        ]);
    }

    async function createDeveloper(payload) {
        const data = await shopApi.createDeveloper(payload);
        await invalidateQueries(queryClient, [
            shopKeys.developers,
            ["developer"],
            ["catalog"],
            ["management-games"]
        ]);
        return data;
    }

    async function updateDeveloper(developerId, payload) {
        const data = await shopApi.updateDeveloper(developerId, payload);
        await invalidateQueries(queryClient, [
            shopKeys.developers,
            shopKeys.developer(developerId),
            ["catalog"],
            ["management-games"]
        ]);
        return data;
    }

    async function createDeveloperWithGames(payload) {
        const data = await shopApi.createDeveloperWithGamesTx(payload);
        await invalidateQueries(queryClient, [
            shopKeys.developers,
            ["developer"],
            ["catalog"],
            ["management-games"],
            ["publisher"],
            ["user"]
        ]);
        return data;
    }

    async function deleteDeveloper(developerId) {
        await shopApi.deleteDeveloper(developerId);
        await invalidateQueries(queryClient, [
            shopKeys.developers,
            ["developer"],
            ["catalog"],
            ["management-games"],
            ["user"]
        ]);
    }

    async function createPublisher(payload) {
        const data = await shopApi.createPublisher(payload);
        await invalidateQueries(queryClient, [
            shopKeys.publishers,
            ["publisher"],
            ["catalog"],
            ["management-games"]
        ]);
        return data;
    }

    async function updatePublisher(publisherId, payload) {
        const data = await shopApi.updatePublisher(publisherId, payload);
        await invalidateQueries(queryClient, [
            shopKeys.publishers,
            shopKeys.publisher(publisherId),
            ["catalog"],
            ["management-games"]
        ]);
        return data;
    }

    async function deletePublisher(publisherId) {
        await shopApi.deletePublisher(publisherId);
        await invalidateQueries(queryClient, [
            shopKeys.publishers,
            ["publisher"],
            ["catalog"],
            ["management-games"],
            ["user"]
        ]);
    }

    async function createCategory(payload) {
        const data = await shopApi.createCategory(payload);
        await invalidateQueries(queryClient, [
            shopKeys.categories,
            ["catalog"],
            ["management-games"],
            ["game"],
            ["user"]
        ]);
        return data;
    }

    async function updateCategory(categoryId, payload) {
        const data = await shopApi.updateCategory(categoryId, payload);
        await invalidateQueries(queryClient, [
            shopKeys.categories,
            ["catalog"],
            ["management-games"],
            ["game"],
            ["user"]
        ]);
        return data;
    }

    async function deleteCategory(categoryId) {
        await shopApi.deleteCategory(categoryId);
        await invalidateQueries(queryClient, [
            shopKeys.categories,
            ["catalog"],
            ["management-games"],
            ["game"],
            ["user"]
        ]);
    }

    async function createUser(payload) {
        const data = await shopApi.createUser(payload);
        await invalidateQueries(queryClient, [shopKeys.users]);
        return data;
    }

    async function updateUser(userId, payload) {
        const data = await shopApi.updateUser(userId, payload);
        await invalidateQueries(queryClient, [
            shopKeys.users,
            shopKeys.user(userId)
        ]);
        return data;
    }

    async function deleteUser(userId) {
        await shopApi.deleteUser(userId);
        await invalidateQueries(queryClient, [
            shopKeys.users,
            ["user"]
        ]);
    }

    async function addGameToLibrary(userId, gameId) {
        await shopApi.addGameToUser(userId, gameId);
        await invalidateQueries(queryClient, [
            shopKeys.user(userId),
            ["user-catalog", userId]
        ]);
    }

    async function removeGameFromLibrary(userId, gameId) {
        await shopApi.removeGameFromUser(userId, gameId);
        await invalidateQueries(queryClient, [
            shopKeys.user(userId),
            ["user-catalog", userId]
        ]);
    }

    const value = {
        createGame,
        updateGame,
        deleteGame,
        createDeveloper,
        updateDeveloper,
        createDeveloperWithGames,
        deleteDeveloper,
        createPublisher,
        updatePublisher,
        deletePublisher,
        createCategory,
        updateCategory,
        deleteCategory,
        createUser,
        updateUser,
        deleteUser,
        addGameToLibrary,
        removeGameFromLibrary
    };

    return <AppStateContext.Provider value={value}>{children}</AppStateContext.Provider>;
}
