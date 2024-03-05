package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxInTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private final int lo;
    private final int hi;
    private final int[]dist;
    private final int[]pred;
    private final int[]currDist;
    private final List<Map<Integer, Integer>> g;

    public RelaxInTask(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g) {
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
            RelaxInTask left = new RelaxInTask(lo, mid, dist, pred, currDist, g);
            RelaxInTask right = new RelaxInTask(mid, hi, dist, pred, currDist, g);

            left.fork();
            right.compute();
            left.join();
        }
    }

    public static void sequential(int lo, int hi, int[] dist, int[] pred, int[] currDist, List<Map<Integer, Integer>> g) {
        // iterate over the target vertices in the range
        for (int i = lo; i < hi; i++) {
            Map<Integer, Integer> neighbors = g.get(i);
            for (int edge : neighbors.keySet()) {
                int cost = neighbors.get(edge);
                if (currDist[edge] != GraphUtil.INF && (currDist[edge] + cost < dist[i])) {
                    dist[i] = currDist[edge] + cost;
                    pred[i] = edge;
                }
            }
        }
    }


            public static void parallel ( int lo, int hi, int[] dist, int[] pred, int[] currDist, List<
            Map<Integer, Integer>>g){
                RelaxInTask task = new RelaxInTask(lo, hi, dist, pred, currDist, g);
                pool.invoke(task);
            }
        }
