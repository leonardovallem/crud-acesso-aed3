package controller;

import entity.Usuario;
import service.PerguntaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UserPerguntasController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final PerguntaService perguntaService;

    public UserPerguntasController(Usuario loggedUser) throws Exception {
        this.perguntaService = new PerguntaService(loggedUser);
    }

    /**
     * Menu que permite ao usuário: listar incluir alterar arquivar suas perguntas
     */
    public void menu() throws Exception {
        int option = -1;

        while (option != 0) {
            System.out.println("INÍCIO > MINHAS PERGUNTAS\n");
            System.out.println("1) Pesquisar\n" + "2) Listar\n" + "3) Incluir\n" + "4) Alterar\n" + "5) Arquivar");
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(INPUT.readLine());

            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    System.out.println("Busque as perguntas por palavras chave separadas por espaço\n" +
                            "Palavras irrelevantes (como pronomes) serão ignoradas.\n");
                    System.out.print("Palavras chave:\n\t↳ ");

                    perguntaService.find(INPUT.readLine());
                    break;
                case 2:
                    perguntaService.list();
                    break;
                case 3:
                    perguntaService.create();
                    break;
                case 4:
                    perguntaService.update();
                    break;
                case 5:
                    perguntaService.archive();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
