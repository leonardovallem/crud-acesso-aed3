package controller;

import dal.DAO;
import entity.Pergunta;
import entity.Resposta;
import entity.Usuario;
import config.Const;
import service.PerguntaService;
import service.RespostaService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class RespostasController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final RespostaService respostaService;
    private final PerguntaService perguntaService;
    private final Usuario loggedUser;

    public RespostasController(Usuario loggedUser) throws Exception {
        this.respostaService = new RespostaService(loggedUser);
        this.perguntaService = new PerguntaService(loggedUser);
        this.loggedUser = loggedUser;
    }

    public void menu(Pergunta pergunta) throws Exception {
        DAO<Usuario> usuariosDAO = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);
        int chosen = 0;

        do {
            System.out.println(pergunta.toString(usuariosDAO.read(pergunta.getIdUsuario())));
            List<Integer> respostas = respostaService.list(pergunta.getId());

            String menuOptions = "\n1) Responder pergunta\n2) Avaliar pergunta";
            if (respostas.size() > 0) menuOptions = menuOptions.concat("\n3) Selecionar alguma resposta");

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
                        selectedRespostaMenu(pergunta);
                        break;
                    }
                default:
                    System.out.println("\nPerguntas\n");
            }
        } while(chosen != 0);
    }

    private void selectedRespostaMenu(Pergunta pergunta) throws Exception {
        List<Integer> respostasOfPergunta = respostaService.list(pergunta.getId());
        System.out.print("Selecione uma resposta (1-" + respostasOfPergunta.size() + "): ");

        int respostaSelecionada = Integer.parseInt(INPUT.readLine());
        Resposta resposta = respostaService.getOne(respostasOfPergunta.get(respostaSelecionada-1));

        boolean respostaIsFromUsuario = resposta.getIdUsuario() == loggedUser.getId();

        boolean running = true;
        while(running) {
            System.out.println(resposta);

            String menuOptions = "\n1) Avaliar resposta\n";
            if (respostaIsFromUsuario)
                menuOptions = menuOptions.concat("2) Alterar resposta\n3) Arquivar resposta\n");

            System.out.println(menuOptions + "\n0) Voltar à pergunta");
            int chosen = Integer.parseInt(INPUT.readLine());

            switch (chosen) {
                case 0:
                    running = false;
                    break;
                case 1:
                    respostaService.rateOne(resposta);
                    break;
                case 2:
                    if (respostaIsFromUsuario) {
                        respostaService.updateOne(resposta);
                        break;
                    }
                case 3:
                    if (respostaIsFromUsuario) {
                        respostaService.archiveOne(resposta);
                        break;
                    }
                default:
                    System.out.println("Opção inválida");
            }
        }
    }
}
