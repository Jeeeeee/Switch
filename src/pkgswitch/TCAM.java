// This class is a simulation of a TCAM(Ternary Content Address Memory)

package pkgswitch;

import java.util.ArrayList;

public class TCAM {
    
    private final String NORMAL_MODE = "NORMAL_MODE";
    private final String BUCKET_MODE = "BUCKET_MODE";
    private final String INDEX_MODE = "INDEX_MODE";
    private String mode;
    public ArrayList<String> mem;
    public ArrayList<Integer> bucketSize;
    private int buckets;

    public TCAM(){
        this("NORMAL_MODE");
    }
 
    public TCAM(String m){
        mode = m;
        mem = new ArrayList<>();
        if(mode.equals(BUCKET_MODE)){
            bucketSize = new ArrayList<>();
            buckets = 0;
        }
    }
    
    // Search function for NORMAL TCAM mode
    public int search(String str){
        int result = -1;
        if(mode.equals(NORMAL_MODE) || mode.equals(INDEX_MODE)){
            int index = 0;
            ArrayList<Integer> res = new ArrayList<>();
            int oneCounter = 0;
            for(int i=0;i<mem.size();i++){
                if(str.startsWith(mem.get(i))){
                    res.add(1);
                    oneCounter++;
                }
                else    res.add(0);
            }
            if(oneCounter > 0)  result = getAddress(res);  
            else    System.out.println("CAUTION: No match found at TCAM![search(String) in TCAM class]");
        }
        else    System.out.println("ERROR: You've set the TCAM to BUCKET mode but using wrong search method! You must use 'search(String key, int bucketIndex)' method[search(String) in TCAM class]");
        return result;
    }
    
    // Search function for BUCKET TCAM mode
    public int search(String str, int bucketIdx){
        int result = -1;
        if(mode.equals(BUCKET_MODE)){
            int base = getBucketIndex(bucketIdx);
            ArrayList<Integer> res = new ArrayList<>();
            int oneCounter = 0;
            for(int i=base;i<base + bucketSize.get(bucketIdx);i++){
                if(str.startsWith(mem.get(i))){
                    res.add(1);
                    oneCounter++;
                }
                else    res.add(0);
            }
            if(oneCounter > 0)  result = getAddress(res, bucketIdx);  
            else    System.out.println("CAUTION: No match found at TCAM![search(String, int) in TCAM class]");
        }
        else    System.out.println("ERROR: You've set the TCAM to NORMAL mode but using wrong search method! You must use 'search(String key)' method[search(String, int) in TCAM class]");
        //System.out.println(result);
        return result; 
    }
    
    public void addToTCAM(String str){
        if(mode.equals(NORMAL_MODE) || mode.equals(INDEX_MODE)){
            int index = 0;
            while(true){
                if(index == mem.size()){
                    mem.add(index, str);
                    break;
                }
                if(str.length() >= mem.get(index).length())    index++;
                else{
                    mem.add(index, str);
                    break;
                }
            }
        }
        else    System.out.println("ERROR: You've set the TCAM to BUCKET mode but using wrong search method! You must use 'addToTCAM(String key, int bucketIndex)' method[addToTCAM(String) in TCAM class]");
        
    }
    
    public void addToTCAM(String str, int bIndex){
        if(mode.equals(BUCKET_MODE)){
            if(bIndex >= buckets)   System.out.println("ERROR: Out of bands index![addToTCAM(String str, int index) in TCAM class]");
            else{
                int idx, index;
                idx = index = getBucketIndex(bIndex);
                while(true){
                    if(idx == index + bucketSize.get(bIndex)){
                        mem.add(idx, str);
                        break;
                    }
                    if(str.length() >= mem.get(idx).length())   idx++;
                    else{
                        mem.add(idx, str);
                        break;
                    }
                }
                bucketSize.set(bIndex, bucketSize.get(bIndex) + 1);
            }
        }
        else    System.out.println("ERROR: You've set the TCAM to NORMAL mode but using wrong search method! You must use 'addToTCAM(String key, int bucketIndex)' method[addToTCAM(String) in TCAM class]");    
    }
    
    
    public void addBucket(){
        buckets++;
        bucketSize.add(0);
    }
    
    private int getBucketIndex(int bIndex){
        int index = 0;
        for(int i=0;i<bIndex;i++)   index += bucketSize.get(i);
        return index;
    }
    
    // getAddress method for NORMAL and INDEX mode of TCAM
    private int getAddress(ArrayList<Integer> searchResult){
       int address = mem.size() - 1;
        for(int i=address;i>=0;i--){
            if(searchResult.get(i) == 1){
                address = i;
                break;
            }
        }
        return address;
    }
    
    // getAddress method for BUCKET mode of TCAM
    private int getAddress(ArrayList<Integer> searchResult, int bIndex){
        int base = getBucketIndex(bIndex);  
        int address = -1;
        //int address = base + bucketSize.get(bIndex) - 1;
        for(int i=searchResult.size()-1;i>=0;i--){
            if(searchResult.get(i) == 1){
                address = i;
                break;
            }
        }
        address += base;
        return address;
    }
    
    public int getBucketsCount(){
        return buckets;
    }
}
