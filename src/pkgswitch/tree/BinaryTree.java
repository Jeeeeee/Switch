package pkgswitch.tree;

import pkgswitch.Tools;

public class BinaryTree extends Tree{

    public BinaryTree(){
        root = createNode(2);
    }
    
    public BinaryTree(Node r){
        root = r;
    }
    
    @Override
    protected Node createNode(int childs){
        Node  n = null;
        if(childs != 2) System.out.println("ERROR: BinaryTree nodes must have 2 childs![createNode(2)]");
        else{
            n = new Node();
            n.addChild(null);           // Left Child
            n.addChild(null);           // Right Child
        }
        return n;
    }
    
    @Override
    public void insertIP(String ip, int subnetMask, String port) {
        
        long time_1 = System.nanoTime();
        
        Node ptr = root;
        char[] ipChar = ip.toCharArray();
        boolean isOutput = false;
        if(subnetMask == 0)    ptr.setOutput(port);
        for(int i=0;i<subnetMask;i++){
            if(ipChar[i] == '0'){
                if(ptr.getChild(0) == null) insertNode(createNode(2),ptr,0);
                ptr = ptr.getChild(0);
            }
            else{
                if(ptr.getChild(1) == null)    insertNode(createNode(2),ptr,1);
                ptr = ptr.getChild(1);
            }
            
            if(i == subnetMask - 1){
                ptr.setOutput(port);
            }
       }
        
        long time_2 = System.nanoTime();
        this.creationDelay = time_2 - time_1;
    }
    
    @Override
    public String searchIP(String ip){
        String ipSeg, port = "";
        int index, idx = 0;
        Node ptr = root;
        passedNodes = 1;
        
        long time_1 = System.nanoTime();
        
        while(true){
            if(ptr.skip > 0){
                ipSeg = ip.substring(idx,idx+ptr.skip);
                if(ipSeg.equals(ptr.segment)){
                    idx += ptr.skip;
                }
                else    break;
            } 
            if(ptr.isOutput())  port = ptr.getOutputPort();
            if(ptr.isLeaf())    break;
            ipSeg = ip.substring(idx,idx+ptr.stride);
            index = Integer.parseInt(ipSeg, 2);
            if(ptr.getChild(index) == null) break;
            ptr = ptr.getChild(index);
            passedNodes++;
            idx += ptr.stride;
        }
        
        long time_2 = System.nanoTime();
        lookupDelay = time_2 - time_1;
        
        return port;
    }
    
    protected String[] getCoveringPrefix(Node n){
        String res[] = {"",""};
        if(contains(n)){
            Node ptr = n;
            String idx = "";
            int indx = 0;
            while(!ptr.equals(this.root)){
                idx += Integer.toString(ptr.getIndex());
                ptr = ptr.parent;
            }
            idx = Tools.reverseString(idx);
            while(true){
                if(ptr.isOutput()){
                    if(ptr.equals(this.root))     res[0] = "";
                    else    res[0] = idx.substring(0, indx);
                    res[1] = ptr.getOutputPort();
                }
                if(ptr.equals(n))   break;
                ptr = ptr.getChild(Integer.parseInt(idx.substring(indx, indx+1)));
                indx++;
            }
        }
        else    System.out.println("ERROR: Input node is not associated with the original tree! [getCoveringPrefix(Node) in BinaryTree class]");
        return res;
    }
    
    @Override
    public int getStorageUsage() {
        int counter = 1;
        TreePointer p = new TreePointer(this);
        int lastLvl = p.getLastLevel();
        while(true){
            while(true){
                if(p.getPtr() == p.getLastNode())    break;
                else{
                    p.goToNextNode();
                    counter++;
                }
            }
            if(p.getCurrentLevel() == lastLvl) break;
            p.goToNextLevel();
            counter++;
        }
        return counter;
    }
    
    
    // LevelCompressionTrie related functions
 
    public LevelCompressionTrie getLevelCompressionTrie(){
        
        long time_1 = System.nanoTime();
        
        LevelCompressionTrie lc;
        TreePointer tp = new TreePointer(this);
        while(true){
            this.doPathCompression(tp.getPtr());
            this.doLevelCompression(tp.getPtr());
            if(tp.getPtr().equals(tp.getLastNode()) && !tp.isLastLevel())    tp.goToNextLevel();
            else if(tp.getPtr().equals(tp.getLastNode()) && tp.isLastLevel())  break;
            else    tp.goToNextNode();
        }
        lc = new LevelCompressionTrie(this.root);
        
        long Time_2 = System.nanoTime();
        lc.creationDelay = Time_2 - time_1;
        
        return lc;
    }
 
