import { useState } from "react";
import { Anchor, Badge, Button, Card, Group, SimpleGrid, Stack, Text, Title } from "@mantine/core";
import { Link, useParams } from "react-router-dom";
import {
    useCategoriesQuery,
    useDeveloperQuery,
    useGameQuery,
    usePublishersQuery,
    usePublisherQuery,
    useUserQuery
} from "@/api/shopQueries";
import { useSession } from "@/app/session/SessionContext";
import { useAppState } from "@/app/state/appStateContext";
import { CatalogIconButton, CatalogPanel } from "@/components/catalog/CatalogPanel";
import { shopApi } from "@/api/shopApi";
import { SectionCard } from "@/components/ui/SectionCard";
import { formatDate, formatPrice } from "@/utils/format";

function EmptyState({ text }) {
    return (
        <SectionCard>
            <Text c="dimmed" ta="center">{text}</Text>
        </SectionCard>
    );
}

function InfoCard({ title, children }) {
    return (
        <Card withBorder radius="xl" p="lg">
            <Stack gap="sm">
                <Title order={3}>{title}</Title>
                {children}
            </Stack>
        </Card>
    );
}

function QueryState({ isPending, isError, error, emptyText, children }) {
    if (isPending) {
        return <EmptyState text="Загрузка..." />;
    }

    if (isError) {
        return <EmptyState text={error.message} />;
    }

    if (!children) {
        return <EmptyState text={emptyText} />;
    }

    return children;
}

function GameDetailsView({ mode }) {
    const { gameId } = useParams();
    const { currentUser } = useSession();
    const { addGameToLibrary } = useAppState();
    const query = useGameQuery(gameId);
    const currentUserQuery = useUserQuery(currentUser?.id);
    const game = query.data;
    const developerHref = `/${mode}/developers/${game?.developerId}`;
    const publisherHref = `/${mode}/publishers/${game?.publisherId}`;
    const isOwnedByCurrentUser = mode === "user"
        && Boolean(currentUserQuery.data?.games?.some((item) => item.id === game?.id));
    const gameAction = mode === "user" ? (
        isOwnedByCurrentUser ? (
            <Badge variant="light" color="green" radius="xl">В библиотеке</Badge>
        ) : (
            <Button onClick={() => void addGameToLibrary(currentUser.id, game.id)}>Купить</Button>
        )
    ) : null;
    const sectionAction = (
        <Group gap="sm" align="center">
            <Text fw={700} size="xl">{game ? formatPrice(game.price) : null}</Text>
            {gameAction}
        </Group>
    );

    return (
        <QueryState
            isPending={query.isPending}
            isError={query.isError}
            error={query.error}
            emptyText="Игра не найдена."
        >
            {game ? (
                <SectionCard title={game.title} action={sectionAction}>
                    <SimpleGrid cols={{ base: 1, md: 2 }} spacing="lg">
                        <InfoCard title="Основная информация">
                            <Text>{game.description}</Text>
                            <Text c="dimmed">Дата релиза: {formatDate(game.releaseDate)}</Text>
                            <Text>
                                Разработчик: <Anchor component={Link} to={developerHref}>{game.developerName}</Anchor>
                            </Text>
                            <Text>
                                Издатель: <Anchor component={Link} to={publisherHref}>{game.publisherName}</Anchor>
                            </Text>
                        </InfoCard>
                        <InfoCard title="Категории">
                            <Group gap="xs">
                                {(game.categories || []).map((category) => (
                                    <Badge key={category.id} variant="light" radius="xl">{category.name}</Badge>
                                ))}
                            </Group>
                        </InfoCard>
                    </SimpleGrid>
                </SectionCard>
            ) : null}
        </QueryState>
    );
}

function DeveloperDetailsView({ mode }) {
    const { developerId } = useParams();
    const query = useDeveloperQuery(developerId);
    const developer = query.data;

    return (
        <QueryState
            isPending={query.isPending}
            isError={query.isError}
            error={query.error}
            emptyText="Разработчик не найден."
        >
            {developer ? (
                <SectionCard title={developer.name}>
                    <SimpleGrid cols={{ base: 1, md: 2 }} spacing="lg">
                        <InfoCard title="Информация">
                            <Text c="dimmed">Страна: {developer.country}</Text>
                            <Text c="dimmed">Дата основания: {formatDate(developer.foundedDate)}</Text>
                        </InfoCard>
                        <InfoCard title="Игры разработчика">
                            <Stack gap="xs">
                                {(developer.games || []).map((game) => (
                                    <Anchor key={game.id} component={Link} to={`/${mode}/games/${game.id}`}>{game.title}</Anchor>
                                ))}
                            </Stack>
                        </InfoCard>
                    </SimpleGrid>
                </SectionCard>
            ) : null}
        </QueryState>
    );
}

