/*
 * 
 * Here is an implementation of packet switching in IP network routers!
 * User enters an ipv4 address and the system will lookup through the switching table to find the corresponding output to send the packet to.
 * System uses "1-bit Binary Trie", "Level Compression Trie", "Tree Bitmap" and "Subtree-Split Algorithm" for searching corresponding output
 *
 */
package pkgswitch;

/**
 *
 * @author je
 */
public class Switch extends javax.swing.JFrame {
    
    public static void main(String[] args) {
        Window window = new Window();
    }
    
}
