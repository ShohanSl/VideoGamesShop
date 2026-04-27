import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { shopApi } from "@/api/shopApi";

export const shopKeys = {
    categories: ["categories"],
    developers: ["developers"],
    developer: (id) => ["developer", Number(id)],
    publishers: ["publishers"],
    publisher: (id) => ["publisher", Number(id)],
    users: ["users"],
    user: (id) => ["user", Number(id)],
    game: (id) => ["game", Number(id)],
    catalog: (scope, filters, page, size) => [scope, filters ?? {}, page, size]
};

const REFERENCE_STALE_TIME = 5 * 60 * 1000;

export function useCategoriesQuery() {
    return useQuery({
        queryKey: shopKeys.categories,
        queryFn: shopApi.getCategories,
        staleTime: REFERENCE_STALE_TIME
    });
}

export function useDevelopersQuery() {
    return useQuery({
        queryKey: shopKeys.developers,
        queryFn: shopApi.getDevelopers,
        staleTime: REFERENCE_STALE_TIME
    });
}

export function usePublishersQuery() {
    return useQuery({
        queryKey: shopKeys.publishers,
        queryFn: shopApi.getPublishers,
        staleTime: REFERENCE_STALE_TIME
    });
}

export function useUsersQuery() {
    return useQuery({
        queryKey: shopKeys.users,
        queryFn: shopApi.getUsers,
        staleTime: REFERENCE_STALE_TIME
    });
}

export function useGameQuery(id) {
    return useQuery({
        queryKey: shopKeys.game(id),
        queryFn: () => shopApi.getGame(id),
        enabled: Boolean(id)
    });
}

export function useDeveloperQuery(id) {
    return useQuery({
        queryKey: shopKeys.developer(id),
        queryFn: () => shopApi.getDeveloper(id),
        enabled: Boolean(id)
    });
}

export function usePublisherQuery(id) {
    return useQuery({
        queryKey: shopKeys.publisher(id),
        queryFn: () => shopApi.getPublisher(id),
        enabled: Boolean(id)
    });
}

export function useUserQuery(id) {
    return useQuery({
        queryKey: shopKeys.user(id),
        queryFn: () => shopApi.getUser(id),
        enabled: Boolean(id)
    });
}

export function useCatalogQuery({ filters = {}, page = 0, size = 10, scope = "catalog", enabled = true }) {
    return useQuery({
        queryKey: shopKeys.catalog(scope, filters, page, size),
        queryFn: () => shopApi.getGames(filters, page, size),
        enabled,
        placeholderData: keepPreviousData
    });
}
