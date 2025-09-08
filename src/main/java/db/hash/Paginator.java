package db.hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Paginator {

    private final List<List<String>> pages = new ArrayList<>();
    private int recordCount = 0;

    public List<List<String>> getPages() {
        return pages;
    }

    public int getRecordCount() {
        return this.recordCount;
    }

    public void pageMaker(String file, int regPerPag) {
        Path filePath = Path.of(file);

        this.recordCount = 0;

        this.pages.clear();

        List<String> currentPage = new ArrayList<>();

        try (Stream<String> lines = Files.lines(filePath)) {
            // Filtra linhas vazias para garantir uma contagem precisa.
            lines.filter(line -> line != null && !line.trim().isEmpty()).forEach(line -> {
                this.recordCount++;
                currentPage.add(line.trim()); 

                if (currentPage.size() == regPerPag) {
                    this.pages.add(new ArrayList<>(currentPage));
                    currentPage.clear();
                }
            });

            if (!currentPage.isEmpty()) {
                this.pages.add(new ArrayList<>(currentPage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pageVisualizer() {
        if (pages.isEmpty()) {
            System.out.println("Nenhuma página foi criada.");
            return;
        }
        List<String> firstPage = pages.getFirst();
        List<String> lastPage = pages.getLast();

        System.out.println("---- Primeira Página ----");
        for (String word : firstPage) {
            System.out.println(word);
        }
        System.out.println("\n---- Última Página ----");
        for (String word : lastPage) {
            System.out.println(word);
        }
        System.out.println("Total de palavras na última página: " + lastPage.size());
    }
}
