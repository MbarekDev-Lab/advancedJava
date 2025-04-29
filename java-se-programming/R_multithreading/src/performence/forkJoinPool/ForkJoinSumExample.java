package performence.forkJoinPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Random;

public class ForkJoinSumExample {
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 1_000;
        private final int[] array;
        private final int start;
        private final int end;

        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if ((end - start) <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                SumTask left = new SumTask(array, start, mid);
                SumTask right = new SumTask(array, mid, end);

                left.fork();                        // compute left asynchronously
                long rightResult = right.compute(); // compute right synchronously
                long leftResult = left.join();       // wait for the left

                return leftResult + rightResult;
            }
        }
    }

    public static void main(String[] args) {
        int[] array = new int[10_000_000];
        Random rand = new Random();

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(100);
        }

        // Use ForkJoinPool with custom parallelism
        ForkJoinPool pool = new ForkJoinPool(4); // 4 threads

        // Submit the main task
        SumTask task = new SumTask(array, 0, array.length);
        long start = System.nanoTime();
        long result = pool.invoke(task);
        long end = System.nanoTime();

        System.out.println("Total sum: " + result);
        System.out.println("Time taken (ms): " + ((end - start) / 1_000_000));
    }
}