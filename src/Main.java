import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        System.out.println("PERGUNTAS 1.0\n" +
                "=============\n");

        int option = -1;
        while(option != 0) {
            System.out.println("ACESSO\n");

            System.out.println("1) Acesso ao sistema\n" +
                    "2) Novo usuário (primeiro acesso)\n\n" +
                    "0) Sair\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            switch (option) {
                case 0:
                    System.out.println("\nAté mais!");
                    break;
                case 1:
                    access();
                    break;
                case 2:
                    createAccount();
                    break;
                default:
                    System.out.println("A opção fornecida é inválida.\n");
            }
        }
    }

    /**
     * Método que permite acesso ao sistema
     * Não necessita do usuário estar logado
     */
    private static void access() {
    }

    /**
     * Método que permite a criação de uma conta
     * @link https://replit.com/@kutova/TabelaHashExtensivel
     */
    private static void createAccount() {
    }
}
