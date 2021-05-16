import dal.DAO;
import entity.Usuario;
import files.Const;
import service.PerguntaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Classe que permite a navegação no sistema por usuários autenticados
 */
public class Authenticated {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private final Usuario loggedUser;
    private final DAO<Usuario> usuariosDao;
    private final PerguntaService perguntaService;

    public Authenticated(Usuario loggedUser) throws Exception {
        this.loggedUser = loggedUser;
        this.usuariosDao = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);
        perguntaService = new PerguntaService(loggedUser);
    }

    /**
     * Menu que permite ao usuário: acessar o menu de perguntas/respostas alterar
     * sua senha outras opções que o usuário precisar ter à disposição
     */
    public void loggedInMenu() {
        System.out.println("Seja bem-vindo!\nO que deseja fazer?");

        int option = -1;
        while (option != 0) {
            try {
                System.out.println("\n1) Acessar perguntas" + "\n2) Alterar senha"
                        + (loggedUser.getIsAdmin() ? "\n3) Área restrita" : "") + "\n\n0) Logout");

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
                        changePassword();
                        break;
                    case 3:
                        if (loggedUser.getIsAdmin()) {
                            adminMenu();
                        } else {
                            System.out.println("Opção inválida.");
                        }
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
                StackTraceElement[] stackTraceArray = e.getStackTrace();
                for (StackTraceElement stackTrace : stackTraceArray) {
                    System.out.println("\t" + stackTrace.toString());
                }
            }
        }
    }

    /**
     * Altera a senha do usuário
     */
    private void changePassword() throws Exception {
        DAO<Usuario> userDAO = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);

        Usuario user = userDAO.read(loggedUser.getId());

        System.out.println("Insira sua senha atual:");
        String oldPass = input.readLine();

        int chances = 3;
        while (oldPass.hashCode() != user.getSenha() && chances > 0) {
            if (oldPass.hashCode() != user.getSenha()) {
                chances--;
                if (chances > 0) {
                    System.out.println("Senha incorreta. Tente novamente");
                    oldPass = input.readLine();
                } else
                    System.out.println("Três tentativas incorretas. Tente novamente mais tarde ou"
                            + "volte ao menu inicial e responda à sua pergunta secreta.");
            }
        }
        if (oldPass.hashCode() == user.getSenha()) {
            System.out.print("Digite sua nova senha: ");
            user.setSenha(input.readLine().hashCode());
            if (userDAO.update(user)) {
                System.out.println("Senha alterada com sucesso!");
            } else
                System.out.println("Erro durante a operação");
        }
    }

    /**
     * Menu que permite ao usuário: criar perguntas
     */
    private void perguntasMenu() throws Exception {
        int notificacoes = 0; // pegar dos arquivos as notificacoes
        int option = -1;

        while (option != 0) {
            System.out.println("INÍCIO\n");
            System.out.println("1) Minhas perguntas\n" + "2) Consultar/responder perguntas\n" + "3) Notificações: "
                    + notificacoes);
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            switch (option) {
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
     * Menu que permite ao usuário: listar incluir alterar arquivar suas perguntas
     */
    private void QA() throws Exception {
        int option = -1;

        while (option != 0) {
            System.out.println("INÍCIO > MINHAS PERGUNTAS\n");
            System.out.println("1) Pesquisar\n" + "2) Listar\n" + "3) Incluir\n" + "4) Alterar\n" + "5) Arquivar");
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    System.out.println("Busque as perguntas por palavras chave separadas por espaço\n" +
                            "Palavras irrelevantes (como pronomes) serão ignoradas.\n");
                    System.out.print("Palavras chave:\n\t↳ ");

                    perguntaService.list(input.readLine());
                    break;
                case 2:
                    perguntaService.list();
                    break;
                case 3:
                    perguntaService.create();
                    break;
                case 4:
                    perguntaService.update();
                    break;
                case 5:
                    perguntaService.archive();
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

    // Métodos da área restrita para usuários adminstradores
    // #region restrict area
    private void adminMenu() throws Exception {
        int option = -1;
        do {
            System.out.println("ÁREA RESTRITA\n\n" + "1) Usuários\n" + "\n0) Retornar ao menu anterior\n");
            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());
            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    adminUsuariosMenu();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (option != 0);
    }

    private void adminUsuariosMenu() throws Exception {
        int option = -1;
        do {
            System.out.println("ÁREA RESTRITA > USUÁRIOS\n\n" + "1) Listar\n" + "2) Excluír\n"
                    + "\n0) Retornar ao menu anterior\n");
            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());
            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    listUsers();
                    break;
                case 2:
                    deleteUser();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (option != 0);
    }

    private void listUsers() throws Exception {
        usuariosDao.readAll().forEach(System.out::println);
    }

    private void deleteUser() throws Exception {
        System.out.print("Informe o Número/ID do usuário que você deseja excluír: ");
        int exId = Integer.parseInt(input.readLine());
        Usuario exUser = usuariosDao.read(exId);

        if (exUser == null) {
            System.out.println("Usuário informado não existe, tente mais tarde");
        } else {
            System.out.println("Deseja confirmar exclusão do usuário \"" + exUser.getNome()
                    + "\"? (Sim/Não)\nObs: Todos os dados relacionados à esse também serão excluídos");
            String option = input.readLine();
            if (option.charAt(0) == 'S' || option.charAt(0) == 's') {
                boolean status = perguntaService.truncate(exId);

                if (!status) {
                    System.out.println(
                            "Erro ao excluír perguntas relacionadas ao usuário. Ação abortada, tente mais tarde");
                } else {
                    // Excluíndo o usuário
                    status = usuariosDao.delete(exId);

                    if (status) {
                        System.out.println("Usuário excluído com sucesso");
                    } else {
                        System.out.println("Erro ao excluír usuário. Ação abortada, tente mais tarde");
                    }
                }
            }
        }
    }
    // #endregion
}
