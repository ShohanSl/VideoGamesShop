import { AppShell as MantineAppShell, NavLink, ScrollArea, Stack, Text, Title } from "@mantine/core";
import { IconCategory, IconDeviceGamepad2, IconHome2, IconReportAnalytics, IconUsers, IconBuildingStore, IconCode } from "@tabler/icons-react";
import { NavLink as RouterNavLink, Outlet } from "react-router-dom";
import { ReportPanel } from "@/components/shell/ReportPanel";

const NAV_ITEMS = [
    { to: "/catalog", label: "Каталог", icon: IconHome2 },
    { to: "/management/games", label: "Игры", icon: IconDeviceGamepad2 },
    { to: "/management/developers", label: "Разработчики", icon: IconCode },
    { to: "/management/publishers", label: "Издатели", icon: IconBuildingStore },
    { to: "/management/categories", label: "Категории", icon: IconCategory },
    { to: "/management/users", label: "Пользователи", icon: IconUsers }
];

export function AppShell() {
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
                        <Title order={3}>Панель управления</Title>
                    </Stack>
                </MantineAppShell.Section>
                <MantineAppShell.Section grow component={ScrollArea} mt="lg">
                    <Stack gap="xs">
                        {NAV_ITEMS.map((item) => (
                            <NavLink
                                key={item.to}
                                component={RouterNavLink}
                                to={item.to}
                                label={item.label}
                                leftSection={<item.icon size={18} />}
                            />
                        ))}
                    </Stack>
                </MantineAppShell.Section>
                <MantineAppShell.Section>
                    <ReportPanel icon={IconReportAnalytics} />
                </MantineAppShell.Section>
            </MantineAppShell.Navbar>
            <MantineAppShell.Main>
                <Outlet />
            </MantineAppShell.Main>
        </MantineAppShell>
    );
}