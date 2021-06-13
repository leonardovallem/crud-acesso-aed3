package entity;

import data.Registro;

import java.io.*;

public class Nota implements Registro {
    private int id;
    private int idUsuario;
    private int idPostagem;
    private byte tipo;
    private float voto;

    public Nota() {
        this(-1, -1, -1, (byte) 1, 0);
    }

    public Nota(int idUsuario, int idPostagem, byte tipo, float voto) {
        this(-1, idUsuario, idPostagem, tipo, voto);
    }

    public Nota(int id, int idUsuario, int idPostagem, byte tipo, float voto) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idPostagem = idPostagem;
        this.tipo = tipo;
        this.voto = voto;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        //saida.writeLong(this.criacao);

        return dados.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        //this.criacao = entrada.readLong();
    }
}
