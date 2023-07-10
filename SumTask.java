import java.util.concurrent.atomic.AtomicLong;

public class SumTask implements Runnable {
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
