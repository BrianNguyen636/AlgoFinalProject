import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final int ITERATIONS = 100;
        
        String[] tests = {
                // "test.txt",
                // "test2.txt",
                // "Bipartite/g1.txt",
                // "Bipartite/g2.txt",
                // "FixedDegree/20v-3out-4min-355max.txt",
                // "FixedDegree/100v-5out-25min-200max.txt",
                // "Mesh/smallMesh.txt",
                // "Mesh/mediumMesh.txt",
                
                "Bipartite/100s_100t_0.5p_1min_1max.txt",
                "Bipartite/100s_100t_0.5p_1min_10max.txt",
                "Bipartite/100s_100t_0.5p_1min_50max.txt",
                "Bipartite/100s_100t_0.5p_1min_100max.txt",
                "Bipartite/100s_100t_0.5p_1min_500max.txt",
                "Bipartite/100s_100t_0.5p_1min_1000max.txt",

                "FixedDegree/1000v_100e_1min_1max.txt",
                "FixedDegree/1000v_100e_1min_10max.txt",
                "FixedDegree/1000v_100e_1min_50max.txt",
                "FixedDegree/1000v_100e_1min_100max.txt",
                "FixedDegree/1000v_100e_1min_500max.txt",
                "FixedDegree/1000v_100e_1min_1000max.txt",

                "Mesh/50r_50c_1cap.txt",
                "Mesh/50r_50c_10cap.txt",
                "Mesh/50r_50c_50cap.txt",
                "Mesh/50r_50c_100cap.txt",
                "Mesh/50r_50c_500cap.txt",
                "Mesh/50r_50c_1000cap.txt",

                // "Random/n10-m10-cmin5-cmax10-f30.txt",
                // "Random/n100-m100-cmin10-cmax20-f949.txt"
        };

        long start = System.currentTimeMillis();
        long totalTime = (System.currentTimeMillis() - start);
        long timeSum = 0;

        System.out.println("Taking average time of " + ITERATIONS + " iterations.");
        System.out.println();
                
        for (String s : tests) {   
            System.out.println("Input graph: " + s);
            double[] avgTimes = new double[3];

            timeSum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.currentTimeMillis();
                FordFulkerson.fordFulkerson(s);
                totalTime = (System.currentTimeMillis() - start);
                timeSum += totalTime;
            }
            avgTimes[0] = (double) timeSum / ITERATIONS;
            

            timeSum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.currentTimeMillis();
                CapacityScaling.capScaling(s);
                totalTime = (System.currentTimeMillis() - start);
                timeSum += totalTime;
            }
            avgTimes[1] = (double) timeSum / ITERATIONS;
            

            timeSum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                start = System.currentTimeMillis();
                PreflowPush.preflowpush(s);
                totalTime = (System.currentTimeMillis() - start);
                timeSum += totalTime;
            }
            avgTimes[2] = (double) timeSum / ITERATIONS;

            double minTime = Arrays.stream(avgTimes).min().getAsDouble();
            double maxTime = Arrays.stream(avgTimes).max().getAsDouble();
            
            System.out.print("- Ford-Fulkerson:\t" + avgTimes[0] + " ms");
            fastestOrSlowest(avgTimes[0], minTime, maxTime);
            System.out.println();

            System.out.print("- Capacity Scaling:\t" + avgTimes[1] + " ms");
            fastestOrSlowest(avgTimes[1], minTime, maxTime);
            System.out.println();

            System.out.print("- Preflow Push:\t\t" + avgTimes[2] + " ms");
            fastestOrSlowest(avgTimes[2], minTime, maxTime);
            System.out.println();

            System.out.println();
        }
        
    }

    public static void fastestOrSlowest(double time, double min, double max) {
        if (time == min) {
            System.out.print(" - (fastest)");
        } else if (time == max) {
            System.out.print(" - (slowest)");
        }
    }
}