    public int getStride(Node n){
        BinaryTree t = new BinaryTree(n);
        TreePointer lp = new TreePointer(t);
        int lvl = 0;
        while(lp.getPtr().getChild(0) != null && lp.getPtr().getChild(1) != null && lvl != 4){
            if(lvl != 0 && lp.getPtr().isOutput()) break;  
            if(lp.getLastNode().equals(lp.getPtr())){
                lp.goToNextLevel();
                lvl++;
            }
            else    lp.goToNextNode();
        }
        if(lvl > 1) n.stride = lvl;
        return n.stride;
    }
    
    private void doLevelCompression(Node n){
        int str = this.getStride(n);
        if(str > 1){
            BinaryTree bt = new BinaryTree(n);
            TreePointer tp = new TreePointer(bt);
            for(int i=0;i<str;i++){
                tp.goToNextLevel();
            }
            int childs = convertStride(str);
            Node[] c = new Node[childs];
            for(int i=0;i<childs;i++){
                c[i] = tp.getPtr();
                if(!tp.getPtr().equals(tp.getLastNode()))   tp.goToNextNode();
            }
            for(int i=0;i<childs;i++){
                n.setChild(c[i], i);
                c[i].parent = n;
            }
        }
    }
    
    // If no path could be compressed, the method returns null
    private void doPathCompression(Node n){
        Node p = n;
        int lvl = 0;
        String seg = "";
        while(p.getChildsCount() == 1){
            if(p.getMostLeftChild().isOutput() && n.isOutput()) break;
            p = p.getMostLeftChild();                                  // or node.getMostRightChild()! Doesn't matter which method is being used since there's only one child 
            seg += Integer.toString(p.getIndex());
            lvl++;
            if(p.isOutput())    break;
        }
        if(lvl > 0){
            if(p.skip > 0)  n.segment = seg + p.segment;
            else    n.segment = seg;
            n.stride = p.stride;
            if(p.isOutput())    n.setOutput(p.getOutputPort());
            for(int i=0;i<p.getBranchesCount();i++){
                if(p.getChild(i) == null)   n.setChild(null, i);
                else{
                    n.setChild(p.getChild(i),i);
                    p.getChild(i).parent = n;
                }  
            }
        }
        n.skip = lvl;
    }
    
    
    // Tree Bitmap related functions
    
    public TreeBitmap getTreeBitmap(){
        
        long time_1 = System.nanoTime();
        
        SuperNode superNode = toSuperNode(this.root);
        TreeBitmap bitmapTree = new TreeBitmap(superNode);
        
        long time_2 = System.nanoTime();
        bitmapTree.creationDelay = time_2 - time_1;
        
        return bitmapTree;
    }
    
    private SuperNode toSuperNode(Node n){
        SuperNode superNode = new SuperNode();
        BinaryTree t = new BinaryTree(n);
        int cIndex = 0, lvl = 0, idx = 1;
        for(int i=0;i<7;i++){
            Node node = t.getNodeAt(lvl, idx);
            if(node != null && node.isOutput()){
                superNode.resultBitmap[i] = 1;
                superNode.result[i] = node.getOutputPort();
            } 
            if(i > 2 && !isLastSuperNode(n)){
                if(node == null)    cIndex += 2;
                else{
                    if(node.getChild(0) != null){
                        superNode.childBitmap[cIndex] = 1;
                        superNode.child[cIndex] = toSuperNode(node.getChild(0));
                    }
                    cIndex++;
                    if(node.getChild(1) != null){
                        superNode.childBitmap[cIndex] = 1;
                        superNode.child[cIndex] = toSuperNode(node.getChild(1));
                    }
                    cIndex++;
                }
            }
            if(i == 0 || i == 2){
                lvl++;
                idx = 1;
            }
            else    idx++;
        }
        return superNode;
    }

    private boolean isLastSuperNode(Node n){
        boolean isLast = false;
        int lvl = 0;
        BinaryTree t = new BinaryTree(n);
        TreePointer tp = new TreePointer(t);
        for(int i=0;i<3;i++){
            if(tp.goToNextLevel() == 1) lvl++;
        }
        if(lvl < 3) isLast = true;
        return isLast;
    }
    
    
    // SubTree Split related functions
    
    public SubtreeSplit getSubtreeSplit(){
        
        long time_1 = System.nanoTime();
        
        SubtreeSplit subtreeSplit = new SubtreeSplit();
        carveOut(subtreeSplit);
        
        long time_2 = System.nanoTime();
        subtreeSplit.creationDelay = time_2 - time_1;
        
        return subtreeSplit;
    }
    
