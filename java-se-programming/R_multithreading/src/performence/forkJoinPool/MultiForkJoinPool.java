package performence.forkJoinPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class MultiForkJoinPool {
    public static void main(String[] args) {
        //  Create an array of three ForkJoinPools (no generic wildcard)
        ForkJoinPool[] pools = new ForkJoinPool[3];

        // Create each pool with different parallelism levels (thread counts)
        pools[0] = new ForkJoinPool(2); // Pool 0: 2 threads
        pools[1] = new ForkJoinPool(4); // Pool 1: 4 threads
        pools[2] = new ForkJoinPool(1); // Pool 2: 1 thread

        // Example tasks (dummy tasks for demonstration)
        RecursiveTask<Void> task1 = new RecursiveTask<>() {
            @Override
            protected Void compute() {
                System.out.println("Task 1 running in pool 0: " + Thread.currentThread().getName());
                sleep();
                return null;
            }
        };

        RecursiveTask<Void> task2 = new RecursiveTask<>() {
            @Override
            protected Void compute() {
                System.out.println("Task 2 running in pool 1: " + Thread.currentThread().getName());
                sleep();
                return null;
            }
        };

        RecursiveTask<Void> task3 = new RecursiveTask<>() {
            @Override
            protected Void compute() {
                System.out.println("Task 3 running in pool 2: " + Thread.currentThread().getName());
                sleep();
                return null;
            }
        };

        // Execute tasks in different pools
        pools[0].execute(task1);
        pools[1].execute(task2);
        pools[2].execute(task3);

        // Shutdown and await termination
        for (ForkJoinPool pool : pools) {
            pool.shutdown();
        }

        for (ForkJoinPool pool : pools) {
            try {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
