import { useMemo, useState } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    ActionIcon,
    Alert,
    Anchor,
    Button,
    Card,
    Group,
    MultiSelect,
    NumberInput,
    Pagination,
    SegmentedControl,
    Select,
    SimpleGrid,
    Stack,
    TextInput,
    Textarea,
    Title
} from "@mantine/core";
import { useFieldArray, useForm, Controller } from "react-hook-form";
import { Link } from "react-router-dom";
import { z } from "zod";
import { IconPencil, IconPlus, IconTrash } from "@tabler/icons-react";
import {
    useCatalogQuery,
    useCategoriesQuery,
    useDevelopersQuery,
    usePublishersQuery,
    useUsersQuery
} from "@/api/shopQueries";
import { useAppState } from "@/app/state/appStateContext";
import { EntityForm } from "@/components/forms/EntityForm";
import { DataTable } from "@/components/ui/DataTable";
import { SectionCard } from "@/components/ui/SectionCard";
import { formatDate, formatPrice } from "@/utils/format";

const STR = {
    required: "обязательное поле",
    positive: "должно быть больше нуля",
    chooseCategory: "Выберите хотя бы одну категорию",
    addGame: "Добавьте хотя бы одну игру",
    name: "Название",
    gameName: "Название игры",
    price: "Цена",
    releaseDate: "Дата релиза",
    description: "Описание",
    developer: "Разработчик",
    publisher: "Издатель",
    country: "Страна",
    foundedDate: "Дата основания",
    username: "Имя пользователя",
    list: "Список",
    edit: "Изменить",
    remove: "Удалить",
    gamesPage: "Управление играми",
    gamesPageDesc: "Создание, редактирование и удаление игр каталога",
    newGame: "Новая игра",
    editGame: "Редактирование игры",
    addGameBtn: "Добавить игру",
    save: "Сохранить",
    developersPage: "Управление разработчиками",
    developersPageDesc: "Поддержка разработчиков и массовое создание вместе с играми",
    noGames: "Без игр",
    withGames: "С играми",
    newDeveloper: "Новый разработчик",
    editDeveloper: "Редактирование разработчика",
    addDeveloper: "Добавить разработчика",
    bundleDeveloper: "Добавить разработчика с играми",
    addOneMoreGame: "Добавить ещё игру",
    createBundle: "Создать набор",
    developerName: "Название разработчика",
    gameShort: "Игра",
    publishersPage: "Управление издателями",
    publishersPageDesc: "Поддержка списка издателей и связанных данных",
    newPublisher: "Новый издатель",
    editPublisher: "Редактирование издателя",
    addPublisher: "Добавить издателя",
    categoriesPage: "Управление категориями",
    categoriesPageDesc: "Категории используются в фильтрации и карточках игр",
    newCategory: "Новая категория",
    editCategory: "Редактирование категории",
    addCategory: "Добавить категорию",
    usersPage: "Управление пользователями",
    usersPageDesc: "Создание профилей и управление библиотеками через карточку пользователя",
    newUser: "Новый пользователь",
    editUser: "Редактирование пользователя",
    addUser: "Добавить пользователя",
    actions: "Действия"
};

const requiredText = (label) => z.string().trim().min(1, `${label}: ${STR.required}`);
const positiveNumber = (label) => z.coerce.number().positive(`${label}: ${STR.positive}`);

const gameSchema = z.object({
    title: requiredText(STR.name),
    price: positiveNumber(STR.price),
    releaseDate: requiredText(STR.releaseDate),
    description: requiredText(STR.description),
    developerId: requiredText(STR.developer),
    publisherId: requiredText(STR.publisher),
    categoryIds: z.array(z.string()).min(1, STR.chooseCategory)
});

const developerSchema = z.object({
    name: requiredText(STR.name),
    country: requiredText(STR.country),
    foundedDate: requiredText(STR.foundedDate)
});

const publisherSchema = z.object({
    name: requiredText(STR.name),
    country: requiredText(STR.country),
    foundedDate: requiredText(STR.foundedDate)
});

const categorySchema = z.object({
    name: requiredText(STR.name)
});

const userSchema = z.object({
    username: requiredText(STR.username)
});

