package db.hash;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private Paginator paginator;
    private HashTable hashT;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Buscador com Hash Table");

        Label pageSizeLabel = new Label("Tamanho da página (registros por página):");
        TextField pageSizeField = new TextField("450");
        Button buildIndexButton = new Button("Construir Índice");
        Label searchLabel = new Label("Digite uma palavra para buscar:");
        TextField searchField = new TextField();

        Button searchWithIndexButton = new Button("Buscar (com Índice)");
        Button tableScanButton = new Button("Buscar (com Table Scan)");

        HBox searchButtonsBox = new HBox(10, searchWithIndexButton, tableScanButton);

        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPrefHeight(400);

        searchField.setDisable(true);
        searchWithIndexButton.setDisable(true);
        tableScanButton.setDisable(true);


        buildIndexButton.setOnAction(event -> {
            try {
                resultsArea.clear();
                int regPerPage = Integer.parseInt(pageSizeField.getText());
                final int bucketSize = 15; // FR (Tamanho do bucket)

                resultsArea.setText("Criando páginas a partir de 'words.txt'...\n");

                paginator = new Paginator();
                paginator.pageMaker("words.txt", regPerPage);
                int totalWords = paginator.getRecordCount();

                resultsArea.appendText("Número de páginas criadas: " + paginator.getPages().size() + "\n");

                displayFirstAndLastPages(resultsArea);

                // Cálculo otimizado de buckets
                final int qtBuckets = (int) ((totalWords / (double) bucketSize) / 0.75);
                resultsArea.appendText("\nConstruindo índice com " + qtBuckets + " buckets...\n");

                hashT = new HashTable(bucketSize, qtBuckets, totalWords);
                hashT.buildIndex(paginator.getPages());

                resultsArea.appendText("Índice construído com sucesso!\n\n");
                hashT.appendStatistics(resultsArea);

                // Habilita o campo de busca
                searchField.setDisable(false);

            } catch (NumberFormatException e) {
                resultsArea.setText("Erro: O tamanho da página deve ser um número válido.");
            } catch (Exception e) {
                resultsArea.setText("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace();
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean hasText = !newValue.trim().isEmpty();
            searchWithIndexButton.setDisable(!hasText);
            tableScanButton.setDisable(!hasText);
        });

        searchWithIndexButton.setOnAction(event -> {
            String keyToSearch = searchField.getText().trim();
            resultsArea.clear(); 

            long startTime = System.nanoTime();
            SearchResult indexResult = hashT.searchWithIndex(keyToSearch);
            long indexTime = System.nanoTime() - startTime;

            StringBuilder resultsBuilder = new StringBuilder();
            resultsBuilder.append("--- BUSCA COM ÍNDICE ---\n");
            if (indexResult.isFound()) {
                resultsBuilder.append("Palavra '").append(keyToSearch).append("' encontrada na página: ").append(indexResult.getEntry().pageNumber()).append("\n");
            } else {
                resultsBuilder.append("Palavra '").append(keyToSearch).append("' não encontrada.\n");
            }
            resultsBuilder.append("Custo (acessos a disco): ").append(indexResult.getCost()).append("\n");
            resultsBuilder.append("Tempo: ").append(indexTime).append(" nanossegundos.\n");

            resultsArea.setText(resultsBuilder.toString());
        });

        tableScanButton.setOnAction(event -> {
            String keyToSearch = searchField.getText().trim();
            resultsArea.clear(); 

            long startTime = System.nanoTime();
            SearchResult scanResult = Searcher.searchWithTableScan(keyToSearch, paginator.getPages());
            long scanTime = System.nanoTime() - startTime;

            StringBuilder resultsBuilder = new StringBuilder();
            resultsBuilder.append("--- BUSCA COM TABLE SCAN ---\n");
            if (scanResult.isFound()) {
                resultsBuilder.append("Palavra '").append(keyToSearch).append("' encontrada na página: ").append(scanResult.getEntry().pageNumber()).append("\n");
            } else {
                resultsBuilder.append("Palavra '").append(keyToSearch).append("' não encontrada.\n");
            }
            resultsBuilder.append("Custo (páginas lidas): ").append(scanResult.getCost()).append("\n");
            resultsBuilder.append("Tempo: ").append(scanTime).append(" nanossegundos.\n");

            resultsArea.setText(resultsBuilder.toString());
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                pageSizeLabel,
                pageSizeField,
                buildIndexButton,
                searchLabel,
                searchField,
                searchButtonsBox, 
                resultsArea
        );

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayFirstAndLastPages(TextArea resultsArea) {
        if (paginator == null || paginator.getPages().isEmpty()) {
            return;
        }

        List<String> firstPage = paginator.getPages().getFirst();
        resultsArea.appendText("\n--- Amostra da Primeira Página (Página 0) ---\n");
        resultsArea.appendText(getPageSample(firstPage)); 
        resultsArea.appendText("\n------------------------------------------\n");

        if (paginator.getPages().size() > 1) {
            List<String> lastPage = paginator.getPages().getLast();
            int lastPageIndex = paginator.getPages().size() - 1;
            resultsArea.appendText("\n--- Amostra da Última Página (Página " + lastPageIndex + ") ---\n");
            resultsArea.appendText(getPageSample(lastPage)); 
            resultsArea.appendText("\n------------------------------------------\n");
        }
    }

    private String getPageSample(List<String> page) {
        final int sampleSize = 7; 

        if (page.size() <= sampleSize * 2) {
            return String.join(", ", page);
        } else {
            String firstPart = page.stream().limit(sampleSize).collect(Collectors.joining(", "));

            String lastPart = page.stream().skip(page.size() - sampleSize).collect(Collectors.joining(", "));

            return firstPart + ",  ...  , " + lastPart;
        }
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}
