package quiz;
import java.util.concurrent.atomic.AtomicLong;

public class RaceConditionIssue {
  // Question 1:
    //Multiple threads have an instance of the Metric class.
    public class Metric {
        private long count;
        private long sum;
        public void addSample(int sample) {
            sum += sample;
            count++;
        }
        public double getAverage() {
            double average = (double)sum/count;
            reset();
            return average;
        }
        private void reset() {
            count = 0;
            sum = 0;
        }
    }
    //The threads use this instance to measure the execution time of a particular critical piece of code.
          //  Example:
//    private Metric metric;
//    public void criticalMethod() {
//        long start = System.currentTimeMillis();
//        //someVeryImportantOperation();
//        long end =  System.currentTimeMillis();;
//        metric.addSample(end - start);
//    }
    //Another thread called Emitter that also has a reference to that instance of the class,
   // calls the getAverage(); method once a minute. That thread sends the average of the execution of criticalMethod(), for the last minute, to an external server.
   // Question :
    //  Is the class thread safe?

//    No, the Metric class is not thread-safe.
//    Multiple threads can modify sum and count at the same time, leading to race
//    conditions and inconsistent data.
    //Race Condition on sum and count Race Condition in getAverage()
public class MetricStanderdFixed {
    private long count;
    private long sum;

    public synchronized void addSample(int sample) {
        sum += sample;
        count++;
    }

    public synchronized double getAverage() {
        if (count == 0) return 0; // Prevent division by zero
        double average = (double) sum / count;
        reset();
        return average;
    }

    private void reset() {
        count = 0;
        sum = 0;
    }
}



    public class MetricLockFreeApproach {
        private AtomicLong count = new AtomicLong(0);
        private AtomicLong sum = new AtomicLong(0);

        public void addSample(int sample) {
            sum.addAndGet(sample);
            count.incrementAndGet();
        }

        public double getAverage() {
            long currentSum = sum.getAndSet(0); // Atomically reset sum
            long currentCount = count.getAndSet(0); // Atomically reset count
            return (currentCount == 0) ? 0 : (double) currentSum / currentCount;
        }
    }

    /*Question 2:

The previous code was modified in the attempt to make Metric a thread safe class.

We know that many threads are calling methods of this class concurrently, and we would like to make it lock-free if possible

        public class Metric {
            private AtomicLong count = new AtomicLong(0);
            private AtomicLong sum = new AtomicLong(0);

            public void addSample(long sample) {
                sum.addAndGet(sample);
                count.incrementAndGet();
            }

            public double getAverage() {
                double average = (double)sum.get()/count.get();
                reset();
                return average;
            }

            private void reset() {
                count.set(0);
                sum.set(0);
            }
        }
Is the class thread safe now?

No, the class is still not fully thread-safe due to a race condition in getAverage().
Since getAverage() does not guarantee atomicity between reading and resetting, another thread could call addSample() in between those operations.
public double getAverage() {
        long currentSum = sum.getAndSet(0);  // Atomically reset sum
        long currentCount = count.getAndSet(0); // Atomically reset count
        return (currentCount == 0) ? 0 : (double) currentSum / currentCount;
    }
*/








}
