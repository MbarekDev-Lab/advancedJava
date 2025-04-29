package performence;
public class FalseSharingExample {
/*
False sharing in Java occurs when two threads running on two different CPUs
 write to two different variables which happen to be stored within the same CPU cache line.
 When the first thread modifies one of the variables - the whole CPU cache line is invalidated in the CPU caches
 of the other CPU where the other thread is
 running. This means, that the other CPUs need to reload the content of the invalidated cache line -
 even if they don't really need the variable that was modified within that cache line.
 This false sharing tutorial explains how false sharing can occur in Java code, as we well as what you
 can do to avoid false sharing in your Java apps - e.g. by using the @Contended Java annotation.
 ---------------------------------------------------------------------------------------------------------
 How False Sharing Occurs
 In Java, objects and fields are stored in memory in a way that can lead to two independent variables
 sharing the same 64-byte cache line (common cache line size in modern CPUs).
 If two threads running on different CPU cores modify separate variables within the same cache line,
 they inadvertently cause cache coherence traffic,
 making the CPU cores synchronize unnecessarily.
 */
    public static void main(String[] args) {
        SharedResource counter1 = new SharedResource();
        SharedResource counter2 = counter1;
        long iterations = 1_000_000_000;

        Thread thread1 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            for(long i=0; i<iterations; i++) {
                counter1.count1++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("total time: " + (endTime - startTime));
        });
        Thread thread2 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            for(long i=0; i<iterations; i++) {
                counter2.count2++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("total time: " + (endTime - startTime));
        });
        thread1.start();
        thread2.start();
    }
}