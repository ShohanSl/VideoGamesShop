import { AppShell as MantineAppShell, Button, NavLink, ScrollArea, Stack, Text, Title } from "@mantine/core";
import { NavLink as RouterNavLink, Outlet, useNavigate } from "react-router-dom";
import {
    IconBooks,
    IconBuildingStore,
    IconCategory,
    IconCode,
    IconDeviceGamepad2,
    IconHome2,
    IconReportAnalytics,
    IconUsers
} from "@tabler/icons-react";
import { shopApi } from "@/api/shopApi";
import { ReportPanel } from "@/components/shell/ReportPanel";
import { useSession } from "@/app/session/SessionContext";

const USER_NAV_ITEMS = [
    { to: "/catalog", label: "Каталог", icon: IconHome2 },
    { to: "/library", label: "Библиотека", icon: IconBooks }
];

const ADMIN_NAV_ITEMS = [
    { to: "/catalog", label: "Каталог", icon: IconHome2 },
    { to: "/management/games", label: "Игры", icon: IconDeviceGamepad2 },
    { to: "/management/developers", label: "Разработчики", icon: IconCode },
    { to: "/management/publishers", label: "Издатели", icon: IconBuildingStore },
    { to: "/management/categories", label: "Категории", icon: IconCategory },
    { to: "/management/users", label: "Пользователи", icon: IconUsers }
];

export function AppShell() {
    const navigate = useNavigate();
    const { currentUser, isAdmin, resetSession } = useSession();
    const navItems = isAdmin ? ADMIN_NAV_ITEMS : USER_NAV_ITEMS;

    async function handleLogout() {
        try {
            await shopApi.logout();
        } catch {
            // Even if the token is already invalid or expired, local logout should still complete.
        } finally {
            resetSession();
            navigate("/", { replace: true });
        }
    }

    return (
        <MantineAppShell
            padding="lg"
            navbar={{ width: 280, breakpoint: "md" }}
            styles={{ main: { background: "linear-gradient(180deg, #f7faff 0%, #f1f5fb 100%)", minHeight: "100vh" } }}
        >
            <MantineAppShell.Navbar p="md">
                <MantineAppShell.Section>
                    <Stack gap={2}>
                        <Text c="blue" fw={700} tt="uppercase" size="xs">Video Games Shop</Text>
                        <Title order={3}>{isAdmin ? "Панель управления" : "Личный кабинет"}</Title>
                        {currentUser?.username ? (
                            <Text c="dimmed" size="sm">Пользователь: {currentUser.username}</Text>
                        ) : null}
                    </Stack>
                </MantineAppShell.Section>
                <MantineAppShell.Section grow component={ScrollArea} mt="lg">
                    <Stack gap="xs">
                        {navItems.map((item) => (
                            <NavLink
                                key={item.to}
                                component={RouterNavLink}
                                to={item.to}
                                label={item.label}
                                leftSection={item.icon ? <item.icon size={18} /> : null}
                            />
                        ))}
                    </Stack>
                </MantineAppShell.Section>
                <MantineAppShell.Section mb="sm">
                    <Button
                        variant="default"
                        fullWidth
                        onClick={() => void handleLogout()}
                    >
                        Выйти
                    </Button>
                </MantineAppShell.Section>
                {isAdmin ? (
                    <MantineAppShell.Section>
                        <ReportPanel icon={IconReportAnalytics} />
                    </MantineAppShell.Section>
                ) : null}
            </MantineAppShell.Navbar>
            <MantineAppShell.Main>
                <Outlet />
            </MantineAppShell.Main>
        </MantineAppShell>
    );
}
