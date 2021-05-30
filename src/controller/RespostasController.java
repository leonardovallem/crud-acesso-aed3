package controller;

import dal.DAO;
import entity.Pergunta;
import entity.Usuario;
import files.Const;
import service.PerguntaService;
import service.RespostaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class RespostasController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final RespostaService respostaService;
    private final PerguntaService perguntaService;

    public RespostasController(Usuario loggedUser) throws Exception {
        this.respostaService = new RespostaService(loggedUser);
        perguntaService = new PerguntaService(loggedUser);
    }

    public void menu(Pergunta pergunta) throws Exception {
        DAO<Usuario> usuariosDAO = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);
        int chosen = 0;

        do {
            System.out.println(pergunta.toString(usuariosDAO.read(pergunta.getIdUsuario())));
            List<Integer> respostas = respostaService.list(pergunta.getId());

            String menuOptions = "\n1) Responder pergunta\n2) Avaliar pergunta";
            if (respostas.size() > 0) menuOptions = menuOptions.concat("\n3) Avaliar alguma resposta");

            System.out.println(menuOptions + "\n\n0) Voltar às perguntas");
            chosen = Integer.parseInt(INPUT.readLine());

            switch (chosen) {
                case 1:
                    respostaService.create(pergunta.getId());
                    break;
                case 2:
                    perguntaService.rate(pergunta);
                    break;
                case 3:
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
