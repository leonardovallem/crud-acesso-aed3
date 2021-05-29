package entity;

import data.Registro;

import java.io.*;
import java.util.Date;

public class Pergunta implements Registro {
    private int id;
    private int idUsuario;
    private String pergunta;
    private int avaliacoes;
    private float nota;
    private boolean ativa;
    private long criacao;

    public Pergunta() {
        this(-1, -1, "", 0, 0, true, System.currentTimeMillis());
    }

    public Pergunta(int idUsuario, String pergunta) {
        this(-1, idUsuario, pergunta, 0, 0, true, System.currentTimeMillis());
    }

    public Pergunta(int id, int idUsuario, String pergunta, int avaliacoes, float nota, boolean ativa, long criacao) {
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
        saida.writeInt(this.avaliacoes);
        saida.writeFloat(this.nota);
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
        this.avaliacoes = entrada.readInt();
        this.nota = entrada.readFloat();
        this.ativa = entrada.readBoolean();
        this.criacao = entrada.readLong();
    }

    @Override
    public String toString() {
        return new Date(criacao) + " (" + (ativa ? "ATIVA" : "ARQUIVADA") + ")\n" + "  ↳ " + pergunta + "\n";
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void rate(float novaNota) {
        setNota( ((this.avaliacoes++ * this.nota) + novaNota) / this.avaliacoes );
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
    public float getNota() {
        return nota;
    }
    public void setNota(float nota) {
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
    public int getAvaliacoes() {
        return avaliacoes;
    }
    public void setAvaliacoes(int avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
