import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTable {
    
    private final Map<Integer, List<Entry>> buckets;
    private final List<Entry> overflowArea;
    private final int bucketSize;
    private final int qBucket;

    public HashTable(int bucketSize, int qBucket, int totalRecords) {
        this.bucketSize = bucketSize;
        this.qBucket = qBucket;
        this.buckets = new HashMap<>();
        this.overflowArea = new ArrayList<>();
    } 
    
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
                Entry newEntry = new Entry(word, pageIndex);
                
                if (bucket.size() < bucketSize) { 
                    bucket.add(newEntry);
                } else {
                    overflowCounter++;
                    overflowArea.add(newEntry);
                }
            }
        }
        System.out.println("qtd de colisoes " + collisionCounter);
        System.out.println("qtd de overflows " + overflowCounter);
        System.out.println("Total de itens na area de overflow: " + overflowArea.size());
    }
    
    public SearchResult searchWithIndex(String key) {
        int bucketIndex = hashFun(key);
        
        if (buckets.containsKey(bucketIndex)) {
            List<Entry> bucket = buckets.get(bucketIndex);
            
            for (Entry entry: bucket) {
                if (entry.word().equals(key)) {
                    
                    return new SearchResult(entry, 1);
                }
            }
        }
        
        // not found, search overflow
        for (Entry entry : overflowArea) {
            if (entry.word().equals(key)) {
                return new SearchResult(entry, 2);
            }
        }
        // not found anywere
        return new SearchResult(null, 0);
    }
    
}
