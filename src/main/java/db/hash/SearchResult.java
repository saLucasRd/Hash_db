package db.hash;

public record SearchResult(Entry entry, int cost) {
    
    public boolean isFound() {
        return entry != null;
    }
}