const bulkDeveloperSchema = z.object({
    developer: developerSchema,
    games: z.array(z.object({
        title: requiredText(STR.gameName),
        price: positiveNumber(STR.price),
        releaseDate: requiredText(STR.releaseDate),
        description: requiredText(STR.description),
        publisherId: requiredText(STR.publisher),
        categoryIds: z.array(z.string()).min(1, STR.chooseCategory)
    })).min(1, STR.addGame)
});

function ActionsCell({ onEdit, onDelete }) {
    return (
        <Group gap="xs" wrap="nowrap">
            <ActionIcon variant="light" color="blue" radius="xl" onClick={onEdit} aria-label={STR.edit}>
                <IconPencil size={16} />
            </ActionIcon>
            <ActionIcon variant="light" color="red" radius="xl" onClick={onDelete} aria-label={STR.remove}>
                <IconTrash size={16} />
            </ActionIcon>
        </Group>
    );
}

function linkCell(href, label) {
    return <Anchor component={Link} to={href}>{label}</Anchor>;
}

function EntityManagementPage({ title, description, data, columns, form, tableFooter, emptyText }) {
    return (
        <SectionCard title={title} description={description}>
            <SimpleGrid cols={{ base: 1, xl: 2 }} spacing="lg" verticalSpacing="lg">
                <Card withBorder radius="xl" p="md">
                    <Stack gap="md">
                        <Title order={3}>{STR.list}</Title>
                        <DataTable data={data} columns={columns} emptyText={emptyText} />
                        {tableFooter || null}
                    </Stack>
                </Card>
                <Card withBorder radius="xl" p="md">
                    {form}
                </Card>
            </SimpleGrid>
        </SectionCard>
    );
}

function BulkDeveloperForm({ publishers, categories, onSubmit }) {
    const [requestError, setRequestError] = useState("");
    const form = useForm({
        resolver: zodResolver(bulkDeveloperSchema),
        defaultValues: {
            developer: { name: "", country: "", foundedDate: "" },
            games: [{ title: "", price: 0, releaseDate: "", description: "", publisherId: "", categoryIds: [] }]
        }
    });
    const { fields, append, remove } = useFieldArray({ control: form.control, name: "games" });

    async function handleSubmit(values) {
        setRequestError("");
        try {
            await onSubmit({
                developer: values.developer,
                games: values.games.map((game) => ({
                    ...game,
                    publisherId: Number(game.publisherId),
                    categoryIds: game.categoryIds.map(Number)
                }))
            });
            form.reset({
                developer: { name: "", country: "", foundedDate: "" },
                games: [{ title: "", price: 0, releaseDate: "", description: "", publisherId: "", categoryIds: [] }]
            });
        } catch (error) {
            setRequestError(error.message);
        }
    }

    const publisherOptions = publishers.map((item) => ({ value: String(item.id), label: item.name }));
    const categoryOptions = categories.map((item) => ({ value: String(item.id), label: item.name }));

    return (
        <form onSubmit={form.handleSubmit(handleSubmit)} noValidate>
            <Stack gap="md">
                <Title order={3}>{STR.bundleDeveloper}</Title>
                <SimpleGrid cols={{ base: 1, sm: 3 }}>
                    <TextInput label={STR.developerName} error={form.formState.errors.developer?.name?.message} {...form.register("developer.name")} />
                    <TextInput label={STR.country} error={form.formState.errors.developer?.country?.message} {...form.register("developer.country")} />
                    <TextInput type="date" label={STR.foundedDate} error={form.formState.errors.developer?.foundedDate?.message} {...form.register("developer.foundedDate")} />
                </SimpleGrid>

                {fields.map((field, index) => (
                    <Card key={field.id} withBorder radius="lg" p="md">
                        <Stack gap="sm">
                            <Group justify="space-between">
                                <Title order={4}>{`${STR.gameShort} ${index + 1}`}</Title>
                                {fields.length > 1 ? (
                                    <ActionIcon variant="light" color="red" radius="xl" onClick={() => remove(index)}>
                                        <IconTrash size={16} />
                                    </ActionIcon>
                                ) : null}
                            </Group>
                            <TextInput label={STR.gameName} error={form.formState.errors.games?.[index]?.title?.message} {...form.register(`games.${index}.title`)} />
                            <NumberInput
                                label={STR.price}
                                error={form.formState.errors.games?.[index]?.price?.message}
                                min={0}
                                decimalScale={2}
                                allowNegative={false}
                                {...form.register(`games.${index}.price`, { valueAsNumber: true })}
                            />
                            <TextInput type="date" label={STR.releaseDate} error={form.formState.errors.games?.[index]?.releaseDate?.message} {...form.register(`games.${index}.releaseDate`)} />
                            <Textarea label={STR.description} minRows={3} error={form.formState.errors.games?.[index]?.description?.message} {...form.register(`games.${index}.description`)} />
                            <Controller
                                name={`games.${index}.publisherId`}
                                control={form.control}
                                render={({ field: controllerField }) => (
                                    <Select
                                        label={STR.publisher}
                                        data={publisherOptions}
                                        value={controllerField.value}
                                        onChange={controllerField.onChange}
                                        error={form.formState.errors.games?.[index]?.publisherId?.message}
                                    />
                                )}
                            />
                            <Controller
                                name={`games.${index}.categoryIds`}
                                control={form.control}
                                render={({ field: controllerField }) => (
                                    <MultiSelect
                                        label={"Категории"}
                                        data={categoryOptions}
                                        value={controllerField.value}
                                        onChange={controllerField.onChange}
                                        error={form.formState.errors.games?.[index]?.categoryIds?.message}
                                        searchable
                                    />
                                )}
                            />
                        </Stack>
                    </Card>
                ))}

                {requestError ? <Alert color="red">{requestError}</Alert> : null}

                <Group justify="space-between">
                    <Button variant="default" leftSection={<IconPlus size={16} />} onClick={() => append({ title: "", price: 0, releaseDate: "", description: "", publisherId: "", categoryIds: [] })}>
                        {STR.addOneMoreGame}
                    </Button>
                    <Button type="submit">{STR.createBundle}</Button>
                </Group>
            </Stack>
        </form>
    );
}

