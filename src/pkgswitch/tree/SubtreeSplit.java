package pkgswitch.tree;

import java.util.ArrayList;
import pkgswitch.TCAM;

public class SubtreeSplit extends Tree {
    public TCAM indexTCAM;
    public TCAM TCAM;
    ArrayList<Integer> indexSRAM;
    private ArrayList<String> SRAM;
    
    public SubtreeSplit(){
        indexTCAM = new TCAM("INDEX_MODE");
        TCAM = new TCAM("BUCKET_MODE");
        indexSRAM = new ArrayList<>();
        SRAM = new ArrayList<>();
    }
    
    @Override
    public String searchIP(String ip){
        String port = "";
        int iSRAMIndex, bucketIndex, sramIndex;
        
        long time_1 = System.nanoTime();
        
        iSRAMIndex = indexTCAM.search(ip);
        bucketIndex = readIndexSRAM(iSRAMIndex);
        sramIndex = TCAM.search(ip, bucketIndex);
        if(sramIndex != -1) port = readSRAM(sramIndex);
        
        long time_2 = System.nanoTime();
        lookupDelay = time_2 - time_1;
        
        return port;
    }
    
    public void writeSRAM(String data){
        SRAM.add(data);
    }
    
    public void writeSRAM(String data, int index){
        if(index == SRAM.size())    writeSRAM(data);
        else if(index > SRAM.size()){
            for(int i=SRAM.size();i<index;i++)  writeSRAM("");
            writeSRAM(data);
        }
        else    SRAM.set(index, data);
    }
    
    public String readSRAM(int index){
        return SRAM.get(index);
    }
    
    public int getSRAMUsage(){
       return SRAM.size();
    }
    
    public void writeIndexSRAM(int data){
        indexSRAM.add(data);
    }
    
    public void writeIndexSRAM(int data, int index){
        if(index == indexSRAM.size())    writeIndexSRAM(data);
        else if(index > indexSRAM.size()){
            for(int i=indexSRAM.size();i<index;i++)  writeIndexSRAM(-1);
            writeIndexSRAM(data);
        }
        else    indexSRAM.set(index, data);
    }
    
    public Integer readIndexSRAM(int index){
        return indexSRAM.get(index);
    }
    
    public int getIndexSRAMUsage(){
        return indexSRAM.size();
    }
    
    @Override
    public int getStorageUsage() {
        return (indexTCAM.mem.size() + indexSRAM.size() + TCAM.mem.size() + SRAM.size());
    }

    @Override
    protected Node createNode(int childs) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void insertIP(String ip, int subnetMask, String port) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
