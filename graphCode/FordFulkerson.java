import java.util.*;

public class FordFulkerson {

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

            //Inserting forwards edge
            residual.insertEdge(v1res, v2res,
                new EdgeData(0, eData.capacity-eData.flow),
                v1.getName() + "-" + v2.getName()
            );
            //Backwards edge
            residual.insertEdge(v2res, v1res,
                    new EdgeData(0, eData.flow),
                    v2.getName() + "-" + v1.getName()
            );


        }
        return residual;
    }

    /**
     * Finds an augmenting path and returns it or null if none
     * @param residual The residual graph
     * @return List of vertices for the augmenting path
     */
    public static List<Vertex> augmentingPath(SimpleGraph residual) {
        List<Vertex> path = new LinkedList<>();
        Vertex source = residual.aVertex();
        path.add(source);
        Set<Vertex> visited = new HashSet<>();
        visited.add(source);
        if (dfsAugment(source, path, visited)) return path;
        else return null;

//        List<String> strPath = bfsAugment(residual);
//        if (strPath == null) return null;
//        for (String s : strPath) {
//            path.add(residual.vertexMap.get(s));
//        }
//        return path;
    }

    /**
     * Recursive helper for augmentingPath()
     * @param v vertex searched
     * @param path the path built so far
     * @return True or False the target can be reached.
     */
    public static boolean dfsAugment(Vertex v, List<Vertex> path, Set<Vertex> visited) {
//        System.out.println("Visiting:" + v.getName());

        if (v.getName().equals("t")) {
            return true;
        }

        for (Object e : v.incidentEdgeList) {
            Edge edge = (Edge)e;
            EdgeData data = (EdgeData) edge.getData();
            Vertex v1 = edge.getFirstEndpoint();
            Vertex v2 = edge.getSecondEndpoint();
            if (v1 == v && data.capacity - data.flow > 0 && visited.add(v2)) {
//                System.out.println("Edge: " + edge.getName());
//                System.out.println("Flow:"+ (int)(data.capacity - data.flow));
                path.add(v2);
                if (dfsAugment(v2, path, visited)) {
                    return true;
                }
            }
        }
        path.removeLast();
        return false;
    }
    public static List<String> bfsAugment(SimpleGraph residual) {
        Vertex v = residual.aVertex();
        List<String> path = new LinkedList<>();
        path.add((String)v.getName());
        Queue<List<String>> queue = new LinkedList<>();
        queue.add(path);
        while (queue.size() != 0) {
            List<String> p = queue.poll();
            Vertex vert = residual.vertexMap.get(p.getLast());
            if (vert.getName().equals("t")) {
                return p;
            }
            for (Object e : vert.incidentEdgeList) {
                Edge edge = (Edge)e;
                Vertex v1 = edge.getFirstEndpoint();
                Vertex v2 = edge.getSecondEndpoint();
                if (v1 == vert) {
                    EdgeData data = (EdgeData) edge.getData();
//                System.out.println("Edge: " + edge.getName());
//                System.out.println("Flow:"+ (int)(data.capacity - data.flow));
                    if (data.capacity - data.flow > 0 && !p.contains((String)v2.getName())) {
                        List<String> next = new LinkedList<>(p);
                        next.add((String)v2.getName());
                        queue.add(next);
                    }
                }
            }

        }
        return null;
    }
    public static int findBottleneck(List<Vertex> path, SimpleGraph residual) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex v = path.get(i);

            Edge edge = residual.edgeMap.get(v.getName() + "-" + path.get(i+1).getName());
            EdgeData data = (EdgeData) edge.getData();
            if (data.capacity < min) {
                min = data.capacity;
            }

//            for (Object e : v.incidentEdgeList) {
//                Edge edge = (Edge) e;
//                Vertex v1 = edge.getFirstEndpoint();
//                Vertex v2 = edge.getSecondEndpoint();
//                if (v1 == v && v2 == path.get(i + 1)) {
//                    EdgeData data = (EdgeData) edge.getData();
//                    if (data.capacity < min) {
//                        min = data.capacity;
//                    }
//                }
//            }
        }
        return min;
    }
    public static int augment(SimpleGraph graph, SimpleGraph residual) {

        System.out.println("Finding augment");
        List<Vertex> path = augmentingPath(residual);

        if (path == null) return 0;

//        System.out.print("Path: ");
//        for (Vertex v : path) {
//            System.out.print((","+v.getName()));
//        }
//        System.out.println();

        System.out.println("Finding bottleneck");
        int flow = findBottleneck(path, residual);
        Vertex graphVertex = graph.aVertex();

        System.out.println("Modifying graphs");
        for (int i = 1; i < path.size(); i++) {
            Vertex v = path.get(i);
            String v1name = (String)graphVertex.getName();
            String v2name = (String)v.getName();

            //Graph
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

            graphVertex = v;

        }
        System.out.println(flow + " flow added");
        return flow;
    }

    public static int fordFulkerson(String filepath) {

        SimpleGraph graph = GraphReader.readGraph(filepath);
        SimpleGraph residual = buildResidual(graph);

        int maxflow = 0;
        int flow = augment(graph, residual);
        while (flow != 0) {
            System.out.println("Flow:"+flow);
            maxflow += flow;
//            Thread.sleep(1000);
            flow = augment(graph, residual);
        }
        return maxflow;
    }
    public static void main(String[] args){
//        SimpleGraph graph = GraphReader.readGraph("test2.txt");
//        List<String> path = bfsAugment(buildResidual(graph));
//        if (path == null) System.out.println("null");
//        for (String s : path) {
//            System.out.print(s);
//        }
//        System.out.println();
//
//        System.out.println(fordFulkerson("test2.txt"));

//        System.out.println(fordFulkerson("bipartite/g1.txt"));
//        System.out.println(fordFulkerson("bipartite/g2.txt"));
//        System.out.println(fordFulkerson("FixedDegree/20v-3out-4min-355max.txt"));
        System.out.println(fordFulkerson("FixedDegree/100v-5out-25min-200max.txt"));
    }
}
