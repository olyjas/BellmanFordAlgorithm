package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final int lo;
    private final int hi;
    private final int[]dist;
    private final int[]pred;
    private final int[]currDist;
    private final List<Map<Integer, Integer>> g;

    public RelaxOutTaskBad(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g) {
        this.lo = lo;
        this.hi = hi;
        this.dist = dist;
        this.pred = pred;
        this.currDist = currDist;
        this.g = g;
    }

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential(lo, hi, dist, pred, currDist, g);
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxOutTaskBad left = new RelaxOutTaskBad(lo, mid, dist, pred, currDist, g);
            RelaxOutTaskBad right = new RelaxOutTaskBad(mid, hi, dist, pred, currDist, g);

            left.fork();
            right.compute();
            left.join();
        }
    }

    public static void sequential(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g) {
        for (int v = lo; v < hi; v++) {
            for (Map.Entry<Integer, Integer> edge : g.get(v).entrySet()) {
                int vertex = edge.getKey();
                int weight = edge.getValue();
                if (currDist[v] != GraphUtil.INF && currDist[v] + weight < dist[vertex]) {
                    dist[vertex] = currDist[v] + weight;
                    pred[vertex] = v;
                }
            }
        }
    }

    public static void parallel(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g) {
        pool.invoke(new RelaxOutTaskBad(lo, hi, dist, pred, currDist, g));
    }

}
