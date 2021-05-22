import dal.DAO;
import entity.KeyValuePairs.UsuarioKeyValuePair;
import files.Const;
import entity.Usuario;
import data.impl.HashExtensivel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static DAO<Usuario> dao;
    private static HashExtensivel<UsuarioKeyValuePair> he2;

    public static void main(String[] args) throws Exception {

        dao = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);
        he2 = new HashExtensivel<>(UsuarioKeyValuePair.class.getConstructor(), 4,
                Const.FilesPath + dao.file.replace(".db", ".mail_d.db"),
                Const.FilesPath + dao.file.replace(".db", ".mail_c.db"));

        System.out.println("PERGUNTAS 1.0\n" +
                "=============");

        int option = -1;
        while(option != 0) {
            System.out.println("\nACESSO\n");

            System.out.println("1) Acesso ao sistema\n" +
                    "2) Novo usuário (primeiro acesso)\n" +
		            "3) Esqueci minha senha\n\n" +
                    "0) Sair\n");

            System.out.print("Opção: ");
            option = Integer.parseInt(input.readLine());

            switch (option) {
                case 0:
                    System.out.println("\nAté mais!");
                    break;
                case 1:
                    access();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    forgotPassword();
                    break;
                default:
                    System.out.println("A opção fornecida é inválida.\n");
            }
        }
    }

    private static void access() throws Exception {
        int passwordHash;
        String mail, password;
        Usuario usuario;

        System.out.println("Informe seu email:");

        do {
            mail = input.readLine();
        } while(mail.isBlank());
        usuario = read(mail);

        if(usuario != null) {
            System.out.println("Digite sua senha:");
            int chances = 3;
            do {
               password = input.readLine();
               passwordHash = password.hashCode();

               if(passwordHash != usuario.getSenha()) {
                   chances--;
                   if(chances > 0) System.out.println("Senha incorreta. Tente novamente");
               }
            } while(passwordHash != usuario.getSenha() && chances > 0);

            if (chances == 0) System.out.println("Três tentativas incorretas. Tente novamente mais tarde.");
            else {
                var auth = new Authenticated(usuario);
                auth.loggedInMenu();
            }
        } else System.out.println("Email não encontrado");
    }

    private static void createAccount() throws Exception {
        String mail, name, password, secretQuestion, secretAnswer, option;
        System.out.println("Informe seu email:\n");
        mail = input.readLine();

        if (!mail.isBlank()) {
            if(read(mail) == null) {
                System.out.println("Digite seu nome:");
                name = input.readLine();
                System.out.println("Digite sua senha:");
                password = input.readLine();

                System.out.println("Digite sua pergunta secreta (validação para alteração de senha):");
                secretQuestion = input.readLine();
                System.out.println("Digite a resposta da sua pergunta secreta:");
                secretAnswer = input.readLine();

                System.out.println("Deseja confirmar o cadastro? (Sim/Não)");
                option = input.readLine();
                if (option.charAt(0) == 'S' || option.charAt(0) == 's') {
                    Usuario newUser = new Usuario(-1, name, mail, secretQuestion, secretAnswer, password.hashCode(), false);
                    int id = dao.create(newUser);
                    //Caso o Id do usuário criado for 0 esse é um adminstrador
                    //#region isAdmin
                    if (id == 0) {
                        newUser.setIsAdmin(true);
                        dao.update(newUser);
                    }
                    //#endregion
                    he2.create(new UsuarioKeyValuePair(mail, id));
                    System.out.println("Conta criada com sucesso");
                }
            } else System.out.println("Email já cadastrado");
        }
    }

    private static void forgotPassword() throws Exception {
        System.out.print("Informe seu email: ");
        String email = input.readLine();
        if (!email.isBlank()) {
            Usuario user = read(email);
            if (user != null) {
                System.out.println("Qual a resposta para a pergunta \"" + user.getPerguntaSecreta() + "\"?");
                String answer = input.readLine();

                int chances = 3;
                while(!answer.equals(user.getRespostaSecreta()) && chances > 0) {
                    if(!answer.equals(user.getRespostaSecreta())) {
                        chances--;
                        if(chances > 0) {
                            System.out.println("Resposta incorreta. Tente novamente");
                            answer = input.readLine();
                        } else System.out.println("Três tentativas incorretas. Tente novamente mais tarde.");
                    }
                }
                if(answer.equals(user.getRespostaSecreta())) {
                    System.out.print("Digite sua nova senha: ");
                    user.setSenha(input.readLine().hashCode());
                    if (dao.update(user)) {
                        System.out.println("Senha alterada com sucesso!");
                    } else System.out.println("Erro durante a operação");
                }
            } else {
                System.out.println("Email não encontrado");
            }
        }
    }

    private static Usuario read(String mail) throws Exception {
        Usuario result = null;
        UsuarioKeyValuePair keyPair = he2.read(mail.hashCode());
        if (keyPair != null) {
            result = dao.read(keyPair.getId());
        }
        return result;
    }
}
