package quiz;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SharedClass {
    private volatile long counter;
    /*
    Simple read/write operations on most primitive types (int, long, etc.) are atomic, but compound operations (++, --, +=) are not.
    to safely perform atomic operations for (++, --, +=) without locks. use atomic classes (AtomicInteger, AtomicLong, etc.)
     */

    public void incrementCounter() {
        /*
            This operation is not atomic because counter++ consists of three steps:
            Read the value of counter
            Increment the value
            Write the updated value back to counter
         */
        counter++;   // NOT atomic
    }

    public long getCounter() {
        /*
            declaring counter as volatile ensures atomic reads and prevents tearing (reading half of the 64-bit value).
         */
        return counter;  // Atomic read (due to volatile)
    }

    // Fix the Race Condition in incrementCounter()
    private final AtomicLong fixedCounter = new AtomicLong(0);

    public void fixIncrementCounter() {
        fixedCounter.incrementAndGet(); // Atomic and thread-safe
    }

    public long fixGetCounter() {
        return fixedCounter.get();  // Atomic read
    }

    // Not Thread-Safe?
    //  private String name;

    //to Make It Thread-Safe using volatile or synchronized or Use AtomicReference (Best for Performance)
    private final AtomicReference<String> name = new AtomicReference<>();

    public void updateString(String name) {
        this.name.set(name);  // ✅ Atomic and thread-safe
    }

    public String getName() {
        return this.name.get();  // ✅ Atomic and thread-safe
    }

}





