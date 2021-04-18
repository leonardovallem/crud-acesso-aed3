import dal.DAO;
import entity.Pergunta;
import entity.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe que permite a navegação no
 * sistema por usuários autenticados
 */
public class Authenticated {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private final int idUsuario;
    private final DAO<Pergunta> dao;

    public Authenticated(int idUsuario) throws Exception {
        this.idUsuario = idUsuario;
        this.dao = new DAO<>(Pergunta.class.getConstructor(),"perguntas.db");
    }

    /**
     * Menu que permite ao usuário:
        * acessar o menu de perguntas/respostas
        * alterar sua senha
        * outras opções que o usuário precisar ter à disposição
     */
    public void loggedInMenu() throws IOException {
        System.out.println("Seja bem-vindo!\nO que deseja fazer?");

        int option = -1;

        while(option != 0) {
            System.out.println("\n1) Acessar perguntas" +
                    "\n2) Alterar senha" +
                    "\n\n0) Logout");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            System.out.println();
            switch (option) {
                case 0:
                    System.out.println("Fazendo logout ...\n");
                    break;
                case 1:
                    perguntasMenu();
                    break;
                case 2:
                    try {
                        changePassword();
                    } catch (Exception e) {
                        System.out.println("ERRO: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Altera a senha do usuário
     */
    private void changePassword() throws Exception {
        DAO<Usuario> userDAO = new DAO<>(Usuario.class.getConstructor(), "arq");

        Usuario user = userDAO.read(idUsuario);

        System.out.println("Insira sua senha atual:");
        String oldPass = input.readLine();

        int chances = 3;
        while(oldPass.hashCode() != user.getSenha() && chances > 0) {
            if(oldPass.hashCode() != user.getSenha()) {
                chances--;
                if(chances > 0) {
                    System.out.println("Senha incorreta. Tente novamente");
                    oldPass = input.readLine();
                } else System.out.println("Três tentativas incorretas. Tente novamente mais tarde ou" +
                        "volte ao menu inicial e responda à sua pergunta secreta.");
            }
        }
        if(oldPass.hashCode() == user.getSenha()) {
            System.out.print("Digite sua nova senha: ");
            user.setSenha(input.readLine().hashCode());
            if (userDAO.update(user)) {
                System.out.println("Senha alterada com sucesso!");
            } else System.out.println("Erro durante a operação");
        }
    }

    /**
     * Menu que permite ao usuário:
        * criar perguntas*/
    private void perguntasMenu() throws IOException {
        int notificacoes = 0;   // pegar dos arquivos as notificacoes
        int option = -1;

        while(option != 0) {
            System.out.println("INÍCIO\n");
            System.out.println("1) Minhas perguntas\n" +
                    "2) Consultar/responder perguntas\n" +
                    "3) Notificações: " + notificacoes);
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            switch(option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    QA();
                    break;
                case 2:
                    checkOrAnswerPerguntas();
                    break;
                case 3:
                    notifications();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

    }

    /**
     * Menu que permite ao usuário:
         * listar
         * incluir
         * alterar
         * arquivar
     *     suas perguntas
     */
    private void QA() throws IOException {
        int option = -1;

        while(option != 0) {
            System.out.println("INÍCIO > MINHAS PERGUNTAS\n");
            System.out.println("1) Listar\n" +
                    "2) Incluir\n" +
                    "3) Alterar\n" +
                    "4) Arquivar");
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            switch(option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    listPerguntas();
                    break;
                case 2:
                    createPergunta();
                    break;
                case 3:
                    updatePergunta();
                    break;
                case 4:
                    archivePergunta();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void checkOrAnswerPerguntas() {
        System.out.println(" ===== EM BREVE =====");
    }

    private void notifications() {
        System.out.println(" ===== EM BREVE =====");
    }

    private void listPerguntas() {
        // arvore B+
    }

    private void createPergunta() throws IOException {
        System.out.println("O que você deseja perguntar?");
        String perguntar = input.readLine();

        Pergunta pergunta = new Pergunta(idUsuario, perguntar);
        dao.create(pergunta);

        // arvore B+
    }

    private void updatePergunta() throws IOException {
        System.out.println("Qual pergunta você deseja alterar?");
        String pergunta = input.readLine();

        // if(pergunta exists)
        System.out.println("O que você deseja perguntar?");
        String novaPergunta = input.readLine();

//        Pergunta perguntaObj = read(pergunta);
//        perguntaObj.setPergunta(novaPergunta);
//        write(perg);

        // arvore B+
    }

    private void archivePergunta() throws IOException {
        System.out.println("Qual pergunta você deseja alterar?");
        String pergunta = input.readLine();

//        Pergunta perguntaObj = read(pergunta);
//        perguntaObj.setAtiva(false);
//        write(perg);

        // arvore B+
    }
}
