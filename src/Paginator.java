import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Paginator {

    // criando o "Livro e suas paginas"
    private List<List<String>> pages = new ArrayList<>();

    public void pageMaker(String file, int regPerPag) {
        Path filePath = Path.of(file);

        // pagina atual temporaria
        List<String> currentPage = new ArrayList<>();

        try (Stream<String> lines = Files.lines(filePath)) {

            lines.forEach(line -> {
                currentPage.add(line);


                if (currentPage.size() == regPerPag) {
                    this.pages.add(new ArrayList<>(currentPage));
                    // esvazia a pagina temporaria ja que terminou de preencher
                    currentPage.clear();
                }
            });

            // logica da ultima pagina, caso sobre espaco, termina mais cedo sem precisar preenche-la toda e utiliza a pagina "temporaria"
            if (!currentPage.isEmpty()) {
                this.pages.add(currentPage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pageVisualizer() {
        List<String> firstPage = pages.get(0);
        List<String> lastPage = pages.get(pages.size() - 1);
        int c = 0 ;
        
        System.out.println("---- First P ----");
        //System.out.println(pages.getFirst());
        for (String word : firstPage) {
            System.out.println(word);
        }
        System.out.println("---- Last P ----");
        //System.out.println(pages.getLast());
        for (String word : lastPage) {
            System.out.println(word);
            c++;
        }
        System.out.println(c);
    }
}
