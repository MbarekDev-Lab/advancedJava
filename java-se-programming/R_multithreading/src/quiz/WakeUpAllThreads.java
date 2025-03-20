package quiz;

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
}
