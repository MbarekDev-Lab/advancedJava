package performence.PerformanceAndComparison;

import concurrency.AtomicOperations;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class CompareAndSetLockFreeHighPerformance {
    public static void main(String[] args) {
        String oldName = "old name";
        String newName = "new Name";
        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);
        atomicReference.set("old name");
        if (atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("New Value is " + atomicReference.get());
        } else {
            System.out.println("Nothing changhed");
        }
    }







}
