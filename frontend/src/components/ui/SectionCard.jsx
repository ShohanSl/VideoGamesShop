import { Card, Group, Stack, Text, Title } from "@mantine/core";

export function SectionCard({ title, description, action, children }) {
    return (
        <Card withBorder shadow="sm" radius="xl" p="lg">
            <Stack gap="lg">
                {(title || description || action) ? (
                    <Group justify="space-between" align="flex-start" wrap="wrap">
                        <Stack gap={4}>
                            {title ? <Title order={2}>{title}</Title> : null}
                            {description ? <Text c="dimmed">{description}</Text> : null}
                        </Stack>
                        {action || null}
                    </Group>
                ) : null}
                {children}
            </Stack>
        </Card>
    );
}