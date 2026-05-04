import { createTheme, MantineProvider } from "@mantine/core";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { SessionProvider } from "@/app/session/SessionContext";
import { AppStateProvider } from "@/app/state/AppStateProvider";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            retry: 1,
            refetchOnWindowFocus: false
        }
    }
});

const theme = createTheme({
    primaryColor: "blue",
    defaultRadius: "md",
    fontFamily: "Segoe UI, Tahoma, Geneva, Verdana, sans-serif",
    headings: {
        fontFamily: "Segoe UI, Tahoma, Geneva, Verdana, sans-serif"
    }
});

export function AppProviders({ children }) {
    return (
        <MantineProvider theme={theme} defaultColorScheme="light">
            <QueryClientProvider client={queryClient}>
                <SessionProvider>
                    <AppStateProvider>{children}</AppStateProvider>
                </SessionProvider>
            </QueryClientProvider>
        </MantineProvider>
    );
}
