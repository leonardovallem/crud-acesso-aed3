package entity;

import java.util.Date;

public class Pergunta extends Postagem {
    public Pergunta() {
        this(-1, -1, "", 0, 0, true, System.currentTimeMillis());
    }

    public Pergunta(int idUsuario, String pergunta) {
        this(-1, idUsuario, pergunta, 0, 0, true, System.currentTimeMillis());
    }

    public Pergunta(int id, int idUsuario, String pergunta, int avaliacoes, float nota, boolean ativa, long criacao) {
        super(id, idUsuario, pergunta, avaliacoes, nota, ativa, criacao);
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Usuario usuario) {
        return new Date(criacao) + " - ☆ " + getNotaGeral() + " em " + this.avaliacoes + " Avaliações\n" +
                (usuario == null ? "" : "\t" + usuario.getNome() + " perguntou:\n")
                + "\t  ↳ " + conteudo + "\n";
    }

    private String getNotaGeral() {
        return this.nota <= 0 ? "N/A" : String.format("%.1f", this.nota);
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
        return conteudo;
    }
    public void setPergunta(String pergunta) {
        this.conteudo = pergunta;
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
