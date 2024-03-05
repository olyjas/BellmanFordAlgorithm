package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxInTask;
import java.util.concurrent.ForkJoinPool;

import java.util.List;
import java.util.Map;

public class InParallel implements BellmanFordSolver {
    private static final ForkJoinPool pool = new ForkJoinPool();

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parseInverse(adjMatrix);

        int vertices = adjMatrix.length;
        int[] dist = new int[vertices];
        int[] pred = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            dist[i] = GraphUtil.INF;
            pred[i] = -1;
        }
        dist[source] = 0;

        for (int i = 0; i < vertices; i++) {
            int[] currDist = ArrayCopyTask.copy(dist);
            RelaxInTask.parallel(0, vertices, dist, pred, currDist, g);
        }
        return GraphUtil.getCycle(pred);
    }
}