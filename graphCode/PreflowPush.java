import java.util.LinkedList;
import java.util.Queue;

public class PreflowPush {
    public static SimpleGraph preflow(SimpleGraph graph) {
        Vertex source = graph.aVertex();
        source.setData(new VertexData(graph.numVertices(),Integer.MAX_VALUE));
        for (Object obj : source.incidentEdgeList) {
            Edge e = (Edge) obj;
            EdgeData eData = (EdgeData) e.getData();
            eData.flow = eData.capacity;
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
    private static void push(Queue<Vertex> queue) {
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
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

                    if (amount <= vData.excess) {
                        eData.flow += amount;
                        vData.excess -= amount;
                    } else {
                        eData.flow += vData.excess;
                        vData.excess = 0;
                    }
                    targetData.excess = eData.flow;

                    //Adding to queue
                    queue.add(v2);
                } else {

                    vData.height = targetData.height + 1;

                }
            }
            if (vData.excess > 0) {

            }
        }
    }
    public static int preflowpush(String filepath) {
        int maxflow = 0;
        SimpleGraph graph = GraphReader.readGraph(filepath);
        preflow(graph);
        return maxflow;
    }

    public static void main(String[] args) {

    }
}
