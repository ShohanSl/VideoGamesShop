import { lazy, Suspense } from "react";
import { BrowserRouter, Navigate, Outlet, Route, Routes } from "react-router-dom";
import { Center, Loader } from "@mantine/core";
import {
    IconCategory,
    IconCode,
    IconDeviceGamepad2,
    IconHome2,
    IconReportAnalytics,
    IconUsers,
    IconBuildingStore,
    IconBooks
} from "@tabler/icons-react";
import { useSession } from "@/app/session/SessionContext";
import { AdminShell, UserShell } from "@/components/shell/AppShell";

const RoleSelectionPage = lazy(() => import("@/pages/AuthPages").then((module) => ({ default: module.RoleSelectionPage })));
const AdminLoginPage = lazy(() => import("@/pages/AuthPages").then((module) => ({ default: module.AdminLoginPage })));
const UserLoginPage = lazy(() => import("@/pages/AuthPages").then((module) => ({ default: module.UserLoginPage })));
const AdminCatalogPage = lazy(() => import("@/pages/CatalogPage").then((module) => ({ default: module.AdminCatalogPage })));
const UserCatalogPage = lazy(() => import("@/pages/CatalogPage").then((module) => ({ default: module.UserCatalogPage })));
const UserLibraryPage = lazy(() => import("@/pages/CatalogPage").then((module) => ({ default: module.UserLibraryPage })));
const AdminGameDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.AdminGameDetailsPage })));
const UserGameDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.UserGameDetailsPage })));
const AdminDeveloperDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.AdminDeveloperDetailsPage })));
const UserDeveloperDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.UserDeveloperDetailsPage })));
const AdminPublisherDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.AdminPublisherDetailsPage })));
const UserPublisherDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.UserPublisherDetailsPage })));
const AdminUserDetailsPage = lazy(() => import("@/pages/EntityDetailsPages").then((module) => ({ default: module.AdminUserDetailsPage })));
const GamesPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.GamesPage })));
const DevelopersPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.DevelopersPage })));
const PublishersPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.PublishersPage })));
const CategoriesPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.CategoriesPage })));
const UsersPage = lazy(() => import("@/pages/ManagementPages").then((module) => ({ default: module.UsersPage })));

const ADMIN_NAV_ITEMS = [
    { to: "/admin/catalog", label: "Каталог", icon: IconHome2 },
    { to: "/admin/management/games", label: "Игры", icon: IconDeviceGamepad2 },
    { to: "/admin/management/developers", label: "Разработчики", icon: IconCode },
    { to: "/admin/management/publishers", label: "Издатели", icon: IconBuildingStore },
    { to: "/admin/management/categories", label: "Категории", icon: IconCategory },
    { to: "/admin/management/users", label: "Пользователи", icon: IconUsers }
];

const USER_NAV_ITEMS = [
    { to: "/user/catalog", label: "Каталог", icon: IconHome2 },
    { to: "/user/library", label: "Библиотека", icon: IconBooks }
];

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

function RequireUserSession() {
    const { role, currentUser, token } = useSession();

    if (role !== "user" || !currentUser || !token) {
        return <Navigate to="/login/user" replace />;
    }

    return <Outlet />;
}

function RequireAdminSession() {
    const { role, token } = useSession();

    if (role !== "admin" || !token) {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
}

function AdminLayout() {
    return <AdminShell navItems={ADMIN_NAV_ITEMS} reportIcon={IconReportAnalytics} />;
}

function UserLayout() {
    const { currentUser } = useSession();
    return <UserShell navItems={USER_NAV_ITEMS} username={currentUser?.username} />;
}

export function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<LazyPage><RoleSelectionPage /></LazyPage>} />
                <Route path="/login/admin" element={<LazyPage><AdminLoginPage /></LazyPage>} />
                <Route path="/login/user" element={<LazyPage><UserLoginPage /></LazyPage>} />

                <Route path="/admin" element={<RequireAdminSession />}>
                    <Route element={<AdminLayout />}>
                        <Route index element={<Navigate to="/admin/catalog" replace />} />
                        <Route path="catalog" element={<LazyPage><AdminCatalogPage /></LazyPage>} />
                        <Route path="games/:gameId" element={<LazyPage><AdminGameDetailsPage /></LazyPage>} />
                        <Route path="developers/:developerId" element={<LazyPage><AdminDeveloperDetailsPage /></LazyPage>} />
                        <Route path="publishers/:publisherId" element={<LazyPage><AdminPublisherDetailsPage /></LazyPage>} />
                        <Route path="users/:userId" element={<LazyPage><AdminUserDetailsPage /></LazyPage>} />
                        <Route path="management/games" element={<LazyPage><GamesPage /></LazyPage>} />
                        <Route path="management/developers" element={<LazyPage><DevelopersPage /></LazyPage>} />
                        <Route path="management/publishers" element={<LazyPage><PublishersPage /></LazyPage>} />
                        <Route path="management/categories" element={<LazyPage><CategoriesPage /></LazyPage>} />
                        <Route path="management/users" element={<LazyPage><UsersPage /></LazyPage>} />
                    </Route>
                </Route>

                <Route path="/user" element={<RequireUserSession />}>
                    <Route element={<UserLayout />}>
                        <Route index element={<Navigate to="/user/catalog" replace />} />
                        <Route path="catalog" element={<LazyPage><UserCatalogPage /></LazyPage>} />
                        <Route path="library" element={<LazyPage><UserLibraryPage /></LazyPage>} />
                        <Route path="games/:gameId" element={<LazyPage><UserGameDetailsPage /></LazyPage>} />
                        <Route path="developers/:developerId" element={<LazyPage><UserDeveloperDetailsPage /></LazyPage>} />
                        <Route path="publishers/:publisherId" element={<LazyPage><UserPublisherDetailsPage /></LazyPage>} />
                    </Route>
                </Route>

                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </BrowserRouter>
    );
}
