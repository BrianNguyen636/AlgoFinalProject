

public class Main {
    public static void main(String[] args) {
        final int ITERATIONS = 1;
        
        String[] tests = {
                // "test.txt",
                // "test2.txt",
                // "Bipartite/g1.txt",
                // "Bipartite/g2.txt",
                // "FixedDegree/20v-3out-4min-355max.txt",
                // "FixedDegree/100v-5out-25min-200max.txt",
                // "Mesh/smallMesh.txt",
                // "Mesh/mediumMesh.txt",
                
                // "Bipartite/100s_100t_0.5p_1min_1max.txt",
                // "Bipartite/100s_100t_0.5p_1min_10max.txt",
                // "Bipartite/100s_100t_0.5p_1min_50max.txt",
                // "Bipartite/100s_100t_0.5p_1min_100max.txt",
                // "Bipartite/100s_100t_0.5p_1min_500max.txt",
                // "Bipartite/100s_100t_0.5p_1min_1000max.txt",

                // "FixedDegree/1000v_100e_1min_1max.txt",
                // "FixedDegree/1000v_100e_1min_10max.txt",
                // "FixedDegree/1000v_100e_1min_50max.txt",
                // "FixedDegree/1000v_100e_1min_100max.txt",
                // "FixedDegree/1000v_100e_1min_500max.txt",
                // "FixedDegree/1000v_100e_1min_1000max.txt",

                // "Mesh/50r_50c_1cap.txt",
                // "Mesh/50r_50c_10cap.txt",
                // "Mesh/50r_50c_50cap.txt",
                // "Mesh/50r_50c_100cap.txt",
                // "Mesh/50r_50c_500cap.txt",
                // "Mesh/50r_50c_1000cap.txt",

                // "Mesh/100r_100c_1cap.txt",

                // "Random/n10-m10-cmin5-cmax10-f30.txt",
                // "Random/n100-m100-cmin10-cmax20-f949.txt"
        };

        long start = System.currentTimeMillis();
        long totalTime = (System.currentTimeMillis() - start);
        long timeSum = 0;
        double avgTime = 0;

        System.out.println("Taking average time of " + ITERATIONS + " iterations.");
        System.out.println();
                
        for (String s : tests) {   
            System.out.println("Input graph: " + s);

            timeSum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.currentTimeMillis();
                FordFulkerson.fordFulkerson(s);
                totalTime = (System.currentTimeMillis() - start);
                timeSum += totalTime;
            }
            avgTime = (double) timeSum / ITERATIONS;
            System.out.println("- Ford-Fulkerson:\t" + avgTime + " ms");

            timeSum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.currentTimeMillis();
                CapacityScaling.capScaling(s);
                totalTime = (System.currentTimeMillis() - start);
                timeSum += totalTime;
            }
            avgTime = (double) timeSum / ITERATIONS;
            System.out.println("- Capacity Scaling:\t" + avgTime + " ms");

            timeSum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.currentTimeMillis();
                PreflowPush.preflowpush(s);
                totalTime = (System.currentTimeMillis() - start);
                timeSum += totalTime;
            }
            avgTime = (double) timeSum / ITERATIONS;
            System.out.println("- Preflow Push:\t\t" + avgTime + " ms");

            System.out.println();
        }
        
    }
}
