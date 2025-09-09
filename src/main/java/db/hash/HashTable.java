package db.hash;

// package db.hash; // Mantenha seu pacote

import java.util.*;

import javafx.scene.control.TextArea;


public class HashTable {

    private Set<Integer> bucketsQueJaSofreramOverflow = new HashSet<>();
    private final Map<Integer, List<Entry>> buckets;
    private final List<Entry> overflowArea; 
    private final int bucketSize; 
    private final int qBucket;
    private final int totalRecords;

    private int overflowCounter = 0;
    private int collisionCounter = 0;

    public HashTable(int bucketSize, int qBucket, int totalRecords) {
        this.bucketSize = bucketSize;
        this.qBucket = qBucket;
        this.totalRecords = totalRecords;
        this.buckets = new HashMap<>();
        this.overflowArea = new ArrayList<>();
    }

    private int hashFun(String key) {
        return Math.abs(key.hashCode()) % qBucket;
    }
    
    
    public void buildIndex(List<List<String>> pages) {

        this.collisionCounter = 0;
        this.overflowCounter = 0;
        this.bucketsQueJaSofreramOverflow.clear();
        this.buckets.clear(); 
        this.overflowArea.clear();
        
        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            List<String> currentPage = pages.get(pageIndex);
            for (String word : currentPage) {
                int bucketIndex = hashFun(word);
                List<Entry> bucket = buckets.computeIfAbsent(bucketIndex, k -> new ArrayList<>());
                Entry newEntry = new Entry(word, pageIndex);

                if (bucket.size() < bucketSize) {
                    bucket.add(newEntry);
                } else {
                    overflowCounter++;

                    // LÓGICA DO PROFESSOR:
                    if (bucketsQueJaSofreramOverflow.add(bucketIndex)) {
                        collisionCounter++;
                    }

                    overflowArea.add(newEntry);
                }
            }
        }
    }

    public SearchResult searchWithIndex(String key) {
        int bucketIndex = hashFun(key);
        List<Entry> bucket = buckets.get(bucketIndex);

        if (bucket != null) {
            for (Entry entry : bucket) {
                if (entry.word().equals(key)) {
                    return new SearchResult(entry, 1);
                }
            }
        }

        for (Entry entry : overflowArea) {
            if (entry.word().equals(key)) {
                return new SearchResult(entry, 1);
            }
        }

        return new SearchResult(null, 0);
    }


    public void appendStatistics(TextArea textArea) {
        textArea.appendText("--- Estatísticas Finais do Índice ---\n");
        textArea.appendText("Total de Registros: " + this.totalRecords + "\n");
        textArea.appendText("Total de Colisões: " + this.collisionCounter + "\n");
        textArea.appendText("Total de Overflows: " + this.overflowCounter + "\n");
        textArea.appendText(String.format("Taxa de Colisão: %.2f%%\n", getCollisionRate()));
        textArea.appendText(String.format("Taxa de Overflow: %.2f%%\n", getOverflowRate()));
    }

    public double getCollisionRate() {
        if (totalRecords == 0) return 0.0;
        return ((double) this.collisionCounter / this.totalRecords) * 100;
    }

    public double getOverflowRate() {
        if (totalRecords == 0) return 0.0;
        return ((double) this.overflowCounter / this.totalRecords) * 100;
    }
}
