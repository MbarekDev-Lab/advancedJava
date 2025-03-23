package performence.PerformanceAndComparison;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class LockFreeStackImp {
    public static void main(String[] args) throws InterruptedException {
        /*String oldName = "old name";
        String newName = "new Name";
        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);
        atomicReference.set("old name");
        if (atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("New Value is " + atomicReference.get());
        } else {
            System.out.println("Nothing changhed");
        }*/
        //StandardStack<Integer> standardStack = new StandardStack(); // 376,506,348 operations were performed in 10 seconds
        LockFreeStack<Integer> standardStack = new LockFreeStack(); // 674,652,048 operations were performed in 10 seconds

        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            standardStack.push(random.nextInt());
        }
        List<Thread> threads = new ArrayList<>();
        int pushingThreads = 2;
        int poppingThreads = 2;

        for (int i = 0; i < pushingThreads; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        standardStack.push(random.nextInt());
                    }
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (int i = 0; i < poppingThreads; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        standardStack.pop();
                    }
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        Thread.sleep(10000);
        System.out.println(String.format("%,d operations were performed in 10 seconds ", standardStack.getCounter()));
    }

    private static class LockFreeStack<T> {
        private final AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private final AtomicInteger counter = new AtomicInteger(0);

        public void push(T value) {
            StackNode<T> newValue = new StackNode<>(value);
            while (true) {
                StackNode<T> currentHeadNode = head.get();
                newValue.next = currentHeadNode;
                if (head.compareAndSet(currentHeadNode, newValue)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
            counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentHeadNode = head.get();
            StackNode<T> newHeadNode;
            while (currentHeadNode != null) {
                newHeadNode = currentHeadNode.next;
                if (head.compareAndSet(currentHeadNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    // that mean other thread is reading the currentHeadNode we shoulould try again !
                    currentHeadNode = head.get();
                }
            }
            counter.incrementAndGet();
            return currentHeadNode != null ? currentHeadNode.value : null;
        }


        public int getCounter() {
            return counter.get();
        }
    }


    public static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public synchronized void push(T value) {
            StackNode<T> newHead = new StackNode<>(value);
            newHead.next = head;
            head = newHead;
            counter++;
        }

        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }

            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }

        public int getCounter() {
            return counter;
        }
    }


    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
            this.next = next;
        }
    }


}
