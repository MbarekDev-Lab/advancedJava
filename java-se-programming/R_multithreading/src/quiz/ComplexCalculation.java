package quiz;

import java.math.BigInteger;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        // Create two threads to compute base1^power1 and base2^power2 concurrently
        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);

        // Start the threads
        thread1.start();
        thread2.start();

        try {
            // Wait for both threads to finish
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Combine results
        return thread1.getResult().add(thread2.getResult());
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private final BigInteger base;
        private final BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            result = base.pow(power.intValue()); // Compute base^power
        }

        public BigInteger getResult() {
            return result;
        }
    }

    public static void main(String[] args) {
        ComplexCalculation calculation = new ComplexCalculation();
        BigInteger result = calculation.calculateResult(
                new BigInteger("10"), new BigInteger("2"),
                new BigInteger("5"), new BigInteger("3"));
        System.out.println("Result: " + result);
    }
}
