package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import cse332.graph.GraphUtil;
import main.Parser;

import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        // maps = outgoing edges from vertex
        // keys = destination vertices
        // values = weights of corresponding edges
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        // initializing variables for number of vertices, distances, predecessors
        int vertNum = adjMatrix.length;
        int[] dist = new int[vertNum];
        int[] pred = new int[vertNum];

        for (int i = 0; i < vertNum; i++) {
            dist[i] = GraphUtil.INF;
            pred[i] = -1;
        }
        dist[source] = 0;

        // Bellman-ford algorithm iterations
        for (int i = 0; i < vertNum; i++) {
            int[] copiedDist = new int[vertNum];

            // copy of current distances for relaxation
            for (int j = 0; j < vertNum; j++) {
                copiedDist[j] = dist[j];
            }

            // relaxing edges in graph
            for (int j = 0; j < vertNum; j++) {
                Map<Integer, Integer> neighbors = g.get(j);
                for (Integer edge : neighbors.keySet()) {
                    // checks if distance can be updated based on relaxation
                    if (copiedDist[j] != GraphUtil.INF && neighbors.get(edge) != GraphUtil.INF && copiedDist[j] + neighbors.get(edge) < dist[edge]) {
                        dist[edge] = copiedDist[j] + neighbors.get(edge);
                        pred[edge] = j;
                    }
                }
            }
        }
        // return list of vertices involved in negative cycle
        return GraphUtil.getCycle(pred);
    }
}
