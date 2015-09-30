/**
 * Created by trd6577 on 6/2/15
 * @author Thomas Daniels (trd6577@g.rit.edu)
 * Purpose: This java program turns a list of IP networks into the fewest number
 * of rules possible in an access list. For example, if the networks 192.168.1.0,
 * 192.168.2.0, and 192.168.3.0 were all entered (assuming a subnet mask of
 * 255.255.255.0 for all of the aforementioned networks), then the resulting rule
 * should be deny 192.168.3.0 0.0.3.0
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Purpose: Main class to run and make the access lists
 */
public class Main {

    public static void main(String[] args) {

        // Local fields
        Scanner in = new Scanner(System.in); // Used for getting information from the user
        String[] ipAndSubnet; // Used to send the IP and subnet to the Network class to create an object
        String nextLine; // Used for error checking
        List<Network> networks = new LinkedList<>();
        AccessListCreator acl;
        List<ACLStatement> wildcardResult = new LinkedList<>();

        // Get input from user
        while(true){
            System.out.print("Enter a network to calculate the access list or press 'Enter' to exit: ");

            // Check input
            nextLine = in.nextLine();

            // If entered line is escape sequence, the user is done giving IPs
            if(nextLine.equals(""))
                break;

            // If the entered line does not contain only numbers, periods, and spaces or if the users has entered too
            // many items, inform the user and allow them to retry
            if(!(nextLine.matches("[0-9.\\ ]+")) || nextLine.split(" ").length != 2){
                System.err.println("INVALID CHARACTER OR SPACING");
                continue;
            }

            ipAndSubnet = nextLine.split(" ");

            // Try make a new Network object. If it fails, tell the user that their input was not valid
            try {
                networks.add(new Network(ipAndSubnet[0], ipAndSubnet[1], true));
            }
            catch (Exception e){
                System.err.println("NOT A VALID IP OR SUBNET");
            }
        }

        if(networks.size() == 0){
            System.err.println("No networks entered. Exiting program.");
            System.exit(0);
        }

        final long start = System.currentTimeMillis();

        acl = new AccessListCreator(networks);

        wildcardResult.addAll(acl.wildcardMaker());

       for(ACLStatement statement : wildcardResult){
           System.out.println(statement);
       }

        System.out.println("\n\nFinished in " + ((System.currentTimeMillis() - start) / 100.0) + " seconds.");

    } // End main method

} // End Main class
