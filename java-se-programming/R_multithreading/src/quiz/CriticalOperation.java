package quiz;
import java.util.concurrent.locks.ReentrantLock;
/**
 * The {@code CriticalOperation} class implements the {@code Runnable} interface
 * and provides a mechanism for performing a critical operation in a thread-safe manner
 * using a {@link ReentrantLock}.
 * <p>
 * The class uses {@link ReentrantLock#tryLock()} to attempt to acquire a lock
 * on the shared resource. If the lock is successfully acquired, the critical operation
 * is executed. Otherwise, if the lock is not available, the operation is skipped,
 * and an appropriate message is logged. This helps in avoiding blocking the thread
 * indefinitely.
 * </p>
 */
public class CriticalOperation implements Runnable {
    /**
     * A {@link ReentrantLock} that is used to control access to the critical section.
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * The entry point of the {@code CriticalOperation} class. This method is used to start
     * the execution of the operation by submitting it to a thread, which will then call
     * the {@link #run()} method.
     *
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        // The main method can be used to initialize and start the operation.
        // For example, you can create a new thread here and start it:
        CriticalOperation operation = new CriticalOperation();
        Thread thread = new Thread(operation);
        thread.start();
    }
    /**
     * The {@code run()} method contains the logic for attempting to acquire the lock
     * and executing the critical operation if the lock is obtained. If the lock is not
     * acquired, a message is logged indicating that the operation is being skipped.
     * <p>
     * This method ensures that the lock is released only if it was successfully acquired
     * by calling {@link ReentrantLock#unlock()} in the {@code finally} block.
     * </p>
     */
    @Override
    public void run() {
        // Attempt to acquire the lock.
        if (lock.tryLock()) { // Check if the lock was acquired
            try {
                // Execute the critical operation
                someOperation();
            } finally {
                // Always release the lock in the finally block to ensure it is released even if an exception occurs
                lock.unlock(); // Ensure the lock is released only if it was acquired
            }
        } else {
            // Lock was not acquired; handle it appropriately (e.g., retry, log, etc.)
            System.out.println("Could not acquire lock, skipping operation.");
        }
    }
    /**
     * Simulates a critical operation that may throw a {@link RuntimeException}.
     * <p>
     * This method represents the business logic of the application that needs to be executed
     * within a synchronized (locked) context.
     * </p>
     *
     * @throws RuntimeException if an error occurs during the operation.
     */
    private void someOperation() {
        // Simulate some critical operation that may throw an exception
        System.out.println("Performing some operation.");
        // For example, a runtime exception can be thrown
        // throw new RuntimeException("An error occurred during operation.");
    }
}
