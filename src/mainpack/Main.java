package mainpack;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    static Object o = new Object(); // SOLUTION

    static class Friend {
        private final String name;

        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public void bow(Friend bower) {
            synchronized (o) { // SOLUTION
                System.out.format("%s: %s has bowed to me!%n", this.name, bower.getName());
                bower.bowBack(this);
            }
        }
        public void bowBack(Friend bower) {
            System.out.format("bowBack>>> %s: ", this.name);
            synchronized (o) { // SOLUTION
                System.out.format("%s has bowed BACK to me!%n", bower.getName());
            }
        }
    }


    public static void main(String[] args) throws InterruptedException
    {
        System.out.println();

        //2
        //CountDownLatch startSignal = new CountDownLatch(2);

        //3
        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(5);

        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");

        //Thread t1 = new Thread(
        WORKER_THREAD_POOL.execute(new Runnable() {
            public void run() {
                alphonse.bow(gaston);
                //startSignal.countDown();
            }
        });
        //t1.start();

        Thread t2 = new Thread(() -> {
        //WORKER_THREAD_POOL.execute(
            gaston.bow(alphonse);
            //startSignal.countDown();
        });
        t2.start();

        // method 1
//        t1.join();
//        System.out.println("t1 finished");
//        t2.join();
//        System.out.println("t2 finished");

        // method 2
        //startSignal.await();

        //3
        WORKER_THREAD_POOL.shutdown();
        WORKER_THREAD_POOL.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        System.out.println("All finished...");

        System.out.println();
    }
}
