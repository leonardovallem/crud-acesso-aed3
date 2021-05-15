package service;

import dal.DAO;
import data.BPlusTree;
import data.ListaInvertida;
import entity.Pergunta;
import entity.Resposta;
import entity.Usuario;
import files.Const;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * @TODO
 */
public class RespostaService {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private final Pergunta currentPergunta;
    private final DAO<Resposta> respostasDao;
    private final BPlusTree respostasStorage;
    private final ListaInvertida indiceReversoRespostas;

    public RespostaService(Pergunta currentPergunta) throws Exception {
        this.currentPergunta = currentPergunta;
        this.respostasDao = new DAO<>(Resposta.class.getConstructor(), Const.RespostasDB);
        this.respostasStorage = new BPlusTree(5, Const.FilesPath + Const.RespostasDB.replace(".db", ".tree.db"));
        this.indiceReversoRespostas = new ListaInvertida(5, "respostas-dict.reverse.db", "respostas-bloco.reverse.db");
    }

//    public int[] list() throws Exception {
//        int[] perguntasIds = perguntasStorage.read(loggedUser.getId());
//        if(perguntasIds.length == 0) System.out.println("\n\t### Nenhuma pergunta ###\n");
//
//        for (int i = 0; i < perguntasIds.length; i++) {
//            var pergunta = respostasDao.read(perguntasIds[i]);
//            if(pergunta.isAtiva()) System.out.println((i + 1) + ". " + pergunta);
//        }
//        return perguntasIds;
//    }
//
//    public int[] list(String chave) throws Exception {
//        int[] perguntasIds = perguntasStorage.read(loggedUser.getId());
//        if(perguntasIds.length == 0) System.out.println("\n\t### Nenhuma pergunta ###\n");
//
//        for (int i = 0; i < perguntasIds.length; i++) {
//            var pergunta = respostasDao.read(perguntasIds[i]);
//            if(pergunta.isAtiva()) System.out.println((i + 1) + ". " + respostasDao.read(perguntasIds[i]));
//        }
//        return perguntasIds;
//    }
//
//    public void create() throws IOException {
//        System.out.print("O que você deseja perguntar?\n\t↳ ");
//        String perguntar = input.readLine();
//
//        Pergunta pergunta = new Pergunta(loggedUser.getId(), perguntar);
//
//        respostasDao.create(pergunta);
//        perguntasStorage.create(loggedUser.getId(), pergunta.getId());
//    }
//
//    public void update() throws Exception {
//        int[] all = list();
//        System.out.println("Qual pergunta você deseja alterar? (1-" + all.length + ")");
//        int alterar = Integer.parseInt(input.readLine());
//
//        Pergunta pergunta = respostasDao.read(all[alterar - 1]);
//        if (pergunta != null) {
//            System.out.print("O que você deseja perguntar agora?\n\t↳ ");
//            String novaPergunta = input.readLine();
//
//            pergunta.setPergunta(novaPergunta);
//            respostasDao.update(pergunta);
//        } else
//            System.out.println("Pergunta inválida.");
//    }
//
//    public void archive() throws Exception {
//        int[] all = list();
//        System.out.println("Qual pergunta você deseja arquivar? (1-" + all.length + ")");
//        int arquivar = Integer.parseInt(input.readLine());
//
//        Pergunta pergunta = respostasDao.read(all[arquivar - 1]);
//        pergunta.setAtiva(false);
//        respostasDao.update(pergunta);
//    }
//
//    public boolean truncate(int userId) throws IOException {
//        boolean status = true;
//        int[] exPerguntasIds = perguntasStorage.read(userId);
//
//        for (int exPerguntaId : exPerguntasIds) {
//            if (respostasDao.delete(exPerguntaId)) {
//                perguntasStorage.delete(userId, exPerguntaId);
//            } else {
//                status = false;
//            }
//        }
//
//        return status;
//    }
}
