package service;

import dal.DAO;
import data.BPlusTree;
import data.ListaInvertida;
import entity.Resposta;
import entity.Usuario;
import files.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RespostaService {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private final Usuario loggedUser;
    private final DAO<Resposta> respostasDao;
    private final BPlusTree respostasStorage;
    private final ListaInvertida indiceReversoRespostas;

    public RespostaService(Usuario loggedUser) throws Exception {
        this.loggedUser = loggedUser;
        this.respostasDao = new DAO<>(Resposta.class.getConstructor(), Const.RespostasDB);
        this.respostasStorage = new BPlusTree(5, Const.FilesPath + Const.RespostasDB.replace(".db", ".tree.db"));
        this.indiceReversoRespostas = new ListaInvertida(5, Const.FilesPath + "respostas-dict.reverse.db", Const.FilesPath + "respostas-bloco.reverse.db");
    }

    public List<Integer> getAll(int perguntaId) throws Exception {
        return Arrays.stream(respostasStorage.read(perguntaId))
                .boxed()
                .filter(pergunta -> {
                    try {
                        return respostasDao.read(pergunta).isAtiva();
                    } catch (Exception e) {
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    public List<Integer> list(int perguntaId) throws Exception {
        List<Integer> validRespostas = getAll(perguntaId);

        if(validRespostas.size() == 0) System.out.println("\n\t### Nenhuma resposta ###\n");
        else exhibit(validRespostas);

        return validRespostas;
    }

    private void exhibit(List<Integer> respostas) throws Exception {
        int counter = 1;
        for (Integer integer : respostas) {
            var resposta = respostasDao.read(integer);
            if (resposta.isAtiva()) System.out.println("\t" + (counter++ + ". " + respostasDao.read(integer)));
        }
    }

    public void create(int perguntaId) throws IOException {
        System.out.print("O que você deseja responder?\n\t↳ ");
        String responder = input.readLine();

        Resposta resposta = new Resposta(loggedUser.getId(), perguntaId, responder);

        respostasDao.create(resposta);
        respostasStorage.create(perguntaId, resposta.getId());
    }

    // @TODO checar se o usuário já avaliou a resposta
    public void rate(int perguntaId) throws Exception {
        List<Integer> all = list(perguntaId);
        System.out.println("Qual resposta você deseja avaliar? (1-" + all.size() + ")");

        int alterar = Integer.parseInt(input.readLine());
        Resposta resposta = respostasDao.read(all.get(alterar - 1));

        System.out.println(resposta);
        System.out.print("Dê uma nota de 1 a 5\n\t☆ ");

        float nota = Float.parseFloat(input.readLine().replace(",", "."));

        resposta.rate(nota);
        respostasDao.update(resposta);
    }

    public void update(int perguntaId) throws Exception {
        List<Integer> all = list(perguntaId);
        System.out.println("Qual resposta você deseja alterar? (1-" + all.size() + ")");
        int alterar = Integer.parseInt(input.readLine());

        Resposta resposta = respostasDao.read(all.get(alterar - 1));
        if (resposta != null) {
            System.out.print("O que você deseja responder agora?\n\t↳ ");
            String novaResposta = input.readLine();

            resposta.setResposta(novaResposta);
            respostasDao.update(resposta);
        } else
            System.out.println("Resposta inválida.");
    }

    public void archive(int perguntaId) throws Exception {
        List<Integer> all = list(perguntaId);
        System.out.println("Qual resposta você deseja arquivar? (1-" + all.size() + ")");
        int arquivar = Integer.parseInt(input.readLine());

        Resposta resposta = respostasDao.read(all.get(arquivar - 1));
        resposta.setAtiva(false);
        respostasDao.update(resposta);
    }
}
