import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTable {
    
    private final Map<Integer, List<Entry>> buckets;
    private final int bucketSize;
    private final int qBucket;
    
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
    
    private int overflowCounter = 0;
    private int collisionCounter = 0;

    public void buildIndex(List<List<String>> pages) {
        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            List<String> currentPage = pages.get(pageIndex);

            for (String word : currentPage) {
                int bucketIndex = hashFun(word);
                
                if (buckets.containsKey(bucketIndex)) {
                    collisionCounter++;
                }

                List<Entry> bucket = buckets.computeIfAbsent(bucketIndex, _ -> new ArrayList<>());

                if (bucket.size() >= bucketSize) { 
                    overflowCounter++;
                }

                Entry newEntry = new Entry(word, pageIndex);
                bucket.add(newEntry);
            }
        }
        System.out.println("qtd de colisoes " + collisionCounter);
        System.out.println("qtd de overflows " + overflowCounter);
    }
    
}
