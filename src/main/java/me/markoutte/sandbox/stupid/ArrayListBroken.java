package me.markoutte.sandbox.stupid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Playing with array list and concurrency.
 *
 * Not so bad, sometimes it give {@link ArrayIndexOutOfBoundsException} (surprised, not so often),
 * and some items are cleared by another thread, that puts data at the same time.
 */
public class ArrayListBroken {

    private static ExecutorService service = Executors.newFixedThreadPool(10000);

    public static void main(String[] args) {

        Runnable[] runnables = new Runnable[100];
        final List<Integer> list = new ArrayList<>(1);//Collections.synchronizedList(new ArrayList<>(1));
        for (int i = 0; i < runnables.length; i++) {
            final int el = i;
            runnables[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10000; j++) {
//                            if (j % 2 == 0) {
                                list.add(el);
//                            } else {
//                                list.remove(el);
//                            }
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            };
        }
        for (Runnable runnable : runnables) {
            service.submit(runnable);
        }

        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println(list.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
