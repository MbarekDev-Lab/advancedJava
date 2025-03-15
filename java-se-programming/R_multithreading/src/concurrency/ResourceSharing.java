package concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceSharing {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();

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

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");



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