export function GamesPage() {
    const { createGame, updateGame, deleteGame } = useAppState();
    const [editingItem, setEditingItem] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [page, setPage] = useState(1);
    const { data: developers = [] } = useDevelopersQuery();
    const { data: publishers = [] } = usePublishersQuery();
    const { data: categories = [] } = useCategoriesQuery();
    const gamesQuery = useCatalogQuery({ scope: "management-games", page: page - 1, size: 10 });
    const games = gamesQuery.data?.content || [];

    const columns = useMemo(() => [
        { header: STR.name, cell: ({ row }) => linkCell(`/games/${row.original.id}`, row.original.title) },
        { header: STR.developer, accessorFn: (row) => row.developerName },
        { header: STR.publisher, accessorFn: (row) => row.publisherName },
        { header: STR.price, cell: ({ row }) => formatPrice(row.original.price) },
        {
            header: STR.actions,
            cell: ({ row }) => (
                <ActionsCell
                    onEdit={() => setEditingItem(row.original)}
                    onDelete={() => void deleteGame(row.original.id)}
                />
            )
        }
    ], [deleteGame]);

    const developerOptions = developers.map((item) => ({ value: String(item.id), label: item.name }));
    const publisherOptions = publishers.map((item) => ({ value: String(item.id), label: item.name }));
    const categoryOptions = categories.map((item) => ({ value: String(item.id), label: item.name }));
    const initialValues = editingItem ? {
        title: editingItem.title,
        price: editingItem.price,
        releaseDate: editingItem.releaseDate,
        description: editingItem.description,
        developerId: String(editingItem.developerId),
        publisherId: String(editingItem.publisherId),
        categoryIds: (editingItem.categories || []).map((item) => String(item.id))
    } : {
        title: "",
        price: 0,
        releaseDate: "",
        description: "",
        developerId: "",
        publisherId: "",
        categoryIds: []
    };

    async function handleSubmit(values) {
        setSubmitting(true);
        try {
            const payload = {
                title: values.title,
                price: Number(values.price),
                releaseDate: values.releaseDate,
                description: values.description,
                developerId: Number(values.developerId),
                publisherId: Number(values.publisherId),
                categoryIds: values.categoryIds.map(Number)
            };
            if (editingItem) {
                await updateGame(editingItem.id, {
                    title: payload.title,
                    price: payload.price,
                    releaseDate: payload.releaseDate,
                    description: payload.description,
                    categoryIds: payload.categoryIds
                });
            } else {
                await createGame(payload);
            }
            setEditingItem(null);
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <EntityManagementPage
            title={STR.gamesPage}
            description={STR.gamesPageDesc}
            data={games}
            columns={columns}
            emptyText={gamesQuery.isPending ? "Загрузка..." : "Список пуст."}
            tableFooter={
                <Group justify="center">
                    <Pagination
                        total={Math.max(1, gamesQuery.data?.totalPages || 1)}
                        value={page}
                        onChange={setPage}
                    />
                </Group>
            }
            form={
                <EntityForm
                    schema={gameSchema}
                    initialValues={initialValues}
                    submitLabel={editingItem ? STR.save : STR.addGameBtn}
                    title={editingItem ? STR.editGame : STR.newGame}
                    onSubmit={handleSubmit}
                    onCancel={() => setEditingItem(null)}
                    loading={submitting}
                    fields={[
                        { name: "title", label: STR.name },
                        { name: "price", label: STR.price, type: "number", step: 0.01, decimalScale: 2, min: 0 },
                        { name: "releaseDate", label: STR.releaseDate, type: "date" },
                        { name: "description", label: STR.description, type: "textarea", minRows: 4 },
                        { name: "developerId", label: STR.developer, type: "select", data: developerOptions, disabled: Boolean(editingItem) },
                        { name: "publisherId", label: STR.publisher, type: "select", data: publisherOptions, disabled: Boolean(editingItem) },
                        { name: "categoryIds", label: "Категории", type: "multiselect", data: categoryOptions }
                    ]}
                />
            }
        />
    );
}

export function DevelopersPage() {
    const { createDeveloper, updateDeveloper, createDeveloperWithGames, deleteDeveloper } = useAppState();
    const [editingItem, setEditingItem] = useState(null);
    const [creationVariant, setCreationVariant] = useState("simple");
    const [submitting, setSubmitting] = useState(false);
    const { data: developers = [] } = useDevelopersQuery();
    const { data: publishers = [] } = usePublishersQuery();
    const { data: categories = [] } = useCategoriesQuery();

    const columns = useMemo(() => [
        { header: STR.name, cell: ({ row }) => linkCell(`/developers/${row.original.id}`, row.original.name) },
        { header: STR.country, accessorFn: (row) => row.country },
        { header: STR.foundedDate, cell: ({ row }) => formatDate(row.original.foundedDate) },
        {
            header: STR.actions,
            cell: ({ row }) => (
                <ActionsCell
                    onEdit={() => setEditingItem(row.original)}
                    onDelete={() => void deleteDeveloper(row.original.id)}
                />
            )
        }
    ], [deleteDeveloper]);

    const initialValues = editingItem ? {
        name: editingItem.name,
        country: editingItem.country,
        foundedDate: editingItem.foundedDate
    } : {
        name: "",
        country: "",
        foundedDate: ""
    };

    async function handleSubmit(values) {
        setSubmitting(true);
        try {
            if (editingItem) {
                await updateDeveloper(editingItem.id, values);
            } else {
                await createDeveloper(values);
            }
            setEditingItem(null);
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <EntityManagementPage
            title={STR.developersPage}
            description={STR.developersPageDesc}
            data={developers}
            columns={columns}
            form={
                <Stack gap="lg">
                    <SegmentedControl
                        value={creationVariant}
                        onChange={setCreationVariant}
                        data={[
                            { value: "simple", label: STR.noGames },
                            { value: "bundle", label: STR.withGames }
                        ]}
                    />
                    {creationVariant === "simple" ? (
                        <EntityForm
                            schema={developerSchema}
                            initialValues={initialValues}
                            submitLabel={editingItem ? STR.save : STR.addDeveloper}
                            title={editingItem ? STR.editDeveloper : STR.newDeveloper}
                            onSubmit={handleSubmit}
                            onCancel={() => setEditingItem(null)}
                            loading={submitting}
                            fields={[
                                { name: "name", label: STR.name },
                                { name: "country", label: STR.country },
                                { name: "foundedDate", label: STR.foundedDate, type: "date" }
                            ]}
                        />
                    ) : (
                        <BulkDeveloperForm
                            publishers={publishers}
                            categories={categories}
                            onSubmit={createDeveloperWithGames}
                        />
                    )}
                </Stack>
            }
        />
    );
}

export function PublishersPage() {
    const { createPublisher, updatePublisher, deletePublisher } = useAppState();
    const [editingItem, setEditingItem] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const { data: publishers = [] } = usePublishersQuery();

    const columns = useMemo(() => [
        { header: STR.name, cell: ({ row }) => linkCell(`/publishers/${row.original.id}`, row.original.name) },
        { header: STR.country, accessorFn: (row) => row.country },
        { header: STR.foundedDate, cell: ({ row }) => formatDate(row.original.foundedDate) },
        {
            header: STR.actions,
            cell: ({ row }) => (
                <ActionsCell
                    onEdit={() => setEditingItem(row.original)}
                    onDelete={() => void deletePublisher(row.original.id)}
                />
            )
        }
    ], [deletePublisher]);

    const initialValues = editingItem ? editingItem : { name: "", country: "", foundedDate: "" };

    async function handleSubmit(values) {
        setSubmitting(true);
        try {
            if (editingItem) {
                await updatePublisher(editingItem.id, values);
            } else {
                await createPublisher(values);
            }
            setEditingItem(null);
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <EntityManagementPage
            title={STR.publishersPage}
            description={STR.publishersPageDesc}
            data={publishers}
            columns={columns}
            form={
                <EntityForm
                    schema={publisherSchema}
                    initialValues={initialValues}
                    submitLabel={editingItem ? STR.save : STR.addPublisher}
                    title={editingItem ? STR.editPublisher : STR.newPublisher}
                    onSubmit={handleSubmit}
                    onCancel={() => setEditingItem(null)}
                    loading={submitting}
                    fields={[
                        { name: "name", label: STR.name },
                        { name: "country", label: STR.country },
                        { name: "foundedDate", label: STR.foundedDate, type: "date" }
                    ]}
                />
            }
        />
    );
}

export function CategoriesPage() {
    const { createCategory, updateCategory, deleteCategory } = useAppState();
    const [editingItem, setEditingItem] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const { data: categories = [] } = useCategoriesQuery();

    const columns = useMemo(() => [
        { header: STR.name, accessorFn: (row) => row.name },
        {
            header: STR.actions,
            cell: ({ row }) => (
                <ActionsCell
                    onEdit={() => setEditingItem(row.original)}
                    onDelete={() => void deleteCategory(row.original.id)}
                />
            )
        }
    ], [deleteCategory]);

    const initialValues = editingItem ? editingItem : { name: "" };

    async function handleSubmit(values) {
        setSubmitting(true);
        try {
            if (editingItem) {
                await updateCategory(editingItem.id, values);
            } else {
                await createCategory(values);
            }
            setEditingItem(null);
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <EntityManagementPage
            title={STR.categoriesPage}
            description={STR.categoriesPageDesc}
            data={categories}
            columns={columns}
            form={
                <EntityForm
                    schema={categorySchema}
                    initialValues={initialValues}
                    submitLabel={editingItem ? STR.save : STR.addCategory}
                    title={editingItem ? STR.editCategory : STR.newCategory}
                    onSubmit={handleSubmit}
                    onCancel={() => setEditingItem(null)}
                    loading={submitting}
                    fields={[
                        { name: "name", label: STR.name }
                    ]}
                />
            }
        />
    );
}

export function UsersPage() {
    const { createUser, updateUser, deleteUser } = useAppState();
    const [editingItem, setEditingItem] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const { data: users = [] } = useUsersQuery();

    const columns = useMemo(() => [
        { header: STR.username, cell: ({ row }) => linkCell(`/users/${row.original.id}`, row.original.username) },
        {
            header: STR.actions,
            cell: ({ row }) => (
                <ActionsCell
                    onEdit={() => setEditingItem(row.original)}
                    onDelete={() => void deleteUser(row.original.id)}
                />
            )
        }
    ], [deleteUser]);

    const initialValues = editingItem ? editingItem : { username: "" };

    async function handleSubmit(values) {
        setSubmitting(true);
        try {
            if (editingItem) {
                await updateUser(editingItem.id, values);
            } else {
                await createUser(values);
            }
            setEditingItem(null);
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <EntityManagementPage
            title={STR.usersPage}
            description={STR.usersPageDesc}
            data={users}
            columns={columns}
            form={
                <EntityForm
                    schema={userSchema}
                    initialValues={initialValues}
                    submitLabel={editingItem ? STR.save : STR.addUser}
                    title={editingItem ? STR.editUser : STR.newUser}
                    onSubmit={handleSubmit}
                    onCancel={() => setEditingItem(null)}
                    loading={submitting}
                    fields={[
                        { name: "username", label: STR.username }
                    ]}
                />
            }
        />
    );
}
