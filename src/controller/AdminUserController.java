package controller;

import entity.Usuario;
import service.UsuarioService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AdminUserController {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    public void menu(Usuario loggedUser) throws Exception {
        UsuarioService usuarioService = new UsuarioService(loggedUser);

        int option = -1;
        do {
            System.out.println("ÁREA RESTRITA > USUÁRIOS\n\n" + "1) Listar\n" + "2) Excluír\n"
                    + "\n0) Retornar ao menu anterior\n");
            System.out.print("Opção: ");
            option = Integer.parseInt(INPUT.readLine());
            switch (option) {
                case 0:
                    System.out.println("Voltando ao menu anterior ...\n");
                    break;
                case 1:
                    usuarioService.list();
                    break;
                case 2:
                    usuarioService.delete();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (option != 0);
    }
}
