package main;

public abstract class Entity
{
    protected static final int INFINITY = 999;

    protected final int id;

    // Each entity will have a distance table
    protected int[][] distanceTable = new int[NetworkSimulator.NUMENTITIES]
            [NetworkSimulator.NUMENTITIES];

    /**
     *
     * @param newId
     * @param initialDistances
     */
    public Entity(int newId, int[] initialDistances) {
        this.id = newId;

        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
                if (i == id)
                    distanceTable[i][j] = initialDistances[j];
                else
                    distanceTable[i][j] = INFINITY;
            }

        transmit();
    }

    // The update function.  Will have to be written in subclasses by students
    public abstract void update(Packet p);

    // The link cost change handler. Will have to be written in appropriate
    // subclasses by students.  Note that only Entity0 and Entity1 will need
    // this, and only if extra credit is being done
    public abstract void linkCostChangeHandler(int whichLink, int newCost);

    // Print the distance table of the current entity.
    protected abstract void printDT();


    /**
     * Helper method to initialize DV table for each entity
     *
     */
    protected void updateTable() {
        // for each column
        for (int col = 0; col < NetworkSimulator.NUMENTITIES; col++) {
            // check to see if there
            // is an easier way to
            // get to that source
            for (int row = 0; row < NetworkSimulator.NUMENTITIES; row++) {

                if (distanceTable[row][col] < distanceTable[id][col]) {
                    int tmp = distanceTable[row][col] + distanceTable[id][row];

                    if (tmp < distanceTable[id][col]) {
                        distanceTable[id][col] = tmp;
                        distanceTable[col][id] = tmp;
                    }
                }
            }
        }
    }

    protected int[] extractPayload(Packet p){
        int[] payload = new int[NetworkSimulator.NUMENTITIES];

        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++) {

            payload[i] = p.getMincost(i);

        }

        return payload;
    }

    protected void addNewPaths(int source, int[] paths){
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
            distanceTable[source][i] = paths[i];
            distanceTable[i][source] = paths[i];
        }

        updateTable();
        transmit();
    }

    public void transmitTable(int src, int dest, int[] row) {
        Packet packet = new Packet(src, dest, row);
        NetworkSimulator.toLayer2(packet);
    }

    public abstract void transmit();

}
