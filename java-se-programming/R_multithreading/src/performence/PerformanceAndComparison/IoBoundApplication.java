package performence.PerformanceAndComparison;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IoBoundApplication {
    private static final int NUMBER_OF_TASKS = 1000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press enter to start");
        scanner.nextLine();
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);
        long start = System.currentTimeMillis();
        performTasks100();
        //performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis() - start);
    }

    private static void performTasks() {
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {

            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(IoBoundApplication::blockingIoOperation);
            }
        }
    }


    private static void performTasks100() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(1000)) {
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < 100; j++) {
                            blockingIoOperation();
                        }
                    }
                });
            }
        }
    }

    // Simulates a long blocking IO
    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: " + Thread.currentThread());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
