import { useMemo, useState } from "react";
import { Anchor, Badge, Button, Card, Group, SimpleGrid, Stack, Text, Title } from "@mantine/core";
import { Link, useParams } from "react-router-dom";
import { shopApi } from "@/api/shopApi";
import {
    useCategoriesQuery,
    useDeveloperQuery,
    useGameQuery,
    usePublishersQuery,
    usePublisherQuery,
    useUserQuery
} from "@/api/shopQueries";
import { CatalogIconButton, CatalogPanel } from "@/components/catalog/CatalogPanel";
import { SectionCard } from "@/components/ui/SectionCard";
import { useAppState } from "@/app/state/appStateContext";
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

export function GameDetailsPage() {
    const { gameId } = useParams();
    const query = useGameQuery(gameId);
    const game = query.data;

    return (
        <QueryState
            isPending={query.isPending}
            isError={query.isError}
            error={query.error}
            emptyText="Игра не найдена."
        >
            {game ? (
                <SectionCard title={game.title} action={<Text fw={700} size="xl">{formatPrice(game.price)}</Text>}>
                    <SimpleGrid cols={{ base: 1, md: 2 }} spacing="lg">
                        <InfoCard title="Основная информация">
                            <Text>{game.description}</Text>
                            <Text c="dimmed">Дата релиза: {formatDate(game.releaseDate)}</Text>
                            <Text>
                                Разработчик: <Anchor component={Link} to={`/developers/${game.developerId}`}>{game.developerName}</Anchor>
                            </Text>
                            <Text>
                                Издатель: <Anchor component={Link} to={`/publishers/${game.publisherId}`}>{game.publisherName}</Anchor>
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

export function DeveloperDetailsPage() {
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
                                    <Anchor key={game.id} component={Link} to={`/games/${game.id}`}>{game.title}</Anchor>
                                ))}
                            </Stack>
                        </InfoCard>
                    </SimpleGrid>
                </SectionCard>
            ) : null}
        </QueryState>
    );
}

export function PublisherDetailsPage() {
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
                                    <Anchor key={game.id} component={Link} to={`/games/${game.id}`}>{game.title}</Anchor>
                                ))}
                            </Stack>
                        </InfoCard>
                    </SimpleGrid>
                </SectionCard>
            ) : null}
        </QueryState>
    );
}

export function UserDetailsPage() {
    const { userId } = useParams();
    const { addGameToLibrary, removeGameFromLibrary } = useAppState();
    const [catalogOpen, setCatalogOpen] = useState(false);
    const userQuery = useUserQuery(userId);
    const { data: categories = [] } = useCategoriesQuery();
    const { data: publishers = [] } = usePublishersQuery();

    const user = userQuery.data;
    const excludedGameIds = useMemo(() => (user?.games || []).map((game) => game.id), [user]);

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
                                    queryKeyPrefix={["user-catalog", Number(user.id)]}
                                    remoteFetch={(filters, page, size) => shopApi.getGames({
                                        ...filters,
                                        excludedGameIds
                                    }, page, size)}
                                    emptyText="Все игры уже добавлены в библиотеку."
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
