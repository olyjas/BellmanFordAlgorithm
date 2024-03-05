package main;

import cse332.exceptions.NotYetImplementedException;

import java.util.List;
import java.util.Map;
import cse332.graph.GraphUtil;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight
     */
    public static List<Map<Integer, Integer>> parse(int[][] adjMatrix) {
       int vertNum = adjMatrix.length;
       List<Map<Integer, Integer>> adjList = new ArrayList<>(vertNum);

       // initializing adjacency list with empty maps for each vertex
       for (int i = 0; i < vertNum; i++) {
           adjList.add(new HashMap<>());
       }
        // adding neighbors to adjacency list
        for (int i = 0; i < vertNum; i++) {
            for (int j = 0; j < vertNum; j++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE) {
                    adjList.get(i).put(j, adjMatrix[i][j]);
                }
            }
        }
        return adjList;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight with incoming edges
     */
    public static List<Map<Integer, Integer>> parseInverse(int[][] adjMatrix) {
        int vertNum = adjMatrix.length;
        List<Map<Integer, Integer>> adjList = new ArrayList<>(vertNum);

        // initializing adjacency list with empty maps for each vertex
        for (int i = 0; i < vertNum; i++) {
            adjList.add(new HashMap<>());
        }
        // adding neighbors to adjacency list
        for (int i = 0; i < vertNum; i++) {
            for (int j = 0; j < vertNum; j++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE) {
                    adjList.get(j).put(i, adjMatrix[i][j]);
                }
            }
        }
        return adjList;
    }
}
