package concurrency.helperClasses;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedThreadPoolExample {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(3); // Fixed number of threads
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.submitTask(() -> {
                System.out.println("Executing task " + taskId);
                try {
                    Thread.sleep(1000); // Simulate task work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Shutdown the pool after tasks are done
        pool.shutdown();
    }
}

class Worker extends Thread {
    private final BlockingQueue<Runnable> taskQueue;

    public Worker(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        try {
            while (true) {
                Runnable task = taskQueue.take(); // Take task from queue (blocks if empty)
                task.run(); // Execute the task
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class ThreadPool {
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final Worker[] workers;

    public ThreadPool(int numThreads) {
        workers = new Worker[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = new Worker(taskQueue);
            workers[i].start();
        }
    }

    public void submitTask(Runnable task) {
        taskQueue.offer(task);
    }

    public void shutdown() {
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }
}
