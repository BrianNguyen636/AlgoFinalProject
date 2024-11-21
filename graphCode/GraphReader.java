import java.io.BufferedReader;

public class GraphReader {

    public static SimpleGraph graphReader(String filename) {
        SimpleGraph graph = new SimpleGraph();

        try (BufferedReader reader = InputLib.fopen(filename)){

            while (reader.ready()) {
                String line = reader.readLine();
                String[] split = line.split("\t");
                String vertex1 = split[1];
                String vertex2 = split[2];
                int value = Integer.parseInt(split[3]);
                //Get the two vertexes
                Vertex v1;
                if (!graph.containsVertex(vertex1)) {
                    v1 = graph.insertVertex(null, vertex1);
                } else {
                    v1 = graph.vertexMap.get(vertex1);
                }
                Vertex v2;
                if (!graph.containsVertex(vertex2)) {
                    v2 = graph.insertVertex(null, vertex2);
                } else {
                    v2 = graph.vertexMap.get(vertex2);
                }

                graph.insertEdge(v1, v2,
                        new int[]{0,0,value},
                        vertex1 + "," + vertex2);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return graph;
    }

    public static void main(String[] args) {
        SimpleGraph g = graphReader("graphs/g2.txt");
        System.out.println("Vertices: " + g.numVertices());
        System.out.println("Edges: " + g.numEdges());
    }
}
