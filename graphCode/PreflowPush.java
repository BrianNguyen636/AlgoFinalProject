import java.util.LinkedList;
import java.util.Queue;

public class PreflowPush {
    public static SimpleGraph preflow(SimpleGraph graph) {
        Vertex source = graph.aVertex();
        source.setData(new VertexData(graph.numVertices(),0));
        for (Object obj : source.incidentEdgeList) {
            Edge e = (Edge) obj;
            EdgeData eData = (EdgeData) e.getData();
            eData.flow = eData.capacity;
            VertexData vData = (VertexData) (e.getSecondEndpoint().getData());
            vData.excess = eData.capacity;
        }
        return graph;
    }

    public static void relabel(Vertex v) {
        VertexData vData = (VertexData) v.getData();
        int min = vData.height;
        for (Object obj : v.incidentEdgeList) {
            Edge e = (Edge) obj;
            Vertex v2 = e.getSecondEndpoint();
            VertexData targetData = (VertexData) v2.getData();
            if (targetData.height < min) {
                min = targetData.height;
            }
        }
        vData.height = min + 1;
    }
    private static boolean push(Vertex v, Queue<Vertex> queue) {
        boolean result = false;
        VertexData vData = (VertexData) v.getData();
        for (Object obj : v.incidentEdgeList) {
            Edge e = (Edge)obj;
            EdgeData eData = (EdgeData) e.getData();
            Vertex v2 = e.getSecondEndpoint();
            VertexData targetData = (VertexData) v2.getData();

            //If target's height less than source height
            if (targetData.height < vData.height) {
                //Sending flow down the edge
                int amount = eData.capacity - eData.flow;
                //If edge can not receive flow, continue.
                if (amount == 0) continue;

                //Add flow to the excess
                if (amount <= vData.excess) {
                    eData.flow += amount;
                    targetData.excess += amount;
                    vData.excess -= amount;
                } else {
                    eData.flow += vData.excess;
                    targetData.excess += vData.excess;
                    vData.excess = 0;
                }
                //Adding to queue
                queue.add(v2);
                result = true;
            }
        }
        return result;
    }
    public static int preflowpush(String filepath) {
        int maxflow = 0;
        SimpleGraph graph = GraphReader.readGraph(filepath);
        SimpleGraph residual = FordFulkerson.buildResidual(graph);
        preflow(graph);
        preflow(residual);
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(residual.aVertex());
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (!push(v, queue)) {
                relabel(v);
            }

            if (queue.isEmpty()) {
                for (Object o : residual.vertexList) {
                    Vertex vertex = (Vertex) o;
                    VertexData vertexData = (VertexData) vertex.getData();
                    if (vertexData.excess > 0
                        && !vertex.getName().equals("s")
                        && !vertex.getName().equals("t")) {
                        queue.add(vertex);
                    }
                }
            }
        }
        return maxflow;
    }

    public static void main(String[] args) {

    }
}