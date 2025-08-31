import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTable {
    
    private Map<Integer, List<String>> buckets;
    private int bucketSize;
    private int qBucket;
    
    public HashTable(int bucketSize, int qBucket) {
       this.bucketSize = bucketSize; 
       this.qBucket = qBucket; 
       this.buckets = new HashMap<>(); 
    } 
    
    // big number goes brrrr
    private int hashFun(String key) {
        int hashPos = Math.abs(key.hashCode());
        return hashPos % qBucket; 
    }
    
    private void buildIndex(List<List<String>> pages) {
        for ( int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            List<String> currentPage = pages.get(pageIndex);
            
            for (String word : currentPage) {
                int bucketIndex = hashFun(word);
                List<String> bucket = buckets.get(bucketIndex);
                if (bucket == null) {
                    bucket.add();
                }
            }
        }
    }
    
}
