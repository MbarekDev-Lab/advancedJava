package concurrency;

import java.math.BigInteger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadTermination {

    public static void main(String[] args) throws InterruptedException {
        if (true) {


        } else {

            Thread thread = new Thread(new LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));
            thread.setDaemon(true);
            thread.start();
            Thread.sleep(100);
            thread.interrupt();

            Thread thread3 = new Thread(new WaitingForUserInput());
            thread3.setName("InputWaitingThread");
            thread3.start();

            Thread thread2 = new Thread(new BlockingTask());
            thread2.start();
            thread2.interrupt();
        }
    }


    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                // here is the secret to exit from thread and terminated   keep tread safer and faster
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            //do things
            try {
                Thread.sleep(500);
                //Thread.sleep(500000);
                // System.out.println("Existing blocking thread");
            } catch (InterruptedException e) {
                System.out.println("Existing blocking thread");
            }
        }
    }

    // QUEZE
    private static class WaitingForUserInput implements Runnable {
        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Waiting for input... Press 'q' to exit.");
                while (true) {
                    String input = reader.readLine();
                    if ("q".equalsIgnoreCase(input)) {
                        System.out.println("Exiting input thread...");
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
