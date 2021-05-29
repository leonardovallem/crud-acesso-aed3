package controller;

import entity.Pergunta;
import entity.Usuario;
import service.RespostaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class RespostasController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final RespostaService respostaService;

    public RespostasController(Usuario loggedUser) throws Exception {
        this.respostaService = new RespostaService(loggedUser);
    }

    public void menu(Pergunta pergunta) throws Exception {
        int chosen = 0;

        do {
            List<Integer> respostas = respostaService.list(pergunta.getId());

            String menuOptions = "\n1) Responder pergunta";
            if (respostas.size() > 0) menuOptions = menuOptions.concat("\n2) Avaliar alguma resposta");

            System.out.println(menuOptions + "\n\n0) Voltar às perguntas");
            chosen = Integer.parseInt(INPUT.readLine());

            switch (chosen) {
                case 1:
                    respostaService.create(pergunta.getId());
                    break;
                case 2:
                    if (respostas.size() > 0) {
                        respostaService.rate(pergunta.getId());
                        break;
                    }
                default:
                    System.out.println("\nPerguntas\n");
            }
        } while(chosen != 0);
    }
}
