import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

class SumTask implements Runnable {
    private int[] array;
    private int start;
    private int end;
    private AtomicLong result;

    public SumTask(int[] array, int start, int end, AtomicLong result) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.result = result;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            result.addAndGet(array[i]);
        }
    }
}

public class main {
    public static void main(String[] args) {
        Random random = new Random();
        int[] array = new int[200_000_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1 + random.nextInt(10);
        }

        // Single-threaded sum
        AtomicLong resultSingle = new AtomicLong();
        long startSingle = System.nanoTime();
        new SumTask(array, 0, array.length, resultSingle).run();
        long endSingle = System.nanoTime();
        System.out.println("Single-threaded sum: " + resultSingle + ". Time taken: "
                + (endSingle - startSingle) / 1_000_000_000.0 + " seconds.");

        // Multi-threaded sum
        AtomicLong resultMulti = new AtomicLong();
        ExecutorService executor = Executors.newFixedThreadPool(4); // adjust depending on your CPU
        int blockSize = (array.length + 3) / 4; // adjust depending on your CPU
        long startMulti = System.nanoTime();
        for (int i = 0; i < 4; i++) { // adjust depending on your CPU
            int start = i * blockSize;
            int end = Math.min(array.length, start + blockSize);
            executor.submit(new SumTask(array, start, end, resultMulti));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endMulti = System.nanoTime();
        System.out.println("Multi-threaded sum: " + resultMulti + ". Time taken: "
                + (endMulti - startMulti) / 1_000_000_000.0 + " seconds.");
    }
}
