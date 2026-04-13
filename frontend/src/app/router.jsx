import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AppShell } from "@/components/shell/AppShell";
import { useAppState } from "@/app/state/AppStateProvider";
import { LandingPage } from "@/pages/LandingPage";
import { UserLoginPage } from "@/pages/UserLoginPage";
import { CatalogPage } from "@/pages/CatalogPage";
import { DeveloperDetailsPage, GameDetailsPage, PublisherDetailsPage } from "@/pages/EntityDetailsPages";
import {
    AdminCategoriesPage,
    AdminDevelopersPage,
    AdminGamesPage,
    AdminPublishersPage,
    AdminUsersPage
} from "@/pages/AdminCrudPages";

function UserGuard({ children }) {
    const { activeUser } = useAppState();
    return activeUser ? children : <Navigate to="/user/login" replace />;
}

export function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/user/login" element={<UserLoginPage />} />

                <Route
                    path="/user"
                    element={
                        <UserGuard>
                            <AppShell role="user" />
                        </UserGuard>
                    }
                >
                    <Route path="catalog" element={<CatalogPage role="user" />} />
                    <Route path="library" element={<CatalogPage role="user" libraryOnly />} />
                    <Route path="games/:gameId" element={<GameDetailsPage role="user" />} />
                    <Route path="developers/:developerId" element={<DeveloperDetailsPage role="user" />} />
                    <Route path="publishers/:publisherId" element={<PublisherDetailsPage role="user" />} />
                </Route>

                <Route path="/admin" element={<AppShell role="admin" />}>
                    <Route path="catalog" element={<CatalogPage role="admin" />} />
                    <Route path="games/:gameId" element={<GameDetailsPage role="admin" />} />
                    <Route path="developers/:developerId" element={<DeveloperDetailsPage role="admin" />} />
                    <Route path="publishers/:publisherId" element={<PublisherDetailsPage role="admin" />} />
                    <Route path="manage/games" element={<AdminGamesPage />} />
                    <Route path="manage/developers" element={<AdminDevelopersPage />} />
                    <Route path="manage/publishers" element={<AdminPublishersPage />} />
                    <Route path="manage/categories" element={<AdminCategoriesPage />} />
                    <Route path="manage/users" element={<AdminUsersPage />} />
                </Route>

                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </BrowserRouter>
    );
}
