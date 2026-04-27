import { useEffect } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button, Group, MultiSelect, NumberInput, Select, Stack, Textarea, TextInput, Title } from "@mantine/core";
import { Controller, useForm } from "react-hook-form";

function renderField(field, form) {
    const { control, register, formState: { errors } } = form;
    const error = errors[field.name]?.message;

    if (field.type === "textarea") {
        return (
            <Textarea
                key={field.name}
                label={field.label}
                minRows={field.minRows || 3}
                error={error}
                {...register(field.name)}
            />
        );
    }

    if (field.type === "select") {
        return (
            <Controller
                key={field.name}
                name={field.name}
                control={control}
                render={({ field: controllerField }) => (
                    <Select
                        label={field.label}
                        data={field.data}
                        value={controllerField.value ?? ""}
                        onChange={controllerField.onChange}
                        error={error}
                        disabled={field.disabled}
                        searchable={field.searchable}
                    />
                )}
            />
        );
    }

    if (field.type === "multiselect") {
        return (
            <Controller
                key={field.name}
                name={field.name}
                control={control}
                render={({ field: controllerField }) => (
                    <MultiSelect
                        label={field.label}
                        data={field.data}
                        value={controllerField.value ?? []}
                        onChange={controllerField.onChange}
                        error={error}
                        searchable
                    />
                )}
            />
        );
    }

    if (field.type === "number") {
        return (
            <Controller
                key={field.name}
                name={field.name}
                control={control}
                render={({ field: controllerField }) => (
                    <NumberInput
                        label={field.label}
                        value={controllerField.value ?? ""}
                        onChange={controllerField.onChange}
                        error={error}
                        decimalScale={field.decimalScale || 2}
                        min={field.min}
                        step={field.step || 1}
                        allowNegative={false}
                    />
                )}
            />
        );
    }

    return (
        <TextInput
            key={field.name}
            label={field.label}
            type={field.type || "text"}
            error={error}
            disabled={field.disabled}
            {...register(field.name)}
        />
    );
}

export function EntityForm({
    schema,
    fields,
    initialValues,
    title,
    submitLabel,
    cancelLabel = "Сбросить",
    onSubmit,
    onCancel,
    loading = false,
    children
}) {
    const form = useForm({
        resolver: zodResolver(schema),
        defaultValues: initialValues
    });

    useEffect(() => {
        form.reset(initialValues);
    }, [form, initialValues]);

    return (
        <form onSubmit={form.handleSubmit(onSubmit)} noValidate>
            <Stack gap="md">
                {title ? <Title order={3}>{title}</Title> : null}
                {fields.map((field) => renderField(field, form))}
                {children ? children(form) : null}
                <Group justify="flex-end">
                    {onCancel ? <Button variant="default" type="button" onClick={() => { form.reset(initialValues); onCancel(); }}>{cancelLabel}</Button> : null}
                    <Button type="submit" loading={loading}>{submitLabel}</Button>
                </Group>
            </Stack>
        </form>
    );
}