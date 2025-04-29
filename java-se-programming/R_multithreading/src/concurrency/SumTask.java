package concurrency;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class SumTask extends RecursiveTask<Long> {
    private final long start, end;
    private static final long THRESHOLD = 10_000;

    public SumTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if ((end - start) <= THRESHOLD) {
            long sum = 0;
            for (long i = start; i <= end; i++) sum += i;
            return sum;
        } else {
            long mid = (start + end) / 2;
            SumTask left = new SumTask(start, mid);
            SumTask right = new SumTask(mid + 1, end);
            left.fork();  // fork left part
            long rightResult = right.compute(); // compute right directly
            long leftResult = left.join(); // wait for left to finish
            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();  // Default: available cores
        SumTask task = new SumTask(1, 1_000_000);
        long result = pool.invoke(task);
        //long result1 = pool.submit(task);

        System.out.println("Total sum: " + result);




    }
}