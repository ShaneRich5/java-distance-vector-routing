package main;

import java.util.List;

public abstract class Entity
{
    /**
     * Represents a path with an infinite cost
     */
    protected static final int INFINITY = 999;

    /**
     *
     */
    protected final int id;

    /**
     *
     */
    protected List<Integer> neighbours;

    // Each entity will have a distance table
    protected int[][] distanceTable = new int[NetworkSimulator.NUMENTITIES]
            [NetworkSimulator.NUMENTITIES];

    /**
     *
     * Constructs a new entity based on the parameters provided.
     *
     * @param newId             The ID that identity each entity on the
     *                          network. Analogous to an IP address.
     * @param initialDistances  The initial distance that corresponds the
     *                          cost associated with each path.
     * @param initialNeighbours Represents the entity numbers of directly
     *                          connected nodes.
     */
    public Entity(int newId, int[] initialDistances, List<Integer> initialNeighbours) {
        this.id = newId;
        this.neighbours = initialNeighbours;

        // initializes all path to the appropriate default cost
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
                if (i == id) {
                    distanceTable[i][j] = initialDistances[j];
                    distanceTable[j][i] = initialDistances[j];
                }
                else
                    distanceTable[i][j] = INFINITY;
            }

        // transmits the initial packet
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
     * Helper method used to update the weight of the paths for nodes
     * in the row that corresponds to this entity's id. This is done
     * by comparing
     *
     */
    protected void updateTable() {
        // for each column
        for (int col = 0; col < NetworkSimulator.NUMENTITIES; col++) {

            // checks to see if there is an easier way to get to that source
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

    /**
     * Helper method used to extract a packet's data
     * and return it as an array
     *
     * @param p     packet to extract data from
     * @return      the extracted data as a array
     */
    protected int[] extractPayload(Packet p){
        int[] payload = new int[NetworkSimulator.NUMENTITIES];

        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
            payload[i] = p.getMincost(i);

        return payload;
    }

    /**
     * A helper method used to up data the paths of the distance vector
     * tables for each entity.
     *
     * @param source    represents the id of the sender. Similar to the IP address.
     * @param paths     an array of path costs
     */
    protected void addNewPaths(int source, int[] paths){
        System.arraycopy(paths, 0, distanceTable[source], 0, NetworkSimulator.NUMENTITIES);

        //
        updateTable();
        transmit();
    }

    /**
     * Packages the packet with the entity's distance paths
     * and sends it to the layer below.
     *
     * @param src   Identifies the sending entity
     * @param dest  Identifies the destination entity
     * @param row   The data to transmit to the neighbouring node
     */
    public void transmitTable(int src, int dest, int[] row) {
        Packet packet = new Packet(src, dest, row);
        NetworkSimulator.toLayer2(packet);
    }

    /**
     * Transmits the entity's DV row to all neighbouring packets
     */
    public void transmit() {
        for (int neighbour: neighbours) {
            transmitTable(id, neighbour, distanceTable[id]);
        }
    }

    /**
     * Helper method that determines whether the elements in the
     * path array is similar to the corresponding row of the
     * distance vector table.
     *
     * @param from  id from the entity from which the payload originated
     * @param paths the costs from the payload
     * @return      whether the cost has updated
     */
    protected boolean isCostsEqual(int from, int[] paths) {

        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
            if (paths[i] != distanceTable[from][i])
                return false;
        return true;
    }

}
