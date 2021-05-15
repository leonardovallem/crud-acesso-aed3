package entity;

import data.Registro;

import java.io.*;
import java.util.Date;

/*
 * @TODO
 */
public class Resposta implements Registro {
    private int id;
    private int idUsuario;
    public Resposta() {
        this(-1, -1, "", (short) 0, true, System.currentTimeMillis());
    }

    public Resposta(int idUsuario, String pergunta) {
        this(-1, idUsuario, pergunta, (short) 0, true, System.currentTimeMillis());
    }

    public Resposta(int id, int idUsuario, String pergunta, short nota, boolean ativa, long criacao) {
        this.id = id;
        this.idUsuario = idUsuario;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.id);
        saida.writeInt(this.idUsuario);

        return dados.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.idUsuario = entrada.readInt();
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
