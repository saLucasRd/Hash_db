public class SearchResult {

    private final Entry entry;

    private final int cost;

    public SearchResult(Entry entry, int cost) {
        this.entry = entry;
        this.cost = cost;
    }

    public Entry getEntry() {
        return entry;
    }

    public int getCost() {
        return cost;
    }

    public boolean isFound() {
        return entry != null;
    }
}
