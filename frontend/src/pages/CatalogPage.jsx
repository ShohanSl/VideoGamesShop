import { Card } from "@mantine/core";
import { shopApi } from "@/api/shopApi";
import { useCategoriesQuery, usePublishersQuery } from "@/api/shopQueries";
import { CatalogPanel } from "@/components/catalog/CatalogPanel";
import { SectionCard } from "@/components/ui/SectionCard";

export function CatalogPage() {
    const { data: categories = [] } = useCategoriesQuery();
    const { data: publishers = [] } = usePublishersQuery();

    return (
        <SectionCard title="Каталог">
            <Card withBorder radius="xl" p="lg">
                <CatalogPanel
                    title={null}
                    games={[]}
                    categories={categories}
                    publishers={publishers}
                    pageSize={10}
                    remoteFetch={shopApi.getGames}
                />
            </Card>
        </SectionCard>
    );
}
