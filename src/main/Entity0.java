package main;

public class Entity0 extends Entity
{
    // Perform any necessary initialization in the constructor
    public Entity0()
    {
        super(0, new int[] {0, 1, 3, 7});
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    @Override
    public void update(Packet p)
    {
        int source = p.getSource();

        if (p.isCostsEqual(distanceTable[source])) {
            printDT();
            return;
        }

        addNewPaths(source, extractPayload(p));
    }

    @Override
    public void transmit() {
        transmitTable(id, 1, distanceTable[id]);
        transmitTable(id, 2, distanceTable[id]);
        transmitTable(id, 3, distanceTable[id]);
    }

    @Override
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }

    @Override
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D0 |   1   2   3");
        System.out.println("----+------------");
        for (int i = 1; i < NetworkSimulator.NUMENTITIES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
