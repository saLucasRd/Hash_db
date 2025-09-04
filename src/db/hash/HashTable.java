package db.hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTable {
    
    private final Map<Integer, List<Entry>> buckets;
    private final List<Entry> overflowArea;
    private final int bucketSize;
    private final int qBucket;
    private final int totalRecords;

    public HashTable(int bucketSize, int qBucket, int totalRecords) {
        this.bucketSize = bucketSize;
        this.qBucket = qBucket;
        this.totalRecords = totalRecords;
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

    public void printStatistics() {
        System.out.println("--- Estatisticas Finais do Indice ---");
        System.out.println("Total de Registros: " + this.totalRecords);
        System.out.println("Total de Colisoes: " + this.collisionCounter);
        System.out.println("Total de Overflows: " + this.overflowCounter);

        double collisionRate = ((double) this.collisionCounter / this.totalRecords) * 100;
        double overflowRate = ((double) this.overflowCounter / this.totalRecords) * 100;

        System.out.printf("Taxa de Colisao: %.2f%%\n", collisionRate);
        System.out.printf("Taxa de Overflow: %.2f%%\n", overflowRate);
    }

    public double getCollisionRate() {
        if (totalRecords == 0) {
            return 0.0;
        }
        return ((double) this.collisionCounter / this.totalRecords) * 100;
    }

    public double getOverflowRate() {
        if (totalRecords == 0) {
            return 0.0;
        }
        return ((double) this.overflowCounter / this.totalRecords) * 100;
    }
    
}
