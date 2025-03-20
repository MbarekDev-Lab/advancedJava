package quiz;

public class SimpleCountDownLatchTest {
    public static void main(String[] args) {
        SimpleCountDownLatch latch = new SimpleCountDownLatch(3);

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting...");
                latch.await();
                System.out.println(Thread.currentThread().getName() + " proceeding!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
        new Thread(task, "Thread-3").start();

        try {
            Thread.sleep(1000);
            System.out.println("Main thread counting down...");
            latch.countDown();

            Thread.sleep(1000);
            System.out.println("Main thread counting down...");
            latch.countDown();

            Thread.sleep(1000);
            System.out.println("Main thread counting down...");
            latch.countDown(); // All waiting threads will be released
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static class SimpleCountDownLatch {
        private int count;

        public SimpleCountDownLatch(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count cannot be negative");
            }
            this.count = count;
        }

        /**
         * Causes the current thread to wait until the latch has counted down to zero.
         * If the current count is already zero, this method returns immediately.
         */
        public synchronized void await() throws InterruptedException {
            while (count > 0) {
                wait(); // Wait until count reaches zero
            }
        }

        /**
         * Decrements the count of the latch, releasing all waiting threads when the count reaches zero.
         * If the current count already equals zero, then nothing happens.
         */
        public synchronized void countDown() {
            if (count > 0) {
                count--; // Decrease count
                if (count == 0) {
                    notifyAll(); // Wake up all waiting threads
                }
            }
        }

        /**
         * Returns the current count.
         */
        public synchronized int getCount() {
            return count;
        }
    }
}
