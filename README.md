# Switch

Here is an implementation of "Binary Trie", "Level Compression Trie", "TreeBitmap" and "Subtree Split" packet switching algorithms with a simple GUI. these terms are used in Computer Networking. 

Before using this program, some notes need to be considered for the program to be run properly:

1- Your ip list must to be stored in a text file named IP_LIST.txt

2- Your ip format must be like : octet.octet.octet.octet/[0-32] string
   * octet is a number in range [0-255]
   * each ip must be written in a new line
   * string represents output port. for instance: p8 or port8 or 8
   * setting subnet mask to zero(0), creates a default forwarding rule. a packet that does not match any other ports, is forwarded
     according to this rule.
   * your ip list must be ended with '*' character in a new line.

   An example for ip list: 
                           10.10.10.10/24 p1
                           198.162.10.12/16 p2
                           12.10.123.44/18 p3
                           *

|if you encountered any trouble, inform me|
--------------------------------------------------------------------------------------------------------------------------------------


