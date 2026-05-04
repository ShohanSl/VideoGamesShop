import { useMemo } from "react";
import { Badge, Button, Card, Text } from "@mantine/core";
import { shopApi } from "@/api/shopApi";
import { useCategoriesQuery, usePublishersQuery, useUserQuery } from "@/api/shopQueries";
import { useSession } from "@/app/session/SessionContext";
import { useAppState } from "@/app/state/appStateContext";
import { CatalogPanel } from "@/components/catalog/CatalogPanel";
import { SectionCard } from "@/components/ui/SectionCard";

function CatalogLayout({ actionRenderer }) {
    const { data: categories = [] } = useCategoriesQuery();
    const { data: publishers = [] } = usePublishersQuery();

    return (
        <SectionCard title="Каталог">
            <Card withBorder radius="xl" p="lg">
                <CatalogPanel
                    title={null}
                    games={[]}
                    categories={categories}
                    publishers={publishers}
                    pageSize={10}
                    remoteFetch={shopApi.getGames}
                    gameHrefBuilder={(gameId) => `/games/${gameId}`}
                    actionRenderer={actionRenderer}
                />
            </Card>
        </SectionCard>
    );
}

export function CatalogPage() {
    const { currentUser, isUser } = useSession();
    const { addGameToLibrary } = useAppState();
    const userQuery = useUserQuery(isUser ? currentUser?.id : undefined);
    const ownedGameIds = useMemo(
        () => new Set((userQuery.data?.games || []).map((game) => game.id)),
        [userQuery.data]
    );

    const actionRenderer = isUser ? (game) => (
        ownedGameIds.has(game.id) ? (
            <Badge variant="light" color="green" radius="xl">В библиотеке</Badge>
        ) : (
            <Button onClick={() => void addGameToLibrary(currentUser.id, game.id)}>Купить</Button>
        )
    ) : undefined;

    return <CatalogLayout actionRenderer={actionRenderer} />;
}

export function LibraryPage() {
    const { currentUser } = useSession();
    const userQuery = useUserQuery(currentUser?.id);

    if (userQuery.isPending) {
        return (
            <SectionCard title="Библиотека">
                <Text c="dimmed" ta="center">Загрузка...</Text>
            </SectionCard>
        );
    }

    if (userQuery.isError || !userQuery.data) {
        return (
            <SectionCard title="Библиотека">
                <Text c="red" ta="center">{userQuery.error?.message || "Пользователь не найден."}</Text>
            </SectionCard>
        );
    }

    return (
        <SectionCard title="Библиотека">
            <Card withBorder radius="xl" p="lg">
                <CatalogPanel
                    title={null}
                    games={userQuery.data.games || []}
                    categories={[]}
                    publishers={[]}
                    showFilters={false}
                    pageSize={10}
                    gameHrefBuilder={(gameId) => `/games/${gameId}`}
                    emptyText="Библиотека пока пуста."
                />
            </Card>
        </SectionCard>
    );
}
