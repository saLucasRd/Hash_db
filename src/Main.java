import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("bucket size");
        System.out.println("quantidade de buckets");
        int bucketSize = sc.nextInt();
        int qtBuckts = sc.nextInt();
        int regPerPage = 100;
        
        Paginator paginator = new Paginator();
        paginator.pageMaker("words.txt", regPerPage);
        paginator.pageVisualizer();

        HashTable hashT = new HashTable(bucketSize, qtBuckts);
    }
}
