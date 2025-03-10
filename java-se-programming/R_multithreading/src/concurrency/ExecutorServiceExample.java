package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        //  Java ExecutorService
        //  Java ExecutorService

        if (true) {
            System.out.println("active\n");
            System.out.println("java executor service \n");

            ExecutorService executorServicePool = Executors.newFixedThreadPool(1);

            List<Callable<String>> callables = new ArrayList<>();
            callables.add(newCallable("Task 1.1 "));
            callables.add(newCallable("Task 1.2 "));
            callables.add(newCallable("Task 1.3 "));

            try {
                List<Future<String>> results = executorServicePool.invokeAll(callables);
                for (Future<String> future : results) {
                    try {
                        System.out.println("Result: " + future.get());
                    } catch (ExecutionException e) {
                        System.err.println("Task execution failed: " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Tasks were interrupted");
            } finally {
                executorServicePool.shutdown();
            }



            try {
                //RejectedExecutionException ->   Make sure shutdown() is not called before invoking tasks
                String result = executorServicePool.invokeAny(callables);
                System.out.println("First completed task result: " + result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Task execution failed: " + e.getMessage());
            }
            // Shutdown only after tasks are executed
            executorServicePool.shutdown();



            try {
                String result = executorServicePool.invokeAny(callables);
                System.out.println("First completed task result: " + result);
                //  String result = (String) executorServicePool.invokeAll((Collection) callables);

            } catch (InterruptedException e) {
                System.out.println();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }


            Future future = executorServicePool.submit(newCallable("Task 1.1"));
            System.out.println(future.isDone());
            boolean mayInterrupt = true;
            future.cancel(mayInterrupt);

            try {
                String res = (String) future.get();
                System.out.println(res);
            } catch (ExecutionException | InterruptedException | CancellationException e) {
                System.out.println("cannot call the future.get -----");
            }
            System.out.println(future.isDone());
            System.out.println(future.isCancelled());

            executorServicePool.shutdown();

            List<Runnable> runnableList = executorServicePool.shutdownNow();
            try {
                executorServicePool.awaitTermination(3000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        } else {
            ExecutorService singleThreadExecuto = Executors.newSingleThreadExecutor();
            ExecutorService executorServicePool = Executors.newFixedThreadPool(1);

            Future future = executorServicePool.submit(newCallable("Task 1.1"));
            System.out.println(future.isDone()); //false

            try {
                // future.get(); // it will always return null.
                String result = (String) future.get();
                System.out.println(result); //pool-2-thread-1 : Task 1.1

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(future.isDone()); // true

            executorServicePool.shutdown();


            int corePoolSize = 10;
            int maxPoolSize = 20;
            long keepAliveTime = 3000;


            ExecutorService executorSexcrviceSingl = Executors.newSingleThreadExecutor();
            ExecutorService thresdPoolExec = new ThreadPoolExecutor(
                    corePoolSize
                    , maxPoolSize
                    , keepAliveTime
                    , TimeUnit.MILLISECONDS
                    , new ArrayBlockingQueue<>(128)
            );
            thresdPoolExec = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
            ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            executorService.execute(newRunnable("Task 1"));
            executorService.execute(newRunnable("Task 2"));
            executorService.execute(newRunnable("Task 3"));
            executorService.execute(newRunnable("Task 4"));
            executorService.shutdown();

        }

    }

    private static Runnable newRunnable(String msg) {
        return new Runnable() {
            @Override
            public void run() {
                String comMsg = Thread.currentThread().getName() + " : " + msg;
                System.out.println(comMsg);
            }
        };
    }

    private static Callable<String> newCallable(String msg) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName() + " : " + msg;
            }
        };
    }


}