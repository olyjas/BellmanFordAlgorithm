package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {


    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final int lo;
    private final int hi;
    private final int[]dist;
    private final int[]pred;
    private final int[]currDist;
    private final List<Map<Integer, Integer>> g;
    private final ReentrantLock[] locks;

    public RelaxOutTaskLock(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g, ReentrantLock[] locks) {
        this.lo = lo;
        this.hi = hi;
        this.dist = dist;
        this.pred = pred;
        this.currDist = currDist;
        this.g = g;
        this.locks = locks;
    }

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential(lo, hi, dist, pred, currDist, g, locks);
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxOutTaskLock left = new RelaxOutTaskLock(lo, mid, dist, pred, currDist, g, locks);
            RelaxOutTaskLock right = new RelaxOutTaskLock(mid, hi, dist, pred, currDist, g, locks);

            left.fork();
            right.compute();
            left.join();
        }
    }

    public static void sequential(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g, ReentrantLock[] locks) {
        for (int v = lo; v < hi; v++) {
            try {
                locks[v].lock();
                for (Map.Entry<Integer, Integer> edge : g.get(v).entrySet()) {
                    int vertex = edge.getKey();
                    int weight = edge.getValue();
                    if (currDist[v] != GraphUtil.INF && currDist[v] + weight < dist[vertex]) {
                        dist[vertex] = currDist[v] + weight;
                        pred[vertex] = v;
                    }
                }
                } finally {
                    locks[v].unlock();
                }
            }
        }

    public static void parallel(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g, ReentrantLock[] locks) {
        pool.invoke(new RelaxOutTaskLock(lo, hi, dist, pred, currDist, g, locks));
    }

}
