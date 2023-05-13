package paralleltasks;

import cse332.exceptions.NotYetImplementedException;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ArrayCopyTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    public static int[] copy(int[] src) {
        int[] dst = new int[src.length];
        pool.invoke(new ArrayCopyTask(src, dst, 0, src.length));
        return dst;
    }

    private final int[] src, dst;
    private final int lo, hi;

    public static void sequential() {
        throw new NotYetImplementedException();
    }

    public ArrayCopyTask(int[] src, int[] dst, int lo, int hi) {
        throw new NotYetImplementedException();
    }

    @SuppressWarnings("ManualArrayCopy")
    protected void compute() {
        throw new NotYetImplementedException();
    }
}
