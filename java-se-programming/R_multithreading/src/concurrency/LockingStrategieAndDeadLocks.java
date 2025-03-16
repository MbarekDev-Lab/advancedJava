package concurrency;

import java.util.Random;

public class LockingStrategieAndDeadLocks {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread trainAThread = new Thread(new TrainA(intersection));
        Thread trainBThread = new Thread(new TrainB(intersection));

        trainAThread.start();
        trainBThread.start();
    }

    public static class TrainB implements Runnable {
        private final Intersection intersection;
        private final Random random = new Random();

        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }

                intersection.takeRoadB();
            }
        }
    }

    public static class TrainA implements Runnable {
        private final Intersection intersection;
        private final Random random = new Random();

        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                }

                intersection.takeRoadA();
            }
        }
    }

    public static class Intersection {
        private final Object roadA = new Object();
        private final Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void takeRoadB() {
            synchronized (roadB) {
                System.out.println("Road B is locked by thread " + Thread.currentThread().getName());

                synchronized (roadA) {
                    System.out.println("Train is passing through road B");

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }


    /*
    Coarse-grained locking refers to using a single lock to protect a large section of code or a large data structure.
    This ensures safety but can reduce parallelism,
    as multiple threads are often forced to wait.
     */
    public class CoarseGrainedLocking {
        private final Object lock = new Object();
        private int a;
        private int b;

        public void updateBoth() {
            synchronized (lock) {
                a++;
                b++;
            }
        }

        public int getSum() {
            synchronized (lock) {
                return a + b;
            }
        }
    }


    /*
    Fine-grained locking means using multiple locks to protect smaller sections of code or different parts of a data structure,
     allowing more parallelism.
     */

    public class FineGrainedLocking {
        private final Object lockA = new Object();
        private final Object lockB = new Object();
        private int a;
        private int b;

        public void updateA() {
            synchronized (lockA) {
                a++;
            }
        }

        public void updateB() {
            synchronized (lockB) {
                b++;
            }
        }

        public int getSum() {
            synchronized (lockA) {
                synchronized (lockB) {
                    return a + b;
                }
            }
        }
    }


}
