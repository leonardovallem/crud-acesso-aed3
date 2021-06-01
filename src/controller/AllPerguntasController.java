package controller;

import entity.Pergunta;
import entity.Usuario;
import service.PerguntaService;
import service.RespostaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class AllPerguntasController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));
    private List<Integer> allPerguntas;

    private final PerguntaService perguntaService;
    private final RespostaService respostaService;
    private final Usuario loggedUser;
    private final RespostasController respostasController;


    public AllPerguntasController(Usuario loggedUser) throws Exception {
        this.perguntaService = new PerguntaService(loggedUser);
        this.respostaService = new RespostaService(loggedUser);
        this.loggedUser = loggedUser;
        this.respostasController = new RespostasController(loggedUser);
    }

    public void menu() throws Exception {
        int option = -1;

        while (option != 0) {
            System.out.println("INÍCIO > PERGUNTAS & RESPOSTAS\n");
            System.out.println("1) Pesquisar\n" + "2) Últimas perguntas\n");
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(INPUT.readLine());

            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    System.out.println("INÍCIO > PESQUISAR\n");
                    System.out.println("Busque as perguntas por palavras chave separadas por espaço\n" +
                            "Palavras irrelevantes (como pronomes) serão ignoradas.\n");
                    System.out.print("Palavras chave:\n\t↳ ");

                    search(INPUT.readLine());
                    break;
                case 2:
                    // todas as perguntas presentes no sistema
                    allPerguntas = new LinkedList<>(perguntaService.getAllSorted());
                    int min = 0, max = 10;
                    int page = 1;
                    char userOpt = '1';

                    while (page != 0) {
                        System.out.println("INÍCIO > PERGUNTAS MAIS RECENTES\nPágina " + page + "\n");
                        int lastIndex = Math.min(max, allPerguntas.size());

                        // paginação das perguntas
                        perguntaService.exhibit(allPerguntas.subList(min, lastIndex));

                        boolean hasNext = ((page * 10) + allPerguntas.size() % max) <= allPerguntas.size(), hasPrev = page > 1;
                        String menuOptions = "Selecionar" +
                                (allPerguntas.size() == 0 ? " " : " uma pergunta (1-"+(lastIndex%11)+")\nou ")
                                + "0 para voltar";

                        if(hasNext) menuOptions = "\">\" para avançar a página\n".concat(menuOptions);
                        if(hasPrev) menuOptions = "Voltar página com \"<\"\nou ".concat(menuOptions);
                        System.out.println(menuOptions);

                        userOpt = INPUT.readLine().charAt(0);

                        switch (userOpt) {
                            case '0':
                                page = 0;
                                break;
                            case '>':
                                if(hasNext) {
                                    min += 10;
                                    max += 10;
                                    page++;
                                    break;
                                }
                            case '<':
                                if(hasPrev) {
                                    min -= 10;
                                    max -= 10;
                                    page--;
                                    break;
                                }
                            default:
                                int number = userOpt - '0';
                                // se o usuário passar o número de uma pergunta, ela entra em destaque e dá acesso às respostas
                                if(number > 0 && number <= lastIndex) {
                                    Pergunta pergunta = perguntaService.getOne(allPerguntas.get(min + number - 1));
                                    respostasController.menu(pergunta);
                                }
                        }
                    }
                    // ">" para próxima página
                    // "<" para página anterior
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void search(String keywords) throws Exception {
        int chosen = -1;

        do {
            List<Integer> matchingPerguntas = perguntaService.find(keywords);

            System.out.println("Escolha uma pergunta (1-" + matchingPerguntas.size() + ") ou 0 para voltar");
            chosen = Integer.parseInt(INPUT.readLine());
            if(chosen == 0) return;

            Pergunta pergunta = perguntaService.getOne(matchingPerguntas.get(chosen - 1));
            System.out.println(pergunta);

            respostasController.menu(pergunta);
        } while(chosen != 0);
    }
}
