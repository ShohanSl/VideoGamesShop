import { AppShell as MantineAppShell, Button, NavLink, ScrollArea, Stack, Text, Title } from "@mantine/core";
import { NavLink as RouterNavLink, Outlet, useNavigate } from "react-router-dom";
import { ReportPanel } from "@/components/shell/ReportPanel";
import { useSession } from "@/app/session/SessionContext";

export function AppShell({
    appLabel = "Video Games Shop",
    title,
    subtitle,
    navItems,
    footer,
    showModeReset = true
}) {
    const { resetSession } = useSession();
    const navigate = useNavigate();

    return (
        <MantineAppShell
            padding="lg"
            navbar={{ width: 280, breakpoint: "md" }}
            styles={{ main: { background: "linear-gradient(180deg, #f7faff 0%, #f1f5fb 100%)", minHeight: "100vh" } }}
        >
            <MantineAppShell.Navbar p="md">
                <MantineAppShell.Section>
                    <Stack gap={2}>
                        <Text c="blue" fw={700} tt="uppercase" size="xs">{appLabel}</Text>
                        {title ? <Title order={3}>{title}</Title> : null}
                        {subtitle ? <Text c="dimmed" size="sm">{subtitle}</Text> : null}
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
                {showModeReset ? (
                    <MantineAppShell.Section mb="sm">
                        <Button
                            variant="default"
                            fullWidth
                            onClick={() => {
                                resetSession();
                                navigate("/");
                            }}
                        >
                            Сменить режим
                        </Button>
                    </MantineAppShell.Section>
                ) : null}
                {footer ? <MantineAppShell.Section>{footer}</MantineAppShell.Section> : null}
            </MantineAppShell.Navbar>
            <MantineAppShell.Main>
                <Outlet />
            </MantineAppShell.Main>
        </MantineAppShell>
    );
}

export function AdminShell({ navItems, reportIcon: ReportIcon }) {
    return (
        <AppShell
            title="Панель управления"
            navItems={navItems}
            footer={<ReportPanel icon={ReportIcon} />}
        />
    );
}

export function UserShell({ navItems, username }) {
    return (
        <AppShell
            title="Пользовательский режим"
            subtitle={username ? `Пользователь: ${username}` : null}
            navItems={navItems}
        />
    );
}
