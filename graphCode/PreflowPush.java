import java.util.LinkedList;
import java.util.Queue;




public class PreflowPush {
    static boolean DEBUG = false;

    public static SimpleGraph preflow(SimpleGraph graph) {
        Vertex source = graph.aVertex();
        source.setData(new VertexData(graph.numVertices(),0));
        for (Object obj : source.incidentEdgeList) {
            Edge e = (Edge) obj;
            EdgeData eData = (EdgeData) e.getData();
            if (source == e.getFirstEndpoint()) {
                eData.flow = eData.capacity;
                VertexData vData = (VertexData) (e.getSecondEndpoint().getData());
                vData.excess = eData.capacity;

                //Add flow to backwards edge
                Edge back = graph.edgeMap.get(e.getSecondEndpoint().getName() + "-s");
                EdgeData backData = (EdgeData) back.getData();
                back.setData(new EdgeData(backData.flow, backData.capacity += eData.capacity));
            }

        }
        return graph;
    }

    public static void relabel(Vertex v) {
        VertexData vData = (VertexData) v.getData();
        int min = vData.height;
        for (Object obj : v.incidentEdgeList) {
            Edge e = (Edge) obj;
            EdgeData eData = (EdgeData)e.getData();
            Vertex v2 = e.getSecondEndpoint();
            VertexData targetData = (VertexData) v2.getData();
            if (targetData.height < min && v == e.getFirstEndpoint() && eData.flow < eData.capacity) {

                min = targetData.height;
            }
        }
        if (DEBUG) System.out.println("Relabeling: " + v.getName());
        v.setData(new VertexData(min + 1, vData.excess));
    }
    private static boolean push(Vertex v, Queue<Vertex> queue, SimpleGraph residual) {
        boolean result = false;
        VertexData vData = (VertexData) v.getData();

        if (DEBUG) System.out.println("h:" + vData.height +",e:"+ vData.excess);

        for (Object obj : v.incidentEdgeList) {
            Edge e = (Edge)obj;
            EdgeData eData = (EdgeData) e.getData();
            Vertex v2 = e.getSecondEndpoint();
            VertexData targetData = (VertexData) v2.getData();
            if (DEBUG) System.out.println("Checking edge " + e.getName() + ": " + eData.flow + "," + eData.capacity);
            if (DEBUG) System.out.println("h:" + targetData.height +",e:"+ targetData.excess);

            //If forward edge
            //and target's height less than source height
            if (v == e.getFirstEndpoint() && targetData.height < vData.height) {
                //Sending flow down the edge
                int amount = Math.min(eData.capacity - eData.flow, vData.excess);
                //If edge can not receive flow, continue.
                if (amount == 0) continue;
                if (DEBUG) System.out.println(amount + " flow down " + e.getName());
                //Add flow to forward edge
                eData.flow += amount;
                targetData.excess += amount;
                vData.excess -= amount;

                //Add flow to backwards edge
                Edge back = residual.edgeMap.get(v2.getName() + "-" + v.getName());
                EdgeData backData = (EdgeData) back.getData();
                back.setData(new EdgeData(backData.flow, backData.capacity += amount));

                //Adding to queue
//                System.out.println("Adding " + v2.getName());
//                queue.add(v2);
                result = true;
            }
        }
        return result;
    }
    public static int preflowpush(String filepath) {
        long start = System.currentTimeMillis();
        SimpleGraph graph = GraphReader.readGraph(filepath);
        SimpleGraph residual = FordFulkerson.buildResidual(graph);
        preflow(residual);
        Queue<Vertex> queue = new LinkedList<>();
        for (Object o : residual.aVertex().incidentEdgeList) {
            Edge e = (Edge)o;
            Vertex v2 = e.getSecondEndpoint();
            if (e.getFirstEndpoint() == residual.aVertex()) {
                queue.add(v2);
            }
        }
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (!push(v, queue, residual)) {
                relabel(v);
            }


            if (queue.isEmpty()) {
                for (Object o : residual.vertexList) {
                    Vertex vertex = (Vertex) o;
                    VertexData vertexData = (VertexData) vertex.getData();
                    if (vertexData.excess > 0
                        && !vertex.getName().equals("s")
                        && !vertex.getName().equals("t")) {
//                        System.out.println("Adding " + vertex.getName());
                        queue.add(vertex);
                    }
                }
            }
        }
        //Get excess at sink
        Vertex sink = residual.vertexMap.get("t");
        VertexData vData = (VertexData) sink.getData();
        long totalTime = (System.currentTimeMillis() - start);
        System.out.println("Time Elapsed: " + totalTime + "ms");
        return vData.excess;
    }

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
            System.out.println(preflowpush(s));
        }

    }
}