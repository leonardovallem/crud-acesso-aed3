package entity;

import data.Registro;

import java.io.*;

public class Postagem implements Registro {
    protected int id;
    protected int idUsuario;
    protected String conteudo;
    protected int avaliacoes;
    protected float nota;
    protected boolean ativa;
    protected long criacao;

    public Postagem(int id, int idUsuario, String conteudo, int avaliacoes, float nota, boolean ativa, long criacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.conteudo = conteudo;
        this.nota = nota;
        this.ativa = ativa;
        this.criacao = criacao;
    }

    public void rate(float novaNota) {
        setNota( ((this.avaliacoes++ * this.nota) + novaNota) / this.avaliacoes );
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.id);
        saida.writeInt(this.idUsuario);
        saida.writeUTF(this.conteudo);
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
        this.conteudo = entrada.readUTF();
        this.avaliacoes = entrada.readInt();
        this.nota = entrada.readFloat();
        this.ativa = entrada.readBoolean();
        this.criacao = entrada.readLong();
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }
    public String getConteudo() {
        return conteudo;
    }
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    public int getAvaliacoes() {
        return avaliacoes;
    }
    public void setAvaliacoes(int avaliacoes) {
        this.avaliacoes = avaliacoes;
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
}
