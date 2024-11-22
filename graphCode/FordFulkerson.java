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

        System.out.println("Vertices: " + graph.numVertices());
        System.out.println("Edges: " + graph.numEdges());


        return residual;
    }

    public static void main(String[] args) {
        SimpleGraph residual = buildResidual(GraphReader.readGraph("test.txt"));
        System.out.println("Vertices:" + residual.numVertices());
        System.out.println("Edges:" + residual.numEdges());
    }
}
