import { useEffect, useState } from "react";
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

export function LoginPage() {
    const navigate = useNavigate();
    const { enterSession, isAuthenticated } = useSession();
    const [loading, setLoading] = useState(false);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        if (isAuthenticated) {
            navigate("/catalog", { replace: true });
        }
    }, [isAuthenticated, navigate]);

    async function handleSubmit(event) {
        event.preventDefault();
        setLoading(true);
        setError("");
        try {
            const auth = await shopApi.login(username.trim(), password);
            enterSession(auth);
            navigate("/catalog", { replace: true });
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <EntryCard>
            <Stack gap={6}>
                <Text c="blue" fw={700} tt="uppercase" size="xs">Video Games Shop</Text>
                <Title order={2}>Вход</Title>
            </Stack>
            <form onSubmit={handleSubmit}>
                <Stack gap="md">
                    <TextInput
                        label="Имя пользователя"
                        value={username}
                        autoComplete="username"
                        onChange={(event) => {
                            setUsername(event.currentTarget.value);
                            setError("");
                        }}
                    />
                    <PasswordInput
                        label="Пароль"
                        value={password}
                        autoComplete="current-password"
                        onChange={(event) => {
                            setPassword(event.currentTarget.value);
                            setError("");
                        }}
                    />
                    {error ? <Alert color="red">{error}</Alert> : null}
                    <Button type="submit" loading={loading}>Продолжить</Button>
                </Stack>
            </form>
        </EntryCard>
    );
}
