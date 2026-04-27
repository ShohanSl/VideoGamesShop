import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import {
    ActionIcon,
    Badge,
    Button,
    Card,
    Group,
    Popover,
    Pagination,
    Select,
    SimpleGrid,
    Stack,
    Text,
    TextInput,
    Title
} from "@mantine/core";
import { useDebouncedValue } from "@mantine/hooks";
import { useQuery } from "@tanstack/react-query";
import { IconFilter, IconPlus, IconX } from "@tabler/icons-react";
import { formatPrice } from "@/utils/format";

function paginate(items, page, pageSize) {
    const start = (page - 1) * pageSize;
    return items.slice(start, start + pageSize);
}

export function CatalogPanel({
    title,
    games,
    categories,
    publishers,
    actionRenderer,
    emptyText = "Список пуст.",
    pageSize = 10,
    compact = false,
    showFilters = true,
    showSearch = true,
    leadingAction,
    trailingAction,
    remoteFetch,
    queryKeyPrefix = "catalog"
}) {
    const [selectedCategory, setSelectedCategory] = useState("all");
    const [selectedPublisher, setSelectedPublisher] = useState("all");
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(1);
    const [filtersOpened, setFiltersOpened] = useState(false);
    const [debouncedSearch] = useDebouncedValue(search, 250);
    const isRemote = typeof remoteFetch === "function";
    const remoteScopeKey = Array.isArray(queryKeyPrefix) ? queryKeyPrefix : [queryKeyPrefix];

    const filteredGames = useMemo(() => {
        return games.filter((game) => {
            const matchesCategory = selectedCategory === "all"
                ? true
                : game.categories?.some((category) => String(category.id) === selectedCategory);
            const matchesPublisher = selectedPublisher === "all"
                ? true
                : String(game.publisherId) === selectedPublisher;
            const matchesSearch = game.title.toLowerCase().includes(search.trim().toLowerCase());
            return matchesCategory && matchesPublisher && matchesSearch;
        });
    }, [games, search, selectedCategory, selectedPublisher]);

    const remoteQuery = useQuery({
        queryKey: [...remoteScopeKey, page, pageSize, selectedCategory, selectedPublisher, debouncedSearch],
        queryFn: () => remoteFetch({
            categoryIds: selectedCategory === "all" ? undefined : [Number(selectedCategory)],
            publisherId: selectedPublisher === "all" ? undefined : Number(selectedPublisher),
            title: debouncedSearch.trim() || undefined
        }, page - 1, pageSize),
        enabled: isRemote,
        placeholderData: (previousData) => previousData
    });

    const totalPages = isRemote
        ? Math.max(1, remoteQuery.data?.totalPages || 1)
        : Math.max(1, Math.ceil(filteredGames.length / pageSize));
    const visibleGames = isRemote
        ? (remoteQuery.data?.content || [])
        : paginate(filteredGames, Math.min(page, totalPages), pageSize);

    const categoryOptions = [{ value: "all", label: "Все" }, ...categories.map((item) => ({ value: String(item.id), label: item.name }))];
    const publisherOptions = [{ value: "all", label: "Все" }, ...publishers.map((item) => ({ value: String(item.id), label: item.name }))];

    function resetFilters() {
        setSelectedCategory("all");
        setSelectedPublisher("all");
        setSearch("");
        setPage(1);
    }

    return (
        <Stack gap="md">
            <Group justify="space-between" align="flex-start" wrap="wrap">
                <Group gap="sm" align="center">
                    {leadingAction || null}
                    <Title order={2}>{title}</Title>
                </Group>
                <Group gap="sm" align="center" wrap={compact ? "wrap" : "nowrap"}>
                    {showSearch ? (
                        <TextInput
                            placeholder="Поиск по названию"
                            value={search}
                            onChange={(event) => {
                                setSearch(event.currentTarget.value);
                                setPage(1);
                            }}
                            w={compact ? "100%" : 280}
                            miw={compact ? 0 : 240}
                            flex={compact ? "1 1 220px" : "0 0 280px"}
                        />
                    ) : null}
                    {showFilters ? (
                        <Popover
                            shadow="md"
                            width={260}
                            position="bottom-end"
                            opened={filtersOpened}
                            onChange={setFiltersOpened}
                        >
                            <Popover.Target>
                                <Button
                                    variant="default"
                                    leftSection={<IconFilter size={16} />}
                                    onClick={() => setFiltersOpened((opened) => !opened)}
                                >
                                    Фильтры
                                </Button>
                            </Popover.Target>
                            <Popover.Dropdown>
                                <Stack gap="sm">
                                    <Select
                                        label="Категория"
                                        data={categoryOptions}
                                        value={selectedCategory}
                                        comboboxProps={{ withinPortal: false }}
                                        onChange={(value) => {
                                            setSelectedCategory(value || "all");
                                            setPage(1);
                                        }}
                                    />
                                    <Select
                                        label="Издатель"
                                        data={publisherOptions}
                                        value={selectedPublisher}
                                        comboboxProps={{ withinPortal: false }}
                                        onChange={(value) => {
                                            setSelectedPublisher(value || "all");
                                            setPage(1);
                                        }}
                                    />
                                    <Button variant="default" onClick={resetFilters}>Сбросить</Button>
                                </Stack>
                            </Popover.Dropdown>
                        </Popover>
                    ) : null}
                    {trailingAction || null}
                </Group>
            </Group>

            {isRemote && remoteQuery.isError ? (
                <Card withBorder radius="xl" p="xl">
                    <Text c="red" ta="center">{remoteQuery.error.message}</Text>
                </Card>
            ) : !visibleGames.length ? (
                <Card withBorder radius="xl" p="xl">
                    <Text c="dimmed" ta="center">
                        {isRemote && remoteQuery.isFetching ? "Загрузка..." : emptyText}
                    </Text>
                </Card>
            ) : (
                <SimpleGrid cols={1} spacing="md">
                    {visibleGames.map((game) => (
                        <Card key={game.id} withBorder radius="xl" padding={compact ? "md" : "lg"}>
                            <Group justify="space-between" align="flex-start" wrap="nowrap">
                                <Stack gap="xs" style={{ flex: 1, minWidth: 0 }}>
                                    <Text component={Link} to={`/games/${game.id}`} fw={700} size="lg" td="none" c="dark">
                                        {game.title}
                                    </Text>
                                    <Text c="dimmed">{game.developerName} • {game.publisherName}</Text>
                                    <Group gap="xs">
                                        {(game.categories || []).map((category) => (
                                            <Badge key={category.id} variant="light" radius="xl">{category.name}</Badge>
                                        ))}
                                    </Group>
                                </Stack>
                                <Stack gap="sm" align="flex-end">
                                    <Text fw={700} size="lg">{formatPrice(game.price)}</Text>
                                    {actionRenderer ? actionRenderer(game) : null}
                                </Stack>
                            </Group>
                        </Card>
                    ))}
                </SimpleGrid>
            )}

            <Group justify="center">
                <Pagination total={totalPages} value={Math.min(page, totalPages)} onChange={setPage} />
            </Group>
        </Stack>
    );
}

export function CatalogIconButton({ icon = "plus", label, onClick }) {
    const Icon = icon === "close" ? IconX : IconPlus;
    return (
        <ActionIcon variant="light" color="blue" radius="xl" size="lg" onClick={onClick} aria-label={label}>
            <Icon size={18} />
        </ActionIcon>
    );
}
