package quiz;
import java.util.concurrent.atomic.AtomicLong;
public class MinMaxMetrics {
    // Add all necessary member variables
    private final AtomicLong min;
    private final AtomicLong max;
    private volatile boolean hasSample;


    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        // Add code here
        min = new AtomicLong(Long.MAX_VALUE);
        max = new AtomicLong(Long.MIN_VALUE);
        hasSample = false;

    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        // Add code here
        min.updateAndGet(l -> Math.min(l, newSample));
        max.updateAndGet(l -> Math.max(l, newSample));
        hasSample = true;

    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        // Add code here
        return hasSample ? min.get(): 0;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        // Add code here
        return hasSample ? max.get():0;
    }
}
