import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Uses Capacity Scaling algorithm to find the max flow for a network flow graph.
 * 
 * @author Brian Nguyen
 * @version 05/12/2024
 */
public class CapacityScaling extends FordFulkerson {

    /**
     * Finds the delta value for a graph's edges
     * @param graph The graph
     * @return value of delta
     */
    public static int findDelta(SimpleGraph graph) {
        int delta = 1;
        for (Object obj: graph.edgeList) {
            Edge e = (Edge) obj;
            EdgeData edgeData = (EdgeData) e.getData();
            if (edgeData.capacity >= delta * 2) {
                delta *= 2;
            }
        }
        return delta;
    }

    /**
     * Ford Fulkerson augmenting path method modified with delta value
     * @param residual Residual graph
     * @param delta The delta value
     * @return The augmenting path of edges
     */
    public static List<Object> augmentingPath(SimpleGraph residual, int delta) {
        List<Object> path = new LinkedList<>();
        Vertex source = residual.aVertex();
        Set<Vertex> visited = new HashSet<>();
        visited.add(source);
        if (dfsAugment(source, path, visited, delta)) return path;
        else return null;
    }

    /**
     * Modified dfs algorithm to find an augmenting path with capacities less than delta
     * @param v The current vertex
     * @param path The path of edges so far
     * @param visited all Vertexes visited
     * @param delta The delta to check edges
     * @return boolean if path can be found
     */
    public static boolean dfsAugment(Vertex v, List<Object> path, Set<Vertex> visited, int delta) {
        if (v.getName().equals("t")) {
            return true;
        }

        for (Object e : v.incidentEdgeList) {
            Edge edge = (Edge)e;
            EdgeData data = (EdgeData) edge.getData();
            Vertex v1 = edge.getFirstEndpoint();
            Vertex v2 = edge.getSecondEndpoint();
//            System.out.println(edge.getName());
//            System.out.println(((EdgeData) edge.getData()).capacity);
            if (v1 == v && data.flow < data.capacity && visited.add(v2)
                    && data.capacity - data.flow >= delta) {
                path.add(e);
                if (dfsAugment(v2, path, visited)) {
                    return true;
                } else {
                    path.remove(e);
                }
            }
        }
        return false;
    }

    /**
     * Adds flow based on an augmenting path in the given graph and residual graph, with the given delta value
     * @param graph The given graph
     * @param residual The residual graph
     * @param delta The delta value
     * @return The value of the flow added
     */
    public static int augment(SimpleGraph graph, SimpleGraph residual, int delta) {

//        System.out.println("---Finding augment");
        List<Object> path = augmentingPath(residual, delta);
        if (path == null) return 0;

//        System.out.println("---Finding bottleneck");
        int flow = findBottleneck(path, residual);
//
//        System.out.println("---Modifying graphs");
        modifyGraphEdges(graph, residual, path, flow);
        return flow;
    }

    /**
     * Method for finding max flow using Capacity Scaling
     * @param filepath The file path to the graph input
     * @return The max flow
     */
    public static int capScaling(String filepath) {
        SimpleGraph graph = GraphReader.readGraph(filepath);
        SimpleGraph residual = buildResidual(graph);
        int delta = findDelta(graph);
        int maxflow = 0;
        int flow = augment(graph, residual, delta);
        while (delta >= 1) {
            if (flow == 0) {
                delta /=2;
            } else {
                maxflow += flow;
            }
            flow = augment(graph, residual, delta);
        }
        return maxflow;
    }

    /**
     * Main method for testing Capacity Scaling algorithm.
     * @param args Command line args
     */
    public static void main(String[] args) {
        String[] tests = {
                "test.txt",
                "test2.txt",
                "Bipartite/g1.txt",
                "Bipartite/g2.txt",
                "FixedDegree/20v-3out-4min-355max.txt",
                "FixedDegree/100v-5out-25min-200max.txt",
                "Mesh/smallMesh.txt",
                "Mesh/mediumMesh.txt",
                "Random/n10-m10-cmin5-cmax10-f30.txt",
                "Random/n100-m100-cmin10-cmax20-f949.txt"
        };
        for (String s : tests) {
            System.out.println(capScaling(s));
        }
    }
}
