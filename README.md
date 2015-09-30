# ACLMaker
Given a list of IPs and subnets, this program calculates the least number of lines required to write the ACL
Notes:
It currently only works for the last octect of the IPv4 address space
When entering in networks, enter the ip address followed by the subnet
The program assumes that the router has an implicit DENY ALL at the end of every list

Examples:

Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.2 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.4 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.16 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.42 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.94 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.124 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.206 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.46 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.86 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 
permit 192.168.0.42 0.0.0.4
permit 192.168.0.94 0.0.0.8
permit 192.168.0.2 0.0.0.0
permit 192.168.0.4 0.0.0.0
permit 192.168.0.16 0.0.0.0
permit 192.168.0.124 0.0.0.0
permit 192.168.0.206 0.0.0.0


Finished in 1.72 seconds.


Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.6 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.9 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.7 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.16 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.5 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.2 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.12 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.14 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 192.168.0.0 255.255.255.0
Enter a network to calculate the access list or press 'Enter' to exit: 
deny 192.168.0.4 0.0.0.0
permit 192.168.0.6 0.0.0.3
deny 192.168.0.18 0.0.0.0
permit 192.168.0.16 0.0.0.18
permit 192.168.0.12 0.0.0.2
permit 192.168.0.9 0.0.0.0


Finished in 1.79 seconds.
