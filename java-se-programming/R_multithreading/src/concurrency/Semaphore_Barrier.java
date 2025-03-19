package concurrency;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore_Barrier {

    public static void main(String[] args) {
        int numberOfThreads = 200; // or any number you'd like

        ArrayList<Thread> threads = new ArrayList<>();
        Barrier barrier = new Barrier(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static class Barrier {
        private final int numberOfWorkers;
        private final Semaphore semaphore = new Semaphore(0); // Initially 0, will block all threads
        private int counter = 0; // Keeps track of how many threads have arrived
        private final Lock lock = new ReentrantLock(); // Lock to protect the counter

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void waitForOthers() throws InterruptedException {
            lock.lock(); // Acquire the lock to ensure thread-safe updates to the counter
            boolean isLastWorker = false; // Flag to check if this thread is the last one
            try {
                counter++; // Increment the counter each time a thread reaches this point

                if (counter == numberOfWorkers) {
                    isLastWorker = true; // If this is the last thread, it will release the others
                    counter = 0; // Reset the counter for the next round of barrier usage
                }
            } finally {
                lock.unlock(); // Release the lock, so other threads can increment the counter
            }

            if (isLastWorker) {
                semaphore.release(numberOfWorkers); // Release all waiting threads
            }
            semaphore.acquire(); // All threads must acquire the semaphore before proceeding
        }
    }

    static class CoordinatedWorkRunner implements Runnable {
        private final Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                task();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interruption properly
            }
        }

        private void task() throws InterruptedException {
            // Performing Part 1
            System.out.println(Thread.currentThread().getName() + " part 1 of the work is finished");

            // Wait for other threads to complete Part 1
            barrier.waitForOthers();

            // Performing Part 2
            System.out.println(Thread.currentThread().getName() + " part 2 of the work is finished");
        }
    }
}