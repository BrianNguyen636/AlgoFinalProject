

public class Main {
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

        long start = System.currentTimeMillis();
        long totalTime = (System.currentTimeMillis() - start);

        System.out.println();
                
        for (String s : tests) {   
            System.out.println("Input graph: " + s);

            start = System.currentTimeMillis();
            FordFulkerson.fordFulkerson(s);
            totalTime = (System.currentTimeMillis() - start);
            System.out.println("- Ford-Fulkerson:\t" + totalTime + " ms");

            start = System.currentTimeMillis();
            CapacityScaling.capScaling(s);
            totalTime = (System.currentTimeMillis() - start);
            System.out.println("- Capacity Scaling:\t" + totalTime + " ms");

            start = System.currentTimeMillis();
            PreflowPush.preflowpush(s);
            totalTime = (System.currentTimeMillis() - start);
            System.out.println("- Preflow Push:\t\t" + totalTime + " ms");

            System.out.println();
        }


        // System.out.println();
        // System.out.println("### Testing Capacity Scaling ###");

        // for (String s : tests) {   
        //     System.out.println("input graph: " + s);
        //     long start = System.currentTimeMillis();
        //     CapacityScaling.capScaling(s);
        //     long totalTime = (System.currentTimeMillis() - start);
        //     System.out.println("Time Elapsed: " + totalTime + "ms");
        //     System.out.println();
        // }
        
    }
}