function PublisherDetailsView({ mode }) {
    const { publisherId } = useParams();
    const query = usePublisherQuery(publisherId);
    const publisher = query.data;

    return (
        <QueryState
            isPending={query.isPending}
            isError={query.isError}
            error={query.error}
            emptyText="Издатель не найден."
        >
            {publisher ? (
                <SectionCard title={publisher.name}>
                    <SimpleGrid cols={{ base: 1, md: 2 }} spacing="lg">
                        <InfoCard title="Информация">
                            <Text c="dimmed">Страна: {publisher.country}</Text>
                            <Text c="dimmed">Дата основания: {formatDate(publisher.foundedDate)}</Text>
                        </InfoCard>
                        <InfoCard title="Игры издателя">
                            <Stack gap="xs">
                                {(publisher.games || []).map((game) => (
                                    <Anchor key={game.id} component={Link} to={`/${mode}/games/${game.id}`}>{game.title}</Anchor>
                                ))}
                            </Stack>
                        </InfoCard>
                    </SimpleGrid>
                </SectionCard>
            ) : null}
        </QueryState>
    );
}

export function AdminGameDetailsPage() {
    return <GameDetailsView mode="admin" />;
}

export function UserGameDetailsPage() {
    return <GameDetailsView mode="user" />;
}

export function AdminDeveloperDetailsPage() {
    return <DeveloperDetailsView mode="admin" />;
}

export function UserDeveloperDetailsPage() {
    return <DeveloperDetailsView mode="user" />;
}

export function AdminPublisherDetailsPage() {
    return <PublisherDetailsView mode="admin" />;
}

export function UserPublisherDetailsPage() {
    return <PublisherDetailsView mode="user" />;
}

export function AdminUserDetailsPage() {
    const { userId } = useParams();
    const { addGameToLibrary, removeGameFromLibrary } = useAppState();
    const [catalogOpen, setCatalogOpen] = useState(false);
    const userQuery = useUserQuery(userId);
    const { data: categories = [] } = useCategoriesQuery();
    const { data: publishers = [] } = usePublishersQuery();
    const user = userQuery.data;
    const excludedGameIds = (user?.games || []).map((game) => game.id);

    return (
        <QueryState
            isPending={userQuery.isPending}
            isError={userQuery.isError}
            error={userQuery.error}
            emptyText="Пользователь не найден."
        >
            {user ? (
                <SectionCard title={user.username} action={<Text c="dimmed">Игр в библиотеке: {user.games.length}</Text>}>
                    <SimpleGrid cols={{ base: 1, xl: catalogOpen ? 2 : 1 }} spacing="lg">
                        <Card withBorder radius="xl" p="lg">
                            <CatalogPanel
                                title="Библиотека"
                                games={user.games}
                                categories={categories}
                                publishers={publishers}
                                showFilters={false}
                                emptyText="Библиотека пока пуста."
                                gameHrefBuilder={(gameId) => `/admin/games/${gameId}`}
                                trailingAction={!catalogOpen ? <CatalogIconButton label="Открыть каталог" onClick={() => setCatalogOpen(true)} /> : null}
                                actionRenderer={(game) => (
                                    <Button color="red" variant="light" onClick={() => void removeGameFromLibrary(user.id, game.id)}>Удалить</Button>
                                )}
                            />
                        </Card>
                        {catalogOpen ? (
                            <Card withBorder radius="xl" p="lg">
                                <CatalogPanel
                                    title="Каталог"
                                    games={[]}
                                    categories={categories}
                                    publishers={publishers}
                                    compact
                                    pageSize={10}
                                    queryKeyPrefix={["admin-user-catalog", Number(user.id)]}
                                    remoteFetch={(filters, page, size) => shopApi.getGames({
                                        ...filters,
                                        excludedGameIds
                                    }, page, size)}
                                    emptyText="Все игры уже добавлены в библиотеку."
                                    gameHrefBuilder={(gameId) => `/admin/games/${gameId}`}
                                    leadingAction={<CatalogIconButton icon="close" label="Закрыть каталог" onClick={() => setCatalogOpen(false)} />}
                                    actionRenderer={(game) => (
                                        <Button onClick={() => void addGameToLibrary(user.id, game.id)}>Добавить</Button>
                                    )}
                                />
                            </Card>
                        ) : null}
                    </SimpleGrid>
                </SectionCard>
            ) : null}
        </QueryState>
    );
}
