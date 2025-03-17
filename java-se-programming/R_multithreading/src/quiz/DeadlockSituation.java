package quiz;

public class DeadlockSituation {
    private long count;
    private double average;
    private long max;

    private final Object countLock = new Object();
    private final Object averageLock = new Object();
    private final Object maxLock = new Object();

    public void addSample(long sample) {
        synchronized (countLock) {
            synchronized (averageLock) {
                synchronized (maxLock) {
                    average = (average * count + sample) / (++count);
                    max = Math.max(max, sample);
                }
            }
        }
    }

    public void reset() {
        synchronized (maxLock) {
            synchronized (averageLock) {
                synchronized (countLock) {
                    count = 0;
                    max = Integer.MIN_VALUE;
                    average = 0.0;
                }
            }
        }
    }

    public long getCount() {
        synchronized (countLock) {
            return count;
        }
    }

    public long getMax() {
        synchronized (maxLock) {
            return max;
        }
    }

    public double getAverage() {
        synchronized (averageLock) {
            return average;
        }
    }
}
