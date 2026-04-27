/* eslint-disable react-hooks/incompatible-library */
import { flexRender, getCoreRowModel, useReactTable } from "@tanstack/react-table";
import { Paper, ScrollArea, Table, Text } from "@mantine/core";

export function DataTable({ data, columns, emptyText = "Список пуст." }) {
    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel()
    });

    if (!data.length) {
        return (
            <Paper withBorder radius="lg" p="xl">
                <Text c="dimmed" ta="center">{emptyText}</Text>
            </Paper>
        );
    }

    return (
        <ScrollArea>
            <Table striped highlightOnHover withTableBorder verticalSpacing="sm">
                <Table.Thead>
                    {table.getHeaderGroups().map((headerGroup) => (
                        <Table.Tr key={headerGroup.id}>
                            {headerGroup.headers.map((header) => (
                                <Table.Th key={header.id}>
                                    {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                                </Table.Th>
                            ))}
                        </Table.Tr>
                    ))}
                </Table.Thead>
                <Table.Tbody>
                    {table.getRowModel().rows.map((row) => (
                        <Table.Tr key={row.id}>
                            {row.getVisibleCells().map((cell) => (
                                <Table.Td key={cell.id}>
                                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                </Table.Td>
                            ))}
                        </Table.Tr>
                    ))}
                </Table.Tbody>
            </Table>
        </ScrollArea>
    );
}