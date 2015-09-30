import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by trd6577 on 6/4/15
 * File: ACLStatement.java
 * @author Thomas Daniels (trd6577@g.rit.edu)
 * Purpose: Serves as a class to hold a single ACL statement. This includes states such as networks, the wildcard,
 * and binary translations for the networks and the wildcards.
 */

public class ACLStatement {

    // Fields
    private Set<Network> coveredNetworks = new HashSet<>();
    private int numCoveredNetworks = 0;
    private Network originalNetwork;
    private final String wildcardBinary;
    private final String wildcard;
    private final int numberBitsChangedInWildcard;
    private String originalNetworkBinary;
    private Set<Network> deniedNetworks = new HashSet<>();


    // Constructor
    public ACLStatement(Network originalNetwork, String wildcardBinary){
        this.originalNetwork = originalNetwork;
        this.wildcardBinary = wildcardBinary;
        this.numberBitsChangedInWildcard = calculateNumberOnesInWildcard();
        this.wildcard = toNumerical(this.wildcardBinary);
        this.addNetwork(this.originalNetwork);
        this.originalNetworkBinary = this.originalNetwork.getBinaryString().replaceAll(Pattern.quote("."), "").substring(24);

    }

    // Methods
    private void addNetwork(Network n){
        if(coveredNetworks.contains(n))
            return;
        coveredNetworks.add(n);
        numCoveredNetworks += 1;
    }

    private int calculateNumberOnesInWildcard(){
        int counter = 0;
        for(int i = 0; i < 8; i++){
            if(Integer.parseInt(Character.toString(wildcardBinary.charAt(i))) == 1){
                counter++;
            }
        }
        return counter;
    }

    private String toNumerical(String bite){
        int octet = 0;
        int bit = 128;
        for (int i = 0; i < 8; i++){
            if(Integer.parseInt(Character.toString(bite.charAt(i))) == 1){
                octet += bit;
            }
            bit=bit/2;
        }

        return Integer.toString(octet);

    }

    public boolean coversNetwork(Network n){
        String newNetworkBinary = n.getBinaryString().replaceAll(Pattern.quote("."), "").substring(24);

        for(int i = 0; i < 8; i++){
            if(Integer.parseInt(Character.toString(wildcardBinary.charAt(i))) == 1){
                continue;
            }
            if(Integer.parseInt(Character.toString(newNetworkBinary.charAt(i))) != Integer.parseInt(
                    Character.toString(originalNetworkBinary.charAt(i)))){
                return false;
            }
        }
        addNetwork(n);
        return true;
    }

    public String debugString(){
        return originalNetwork.getIp() + " 0.0.0." + wildcard + " [" + numCoveredNetworks + "] " + coveredNetworks;
    }

    public String toString(){
        String result = "";
        if(!deniedNetworks.isEmpty()){
            for (Network n : deniedNetworks){
                result += "deny " + n.getIp() + " 0.0.0.0\n";
            }
        }

        result += "permit " + originalNetwork.getIp() + " 0.0.0." + wildcard;

        return result;
    }

    public void wildcardCleanUp(){
        List<String> possibleNetworks = new LinkedList<>(Collections.singletonList(""));
        boolean contains = false;
        List<String> tempList = new LinkedList<>();
        int maxSize;
        for (int i = 0; i < wildcardBinary.length(); i++){
            if(Integer.parseInt(Character.toString(wildcardBinary.charAt(i))) == 1){
                maxSize = possibleNetworks.size();
                for (int counter = 0; counter < maxSize; counter++){
                    String temp = possibleNetworks.get(counter);
                    tempList.add(temp + "0");
                    tempList.add(temp + "1");
                }
                possibleNetworks.clear();
                possibleNetworks.addAll(tempList);
                tempList.clear();
            }
            else{
                for (int counter = 0; counter < possibleNetworks.size(); counter++){
                    String temp = possibleNetworks.get(counter);
                    temp += originalNetworkBinary.charAt(i);
                    possibleNetworks.set(counter, temp);
                }

            }
        }


        for (String possibleNetwork : possibleNetworks){
            for (Network network : coveredNetworks){
                if (network.getIp().split(Pattern.quote("."))[3].equals(toNumerical(possibleNetwork))){
                    contains = true;
                }
            }
            //TODO: Change it so that you're not arbitrarily giving an ip of 192.168.0.XXX
            // At this point in the code, we no longer worry about subnets, therefore, I arbitrarily assigned the
            // new networks subnet values.
            if (!contains){
                deniedNetworks.add(new Network("192.168.0." + toNumerical(possibleNetwork), "255.255.255.0", false));
            }
            else{
                contains = false;
            }
        }

    }

    // Getters

    public Set<Network> getCoveredNetworks(){
        return this.coveredNetworks;
    }

    public int getNumberBitsChangedInWildcard(){
        return numberBitsChangedInWildcard;
    }

    public int getNumCoveredNetworks(){
        return numCoveredNetworks;
    }

} // End ACLStatement class
