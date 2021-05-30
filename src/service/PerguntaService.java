package service;

import config.SetUtils;
import dal.DAO;
import data.BPlusTree;
import data.ListaInvertida;
import config.KeywordHandler;
import entity.Pergunta;
import entity.Resposta;
import entity.Usuario;
import files.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        this.indiceReversoPerguntas = new ListaInvertida(5, Const.FilesPath + "perguntas-dict.reverse.db", Const.FilesPath + "perguntas-bloco.reverse.db");
    }

    public List<Integer> getAll() throws IOException {
        return getAll(loggedUser.getId());
    }

    private List<Integer> getAll(int usuarioId) throws IOException {
        return Arrays.stream(perguntasStorage.read(usuarioId))
                .boxed()
                .filter(pergunta -> {
                    try {
                        return perguntasDao.read(pergunta).isAtiva();
                    } catch (Exception e) {
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    /**
     * Retorna uma lista ordenada por data de criação mais recente
     * contendo todas as perguntas ativas contidas no sistema
     */
    public List<Integer> getAllSorted() throws Exception {
        var list = getAllGlobally();
        list.sort(Comparator.comparingLong(p -> {
            try {
                return -perguntasDao.read(p).getCriacao();
            } catch (Exception e) {
                return 0;
            }
        }));
        return list;
    }

    /**
     * Retorna todas as perguntas contidas no sistema,
     * de todos os usuários
     */
    private List<Integer> getAllGlobally() throws Exception {
        UsuarioService usuarioService = new UsuarioService(loggedUser);
        List<Integer> allPerguntas = new ArrayList<>();
        usuarioService.getAll()
                .forEach(usuario -> {
                    try {
                        allPerguntas.addAll(getAll(usuario.getId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return allPerguntas;
    }

    public List<Integer> list() throws Exception {
        List<Integer> validPerguntas = getAll();

        if(validPerguntas.size() == 0) System.out.println("\n\t### Nenhuma pergunta ###\n");
        else exhibit(validPerguntas);

        return validPerguntas;
    }

    public List<Integer> find(String chaves) throws Exception {
        // obtém as palavras-chave relevantes a partir da string pesquisada
        List<String> keywords = KeywordHandler.relevantKeywords(chaves);

        List<List<Integer>> retrievedPerguntas = new ArrayList<>(); // resultados das pesquisas para cada palavra-chave
        for(var keyword : keywords) {
            // transforma o int[] retornado pela lista invertida em List<Integer>
            List<Integer> matchingPerguntas = Arrays.stream(indiceReversoPerguntas.read(keyword)).boxed().collect(Collectors.toList());
            retrievedPerguntas.add(matchingPerguntas);
        }

        List<Integer> finalPerguntas = SetUtils.intersection(retrievedPerguntas);
        if(finalPerguntas.isEmpty()) System.out.println("\n\t### Nenhuma pergunta ###\n");
        else exhibit(finalPerguntas);

        return finalPerguntas;
    }

    public void exhibit(List<Integer> perguntas) throws Exception {
        DAO<Usuario> usuariosDAO = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);

        int counter = 1;
        for (Integer integer : perguntas) {
            var pergunta = perguntasDao.read(integer);
            if (pergunta.isAtiva()) {
                var usuarioPergunta = usuariosDAO.read(pergunta.getIdUsuario());
                System.out.println(counter++ + ". " + pergunta.toString(usuarioPergunta));
            }
        }
    }

    public Pergunta getOne(int id) throws Exception {
        return perguntasDao.read(id);
    }

    public void create() throws Exception {
        System.out.print("O que você deseja perguntar?\n\t↳ ");
        String perguntar = input.readLine();

        Pergunta pergunta = new Pergunta(loggedUser.getId(), perguntar);

        var id = perguntasDao.create(pergunta);
        perguntasStorage.create(loggedUser.getId(), pergunta.getId());

        String[] keywords = KeywordHandler.normalize(perguntar).split(" ");
        for(String keyword : keywords) {    // adiciona a pergunta ao índice de cada palavra-chave contida,
            if(KeywordHandler.isRelevant(keyword)) indiceReversoPerguntas.create(keyword, id);  // caso seja relevante
        }
    }

    // @TODO checar se o usuário já avaliou a pergunta
    public void rate(Pergunta pergunta) throws Exception {
        System.out.print("Dê uma nota de 1 a 5\n\t☆ ");

        float nota = Float.parseFloat(input.readLine().replace(",", "."));

        pergunta.rate(nota);
        perguntasDao.update(pergunta);
    }

    public void update() throws Exception {
        List<Integer> all = list();
        System.out.println("Qual pergunta você deseja alterar? (1-" + all.size() + ")");
        int alterar = Integer.parseInt(input.readLine());

        Pergunta pergunta = perguntasDao.read(all.get(alterar - 1));

        List<String> keywords = KeywordHandler.relevantKeywords(pergunta.getPergunta());
        keywords.forEach(keyword -> {
            try {
                indiceReversoPerguntas.delete(keyword, pergunta.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (pergunta != null) {
            System.out.print("O que você deseja perguntar agora?\n\t↳ ");
            String novaPergunta = input.readLine();

            String[] newKeywords = KeywordHandler.normalize(novaPergunta).split(" ");
            for(String keyword : newKeywords) {    // adiciona a pergunta ao índice de cada palavra-chave contida,
                if(KeywordHandler.isRelevant(keyword)) indiceReversoPerguntas.create(keyword, pergunta.getId());  // caso seja relevante
            }

            pergunta.setPergunta(novaPergunta);
            perguntasDao.update(pergunta);

        } else System.out.println("Pergunta inválida.");
    }

    public void archive() throws Exception {
        List<Integer> all = list();
        System.out.println("Qual pergunta você deseja arquivar? (1-" + all.size() + ")");
        int arquivar = Integer.parseInt(input.readLine());

        Pergunta pergunta = perguntasDao.read(all.get(arquivar - 1));
        pergunta.setAtiva(false);
        perguntasDao.update(pergunta);

        List<String> keywords = KeywordHandler.relevantKeywords(pergunta.getPergunta());
        keywords.forEach(keyword -> {
            try {
                indiceReversoPerguntas.delete(keyword, pergunta.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
