import java.io.BufferedReader;

public class GraphReader {

    /**
     * Takes the file path from within the graphGenerationCode directory and returns
     * the generated SimpleGraph
     * @param filepath folder/graph.txt
     * @return SimpleGraph constructed from the text.
     */
    public static SimpleGraph readGraph(String filepath) {
        SimpleGraph graph = new SimpleGraph();
        String path = "graphGenerationCode/graphGenerationCode/";
        String graphType = filepath.split("/")[0];

        try (BufferedReader reader = InputLib.fopen(path + filepath)){
            while (reader.ready()) {
                String line = reader.readLine();
                String vertex1, vertex2;
                int value;

                if (graphType.equalsIgnoreCase("bipartite")) {
                    String[] split = line.split("\t");
                    vertex1 = split[1];
                    vertex2 = split[2];
                    value = Integer.parseInt(split[3]);
                } else {
                    String[] split = line.split(" ");
                    vertex1 = split[0];
                    vertex2 = split[1];
                    value = Integer.parseInt(split[2]);
                }

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
                        new EdgeData(0,value),
                        vertex1 + "-" + vertex2);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return graph;
    }

    public static void main(String[] args) {
        SimpleGraph g;
//        g = graphReader("Bipartite/g1.txt");
//        g = graphReader("FixedDegree/20v-3out-4min-355max.txt");
        g = readGraph("Mesh/smallMesh.txt");
//        g = graphReader("Random/n10-m10-cmin5-cmax10-f30.txt");
        System.out.println("Vertices: " + g.numVertices());
        System.out.println("Edges: " + g.numEdges());
    }
}
