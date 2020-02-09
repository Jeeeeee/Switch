package pkgswitch.tree;

public class LevelCompressionTrie extends Tree {

    public LevelCompressionTrie(int str, int skp, String seg){
        root = createNode(convertStride(str));
        root.skip = skp;
        root.segment = seg;
    }
    
    // This constructor can be used for partial-tree s
    public LevelCompressionTrie(Node r){
        root = r;
    }
    
    @Override
    protected Node createNode(int childs){
        Node n = new Node();
        n.stride = toStride(childs);
        for(int i=0;i<childs;i++)   n.addChild(null);
        return n;
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
            idx += ptr.stride;
            index = Integer.parseInt(ipSeg, 2);
            if(ptr.getChild(index) == null) break;
            ptr = ptr.getChild(index);
            passedNodes++;
            
        }
        
        long time_2 = System.nanoTime();
        lookupDelay = time_2 - time_1;
        
        return port;
    }
    
    @Override
    public void insertIP(String ip, int subnetMask, String port) {
        throw new UnsupportedOperationException("Not Supported Yet.");
    }
    
    private int toStride(int childs){
        int counter = 0;
        if(childs == 1) counter = 1;
        else{
            int exp = 1;
            while(exp != childs){
                exp *= 2;
                counter++;
            }
        }
        return counter;
    }
    
}
