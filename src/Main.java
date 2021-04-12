import dal.DAO;
import entity.KeyValuePairs.UsuarioKeyValuePair;
import entity.Usuario;
import service.impl.HashExtensivel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static DAO<Usuario> dao;
    private static HashExtensivel<UsuarioKeyValuePair> he2;

    public static void main(String[] args) throws Exception {

        dao = new DAO<>(Usuario.class.getConstructor(),"arq");
        he2 = new HashExtensivel<>(UsuarioKeyValuePair.class.getConstructor(), 4,
                "src/files/" + dao.file.replace(".db", "") + ".mail_d.db",
                "src/files/" + dao.file.replace(".db", "") + ".mail_c.db");

        System.out.println("PERGUNTAS 1.0\n" +
                "=============\n");

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
        int id, passwordHash;
        String mail, password;
        Usuario usuario;

        System.out.println("Informe seu email:");

        do {
            mail = input.readLine();
            id = read(mail);
        } while(mail.isBlank());

        if(id != -1) {
            usuario = dao.read(id);
//            he2.read(id);

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
            else loggedInMenu();
        } else System.out.println("Email não encontrado");
    }

    private static void loggedInMenu() throws IOException {
        System.out.println("Seja bem-vindo!\nO que deseja fazer?");

        int option = -1;

        while(option != 0) {
            System.out.println("\n1) Acessar perguntas e respostas (EM BREVE)" +
                    "\n\n0) Logout");
            option = Integer.parseInt(input.readLine());

            System.out.println();
            switch (option) {
                case 0:
                    System.out.println("Fazendo logout ...\n");
                    break;
                case 1:
                    System.out.println("==== EM BREVE ====");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void createAccount() throws Exception {
        String mail, name, password, secretQuestion, secretAnswer, option;
        System.out.println("Informe seu email:\n");
        mail = input.readLine();

        if (!mail.isBlank()) {
            if(read(mail) == -1) {
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
                    int id = dao.create(new Usuario(-1, name, mail, secretQuestion, secretAnswer, password.hashCode()));
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
            int id = read(email);
            if (id != -1) {
                Usuario user = dao.read(id);
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

    private static int read(String mail) throws Exception {
        int id = -1;
        UsuarioKeyValuePair aux = he2.read(mail.hashCode());
        if(aux != null) id = aux.getId();
        return id;
    }
}
