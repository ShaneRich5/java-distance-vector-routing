package main;

import java.util.Arrays;

public class Entity3 extends Entity
{
    // Perform any necessary initialization in the constructor
    public Entity3()
    {
        // initializes the entity with the appropriate id, initial cost and direct neighbours
        super(3, new int[] {7, INFINITY, 2, 0}, Arrays.asList(0, 2));
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    @Override
    public void update(Packet p)
    {
        int source = p.getSource();         // extracts source id (comparable to IP address)
        int[] payload = extractPayload(p);  // extracts packet payload

        /**
         * Terminated the method prematurely if not updates were made to the DV table.
         */
        if (isCostsEqual(source, payload)) {
            printDT();
            return;
        }

        // updates the table with the new path weights and
        // recalculates the path cost from this entity
        // to its direct neighbours.
        addNewPaths(source, payload);
    }

    @Override
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }

    @Override
    public void printDT()
    {
        System.out.println("         via");
        System.out.println(" D3 |   0   2");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 3)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j += 2)
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
