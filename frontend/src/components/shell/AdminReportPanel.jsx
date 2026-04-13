import { useEffect, useMemo, useRef, useState } from "react";
import { shopApi } from "@/api/shopApi";

function formatReport(result) {
    if (!result) {
        return null;
    }

    const match = result.match(/games=(\d+), developers=(\d+), publishers=(\d+), categories=(\d+), users=(\d+)/i);
    if (!match) {
        return result;
    }

    const [, games, developers, publishers, categories, users] = match;
    return [
        `Игр в каталоге: ${games}`,
        `Разработчиков: ${developers}`,
        `Издателей: ${publishers}`,
        `Категорий: ${categories}`,
        `Пользователей: ${users}`
    ].join("\n");
}

function getStatusLabel(status) {
    const labels = {
        PENDING: "Отчёт поставлен в очередь",
        RUNNING: "Отчёт формируется",
        COMPLETED: "Отчёт готов",
        FAILED: "Не удалось построить отчёт"
    };

    return labels[status] || status;
}

export function AdminReportPanel() {
    const [taskId, setTaskId] = useState("");
    const [status, setStatus] = useState("");
    const [result, setResult] = useState("");
    const [error, setError] = useState("");
    const pollRef = useRef(null);

    useEffect(() => {
        return () => {
            if (pollRef.current) {
                window.clearTimeout(pollRef.current);
            }
        };
    }, []);

    async function pollStatus(nextTaskId) {
        try {
            const data = await shopApi.getAsyncJobStatus(nextTaskId);
            setStatus(data.status);
            setResult(data.result || "");
            setError(data.error || "");

            if (data.status === "PENDING" || data.status === "RUNNING") {
                pollRef.current = window.setTimeout(() => {
                    void pollStatus(nextTaskId);
                }, 900);
            }
        } catch (requestError) {
            setError(requestError.message);
            setStatus("FAILED");
        }
    }

    async function handleRunReport() {
        if (pollRef.current) {
            window.clearTimeout(pollRef.current);
        }

        setError("");
        setResult("");

        try {
            const data = await shopApi.startCatalogReport();
            setTaskId(data.taskId);
            setStatus(data.status);
            void pollStatus(data.taskId);
        } catch (requestError) {
            setError(requestError.message);
            setStatus("FAILED");
        }
    }

    const reportText = useMemo(() => formatReport(result), [result]);

    return (
        <div className="report-widget">
            <button className="nav-link report-trigger" type="button" onClick={handleRunReport}>
                Отчёт
            </button>
            <div className="report-box">
                {status ? <div className="report-status">{getStatusLabel(status)}</div> : <div className="report-status muted-text">Отчёт ещё не запускался</div>}
                {taskId ? <div className="report-meta">ID задачи: {taskId}</div> : null}
                {reportText ? <pre className="report-text">{reportText}</pre> : null}
                {error ? <div className="report-error">{error}</div> : null}
            </div>
        </div>
    );
}
