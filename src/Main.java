import easyaccept.EasyAccept;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[] tests = {
                "tests/us1_1.txt", "tests/us1_2.txt",
                "tests/us2_1.txt", "tests/us2_2.txt",
                "tests/us3_1.txt", "tests/us3_2.txt",
                "tests/us4_1.txt", "tests/us4_2.txt",
                "tests/us5_1.txt", "tests/us5_2.txt",
                "tests/us6_1.txt", "tests/us6_2.txt",
                "tests/us7_1.txt", "tests/us7_2.txt",
                "tests/us8_1.txt", "tests/us8_2.txt",
                "tests/us9_1.txt", "tests/us9_2.txt",
        };

        Scanner scanner = new Scanner(System.in);

        for (String test : tests) {
            System.out.println("Rodando: " + test);
            String[] args2 = {"br.ufal.ic.p2.jackut.Facade", test};
            EasyAccept.main(args2);
            System.out.println("Pressione ENTER para continuar...");
            scanner.nextLine();
        }

        scanner.close();
    }
}
