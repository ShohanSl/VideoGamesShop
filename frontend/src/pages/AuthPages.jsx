import { useState } from "react";
import {
    Alert,
    Button,
    Card,
    Center,
    PasswordInput,
    Stack,
    Text,
    TextInput,
    Title
} from "@mantine/core";
import { useNavigate } from "react-router-dom";
import { shopApi } from "@/api/shopApi";
import { useSession } from "@/app/session/SessionContext";

function EntryCard({ children }) {
    return (
        <Center mih="100vh" px="md" style={{ background: "linear-gradient(180deg, #f7faff 0%, #eef4fb 100%)" }}>
            <Card withBorder shadow="sm" radius="xl" p="xl" miw={360} maw={420} w="100%">
                <Stack gap="lg">{children}</Stack>
            </Card>
        </Center>
    );
}

function LoginForm({
    title,
    submitLabel,
    onSubmit,
    loading,
    error,
    onBack,
    username,
    setUsername,
    password,
    setPassword
}) {
    return (
        <EntryCard>
            <Stack gap={6}>
                <Text c="blue" fw={700} tt="uppercase" size="xs">Video Games Shop</Text>
                <Title order={2}>{title}</Title>
            </Stack>
            <form onSubmit={onSubmit}>
                <Stack gap="md">
                    <TextInput
                        label="Имя пользователя"
                        value={username}
                        autoComplete="username"
                        onChange={(event) => setUsername(event.currentTarget.value)}
                    />
                    <PasswordInput
                        label="Пароль"
                        value={password}
                        autoComplete="current-password"
                        onChange={(event) => setPassword(event.currentTarget.value)}
                    />
                    {error ? <Alert color="red">{error}</Alert> : null}
                    <Button type="submit" loading={loading}>{submitLabel}</Button>
                    <Button variant="default" type="button" onClick={onBack}>Назад</Button>
                </Stack>
            </form>
        </EntryCard>
    );
}

export function RoleSelectionPage() {
    const navigate = useNavigate();

    return (
        <EntryCard>
            <Stack gap={6}>
                <Text c="blue" fw={700} tt="uppercase" size="xs">Video Games Shop</Text>
                <Title order={2}>Выберите режим входа</Title>
            </Stack>
            <Button size="md" onClick={() => navigate("/login/user")}>
                Войти как пользователь
            </Button>
            <Button size="md" variant="default" onClick={() => navigate("/login/admin")}>
                Войти как администратор
            </Button>
        </EntryCard>
    );
}

export function AdminLoginPage() {
    const navigate = useNavigate();
    const { enterAdmin } = useSession();
    const [loading, setLoading] = useState(false);
    const [username, setUsername] = useState("admin");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function handleSubmit(event) {
        event.preventDefault();
        setLoading(true);
        setError("");
        try {
            const auth = await shopApi.loginAdmin(username.trim(), password);
            enterAdmin(auth.token);
            navigate("/admin/catalog");
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <LoginForm
            title="Вход администратора"
            submitLabel="Продолжить"
            onSubmit={handleSubmit}
            loading={loading}
            error={error}
            onBack={() => navigate("/")}
            username={username}
            setUsername={(value) => {
                setUsername(value);
                setError("");
            }}
            password={password}
            setPassword={(value) => {
                setPassword(value);
                setError("");
            }}
        />
    );
}

export function UserLoginPage() {
    const navigate = useNavigate();
    const { enterUser } = useSession();
    const [loading, setLoading] = useState(false);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function handleSubmit(event) {
        event.preventDefault();
        setLoading(true);
        setError("");
        try {
            const auth = await shopApi.loginUser(username.trim(), password);
            enterUser({ id: auth.userId, username: auth.username }, auth.token);
            navigate("/user/catalog");
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <LoginForm
            title="Вход пользователя"
            submitLabel="Продолжить"
            onSubmit={handleSubmit}
            loading={loading}
            error={error}
            onBack={() => navigate("/")}
            username={username}
            setUsername={(value) => {
                setUsername(value);
                setError("");
            }}
            password={password}
            setPassword={(value) => {
                setPassword(value);
                setError("");
            }}
        />
    );
}
