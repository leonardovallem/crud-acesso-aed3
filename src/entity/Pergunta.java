package entity;

import data.Registro;

import java.io.*;
import java.util.Date;

public class Pergunta implements Registro {
    private int id;
    private int idUsuario;
    private String pergunta;
    private short nota;
    private boolean ativa;
    private long criacao;

    public Pergunta() {
        this(-1, -1, "", (short) 0, true, System.currentTimeMillis());
    }

    public Pergunta(int idUsuario, String pergunta) {
        this(-1, idUsuario, pergunta, (short) 0, true, System.currentTimeMillis());
    }

    public Pergunta(int id, int idUsuario, String pergunta, short nota, boolean ativa, long criacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.pergunta = pergunta;
        this.nota = nota;
        this.ativa = ativa;
        this.criacao = criacao;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.id);
        saida.writeInt(this.idUsuario);
        saida.writeUTF(this.pergunta);
        saida.writeShort(this.nota);
        saida.writeBoolean(this.ativa);
        saida.writeLong(this.criacao);

        return dados.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.idUsuario = entrada.readInt();
        this.pergunta = entrada.readUTF();
        this.nota = entrada.readShort();
        this.ativa = entrada.readBoolean();
        this.criacao = entrada.readLong();
    }

    @Override
    public String toString() {
        return new Date(criacao) + " (" + (ativa ? "ATIVA" : "ARQUIVADA") + ")\n" + pergunta;
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

    public String getPergunta() {
        return pergunta;
    }
    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public short getNota() {
        return nota;
    }
    public void setNota(short nota) {
        this.nota = nota;
    }

    public boolean isAtiva() {
        return ativa;
    }
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public long getCriacao() {
        return criacao;
    }
    public void setCriacao(long criacao) {
        this.criacao = criacao;
    }
}
