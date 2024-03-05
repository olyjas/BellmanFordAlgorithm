package solvers;

import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskLock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import java.util.List;
import java.util.Map;

public class OutParallelLock implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        ReentrantLock[] locks = new ReentrantLock[adjMatrix[0].length];
        int vertices = adjMatrix.length;
        int[] dist = new int[vertices];
        int[] pred = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            dist[i] = GraphUtil.INF;
            pred[i] = -1;
            locks[i] = new ReentrantLock();
        }
        dist[source] = 0;

        for (int i = 0; i < vertices; i++) {
            int[] currDist = ArrayCopyTask.copy(dist);
            RelaxOutTaskLock.parallel(0, vertices, dist, pred, currDist, g, locks);
        }
        return GraphUtil.getCycle(pred);
    }

}
