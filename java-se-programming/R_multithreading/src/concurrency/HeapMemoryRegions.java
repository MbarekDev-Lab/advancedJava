package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeapMemoryRegions {
    private final Map<Integer, String> idToNameMap; // Instance variable, allocated on the heap
    private static long numberOfInstances = 0; // Static variable, shared among all instances

    public HeapMemoryRegions() {
        this.idToNameMap = new ConcurrentHashMap<>(); // ConcurrentHashMap for thread safety
        numberOfInstances++;
    }

    public List<String> getAllNames() {
        int count = idToNameMap.size(); // Local variable, allocated on the stack
        // Local variable, reference allocated on the stack, object on the heap
        List<String> allNames = new ArrayList<>(idToNameMap.values());
        return allNames;
    }

    public static void main(String[] args) {
        HeapMemoryRegions example = new HeapMemoryRegions(); // Local variable, reference allocated on the stack, object on the heap

        int count = example.idToNameMap.size(); // Local variable, allocated on the stack
        List<String> allNames = new ArrayList<>(example.idToNameMap.values()); // Local variable, reference allocated on the stack, object on the heap

        System.out.println("Number of instances created: " + numberOfInstances);
        System.out.println("Number of names: " + count);
    }
}