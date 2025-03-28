package concurrency;

import concurrency.helperClasses.BlockingTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VertualThreadsDemo {

    //private static final int NUMBER_OF_THREADS = 1000;
    private static final int NUMBER_OF_THREADS = 2;

    public static void main(String[] args) throws InterruptedException {
            Executor exexcutor = Executors.newVirtualThreadPerTaskExecutor();
        // Runnable runnable = () -> System.out.println("Insider thread: " + Thread.currentThread());
        //  runnable.r#un();
        // Thread platformThread = new Thread(runnable);
        // Thread platformThread = Thread.ofPlatform().unstarted(runnable);
        // Insider thread: Thread[#30,Thread-0,5,main]
        //  id - #30  , Thread-0,  Priority 5 (default of any thread ) , parent iof this thread is main thread.
        List<Thread> vertualTreadList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockingTask());
            vertualTreadList.add(virtualThread);
        }
        /*
        Insider thread: VirtualThread[#30]/runnable@ForkJoinPool-1-worker-1 befor call
        Insider thread: VirtualThread[#31]/runnable@ForkJoinPool-1-worker-2 befor call
        Insider thread: VirtualThread[#30]/runnable@ForkJoinPool-1-worker-2 after
        Insider thread: VirtualThread[#31]/runnable@ForkJoinPool-1-worker-1 after
         */
        for (Thread vt : vertualTreadList) {
            vt.start();
        }
        for (Thread vt : vertualTreadList) {
            vt.join();
        }
        /*
            Insider thread: VirtualThread[#30]/runnable@ForkJoinPool-1-worker-1
            Insider thread: VirtualThread[#31]/runnable@ForkJoinPool-1-worker-2
          the highest  Insider thread: VirtualThread[#1025]/runnable@ForkJoinPool-1-worker-16 (because i have 16 cors on my computere)
         */


    }
}
