import java.util.LinkedList;
import java.util.List;

public class FordFulkerson {

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

    public static List<Vertex> augmentingPath(SimpleGraph residual) {
        List<Vertex> path = new LinkedList<>();
        if (dfsAugment(residual.aVertex(), path)) return path;
        else return null;
    }
    public static boolean dfsAugment(Vertex v, List<Vertex> path) {
        System.out.println("Visiting:" + v.getName());
        path.add(v);
        if (v.getName().equals("t")) return true;

        for (Object e : v.incidentEdgeList) {
            Edge edge = (Edge)e;
            Vertex v1 = edge.getFirstEndpoint();
            Vertex v2 = edge.getSecondEndpoint();
            if (v1 == v) {
                EdgeData data = (EdgeData) edge.getData();
                System.out.println("Edge: " + edge.getName());
                System.out.println("Flow:"+ (int)(data.capacity - data.flow));
                if (data.capacity - data.flow > 0 && !path.contains(v2)) {
                    if (dfsAugment(v2, path)) {
                        return true;
                    }
                }
            }
        }
        //If vertex does not lead to the target
        path.removeLast();
        return false;
    }

    public static int augment(List<Vertex> path, SimpleGraph graph, SimpleGraph residual) {
        int flow = 0;

        return flow;
    }

    public static void main(String[] args) {
        SimpleGraph graph = GraphReader.readGraph("test2.txt");
        SimpleGraph residual = buildResidual(graph);

        System.out.println("Vertices: " + graph.numVertices());
        System.out.println("Edges: " + graph.numEdges());
        System.out.println("Vertices:" + residual.numVertices());
        System.out.println("Edges:" + residual.numEdges());

        List<Vertex> augment = augmentingPath(residual);
        System.out.println(augment);
    }
}
