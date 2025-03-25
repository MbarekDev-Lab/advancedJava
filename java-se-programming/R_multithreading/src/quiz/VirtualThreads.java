package quiz;

import java.util.concurrent.*;
/**
 * The {@code VirtualThreads} class demonstrates the use of different types of threads in Java, including
 * platform threads and virtual threads. The class also showcases the concept of daemon threads and how they
 * behave when managed in a multi-threaded environment.
 * <p>
 * It creates several threads of different types and demonstrates how they are started, how they behave
 * (such as sleeping for a specified time), and how daemon threads are handled. The class also highlights
 * that virtual threads do not accept daemon status.
 * </p>
 */
public class VirtualThreads {
    /**
     * The main method demonstrates the creation and starting of multiple threads, including platform threads,
     * virtual threads, and custom threads. It also sets the daemon status for each thread and starts their execution.
     * <p>
     * The program starts 4 threads: one platform thread, one virtual thread, one custom thread, and a standard thread.
     * </p>
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        // Create a standard thread
        Thread thread1 = new Thread(() -> pauseThread("Thread1", 1000));
        // Create a platform thread
        Thread thread2 = Thread.ofPlatform().unstarted(() -> pauseThread("Thread2", 2000));

        // Create a virtual thread (note: virtual threads do not support daemon status)
        Thread thread3 = Thread.ofVirtual().unstarted(() -> pauseThread("Thread3", 3000));

        // Create a custom thread
        Thread thread4 = new CustomThread("Thread4", 4000);

        // Set the daemon status for the threads
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        //thread3.setDaemon(false);
        //         Uncommenting thread3 will crash the program since virtual threads cannot be daemon
        //         Exception in thread "main" java.lang.IllegalArgumentException: 'false' not legal for virtual threads
        thread4.setDaemon(false);

        // Start all the threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    /**
     * Simulates a thread pausing (sleeping) for a specified amount of time.
     * This method outputs the start and finish status of the thread and handles interruptions.
     * <p>
     * The thread sleeps for the given duration and handles an {@link InterruptedException}
     * if it is interrupted during sleep.
     * </p>
     *
     * @param threadName The name of the thread that is pausing.
     * @param sleepTime  The duration (in milliseconds) for which the thread will sleep.
     */
    private static void pauseThread(String threadName, int sleepTime) {
        System.out.println(threadName + " started");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println(threadName + " was interrupted");
        }
        System.out.println(threadName + " finished");
    }
}

/**
 * The {@code CustomThread} class is a custom thread implementation that extends {@link Thread}.
 * It overrides the {@link Thread#run()} method to perform a custom task of pausing the thread for a specified time.
 * <p>
 * This class is used to demonstrate creating a custom thread that behaves similarly to standard threads but with
 * added customization.
 * </p>
 */
class CustomThread extends Thread {
    /**
     * The duration (in milliseconds) for which this custom thread will sleep.
     */
    private final int sleepTime;

    /**
     * Constructs a new {@code CustomThread} with a specified name and sleep time.
     *
     * @param name      The name of the thread.
     * @param sleepTime The duration (in milliseconds) for which the thread will sleep.
     */
    public CustomThread(String name, int sleepTime) {
        super(name);
        this.sleepTime = sleepTime;
    }

    /**
     * The {@code run()} method is overridden to simulate a thread sleeping for a specified time.
     * <p>
     * The thread sleeps for the duration provided in the constructor, and handles interruptions gracefully.
     * </p>
     */
    @Override
    public void run() {
        System.out.println(getName() + " started");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted");
        }
        System.out.println(getName() + " finished");
    }
}
