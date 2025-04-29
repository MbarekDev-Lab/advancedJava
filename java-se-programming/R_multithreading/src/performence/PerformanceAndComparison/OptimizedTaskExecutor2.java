package performence.PerformanceAndComparison;

import java.util.concurrent.*;

public class OptimizedTaskExecutor2 {
    private final ExecutorService executor;

    public OptimizedTaskExecutor2(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    public CompletableFuture<Object[][]> submitTasks() {
        return CompletableFuture.supplyAsync(() -> {
                    System.out.println("Task 1 started.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    System.out.println("Task 1 completed.");
                    return new Object[]{"Task 1", "Result from Task 1"};
                }, executor)
                .thenApplyAsync(result1 -> {
                    System.out.println("Task 2 started with: " + result1[1]);
                    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    System.out.println("Task 2 completed.");
                    return new Object[]{result1, new Object[]{"Task 2", "Result from Task 2"}};
                }, executor)
                .thenApplyAsync(result2 -> {
                    System.out.println("Task 3 started with: " + ((Object[]) result2[1])[1]);
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    System.out.println("Task 3 completed.");
                    return new Object[]{result2, new Object[]{"Task 3", "Result from Task 3"}};
                }, executor)
                .thenApplyAsync(result3 -> {
                    System.out.println("Task 4 started with: " + ((Object[]) result3[1])[1]);
                    try { Thread.sleep(1200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    System.out.println("Task 4 completed.");
                    return new Object[][]{
                            (Object[]) result3[0],  // Task 1 and 2 results
                            (Object[]) result3[1],  // Task 3 result
                            new Object[]{"Task 4", "Final Result from Task 4"}
                    };
                }, executor);
    }

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

    public static void main(String[] args) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        OptimizedTaskExecutor2 executor = new OptimizedTaskExecutor2(availableProcessors);

        CompletableFuture<Object[][]> resultFuture = executor.submitTasks();

        resultFuture.thenAccept(finalResult -> {
            System.out.println("Final Output:");
            for (Object[] row : finalResult) {
                System.out.println(row[0] + " -> " + row[1]);
            }
            executor.shutdown();
        });

        resultFuture.join(); // Block main thread until completion
    }
}