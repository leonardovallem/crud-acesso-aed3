package controller;

import entity.Usuario;
import service.PerguntaService;
import service.RespostaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UserRespostasController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final PerguntaService perguntaService;
    private final RespostaService respostaService;

    public UserRespostasController(Usuario loggedUser) throws Exception {
        this.perguntaService = new PerguntaService(loggedUser);
        this.respostaService = new RespostaService(loggedUser);
    }

    /**
     * Menu que permite ao usuário: listar incluir alterar arquivar suas perguntas
     */
    public void menu() throws Exception {
        int option = -1;

        while (option != 0) {
            System.out.println("INÍCIO > MINHAS RESPOSTAS\n");
            System.out.println("1) Pesquisar\n" + "2) Listar\n" + "3) Alterar\n" + "4) Arquivar");
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(INPUT.readLine());

            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    System.out.println("Busque as respostas por palavras chave separadas por espaço\n" +
                            "Palavras irrelevantes (como pronomes) serão ignoradas.\n");
                    System.out.print("Palavras chave:\n\t↳ ");

                    respostaService.find(INPUT.readLine());
                    break;
                case 2:
                    respostaService.list();
                    break;
                case 3:
                    respostaService.update();
                    break;
                case 4:
                    respostaService.archive();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
