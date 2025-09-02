import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Registro por pagina");
        final int regPerPage = sc.nextInt();
        
        System.out.println("quantidade de buckets");
        final int qtBuckts = sc.nextInt();

        System.out.println("tamanho do bucket");
        final int bucketSize = sc.nextInt();
        
        Paginator paginator = new Paginator();
        paginator.pageMaker("words.txt", regPerPage);
        paginator.pageVisualizer();

        HashTable hashT = new HashTable(bucketSize, qtBuckts);
        hashT.buildIndex(paginator.getPages());
    }
}
