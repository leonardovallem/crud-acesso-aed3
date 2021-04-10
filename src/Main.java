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
            System.out.println("ACESSO\n");

            System.out.println("1) Acesso ao sistema\n" +
                    "2) Novo usuário (primeiro acesso)\n\n" +
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
                default:
                    System.out.println("A opção fornecida é inválida.\n");
            }
        }
    }

    /**
     * Método que permite acesso ao sistema
     * Não necessita do usuário estar logado
     */
    private static void access() throws Exception {
        int id;
        String mail;
        Usuario usuario;
        String password;
        int passwordHash;
        System.out.println("Informe seu email:\n");
        mail = input.readLine();
        id = read(mail);

        if (!mail.isBlank()) {
            if(id != -1) {
               usuario = dao.read(id);
               he2.read(id);
               System.out.println("Digite sua senha:");
               password = input.readLine();
               passwordHash = password.hashCode();
               if (passwordHash == usuario.getSenha()) {
                   System.out.println("Seja bem-vindo!");
               } else System.out.println("As senhas não coincidem");
            } else System.out.println("Email não encontrado");
        }
    }

    /**
     * Método que permite a criação de uma conta
     * @link https://replit.com/@kutova/TabelaHashExtensivel
     */
    private static void createAccount() throws Exception {
        String mail;
        String name;
        String password;
        String option;
        System.out.println("Informe seu email:\n");
        mail = input.readLine();

        if (!mail.isBlank()) {
            if(read(mail) == -1)
            {
                System.out.println("Digite seu nome:");
                name = input.readLine();
                System.out.println("Digite sua senha:");
                password = input.readLine();
                System.out.println("Deseja confirmar o cadastro? (Sim/Não)");
                option = input.readLine();
                if (option.charAt(0) == 'S' || option.charAt(0) == 's') {
                    int id = dao.create(new Usuario(-1,name,mail,password.hashCode()));
                    he2.create(new UsuarioKeyValuePair(mail,id));
                    System.out.println("Conta criada com sucesso");
                }
            } else System.out.println("Email já cadastrado");
        }
    }

    private static int read(String mail) throws Exception
    {
        int id = -1;
        UsuarioKeyValuePair aux = he2.read(mail.hashCode());
        if(aux != null) id = aux.getId();
        return id;
    }

}
