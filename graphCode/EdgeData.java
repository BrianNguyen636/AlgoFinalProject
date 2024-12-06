/**
 * Data class to hold edge data.
 * 
 * @author Brian Nguyen
 * @version 05/12/2024
 */
class EdgeData {
    int flow;
    int capacity;

    /**
     * Initializes an EdgeData object.
     * @param f the flow.
     * @param c the capacity.
     */
    public EdgeData(int f, int c) {
        flow = f;
        capacity = c;
    }
}