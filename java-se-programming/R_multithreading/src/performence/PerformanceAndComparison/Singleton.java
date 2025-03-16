package performence.PerformanceAndComparison;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

public class Singleton {

    public static void main(String[] args) {
        // Create Singleton instance with a grid size of 10
        Singleton singleton = Singleton.getInstance(10);

        // Update and read values atomically
        singleton.updateGridValue(2, 5); // Set index 2 to 5
        System.out.println("Grid value at index 2: " + singleton.getGridValue(2));

        // Increment value at index 2
        singleton.incrementGridValue(2);
        System.out.println("After increment, grid value at index 2: " + singleton.getGridValue(2));

        // Add 10 to the value at index 2
        singleton.addToGridValue(2, 10);
        System.out.println("After adding 10, grid value at index 2: " + singleton.getGridValue(2));
    }

    // AtomicReference to hold the singleton instance
    private static final AtomicReference<Singleton> INSTANCE = new AtomicReference<>();

    // Example grid-like data (can be any shared resource)
    private final AtomicInteger[] grid;

    // Private constructor to prevent instantiation
    private Singleton(int gridSize) {
        // Initialize the grid with AtomicIntegers for thread safety
        this.grid = new AtomicInteger[gridSize];
        for (int i = 0; i < gridSize; i++) {
            grid[i] = new AtomicInteger(0); // Default value
        }
    }

    // Get the Singleton instance using AtomicReference
    public static Singleton getInstance(int gridSize) {
        // Attempt to get the current instance
        Singleton current = INSTANCE.get();

        // If instance is null, initialize it
        if (current == null) {
            Singleton newSingleton = new Singleton(gridSize);
            // Atomically set the instance if it's still null
            if (INSTANCE.compareAndSet(null, newSingleton)) {
                current = newSingleton;
            } else {
                // Another thread initialized the instance
                current = INSTANCE.get();
            }
        }

        return current;
    }

    // Example method to update a grid value atomically
    public void updateGridValue(int index, int newValue) {
        if (index >= 0 && index < grid.length) {
            grid[index].set(newValue); // Atomic set
        }
    }

    // Example method to retrieve a grid value atomically
    public int getGridValue(int index) {
        if (index >= 0 && index < grid.length) {
            return grid[index].get(); // Atomic get
        }
        return -1; // Invalid index
    }

    // Example method to increment a grid value atomically
    public void incrementGridValue(int index) {
        if (index >= 0 && index < grid.length) {
            grid[index].incrementAndGet(); // Atomic increment
        }
    }

    // Example method to perform an atomic addition on the grid
    public void addToGridValue(int index, int delta) {
        if (index >= 0 && index < grid.length) {
            grid[index].addAndGet(delta); // Atomic add
        }
    }
}
