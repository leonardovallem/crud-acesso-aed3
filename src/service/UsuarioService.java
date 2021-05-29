package service;

import dal.DAO;
import entity.Usuario;
import files.Const;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UsuarioService {
    private static final BufferedReader INPUT = new BufferedReader(new InputStreamReader(System.in));

    private final Usuario loggedUser;
    private final DAO<Usuario> usuariosDao;

    public UsuarioService(Usuario loggedUser) throws Exception {
        this.loggedUser = loggedUser;
        this.usuariosDao = new DAO<>(Usuario.class.getConstructor(), Const.UsuariosDB);;
    }

    public void list() throws Exception {
        usuariosDao.readAll().forEach(System.out::println);
    }

    public void delete() throws Exception {
        System.out.print("Informe o Número/ID do usuário que você deseja excluír: ");
        int exId = Integer.parseInt(INPUT.readLine());
        Usuario exUser = usuariosDao.read(exId);

        if (exUser == null) {
            System.out.println("Usuário informado não existe, tente mais tarde");
        } else {
            System.out.println("Deseja confirmar exclusão do usuário \"" + exUser.getNome()
                    + "\"? (Sim/Não)\nObs: Todos os dados relacionados à esse também serão excluídos");
            String option = INPUT.readLine();
            if (option.charAt(0) == 'S' || option.charAt(0) == 's') {
                boolean status = new PerguntaService(loggedUser).truncate(exId);

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
}
