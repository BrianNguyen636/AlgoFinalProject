class EdgeData {
    int forwardsFlow;
    int backwardsFlow;
    int capacity;
    public EdgeData(int f, int b, int c) {
        forwardsFlow = f;
        backwardsFlow = b;
        capacity = c;
    }
}