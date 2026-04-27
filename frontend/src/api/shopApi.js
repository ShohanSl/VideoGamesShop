import { httpClient } from "@/api/httpClient";
import { buildQuery } from "@/utils/format";

export const shopApi = {
    async getGames(filters = {}, page = 0, size = 10) {
        const { data } = await httpClient.get(
            `/games${buildQuery({
                categoryIds: filters.categoryIds,
                excludedGameIds: filters.excludedGameIds,
                publisherId: filters.publisherId,
                title: filters.title,
                page,
                size,
                sort: "title,asc"
            })}`
        );

        return data;
    },
    async getGame(id) {
        const { data } = await httpClient.get(`/games/${id}`);
        return data;
    },
    async createGame(payload) {
        const { data } = await httpClient.post("/games", payload);
        return data;
    },
    async updateGame(id, payload) {
        const { data } = await httpClient.put(`/games/${id}`, payload);
        return data;
    },
    deleteGame(id) {
        return httpClient.delete(`/games/${id}`);
    },
    async getDevelopers() {
        const { data } = await httpClient.get("/developers");
        return data;
    },
    async getDeveloper(id) {
        const { data } = await httpClient.get(`/developers/${id}`);
        return data;
    },
    async createDeveloper(payload) {
        const { data } = await httpClient.post("/developers", payload);
        return data;
    },
    async createDeveloperWithGamesTx(payload) {
        const { data } = await httpClient.post("/developers/with-games/with-tx", payload);
        return data;
    },
    async updateDeveloper(id, payload) {
        const { data } = await httpClient.put(`/developers/${id}`, payload);
        return data;
    },
    deleteDeveloper(id) {
        return httpClient.delete(`/developers/${id}`);
    },
    attachGameToDeveloper(developerId, gameId) {
        return httpClient.post(`/developers/${developerId}/games/${gameId}`);
    },
    detachGameFromDeveloper(developerId, gameId) {
        return httpClient.delete(`/developers/${developerId}/games/${gameId}`);
    },
    async getPublishers() {
        const { data } = await httpClient.get("/publishers");
        return data;
    },
    async getPublisher(id) {
        const { data } = await httpClient.get(`/publishers/${id}`);
        return data;
    },
    async createPublisher(payload) {
        const { data } = await httpClient.post("/publishers", payload);
        return data;
    },
    async updatePublisher(id, payload) {
        const { data } = await httpClient.put(`/publishers/${id}`, payload);
        return data;
    },
    deletePublisher(id) {
        return httpClient.delete(`/publishers/${id}`);
    },
    async getCategories() {
        const { data } = await httpClient.get("/categories");
        return data;
    },
    async createCategory(payload) {
        const { data } = await httpClient.post("/categories", payload);
        return data;
    },
    async updateCategory(id, payload) {
        const { data } = await httpClient.put(`/categories/${id}`, payload);
        return data;
    },
    deleteCategory(id) {
        return httpClient.delete(`/categories/${id}`);
    },
    async getUsers() {
        const { data } = await httpClient.get("/users");
        return data;
    },
    async getUser(id) {
        const { data } = await httpClient.get(`/users/${id}`);
        return data;
    },
    async createUser(payload) {
        const { data } = await httpClient.post("/users", payload);
        return data;
    },
    async updateUser(id, payload) {
        const { data } = await httpClient.put(`/users/${id}`, payload);
        return data;
    },
    deleteUser(id) {
        return httpClient.delete(`/users/${id}`);
    },
    addGameToUser(userId, gameId) {
        return httpClient.post(`/users/${userId}/games/${gameId}`);
    },
    removeGameFromUser(userId, gameId) {
        return httpClient.delete(`/users/${userId}/games/${gameId}`);
    },
    async startCatalogReport() {
        const { data } = await httpClient.post("/async-jobs/catalog-report");
        return data;
    },
    async getAsyncJobStatus(taskId) {
        const { data } = await httpClient.get(`/async-jobs/${taskId}`);
        return data;
    }
};
