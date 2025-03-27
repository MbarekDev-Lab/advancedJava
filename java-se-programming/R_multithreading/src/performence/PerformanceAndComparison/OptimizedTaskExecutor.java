package performence.PerformanceAndComparison;

import java.util.concurrent.*;
/*
if task 1 produces a result that task 2 needs
CompletableFuture.supplyAsync(() -> {
        return "Task 1 result";
        }, executor)
        .thenApplyAsync(result -> {
        System.out.println("Received result from Task 1: " + result);
    return "Task 2 result";
            }, executor);*/

public class OptimizedTaskExecutor {
    private final ExecutorService executor;
    // Constructor with adjustable thread pool size for maximum CPU utilization
    public OptimizedTaskExecutor(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    // Method to submit tasks for execution in order
    public CompletableFuture<Void> submitTasks(Runnable task1, Runnable task2, Runnable task3, Runnable task4) {
        return CompletableFuture.runAsync(task1, executor)   // Run task 1
                .thenRunAsync(task2, executor)              // Task 2 after task 1 finishes
                .thenRunAsync(task3, executor)              // Task 3 after task 2 finishes
                .thenRunAsync(task4, executor);             // Task 4 after task 3 finishes
    }

    // Graceful shutdown of the ExecutorService
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize with the number of threads equal to the CPU cores
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        OptimizedTaskExecutor executor = new OptimizedTaskExecutor(availableProcessors);

        // Define the tasks
        Runnable task1 = () -> {
            System.out.println("Task 1 started.");
            try {
                Thread.sleep(1000); // Simulate task 1 work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 1 completed.");
        };

        Runnable task2 = () -> {
            System.out.println("Task 2 started.");
            try {
                Thread.sleep(1500); // Simulate task 2 work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 2 completed.");
        };

        Runnable task3 = () -> {
            System.out.println("Task 3 started.");
            try {
                Thread.sleep(1000); // Simulate task 3 work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 3 completed.");
        };

        Runnable task4 = () -> {
            System.out.println("Task 4 started.");
            try {
                Thread.sleep(1200); // Simulate task 4 work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 4 completed.");
        };

        // Submit tasks to the executor for execution
        executor.submitTasks(task1, task2, task3, task4);

        // Wait for enough time to ensure tasks complete
        Thread.sleep(5000); // Adjust this based on total task execution time
        executor.shutdown();
    }
}
