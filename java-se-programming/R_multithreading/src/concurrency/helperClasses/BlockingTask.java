package concurrency.helperClasses;

public class BlockingTask implements Runnable{
    @Override
    public void run() {
      //  Runnable runnable = () -> System.out.println("Insider thread: " + Thread.currentThread());
        System.out.println("Insider thread: " + Thread.currentThread()+ " befor call");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Insider thread: " + Thread.currentThread()+ " after");
    }
}
