// Graphical Codes of the Program (GUI-related codes)

package pkgswitch;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import pkgswitch.tree.*;

public class Window extends javax.swing.JFrame implements java.awt.event.ActionListener, java.awt.event.MouseListener, java.awt.event.KeyListener {
    
    // GUI 
    JPanel userPanel;
    JTextField ip1,ip2,ip3,ip4;
    JLabel p1,p2,p3,text,octetERR;
    JButton lookUp, chooseFile;
    javax.swing.JFrame fileChooser;
    JFileChooser fileChoose;
    JRadioButton bt, lc, tb, sl;
    
    // Control
    String[] algorithm = {"BinaryTree","LevelCompressionTrie","TreeBitmap","SubtreeSplit"};
    String mode = "";
    
    // Data
    BinaryTree tree;
    File ipFile;
    ArrayList<String> ipSet; 

    public Window(){
        tree = null;
        ipSet = new ArrayList<>();
        setLookandFeel();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(350,100,500,500);
        createPanel();
        createButtons();
        add(userPanel);
        createLayout();
        setVisible(true);
    }
    
    private void setLookandFeel(){
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
 
    private void createPanel(){
        userPanel = new JPanel();
        
        createTextFields();
        createOctetERRLabel();
        createRadioButtons();
        
        // Creating 'userPanel' Panel Border
        javax.swing.border.TitledBorder border = new javax.swing.border.TitledBorder("IP");
        border.setTitlePosition(javax.swing.border.TitledBorder.LEFT);
        userPanel.setBorder(border);
        
        GroupLayout layout = new GroupLayout(userPanel);
        layout.setHorizontalGroup(layout.createParallelGroup()
                                  .addComponent(text)
                                  .addGroup(layout.createSequentialGroup()
                                            .addComponent(ip1,GroupLayout.PREFERRED_SIZE,35,GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p1)
                                            .addComponent(ip2,GroupLayout.PREFERRED_SIZE,35,GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p2)
                                            .addComponent(ip3,GroupLayout.PREFERRED_SIZE,35,GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p3)
                                            .addComponent(ip4,GroupLayout.PREFERRED_SIZE,35,GroupLayout.PREFERRED_SIZE)
                                           )
                                            
                                  .addComponent(octetERR)
                                  .addGroup(layout.createSequentialGroup()
                                            .addComponent(bt)
                                            .addComponent(lc)
                                            .addComponent(tb)
                                            .addComponent(sl)
                                           )
                                  );
        layout.setVerticalGroup(layout.createSequentialGroup()
                                .addComponent(text)
                                .addGap(20)
                                .addGroup(layout.createParallelGroup()
                                          .addComponent(ip1,GroupLayout.PREFERRED_SIZE,25,GroupLayout.PREFERRED_SIZE)
                                          .addComponent(p1)
                                          .addComponent(ip2,GroupLayout.PREFERRED_SIZE,25,GroupLayout.PREFERRED_SIZE)
                                          .addComponent(p2)
                                          .addComponent(ip3,GroupLayout.PREFERRED_SIZE,25,GroupLayout.PREFERRED_SIZE)
                                          .addComponent(p3)
                                          .addComponent(ip4,GroupLayout.PREFERRED_SIZE,25,GroupLayout.PREFERRED_SIZE)
                                        )
                                .addComponent(octetERR)
                                .addGap(30)
                                .addGroup(layout.createParallelGroup()
                                          .addComponent(bt)
                                          .addComponent(lc)
                                          .addComponent(tb)
                                          .addComponent(sl)
                                         )
                                );
        layout.setAutoCreateContainerGaps(true);
        
        userPanel.setLayout(layout);
        
    }
    
    private void createTextFields(){
        String txt = "Enter an IPv4 address:";
        text = new JLabel(txt);
        ip1 = new JTextField("0");
        ip1.addMouseListener(this);
        ip1.addKeyListener(this);
        p1 = new JLabel(".");
        ip2 = new JTextField("0");
        ip2.addMouseListener(this);
        ip2.addKeyListener(this);
        p2 = new JLabel(".");
        ip3 = new JTextField("0");
        ip3.addMouseListener(this); 
        ip3.addKeyListener(this);
        p3 = new JLabel(".");
        ip4 = new JTextField("0");
        ip4.addMouseListener(this);
        ip4.addKeyListener(this);
    }
    
    private void createRadioButtons(){
        bt = new JRadioButton("Binary Tree");
        bt.addMouseListener(this);
        lc = new JRadioButton("Level Compression Trie");
        lc.addMouseListener(this);
        tb = new JRadioButton("Tree Bitmap");
        tb.addMouseListener(this);
        sl = new JRadioButton("Subtree Split");
        sl.addMouseListener(this);
    }
    
    private void createButtons(){
        lookUp = new JButton("Lookup");
        chooseFile = new JButton("Choose File");
        lookUp.addActionListener(this);
        chooseFile.addActionListener(this);
    }

    private void createOctetERRLabel(){
        octetERR = new JLabel("*ERROR: Not an octet![0-255]");
        octetERR.setForeground(Color.red);
        octetERR.setVisible(false);
    }
    
    private void checkFieldERR(JTextField text, KeyEvent e){
        int key = (int)e.getKeyChar();
        if((key < 48 || key > 57) && key != 8)    text.setEditable(false);
        else{
            text.setEditable(true);
            if(!(text.getText().equals("") && (int)(e.getKeyChar()) == 8)){
                int tmp = 0;
                String str = null;
                if((int)(e.getKeyChar()) != 8)   str = text.getText() + e.getKeyChar();
                else{
                    int len = text.getText().length();
                    str = text.getText().substring(0, len-1);
                }
                if(!str.equals("")) tmp = Integer.parseInt(str);
                if(tmp > 255) octetERR.setVisible(true);
                else{
                    if(octetERR.isVisible())    octetERR.setVisible(false);
                }
            }
        }
    }
    
    private String getBinaryIP(String ipSeg1, String ipSeg2, String ipSeg3, String ipSeg4){
        
        String tmp1 = Tools.getLargerBinary(Integer.toBinaryString(Integer.parseInt(ipSeg1)), 8);
        String tmp2 = Tools.getLargerBinary(Integer.toBinaryString(Integer.parseInt(ipSeg2)), 8);
        String tmp3 = Tools.getLargerBinary(Integer.toBinaryString(Integer.parseInt(ipSeg3)), 8);
        String tmp4 = Tools.getLargerBinary(Integer.toBinaryString(Integer.parseInt(ipSeg4)), 8);
        
        String ip = tmp1 + tmp2 + tmp3 + tmp4;
        
        return ip;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Begin Test ...
        //LevelCompressionTrie lc = new LevelCompressionTrie();
        // End Test.
        
        if(e.getSource().equals(lookUp)){
            // Check if user entered a correct IP format
            //......
            if(ipSet.isEmpty()){
                String err = "You have not selected any files! do these steps:\n1. create a file named IP_LIST.txt\n2. insert some ip into it\n3. browse the file by clicking the 'Choose File' button";
                JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                tree = new BinaryTree();
                for(int i=0;i<ipSet.size();i+=3){
                    tree.insertIP(ipSet.get(i), Integer.parseInt(ipSet.get(i+1)), ipSet.get(i+2));
                }       
                // ... BinaryTree has been created
                
                String lookupResult = "";
                long lookupDelay = 0, cDelay = 0;
                int encounteredNodes = -1, allNodes = 0;
                switch(mode){
                    
                    case "BinaryTree": lookupResult = tree.searchIP(getBinaryIP(ip1.getText(),ip2.getText(),ip3.getText(),ip4.getText()));
                                       lookupDelay = tree.lookupDelay();
                                       allNodes = tree.getStorageUsage();
                                       cDelay = tree.creationDelay();
                                       encounteredNodes = tree.passedNodes();
                                       break;
                        
                    case "LevelCompressionTrie": LevelCompressionTrie levelCompressionTrie = tree.getLevelCompressionTrie();
                                                 lookupResult = levelCompressionTrie.searchIP(getBinaryIP(ip1.getText(),ip2.getText(),ip3.getText(),ip4.getText()));
                                                 lookupDelay = levelCompressionTrie.lookupDelay();
                                                 allNodes = levelCompressionTrie.getStorageUsage();
                                                 cDelay = levelCompressionTrie.creationDelay();
                                                 encounteredNodes = levelCompressionTrie.passedNodes();
                                                 break;
                        
                    case "TreeBitmap": TreeBitmap treeBitmap = tree.getTreeBitmap();
                                       lookupResult = treeBitmap.searchIP(getBinaryIP(ip1.getText(),ip2.getText(),ip3.getText(),ip4.getText()));
                                       lookupDelay = treeBitmap.lookupDelay();
                                       allNodes = treeBitmap.getStorageUsage();
                                       cDelay = treeBitmap.creationDelay();
                                       encounteredNodes = treeBitmap.passedNodes();
                                       break;
                        
                    case "SubtreeSplit": SubtreeSplit subtreeSplit = tree.getSubtreeSplit();
                                         lookupResult = subtreeSplit.searchIP(getBinaryIP(ip1.getText(),ip2.getText(),ip3.getText(),ip4.getText()));
                                         lookupDelay = subtreeSplit.lookupDelay();
                                         cDelay = subtreeSplit.creationDelay();
                                         allNodes = subtreeSplit.getStorageUsage();
                                         break;
                        
                    default: String err = "You have not selected any lookup algorithms";
                             JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
                    
                }
                if(!mode.equals("")){
                    String message = "Lookup Result: " + (lookupResult.equals("")?"No match found! packet has dropped":"to Port " + lookupResult) + "\n" +
                                     "Elapsed Time: " + Integer.toString((int)lookupDelay) + " ns" + "\n" + 
                                     "Algorithm Memory Usage: " + allNodes + " nodes" + "\n" + 
                                     "Tree Creation Delay: " + Integer.toString((int)cDelay) + " ns" + "\n" +
                                     (encounteredNodes == -1?"":"Nodes Traversed Through Lookup: " + encounteredNodes + " nodes");
                    JOptionPane.showMessageDialog(this, message, "Lookup Done",JOptionPane.INFORMATION_MESSAGE);
                }
                
            }

        }
        if(e.getSource().equals(chooseFile)){
            fileChooser = new javax.swing.JFrame("Select ip file(IP_LIST.txt)");
            fileChoose = new JFileChooser();
            fileChooser.add(fileChoose);fileChoose.cancelSelection();
            java.awt.Component[] c = fileChoose.getComponents();
            fileChoose.addActionListener(this);
            fileChooser.pack();
            fileChooser.setVisible(true);
            
        }
        else if(e.getSource().equals(fileChoose)){
            ipFile = fileChoose.getSelectedFile();
            if(ipFile == null){
                String err = "You have selected no file\nYour filename must be \"IP_LIST.txt\"";
                JOptionPane.showMessageDialog(fileChooser, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(!ipFile.getName().equals("IP_LIST.txt")){
                String err = "Your filename must be \"IP_LIST.txt\"";
                JOptionPane.showMessageDialog(fileChooser, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                ipSet = parseIPs(ipFile);
                fileChooser.setVisible(false);
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {   
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getSource().equals(ip1)){
            checkFieldERR(ip1,e);
        }
        else if(e.getSource().equals(ip2)){
            checkFieldERR(ip2,e);
        }
        else if(e.getSource().equals(ip3)){
            checkFieldERR(ip3,e);
        }
        else if(e.getSource().equals(ip4)){
            checkFieldERR(ip4,e);
        } 
        else{
            
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource().equals(ip1)){
            if(ip1.getText().equals("0"))   ip1.setText(null);
        }
        else if(e.getSource().equals(ip2)){
            if(ip2.getText().equals("0"))   ip2.setText(null);
            
        }
        else if(e.getSource().equals(ip3)){
            if(ip3.getText().equals("0"))   ip3.setText(null);
            
        }
        else if(e.getSource().equals(ip4)){
            if(ip4.getText().equals("0"))   ip4.setText(null);
            
        }
        else if(e.getSource() instanceof JRadioButton){
            JRadioButton[] arr = new JRadioButton[4];
            arr[0] = bt;
            arr[1] = lc;
            arr[2] = tb;
            arr[3] = sl;
            for(int i=0;i<arr.length;i++){
                if(arr[i].equals(e.getSource())){
                    arr[i].setSelected(false);
                    mode = algorithm[i];
                }
                else    arr[i].setSelected(false);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    private void createLayout(){
        GroupLayout layout = new GroupLayout(this.getContentPane());
        
        layout.setHorizontalGroup(layout.createParallelGroup()
                                    .addComponent(userPanel,GroupLayout.PREFERRED_SIZE,472,GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(lookUp)
                                            .addComponent(chooseFile))
                                 );
        layout.setVerticalGroup(layout.createSequentialGroup()
                                .addComponent(userPanel,GroupLayout.PREFERRED_SIZE,400,GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(lookUp)
                                        .addComponent(chooseFile))
                                );
        
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        this.setLayout(layout);
    }
    
    private ArrayList<String> parseIPs(File file){
        ArrayList<String> ipList = new ArrayList<>();
        byte[] buffer = new byte[200];
        String[] ips = {"","",""}; 
        try {
            java.io.FileInputStream stream = new java.io.FileInputStream(file);
            boolean ipSection = false, endofFile = false, subMaskSection = false;
            String octet = "";
            String state = "00";    // "00": ip Fragment - "01": subnet mask Fragment - "10": port Fragment
            while(true){
                stream.read(buffer);
                if(endofFile)    break;
                for(byte b: buffer){
                    char c = (char)(int)b;
                    if(c == '*'){
                        endofFile = true;
                        break;
                    }
                    switch(state){
                        case "00": if(c == '.'){
                                       ips[0] += Tools.getLargerBinary(Integer.toString(Integer.parseInt(octet), 2), 8);
                                       octet = "";
                                    }
                                    else if(c == '/'){
                                        ips[0] += Tools.getLargerBinary(Integer.toString(Integer.parseInt(octet), 2), 8);
                                        octet = "";
                                        state = "01";       // Go to Next State  
                                    }
                                    else    octet += c;
                                    break;
                        case "01": if(c == ' ') state = "10";
                                   else ips[1] += c;
                                   break;
                            
                        case "10": if(c == 'p' || c == 'P') ips[2] = "P";
                                   else if(b == 13)   break;
                                   else if(b == 10){
                                       if(Integer.parseInt(ips[1]) < 0){
                                           String err = "one of your entered subnet mask is wrong!\nsubnet mask value should be within [0-32]\ntry another subnet mask in range";
                                           JOptionPane.showMessageDialog(fileChooser, err, "Error", JOptionPane.ERROR_MESSAGE);
                                       }
                                       ipList.add(ips[0]);
                                       ipList.add(ips[1]);
                                       ipList.add(ips[2]);
                                       ips[0] = ips[1] =  ips[2] = "";
                                       state = "00";
                                   }
                                   else    ips[2] += c;
                                   break;
                        
                    }
                }
            }
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ipList;
    }
    
}
