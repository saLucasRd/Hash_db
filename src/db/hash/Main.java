package db.hash;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite o tamanho da pagina (registros por pagina):");
        final int regPerPage = sc.nextInt();

        final int bucketSize = 10; 

        Paginator paginator = new Paginator();
        paginator.pageMaker("words.txt", regPerPage);
        int totalWords = paginator.getRecordCount(); 
        System.out.println("Numero total de registros encontrado: " + totalWords);

        final int qtBuckets = (totalWords / bucketSize) + 1; 
        System.out.println("Numero de buckets calculado: " + qtBuckets);

        System.out.println("Construindo indice\n");
        HashTable hashT = new HashTable(bucketSize, qtBuckets, totalWords);
        hashT.buildIndex(paginator.getPages());

        hashT.printStatistics();
        System.out.println("------------------------------------------\n");

        System.out.print("Digite uma palavra para buscar ('q' para sair): ");
        String keyToSearch = sc.nextLine();


        while (!keyToSearch.equalsIgnoreCase("sair")) {
            long startTime = System.nanoTime();
            SearchResult indexResult = hashT.searchWithIndex(keyToSearch);
            long indexTime = System.nanoTime() - startTime;

            System.out.println("\n--- BUSCA COM ÍNDICE ---");
            if (indexResult.isFound()) {
                System.out.println("Palavra encontrada na pagina: " + indexResult.getEntry().pageNumber());
            } else {
                System.out.println("Palavra nao encontrada.");
            }
            System.out.println("Custo (acessos logicos): " + indexResult.getCost());
            System.out.println("Tempo: " + indexTime + " nanossegundos.");

            startTime = System.nanoTime();
            SearchResult scanResult = Searcher.searchWithTableScan(keyToSearch, paginator.getPages());
            long scanTime = System.nanoTime() - startTime;

            System.out.println("\n--- BUSCA COM TABLE SCAN ---");
            if (scanResult.isFound()) {
                System.out.println("Palavra encontrada na pagina: " + scanResult.getEntry().pageNumber());
            } else {
                System.out.println("Palavra nao encontrada.");
            }
            System.out.println("Custo (paginas lidas): " + scanResult.getCost());
            System.out.println("Tempo: " + scanTime + " nanossegundos.");

            System.out.println("\n--- ESTATÍSTICAS GERAIS DO ÍNDICE ---");
            System.out.printf("Taxa de Colisao: %.2f%%\n", hashT.getCollisionRate());
            System.out.printf("Taxa de Overflow: %.2f%%\n", hashT.getOverflowRate());
            System.out.printf("Diferenca de tempo %d nanossegundos.\n", (scanTime - indexTime));

            System.out.print("\nDigite outra palavra para buscar ('q'): ");
            keyToSearch = sc.nextLine();
        }
    }
}
