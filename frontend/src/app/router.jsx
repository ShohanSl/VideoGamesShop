import { lazy, Suspense } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Center, Loader } from "@mantine/core";
import { AppShell } from "@/components/shell/AppShell";

const CatalogPage = lazy(() => import("@/pages/CatalogPage").then((module) => ({ default: module.CatalogPage })));
const GameDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.GameDetailsPage })));
const DeveloperDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.DeveloperDetailsPage })));
const PublisherDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.PublisherDetailsPage })));
const UserDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.UserDetailsPage })));
const GamesPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.GamesPage })));
const DevelopersPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.DevelopersPage })));
const PublishersPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.PublishersPage })));
const CategoriesPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.CategoriesPage })));
const UsersPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.UsersPage })));

function RouteLoader() {
    return (
        <Center mih="60vh">
            <Loader />
        </Center>
    );
}

function LazyPage({ children }) {
    return <Suspense fallback={<RouteLoader />}>{children}</Suspense>;
}

export function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/catalog" replace />} />
                <Route path="/" element={<AppShell />}>
                    <Route path="catalog" element={<LazyPage><CatalogPage /></LazyPage>} />
                    <Route path="games/:gameId" element={<LazyPage><GameDetailsPage /></LazyPage>} />
                    <Route path="developers/:developerId" element={<LazyPage><DeveloperDetailsPage /></LazyPage>} />
                    <Route path="publishers/:publisherId" element={<LazyPage><PublisherDetailsPage /></LazyPage>} />
                    <Route path="users/:userId" element={<LazyPage><UserDetailsPage /></LazyPage>} />
                    <Route path="management/games" element={<LazyPage><GamesPage /></LazyPage>} />
                    <Route path="management/developers" element={<LazyPage><DevelopersPage /></LazyPage>} />
                    <Route path="management/publishers" element={<LazyPage><PublishersPage /></LazyPage>} />
                    <Route path="management/categories" element={<LazyPage><CategoriesPage /></LazyPage>} />
                    <Route path="management/users" element={<LazyPage><UsersPage /></LazyPage>} />
                </Route>
                <Route path="*" element={<Navigate to="/catalog" replace />} />
            </Routes>
        </BrowserRouter>
    );
}
