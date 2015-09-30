/**
 * File: AccessListCreator.java
 * @author Thomas Daniels (trd6577@g.rit.edu)
 * Purpose: Serves as the main brain for making the most efficent ACL. AccessListCreator is a class made up of
 * access lists and it's job is simply to hold all of the ACLStatements entered and to calculate the most effective
 * ACL. (In this case "most effective" is defined as the least number of lines to write)
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccessListCreator {

    // Fields
    private List<Network> networks = new LinkedList<>(); // A list of networks to hold
    private Map<Integer, Integer> statementEfficiency = new HashMap<>(); // An efficiency chart

    // A pre-calculated list made by Thomas Daniels. This list helps define the least number of commands
    public AccessListCreator(List<Network> networks){
        this.networks = networks;
        statementEfficiency.put(4, 1);
        statementEfficiency.put(8, 1);
        statementEfficiency.put(16, 2);
        statementEfficiency.put(32, 2);
        statementEfficiency.put(64, 3);
        statementEfficiency.put(128, 4);
    }

    /**
     * Purpose: The best statement is defined as the statement that covers at least
     *          2^(n-1)+1 statements AND has the highest (permittedNetworks/allIncludedNetworks)
     *          ratio.
     * @return (LinkedList) A list containing the most efficient statements (least number of lines)
     */
    public List<ACLStatement> wildcardMaker() {

        List<ACLStatement> finalList = new LinkedList<>();

        while (networks.size() != 0) {

            ACLStatement statement; // Holds the current ACL statement to see if it is the most efficient
            List<ACLStatement> bestStatementList = new LinkedList<>(); // The most efficient ACL statement
            boolean contains;
            boolean lessStatements;// than permit all
            boolean isEmpty; // list is empty
            boolean coversMoreNetworks;
            double newRatio; // includedNetworks / total covered networks
            double currentBestRatio; // includedNetworks / total covered networks
            boolean a; // A boolean to help shorten the lengthy if statements

            // For each ACL statement in the final list of statements, remove all of the networks from the list of
            // networks that have already been covered by an ACL
            for (ACLStatement acl : finalList) {
                acl.getCoveredNetworks().stream().filter(networks::contains).forEach(networks::remove);
            }

            for (Network network : networks) {
                // Loop through every possible wildcard
                for (int firstBit = 0; firstBit <= 1; ++firstBit) {
                    String wildcard = "";
                    wildcard += firstBit;
                    for (int secondBit = 0; secondBit <= 1; ++secondBit) {
                        wildcard = wildcard.substring(0, 1);
                        wildcard += secondBit;
                        for (int thirdBit = 0; thirdBit <= 1; ++thirdBit) {
                            wildcard = wildcard.substring(0, 2);
                            wildcard += thirdBit;
                            for (int fourthBit = 0; fourthBit <= 1; ++fourthBit) {
                                wildcard = wildcard.substring(0, 3);
                                wildcard += fourthBit;
                                for (int fifthBit = 0; fifthBit <= 1; ++fifthBit) {
                                    wildcard = wildcard.substring(0, 4);
                                    wildcard += fifthBit;
                                    for (int sixthBit = 0; sixthBit <= 1; ++sixthBit) {
                                        wildcard = wildcard.substring(0, 5);
                                        wildcard += sixthBit;
                                        for (int seventhBit = 0; seventhBit <= 1; ++seventhBit) {
                                            wildcard = wildcard.substring(0, 6);
                                            wildcard += seventhBit;
                                            for (int eighthBit = 0; eighthBit <= 1; ++eighthBit) {
                                                wildcard = wildcard.substring(0, 7);
                                                wildcard += eighthBit;
                                                statement = new ACLStatement(network, wildcard);

                                                networks.forEach(statement::coversNetwork);

                                                // Reset values for better readability in if statements
                                                isEmpty = bestStatementList.size() == 0;
                                                lessStatements = (Math.pow(2, statement.getNumberBitsChangedInWildcard() - 1) + 1 <= statement.getNumCoveredNetworks());
                                                newRatio = statement.getNumCoveredNetworks() / (Math.pow(2, statement.getNumberBitsChangedInWildcard()));
                                                a = statementEfficiency.get((int) Math.pow(2, statement.getNumberBitsChangedInWildcard())) != null && (int) (Math.pow(2, statement.getNumberBitsChangedInWildcard()) - statement.getNumCoveredNetworks()) <= statementEfficiency.get((int) Math.pow(2, statement.getNumberBitsChangedInWildcard()));
                                                if (!isEmpty) {
                                                    coversMoreNetworks = statement.getNumCoveredNetworks() > bestStatementList.get(0).getNumCoveredNetworks();
                                                    currentBestRatio = bestStatementList.get(0).getNumCoveredNetworks() / (Math.pow(2, bestStatementList.get(0).getNumberBitsChangedInWildcard()));
                                                } else {
                                                    coversMoreNetworks = true;
                                                    currentBestRatio = 0;
                                                }

                                                // If the new statement is the best one seen thus far, clear the list
                                                // of best statements and add the new one in
                                                if (lessStatements && (isEmpty || (((newRatio >= currentBestRatio) || a) && coversMoreNetworks))) {
                                                    bestStatementList.clear();
                                                    bestStatementList.add(statement);

                                                    // If the new statement is as good as the current best, add it to the
                                                    // list of best statements
                                                } else if (lessStatements && (newRatio == currentBestRatio) &&
                                                        statement.getNumCoveredNetworks() == bestStatementList.get(0).getNumCoveredNetworks()) {
                                                    contains = false;

                                                    // Make sure the new statement doesn't cover any networks that
                                                    // have already been covered by previous statements
                                                    for (ACLStatement acls : bestStatementList) {
                                                        for (Network n : statement.getCoveredNetworks()) {
                                                            if (acls.coversNetwork(n)) {
                                                                contains = true;
                                                            }
                                                        }
                                                    }
                                                    // If no overlap occurred, add the statement
                                                    // to the list of best statements.
                                                    if (!contains) {
                                                        bestStatementList.add(statement);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // If there was no best statement, then explicitly permit the rest of the networks
            if (bestStatementList.size() == 0) {
                bestStatementList.addAll(networks.stream().map(network -> new ACLStatement(network, "00000000")).collect(Collectors.toList()));
            }

            // Add the appropriate deny statements to the ACLs so that the wildcards are accurate
            bestStatementList.forEach(ACLStatement::wildcardCleanUp);
            finalList.addAll(bestStatementList);
        }

        return finalList;
    }

} // End AccessListCreator class
