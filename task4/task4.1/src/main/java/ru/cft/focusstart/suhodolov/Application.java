package ru.cft.focusstart.suhodolov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Application {

    private static final int THREADS_COUNT = 10;
    private static final int NUMBER = 10_000_000;
    private static final int STEP = NUMBER / THREADS_COUNT;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(THREADS_COUNT);

        List<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < THREADS_COUNT; i++) {
            int startVal = i * STEP + 1;
            int endVal = (i + 1) * STEP >= NUMBER || i == THREADS_COUNT - 1
                    ? NUMBER
                    : (i + 1) * STEP;

            futures.add(CompletableFuture.supplyAsync(new Task(startVal, endVal), threadPool));
        }

        long result = 0;
        for (Future<Long> future : futures) {
            try {
                result += future.get();
            } catch (InterruptedException e) {
                System.out.println("Occupied thread has been interrupted");
            } catch (ExecutionException e) {
                System.out.println("Task aborted. Can't retrieve the result.");
            }
        }

        System.out.println(result);

        threadPool.shutdown();
    }
}
