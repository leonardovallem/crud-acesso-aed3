package entity;

import data.Registro;

import java.io.*;
import java.util.Date;

public class Resposta implements Registro {
    private int id;
    private int idUsuario;
    private int idPergunta;
    private String resposta;
    private int avaliacoes;
    private float nota;
    private boolean ativa;
    private long criacao;

    public Resposta() {
        this(-1, -1, -1, "", 0, 0, true, System.currentTimeMillis());
    }

    public Resposta(int idUsuario, int idPergunta, String resposta) {
        this(-1, idUsuario, idPergunta, resposta, 0, 0, true, System.currentTimeMillis());
    }

    public Resposta(int id, int idUsuario, int idPergunta, String resposta, int avaliacoes, float nota, boolean ativa, long criacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idPergunta = idPergunta;
        this.resposta = resposta;
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
        saida.writeInt(this.idPergunta);
        saida.writeUTF(this.resposta);
        saida.writeInt(this.avaliacoes);
        saida.writeFloat(this.nota);
        saida.writeBoolean(this.ativa);
        saida.writeLong(this.criacao);

        return dados.toByteArray();
    }


    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Usuario usuario) {
        return new Date(criacao) + " - ☆ " + getNotaGeral() + " em " + this.avaliacoes + " Avaliações\n" +
                (usuario == null ? "" : "\t| " + usuario.getNome() + " respondeu:\n")
                + "\t|  ↳ " + resposta + "\n\t|";
    }

    private String getNotaGeral() {
        return this.nota <= 0 ? "N/A" : String.format("%.1f", this.nota);
    }

    @Override
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);

        this.id = entrada.readInt();
        this.idUsuario = entrada.readInt();
        this.idPergunta = entrada.readInt();
        this.resposta = entrada.readUTF();
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

    public void rate(float novaNota) {
        setNota( ((this.avaliacoes++ * this.nota) + novaNota) / this.avaliacoes );
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public int getIdPergunta() {
        return idPergunta;
    }
    public void setIdPergunta(int idPergunta) {
        this.idPergunta = idPergunta;
    }
    public String getResposta() {
        return resposta;
    }
    public void setResposta(String resposta) {
        this.resposta = resposta;
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
