import java.util.IllegalFormatException;
import java.util.regex.Pattern;

/**
 * Created by trd6577 on 5/24/15
 * File: Network.java
 * @author Thomas Daniels (trd6577@g.rit.edu)
 * Purpose: Serves as a representation of a network. A network has an ip address, a subnet, and a binary equivalent
 */

public class Network {

    // Fields
    private String ip;
    private String subnet;
    private String[] binary;
    boolean allow;

    // Constructor
    public Network(String ipAddress, String subnetMask, boolean allow){
        this.ip = ipAddress;
        this.subnet = subnetMask;
        this.binary = toBinary();
        this.allow = allow;
    }

    private String[] toBinary() throws IllegalFormatException{
        String[] binaryOctets = new String[4];
        String[] numericalOctets = ip.split(Pattern.quote("."));

        /*
        TODO: Create an error checking mechanism that checks to make sure that the number is less than 256
         */

        for (int bite = 0; bite < 4; bite++){
            int num = Integer.parseInt(numericalOctets[bite]);
            String octet = "";
            for (int bit = 128; bit > 0; bit=bit/2){
                if(num - bit >= 0){
                    octet += "1";
                    num -= bit;
                }
                else{
                    octet += "0";
                }

            }
            binaryOctets[bite] = octet;
        }

        return binaryOctets;
    }

    public String toString(){
        String result = "IP Address: " + ip;
        result += " Subnet Mask: " + subnet;
        result += " Binary Equivalent: ";
        for (int i = 0; i < 4; i++){
            result += binary[i];
            if(i < 3){
                result += ".";
            }
        }

        return result;
    }

    // Getters

    public String getIp() {
        return ip;
    }

    public String getBinaryString() {
        String result = "";
        for (int i = 0; i < 4; i++){
            result += binary[i];
            if(i < 3){
                result += ".";
            }
        }
        return result;
    }

    public String[] getBinaryArray() {
        return binary;
    }

    public String getSubnet() {
        return subnet;
    }

} // End Network Class
