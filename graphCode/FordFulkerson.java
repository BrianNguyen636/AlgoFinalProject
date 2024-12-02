import java.util.*;

public class FordFulkerson {

    static boolean DEBUG = false;

    /**
     * Takes a graph and builds a residual graph based on it
     * @param graph The SimpleGraph
     * @return the residual graph as a residual
     */
    public static SimpleGraph buildResidual(SimpleGraph graph) {
        SimpleGraph residual = new SimpleGraph();
        for (Object e : graph.edgeList) {
            Edge edge = (Edge) e;
            EdgeData eData = (EdgeData) edge.getData();
            Vertex v1 = edge.getFirstEndpoint();
            Vertex v2 = edge.getSecondEndpoint();

            Vertex v1res;
            Vertex v2res;
            //Inserting vertices if not already
            if (!residual.containsVertex((String)v1.getName())) {
                v1res = residual.insertVertex(new Vertex(v1.getData(), v1.getName()));
            } else {
                v1res = residual.vertexMap.get((String)v1.getName());
            }
            if (!residual.containsVertex((String)v2.getName())) {
                v2res = residual.insertVertex(new Vertex(v2.getData(), v2.getName()));
            } else {
                v2res = residual.vertexMap.get((String)v2.getName());
            }

            if (!residual.edgeMap.containsKey(v1.getName() + "-" + v2.getName())) {
                //Inserting forwards edge
                Edge e1 = residual.insertEdge(v1res, v2res,
                        new EdgeData(0, eData.capacity-eData.flow),
                        v1.getName() + "-" + v2.getName()
                );
            } else {
                Edge e1 = residual.edgeMap.get(v1.getName() + "-" + v2.getName());
                e1.setData(new EdgeData(0, eData.capacity-eData.flow));
            }
            if (!residual.edgeMap.containsKey(v2.getName() + "-" + v1.getName())) {
                Edge e2 = residual.insertEdge(v2res, v1res,
                        new EdgeData(0, eData.flow),
                        v2.getName() + "-" + v1.getName()
                );
            } else {
                Edge e2 = residual.edgeMap.get(v2.getName() + "-" + v1.getName());
                e2.setData(new EdgeData(0, eData.capacity-eData.flow));
            }

        }
        return residual;
    }

    /**
     * Finds an augmenting path and returns it or null if none
     * @param residual The residual graph
     * @return List of Edges for the augmenting path
     */
    public static List<Object> augmentingPath(SimpleGraph residual) {
        List<Object> path = new LinkedList<>();
        Vertex source = residual.aVertex();
        Set<Vertex> visited = new HashSet<>();
        visited.add(source);
        if (dfsAugment(source, path, visited)) return path;
        else return null;
    }

    /**
     * Recursive helper for augmentingPath()
     * @param v vertex searched
     * @param path the path of edges built so far
     * @param visited All vertices visited so far.
     * @return True or False the target can be reached.
     */
    public static boolean dfsAugment(Vertex v, List<Object> path, Set<Vertex> visited) {
        if (v.getName().equals("t")) {
            return true;
        }

        for (Object e : v.incidentEdgeList) {
            Edge edge = (Edge)e;
            EdgeData data = (EdgeData) edge.getData();
            Vertex v1 = edge.getFirstEndpoint();
            Vertex v2 = edge.getSecondEndpoint();
            if (v1 == v && data.flow < data.capacity && visited.add(v2)) {
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
     * Finds the bottleneck in a path
     * @param path The path of edges
     * @param residual The residual graph the path follows
     * @return The value of the bottleneck
     */
    public static int findBottleneck(List<Object> path, SimpleGraph residual) {

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < path.size(); i++) {
            Edge edge = (Edge)path.get(i);
            EdgeData data = (EdgeData) edge.getData();
            if (data.capacity < min) {
                min = data.capacity;
            }
        }
        return min;
    }

    /**
     * Given a flow value, an augmenting path, a graph, and it's residual, modify the graphs edges
     * @param graph The original graph
     * @param residual The residual graph
     * @param path The augmenting path
     * @param flow The flow value used
     */
    public static void modifyGraphEdges(SimpleGraph graph, SimpleGraph residual, List<Object> path, int flow) {
        for (int i = 0; i < path.size(); i++) {
            Edge edge = (Edge) path.get(i);
            String v1name = (String)edge.getFirstEndpoint().getName();
            String v2name = (String)edge.getSecondEndpoint().getName();

//            Graph
            Edge e;
            if (graph.edgeMap.containsKey((v1name + "-" + v2name))) { //Forward edge
                e = graph.edgeMap.get(v1name+"-"+v2name);
                EdgeData data = (EdgeData) e.getData();
                e.setData(new EdgeData(data.flow + flow, data.capacity));
            } else { //Backwards edge
                e = graph.edgeMap.get(v2name+"-"+v1name);
                EdgeData data = (EdgeData) e.getData();
                e.setData(new EdgeData(data.flow - flow, data.capacity));
            }
            //Residual graph
            //Forward Edge

            Edge resF = residual.edgeMap.get(v1name+"-"+v2name);
            EdgeData dataF = (EdgeData) resF.getData();
            resF.setData(new EdgeData(0, dataF.capacity - flow));
            //Backwards Edge
            Edge resB = residual.edgeMap.get(v2name+"-"+v1name);
            EdgeData data2 = (EdgeData) resB.getData();
            resB.setData(new EdgeData(0, data2.capacity + flow));

        }
    }

    /**
     * Adds flow based on an augmenting path in the given graph and residual graph
     * @param graph The graph
     * @param residual The graph's residual
     * @return The flow added to each graph
     */
    public static int augment(SimpleGraph graph, SimpleGraph residual) {

//        System.out.println("---Finding augment");
        List<Object> path = augmentingPath(residual);

        if (path == null) return 0;

        if (DEBUG) {
            System.out.print("Path: ");
            for (Object o : path) {
                Edge e = (Edge)o;
                System.out.print(e.getName() + ",");
            }
            System.out.println();
            for (Object o : path) {
                Edge e = (Edge)o;
                EdgeData d = (EdgeData) e.getData();
                System.out.print(d.capacity + ",");
            }
            System.out.println();
        }

//        System.out.println("---Finding bottleneck");
        int flow = findBottleneck(path, residual);

//        System.out.println("---Modifying graphs");
        modifyGraphEdges(graph, residual, path, flow);

        if (DEBUG) {
            for (Object o : path) {
                Edge e = (Edge)o;
                EdgeData d = (EdgeData) e.getData();
                System.out.print(d.capacity + ",");
            }
            System.out.println();
            System.out.println("---" + flow + " flow added");
        }
        return flow;
    }

    /**
     * The algorithm to find the max flow of a graph
     * @param filepath The filepath of the given graph
     * @return The max flow of the graph
     */
    public static int fordFulkerson(String filepath) {
        SimpleGraph graph = GraphReader.readGraph(filepath);
        SimpleGraph residual = buildResidual(graph);

        int maxflow = 0;
        int flow = augment(graph, residual);
        while (flow != 0) {
            maxflow += flow;
            flow = augment(graph, residual);
        }
        
        return maxflow;
    }
    public static void main(String[] args){

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
            System.out.println("input graph: " + s);
            System.out.println(fordFulkerson(s));
            System.out.println();
        }
    }
}
