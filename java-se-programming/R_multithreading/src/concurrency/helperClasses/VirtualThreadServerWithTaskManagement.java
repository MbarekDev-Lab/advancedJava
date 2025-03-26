package concurrency.helperClasses;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VirtualThreadServerWithTaskManagement {

    private final Executor executor;

    public VirtualThreadServerWithTaskManagement(int numberOfThreads) {
        // Create a new virtual thread per task executor
        executor = Executors.newFixedThreadPool(numberOfThreads);
        //executor = Executors.newVirtualThreadPerTaskExecutor();
       // executor = Executors.newCachedThreadPool();

    }

    public void startServer() {
        // Creating a chain of tasks using CompletableFuture
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> handleTask(1), executor);
        
        // Task 2 depends on Task 1
        CompletableFuture<Void> task2 = task1.thenRunAsync(() -> handleTask(2), executor);
        
        // Task 3 depends on Task 2
        CompletableFuture<Void> task3 = task2.thenRunAsync(() -> handleTask(3), executor);

        // Task 4 will run even if previous tasks fail
        CompletableFuture<Void> task4 = CompletableFuture.runAsync(() -> handleTaskWithExceptionHandling(4), executor);

        // Combine all tasks to ensure they all finish
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3, task4);

        // Wait for all tasks to complete
        allTasks.join();

        System.out.println("All tasks completed.");
    }

    private void handleTask(int taskId) {
        // Simulate task processing
        System.out.println("Handling Task " + taskId + " on " + Thread.currentThread());
        try {
            Thread.sleep(1000); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed on " + Thread.currentThread());
    }

    private void handleTaskWithExceptionHandling(int taskId) {
        // Simulate a task that may throw an exception
        System.out.println("Handling Task " + taskId + " on " + Thread.currentThread());
        try {
            if (taskId == 4) {
                throw new RuntimeException("Task 4 failed!");
            }
            Thread.sleep(1000); // Simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            System.out.println("Error in Task " + taskId + ": " + e.getMessage());
        }
        System.out.println("Task " + taskId + " completed or failed on " + Thread.currentThread());
    }

    public void shutdownServer() {
        System.out.println("Server shutting down...");
    }

    public static void main(String[] args) {
        VirtualThreadServerWithTaskManagement server = new VirtualThreadServerWithTaskManagement(5);
        server.startServer();
        server.shutdownServer();
    }
}
