package com.example.videogamesshop.service;

import com.example.videogamesshop.repository.CategoryRepository;
import com.example.videogamesshop.repository.DeveloperRepository;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.PublisherRepository;
import com.example.videogamesshop.repository.UserRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncCatalogReportWorker {

    private final AsyncJobRegistryService asyncJobRegistryService;
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Async("demoTaskExecutor")
    public void buildCatalogReport(String taskId) {
        AsyncJobState state = asyncJobRegistryService.getJobState(taskId);
        state.markRunning();
        try {
            long games = gameRepository.count();
            long developers = developerRepository.count();
            long publishers = publisherRepository.count();
            long categories = categoryRepository.count();
            long users = userRepository.count();

            String result = String.format(
                    "Catalog report completed: games=%d, developers=%d, publishers=%d, "
                            + "categories=%d, users=%d",
                    games, developers, publishers, categories, users
            );
            state.markCompleted(result);
        } catch (RuntimeException exception) {
            state.markFailed(exception.getMessage());
            throw exception;
        }
        CompletableFuture.completedFuture(null);
    }
}
