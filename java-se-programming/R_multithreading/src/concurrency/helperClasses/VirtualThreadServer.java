package concurrency.helperClasses;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VirtualThreadServer {

    private final Executor executor;

    public VirtualThreadServer(int numberOfThreads) {
        // Create a new virtual thread per task executor
        executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void startServer() {
        // Simulate receiving 5 tasks to handle
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.execute(() -> handleTask(taskId));
        }
    }

    private void handleTask(int taskId) {
        // Simulate processing a task
        System.out.println("Handling Task " + taskId + " on " + Thread.currentThread());
        try {
            // Simulate some work (e.g., database query, file read, etc.)
            Thread.sleep(1000); // 1 second task
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed on " + Thread.currentThread());
    }

    public void shutdownServer() {
        try {
            // Wait for a short time to let all tasks complete
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Server shutting down...");
        // This would normally clean up resources and shutdown the executor
    }

    public static void main(String[] args) {
        VirtualThreadServer server = new VirtualThreadServer(5); // Example with 5 threads
        server.startServer();
        server.shutdownServer();
    }
}
