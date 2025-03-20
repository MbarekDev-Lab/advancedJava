package quiz;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WakeUpAllThreads {

    class SomeClass1 {
        boolean isCompleted = false;

        public synchronized void declareSuccess() throws InterruptedException {
            while (!isCompleted) {
                wait();
            }
            System.out.println("Success!!");
        }

        // Corrected: Use notifyAll() to wake up all waiting threads
        public synchronized void finishWork() {
            isCompleted = true;
            notifyAll(); // Ensure all waiting threads wake up
        }
    }

    class SomeClass2 {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        boolean isCompleted = false;

        public void declareSuccess() throws InterruptedException {
            lock.lock();
            try {
                while (!isCompleted) {
                    condition.await();
                }
                System.out.println("Success!!");
            } finally {
                lock.unlock();
            }
        }

        // Corrected: Use signalAll() to wake up all waiting threads
        public void finishWork() {
            lock.lock();
            try {
                isCompleted = true;
                condition.signalAll(); // Ensure all waiting threads wake up
            } finally {
                lock.unlock();
            }
        }
    }


   class SomeClass3 {
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        private boolean isCompleted = false;

        public void declareSuccess() {
            lock.lock();
            try {
                while (!isCompleted) {
                    condition.awaitUninterruptibly(); // Doesn't react to interrupts
                }
                System.out.println("Success!!");
            } finally {
                lock.unlock();
            }
        }

        public void finishWork() {
            lock.lock();
            try {
                isCompleted = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    class SomeClass4 {
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        private boolean isCompleted = false;

        public void declareSuccess() throws InterruptedException {
            lock.lock();
            try {
                Date deadline = new Date(System.currentTimeMillis() + 5000); // Wait for 5 seconds max
                while (!isCompleted) {
                    if (!condition.awaitUntil(deadline)) { // Returns false if time expires
                        System.out.println("Timeout! No success.");
                        return;
                    }
                }
                System.out.println("Success!!");
            } finally {
                lock.unlock();
            }
        }

        public void finishWork() {
            lock.lock();
            try {
                isCompleted = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }





}