    private void carveOut(SubtreeSplit s){
        Node n = this.root;
        String index = "";
        setNodesValue(this.root);
        while(true){
            if((n.prefixes >= 2 && n.prefixes <= 4) || (n.prefixes >= 1 && n.prefixes <= 4 && n.equals(this.root))){
                s.indexTCAM.addToTCAM(index);
                String[][] pr = getBucketPrefixes(n, index);
                s.TCAM.addBucket();
                int buckIndex = s.TCAM.getBucketsCount() - 1;
                int iSRAMIdx = s.indexTCAM.search(index);
                if(s.getIndexSRAMUsage() != 0 && iSRAMIdx < s.indexTCAM.mem.size() - 1){
                    for(int i=s.getIndexSRAMUsage() - 1;i>=iSRAMIdx;i--)    s.writeIndexSRAM(s.readIndexSRAM(i),i+1);
                }
                s.writeIndexSRAM(buckIndex,iSRAMIdx);
                for(int i=0;i<pr.length;i++)    s.TCAM.addToTCAM(pr[i][0], buckIndex);
                if(!n.equals(this.root)){
                    String[] coveringPrefix = getCoveringPrefix(n);
                    if(!coveringPrefix[1].equals("") && s.TCAM.search(coveringPrefix[0], buckIndex) == -1){
                        s.TCAM.addToTCAM(coveringPrefix[0], buckIndex);
                        s.writeSRAM(coveringPrefix[1], s.TCAM.search(coveringPrefix[0], buckIndex));
                    }
                    for(int i=0;i<pr.length;i++)    s.writeSRAM(pr[i][1], s.TCAM.search(pr[i][0], buckIndex));
                    n.parent.setChild(null, n.getIndex());
                    n.parent = null;
                    setNodesValue(this.root);
                    index = "";
                    n = this.root;
                }
                else{
                    if(this.root.isOutput() && s.TCAM.search("", buckIndex) == -1){
                        s.TCAM.addToTCAM("", buckIndex);
                        s.writeSRAM("", s.TCAM.search("", buckIndex));
                    }
                    for(int i=0;i<pr.length;i++)    s.writeSRAM(pr[i][1], s.TCAM.search(pr[i][0], buckIndex));
                    this.root = null;
                    break;
                }
            }
            else if((n.prefixes < 2)){
                n = n.parent.getChild(1);
                index = index.substring(0,index.length()-1);
                index += "1";
            }
            else{
                if(n.getChild(0) != null){
                    index += "0";
                    n = n.getChild(0);
                }
                else{
                    index += "1";
                    n = n.getChild(1);
                }
            }
        }
    }
    
    /* Returns input parameter(a node)'s value 
     * and furthermore it sets value for other nodes in the tree rooted at the input parameter
     */
    public int setNodesValue(Node n){
        n.prefixes = 0;
        if(n.getChild(0) != null && n.getChild(1) != null)  n.prefixes = setNodesValue(n.getChild(0)) + setNodesValue(n.getChild(1)); 
        else if(n.getChild(0) == null && n.getChild(1) != null)  n.prefixes = setNodesValue(n.getChild(1));
        else if(n.getChild(0) != null && n.getChild(1) == null)  n.prefixes = setNodesValue(n.getChild(0));
        if(n.isOutput())    n.prefixes++;
        return n.prefixes;
    }
    
    private int getBucketSize(Node n){
        BinaryTree t = new BinaryTree(n);
        TreePointer tp = new TreePointer(t);
        int counter = 0;
        while(true){
            if(tp.getPtr().isOutput())  counter++;
            if(tp.getPtr().equals(tp.getLastNode()) && !tp.isLastLevel())   tp.goToNextLevel();
            else if(!tp.getPtr().equals(tp.getLastNode()))  tp.goToNextNode();
            else    break;
        }
        return counter;
    }
    
    private String[][] getBucketPrefixes(Node n, String idx){
        final int BUCKET_SIZE = getBucketSize(n);
        String[][] prefixes = new String[BUCKET_SIZE][2];
        int index = 0;
        String p = idx;
        while(true){
            if(n.isOutput()){
                prefixes[index][0] = p;
                prefixes[index][1] = n.getOutputPort();
                index++;
                if(index == BUCKET_SIZE)    break;
            }
            //else{
                if(n.isLeaf()){ 
                    while(n.equals(n.parent.getMostRightChild())){
                        n = n.parent;
                        p = p.substring(0,p.length() - 1);
                    }
                    p = p.substring(0,p.length() - 1);
                    n = n.parent.getChild(1);
                    p += "1";
                }
                else if(n.getChild(0) != null){
                    n = n.getChild(0);
                    p += "0";
                }
                else{
                    n = n.getChild(1);
                    p += "1";
                }
            //}
        }
        return prefixes;
    }
    
    private int getPrefixesCount(Node n){
        BinaryTree bt = new BinaryTree(n);
        TreePointer tp = new TreePointer(bt);
        int counter = 0;
        while(true){
            if(tp.getPtr().isOutput())  counter++;
            if(tp.getPtr().equals(tp.getLastNode()) && !tp.isLastLevel())   tp.goToNextLevel();
            else if(!tp.getPtr().equals(tp.getLastNode())) tp.goToNextNode();
            else    break;
        }
        return counter;
    }    
    
}
