package performence.PerformanceAndComparison;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadsPerformanceTest {
    private static final int TASKS = 10_000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < TASKS; i++) {
                int finalI = i;
                executor.submit(() -> pauseThread("Task " + finalI, 1000));
            }
        }

        System.out.println("Time taken: " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void pauseThread(String name, int duration) {
        try {
            Thread.sleep(duration); // Simulates blocking operation
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
