import controller.AdminUserController;
import controller.AllPerguntasController;
import controller.UserPerguntasController;
import controller.UserRespostasController;
import dal.DAO;
import entity.Usuario;
import files.Const;
import service.PerguntaService;
import service.UsuarioService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Classe que permite a navegação no sistema por usuários autenticados
 */
public class Authenticated {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));
    private final Usuario loggedUser;

    public Authenticated(Usuario loggedUser) {
        this.loggedUser = loggedUser;
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
                System.out.println("\n1) Acessar postagens" + "\n2) Alterar senha"
                        + (loggedUser.getIsAdmin() ? "\n3) Área restrita" : "") + "\n\n0) Logout");

                System.out.print("Opção: ");
                option = Integer.parseInt(INPUT.readLine());

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
        String oldPass = INPUT.readLine();

        int chances = 3;
        while (oldPass.hashCode() != user.getSenha() && chances > 0) {
            if (oldPass.hashCode() != user.getSenha()) {
                chances--;
                if (chances > 0) {
                    System.out.println("Senha incorreta. Tente novamente");
                    oldPass = INPUT.readLine();
                } else
                    System.out.println("Três tentativas incorretas. Tente novamente mais tarde ou"
                            + " volte ao menu inicial e responda à sua pergunta secreta.");
            }
        }
        if (oldPass.hashCode() == user.getSenha()) {
            System.out.print("Digite sua nova senha: ");
            user.setSenha(INPUT.readLine().hashCode());
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
        int option = -1;

        while (option != 0) {
            System.out.println("INÍCIO\n");
            System.out.println("1) Acessar postagens do sistema\n" + "2) Minhas postagens\n");
            System.out.println("\n0) Retornar ao menu anterior\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(INPUT.readLine());

            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    new AllPerguntasController(loggedUser).menu();
                    break;
                case 2:
                    while (option != 0) {
                        System.out.println("INÍCIO > MINHAS POSTAGENS\n");
                        System.out.println("1) Perguntas\n" + "2) Respostas\n");
                        System.out.println("\n0) Retornar ao menu anterior\n");

                        System.out.print("Opção: ");
                        option = Integer.parseInt(INPUT.readLine());

                        switch (option) {
                            case 0:
                                System.out.println("Voltando ao menu anterior ...\n");
                                break;
                            case 1:
                                new UserPerguntasController(loggedUser).menu();
                                break;
                            case 2:
                                new UserRespostasController(loggedUser).menu();
                                break;
                            default:
                                System.out.println("Opção inválida.");
                        }
                    }
                    option = 2;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // Métodos da área restrita para usuários adminstradores
    // #region restrict area
    private void adminMenu() throws Exception {
        int option = -1;
        do {
            System.out.println("ÁREA RESTRITA\n\n" + "1) Usuários\n" + "\n0) Retornar ao menu anterior\n");
            System.out.print("Opção: ");
            option = Integer.parseInt(INPUT.readLine());
            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    new AdminUserController().menu(loggedUser);
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (option != 0);
    }
}
