import java.util.List;

public class Searcher {
    
    public static SearchResult searchWithTableScan(String key, List<List<String>> pages) {
        int pagesRead = 0;
        
        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            List<String> currentPage = pages.get(pageIndex);
            pagesRead++;
            
            for (String word : currentPage) {
                if (word.equals(key)) {
                    Entry foundEntry = new Entry(key, pageIndex);
                    return new SearchResult(foundEntry, pagesRead);
                }
            }
        }
        return new SearchResult(null, pagesRead);
    }
}
