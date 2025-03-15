package concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceSharing {

    public static void main(String[] args) throws InterruptedException {
        /*InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);
        IncrementingThread incrementingThread2 = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread2 = new DecrementingThread(inventoryCounter);
        incrementingThread.start();
        decrementingThread.start();
        incrementingThread.join();
        decrementingThread.join();
        incrementingThread2.start();
        decrementingThread2.start();
        incrementingThread2.join();
        decrementingThread2.join();
        System.out.println("We currently have " + inventoryCounter.getItems() + " items");*/

        SharedClass sharedObject = new SharedClass();

        Thread thread1 = new Thread(() -> {
            while (true) {
                sharedObject.incrementCounter1();
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                sharedObject.incrementCounter2();
            }
        });

        thread1.start();
        thread2.start();

        // Delay to allow the threads to modify the counters
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("getcounter1: " + sharedObject.getcounter1() + " counter1");
        System.out.println("getcounter2: " + sharedObject.getcounter2() + " counter2");
    }

    static class SharedClass {
        private int counter1 = 0;
        private int counter2 = 0;

        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        public void incrementCounter1() {
            synchronized (lock1) {
                counter1++;
            }
        }

        public void incrementCounter2() {
            synchronized (lock2) {
                counter2++;
            }
        }

        public int getcounter1() {
            synchronized (lock1) { // Ensure thread safety
                return counter1;
            }
        }

        public int getcounter2() {
            synchronized (lock2) { // Ensure thread safety
                return counter2;
            }
        }
    }


    public static class DecrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    public static class IncrementingThread extends Thread {
        private final InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }


    private static class InventoryCounter {
        private int items = 0;
        //private final Lock lock = new ReentrantLock();
        private static final Lock lock = new ReentrantLock(); // Static Lock for Class-Level Locking

        public void increment() {
            lock.lock();
            try {
                items++;
            } finally {
                lock.unlock();
            }
        }

        public void decrement() {
            lock.lock();
            try {
                items--;
            } finally {
                lock.unlock();
            }
        }

        public int getItems() {
            return items;
        }
    }




/*
Option 1: Synchronizing Methods (synchronized ensures only one thread can execute increment() or decrement() at a time.)

private static class InventoryCounter {
    private int items = 0;
       //  synchronized (InventoryCounter.class) { // Class-level lock
    public synchronized void increment() {
        items++;
    }

    public synchronized void decrement() {
        items--;
    }

    public synchronized int getItems() {
        return items;
    }
}

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Option 2: Using ReentrantLock (More Control)
lock.lock() ensures exclusive access.
finally { lock.unlock(); } prevents deadlocks.

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

private static class InventoryCounter {
    private int items = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            items++;
        } finally {
            lock.unlock();
        }
    }

    public void decrement() {
        lock.lock();
        try {
            items--;
        } finally {
            lock.unlock();
        }
    }

    public int getItems() {
        return items;
    }
}


+++++++++++++++++++++++++++++++++++++
private static class InventoryCounter {
    private int items = 0;
    private final Object lock = new Object(); // Custom lock object
     //private static final Object lock = new Object(); // Shared lock across all instances

    public void increment() {
        synchronized (lock) { // Locking on the object
                                // Synchronizing on a static object
            items++;
        }
    }

    public void decrement() {
        synchronized (lock) { // Locking on the object
            items--;
        }
    }

    public int getItems() {
        synchronized (lock) { // Ensuring thread-safe read
            return items;
        }
    }
}

     */


}
