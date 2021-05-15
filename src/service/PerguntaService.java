package service;

import dal.DAO;
import data.BPlusTree;
import data.ListaInvertida;
import data.config.KeywordHandler;
import entity.Pergunta;
import entity.Usuario;
import files.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class PerguntaService {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private final Usuario loggedUser;
    private final DAO<Pergunta> perguntasDao;
    private final BPlusTree perguntasStorage;
    private final ListaInvertida indiceReversoPerguntas;

    public PerguntaService(Usuario loggedUser) throws Exception {
        this.loggedUser = loggedUser;
        this.perguntasDao = new DAO<>(Pergunta.class.getConstructor(), Const.PerguntasDB);
        this.perguntasStorage = new BPlusTree(5, Const.FilesPath + Const.PerguntasDB.replace(".db", ".tree.db"));
        this.indiceReversoPerguntas = new ListaInvertida(5, Const.FilesPath + "perguntas-dict.reverse.db", "perguntas-bloco.reverse.db");
    }

    public int[] list() throws Exception {
        int[] perguntasIds = perguntasStorage.read(loggedUser.getId());
        if(perguntasIds.length == 0) System.out.println("\n\t### Nenhuma pergunta ###\n");

        for (int i = 0; i < perguntasIds.length; i++) {
            var pergunta = perguntasDao.read(perguntasIds[i]);
            if(pergunta.isAtiva()) System.out.println((i + 1) + ". " + pergunta);
        }
        return perguntasIds;
    }

    public List<Integer> list(String chaves) throws Exception {
        String[] keys = chaves.split(";");

        List<List<Integer>> retrievedPerguntas = new ArrayList<>();
        for(var key : keys) {   // transforma o int[] retornado pela lista invertida em List<Integer>
            // remove caracteres especiais
            key = KeywordHandler.normalize(key);

            List<Integer> list = Arrays.stream(indiceReversoPerguntas.read(key)).boxed().collect(Collectors.toList());
            retrievedPerguntas.add(list);
        }

        Set<Integer> intersecao = new HashSet<>(retrievedPerguntas.get(0));
        for(int i=1 ; i < retrievedPerguntas.size() ; i++) {
            Set<Integer> temp = new HashSet<>(retrievedPerguntas.get(i));
            intersecao.retainAll(temp);
        }

        List<Integer> finalPerguntas = new ArrayList<>(intersecao);
        if(finalPerguntas.isEmpty()) System.out.println("\n\t### Nenhuma pergunta ###\n");

        for (int i = 0; i < finalPerguntas.size(); i++) {
            var pergunta = perguntasDao.read(finalPerguntas.get(i));
            if(pergunta.isAtiva()) System.out.println((i + 1) + ". " + perguntasDao.read(finalPerguntas.get(i)));
        }
        return finalPerguntas;
    }

    public void create() throws Exception {
        System.out.print("O que você deseja perguntar?\n\t↳ ");
        String perguntar = input.readLine();

        Pergunta pergunta = new Pergunta(loggedUser.getId(), perguntar);

        var id = perguntasDao.create(pergunta);
        perguntasStorage.create(loggedUser.getId(), pergunta.getId());

        String[] keywords = KeywordHandler.normalize(perguntar).split(" ");
        for(String keyword : keywords) {
            if(KeywordHandler.isRelevant(keyword)) indiceReversoPerguntas.create(keyword, id);
        }
    }

    public void update() throws Exception {
        int[] all = list();
        System.out.println("Qual pergunta você deseja alterar? (1-" + all.length + ")");
        int alterar = Integer.parseInt(input.readLine());

        Pergunta pergunta = perguntasDao.read(all[alterar - 1]);
        if (pergunta != null) {
            System.out.print("O que você deseja perguntar agora?\n\t↳ ");
            String novaPergunta = input.readLine();

            pergunta.setPergunta(novaPergunta);
            perguntasDao.update(pergunta);
        } else System.out.println("Pergunta inválida.");
    }

    public void archive() throws Exception {
        int[] all = list();
        System.out.println("Qual pergunta você deseja arquivar? (1-" + all.length + ")");
        int arquivar = Integer.parseInt(input.readLine());

        Pergunta pergunta = perguntasDao.read(all[arquivar - 1]);
        pergunta.setAtiva(false);
        perguntasDao.update(pergunta);
    }

    public boolean truncate(int userId) throws IOException {
        boolean status = true;
        int[] exPerguntasIds = perguntasStorage.read(userId);

        for (int exPerguntaId : exPerguntasIds) {
            if (perguntasDao.delete(exPerguntaId)) {
                perguntasStorage.delete(userId, exPerguntaId);
            } else {
                status = false;
            }
        }

        return status;
    }
}
