package concurrency;

public class Concurrency {


    public static void main(String[] args) throws InterruptedException {


        if (true) {
            NewThread newThread = new NewThread();

            newThread.start();


        } else {
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(" now " + Thread.currentThread().getName());
                    System.out.println(" Priority " + Thread.currentThread().getPriority());
                    throw new RuntimeException("intention exp");
                }
            });
            tr.setName("intention");
            tr.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    System.out.println(" Priority " + thread.getName() + throwable.getMessage());
                }
            });


            tr.setPriority(Thread.MAX_PRIORITY);

            System.out.println(" befor " + Thread.currentThread().getName());
            tr.start();
            System.out.println(" after " + Thread.currentThread().getName());
            //Thread.sleep(1000);

        }


    }

    private static class NewThread extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println(" NewThread " + this.getName());
            this.getPriority();


        }
    }


}
