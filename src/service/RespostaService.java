package service;

import config.KeywordHandler;
import config.SetUtils;
import dal.DAO;
import data.BPlusTree;
import data.ListaInvertida;
import entity.Resposta;
import entity.Usuario;
import config.Const;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class RespostaService {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private final Usuario loggedUser;
    private final DAO<Resposta> respostasDao;
    private final BPlusTree respostasOfPergunta;
    private final BPlusTree respostasOfUsuario;
    private final ListaInvertida indiceReversoRespostas;

    public RespostaService(Usuario loggedUser) throws Exception {
        this.loggedUser = loggedUser;
        this.respostasDao = new DAO<>(Resposta.class.getConstructor(), Const.RespostasOfPerguntaDB);
        this.respostasOfPergunta = new BPlusTree(5, Const.FilesPath + Const.RespostasOfPerguntaDB.replace(".db", ".tree.db"));
        this.respostasOfUsuario = new BPlusTree(5, Const.FilesPath + Const.RespostasOfUsuarioDB.replace(".db", ".tree.db"));
        this.indiceReversoRespostas = new ListaInvertida(5, Const.FilesPath + "respostas-dict.reverse.db", Const.FilesPath + "respostas-bloco.reverse.db");
    }

    public List<Integer> getAll() throws Exception {
        return getAll(null);
    }

    public List<Integer> getAll(Integer perguntaId) throws Exception {
        BPlusTree respostasTree = perguntaId == null ? respostasOfUsuario : respostasOfPergunta;
        return Arrays.stream(respostasTree.read(perguntaId == null ? loggedUser.getId() : perguntaId))
                .boxed()
                .filter(resposta -> {
                    try {
                        return respostasDao.read(resposta).isAtiva();
                    } catch (Exception e) {
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    public List<Integer> list() throws Exception {
        return list(null);
    }

    public List<Integer> list(Integer perguntaId) throws Exception {
        List<Integer> validRespostas = perguntaId == null ? getAll() : getAll(perguntaId);

        if(validRespostas.size() == 0) System.out.println("\n\t### Nenhuma resposta ###\n");
        else exhibit(validRespostas);

        return validRespostas;
    }

    public List<Integer> find(String chaves) throws Exception {
        // obtém as palavras-chave relevantes a partir da string pesquisada
        List<String> keywords = KeywordHandler.relevantKeywords(chaves);

        List<List<Integer>> retrievedRespostas = new ArrayList<>(); // resultados das pesquisas para cada palavra-chave
        for(var keyword : keywords) {
            // transforma o int[] retornado pela lista invertida em List<Integer>
            List<Integer> matchingPerguntas = Arrays.stream(indiceReversoRespostas.read(keyword))
                    .boxed()
                    .filter(perguntaId -> {
                        try {
                            return respostasDao.read(perguntaId).getIdUsuario() == loggedUser.getId();
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
            retrievedRespostas.add(matchingPerguntas);
        }
        List<Integer> finalPerguntas = SetUtils.intersection(retrievedRespostas);
        if(finalPerguntas.isEmpty()) System.out.println("\n\t### Nenhuma pergunta ###\n");
        else exhibit(finalPerguntas);

        return finalPerguntas;
    }

    private void exhibit(List<Integer> respostas) throws Exception {
        DAO<Usuario> usuariosDAO = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);;

        int counter = 1;
        for (Integer id : respostas) {
            var resposta = respostasDao.read(id);
            if (resposta.isAtiva()) {
                var usuarioResposta = usuariosDAO.read(resposta.getIdUsuario());
                System.out.println("\t| " + (counter++ + ". " + resposta.toString(usuarioResposta)));
            }
        }
    }

    public void create(int perguntaId) throws Exception {
        System.out.print("O que você deseja responder?\n\t↳ ");
        String responder = input.readLine();

        Resposta resposta = new Resposta(loggedUser.getId(), perguntaId, responder);

        var id = respostasDao.create(resposta);
        respostasOfPergunta.create(perguntaId, resposta.getId());
        respostasOfUsuario.create(loggedUser.getId(), resposta.getId());

        String[] keywords = KeywordHandler.normalize(responder).split(" ");
        for(String keyword : keywords) {    // adiciona a pergunta ao índice de cada palavra-chave contida,
            if(KeywordHandler.isRelevant(keyword)) indiceReversoRespostas.create(keyword, id);  // caso seja relevante
        }
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

    public void update() throws Exception {
        update(null);
    }

    public void update(Integer perguntaId) throws Exception {
        List<Integer> all = perguntaId == null ? list() : list(perguntaId);
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

    public void archive() throws Exception {
        archive(null);
    }

    public void archive(Integer perguntaId) throws Exception {
        List<Integer> all = perguntaId == null ? list() : list(perguntaId);
        System.out.println("Qual resposta você deseja arquivar? (1-" + all.size() + ")");
        int arquivar = Integer.parseInt(input.readLine());

        Resposta resposta = respostasDao.read(all.get(arquivar - 1));
        resposta.setAtiva(false);
        respostasDao.update(resposta);
    }
}
