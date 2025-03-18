package quiz;

import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnectionClass {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Integer, Integer> database = new HashMap<>(); // Simulating a database

    public void update(int key, int value) {
        lock.writeLock().lock();
        try {
            writeToDatabase(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int read(int key) {
        lock.readLock().lock();
        try {
            return readFromDatabase(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Simulated slow database read
    private int readFromDatabase(int key) {
        try {
            Thread.sleep(100); // Simulating delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return database.getOrDefault(key, -1); // Default value if key not found
    }

    // Simulated slow database write
    private void writeToDatabase(int key, int value) {
        try {
            Thread.sleep(100); // Simulating delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        database.put(key, value);
    }
}
