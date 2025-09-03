import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Registro por pagina (e.g., 1000):");
        final int regPerPage = sc.nextInt();

        System.out.println("Quantidade de buckets (e.g., 50000):");
        final int qtBuckets = sc.nextInt();

        System.out.println("Tamanho do bucket (e.g., 10):");
        final int bucketSize = sc.nextInt();
        sc.nextLine();

        System.out.println("\nConstruindo paginas e indice...");
        Paginator paginator = new Paginator();
        paginator.pageMaker("words.txt", regPerPage);

        int totalWords = paginator.getRecordCount();

        HashTable hashT = new HashTable(bucketSize, qtBuckets, totalWords);
        hashT.buildIndex(paginator.getPages());
        System.out.println("------------------------------------------");

        System.out.print("Digite uma palavra para buscar (ou 'sair' para terminar): ");
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

            System.out.print("\nDigite outra palavra para buscar (ou 'sair'): ");
            keyToSearch = sc.nextLine();
        }
    }
}
