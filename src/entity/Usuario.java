package entity;

import data.Registro;

import java.io.*;

public class Usuario implements Registro {
    private int id;
    private String nome;
    private String email;
    private String perguntaSecreta;
    private String respostaSecreta;
    private int senha;
    private boolean isAdmin;

    public Usuario() {}

    public Usuario(int id, String nome, String email, String perguntaSecreta, String respostaSecreta, int senha, boolean isAdmin) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perguntaSecreta = perguntaSecreta;
        this.respostaSecreta = respostaSecreta;
        this.senha = senha;
        this.isAdmin = isAdmin;
    }

    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.id = entrada.readInt();
        this.nome = entrada.readUTF();
        this.email = entrada.readUTF();
        this.perguntaSecreta = entrada.readUTF();
        this.respostaSecreta = entrada.readUTF();
        this.senha = entrada.readInt();
        this.isAdmin = entrada.readBoolean();
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeUTF(this.nome);
        saida.writeUTF(this.email);
        saida.writeUTF(this.perguntaSecreta);
        saida.writeUTF(this.respostaSecreta);
        saida.writeInt(this.senha);
        saida.writeBoolean(this.isAdmin);
        return dados.toByteArray();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerguntaSecreta() {
	return perguntaSecreta;
    }
    public void setPerguntaSecreta(String perguntaSecreta) {
	this.perguntaSecreta = perguntaSecreta;
    }

    public String getRespostaSecreta() {
	return respostaSecreta;
    }
    public void setRespostaSecreta(String respostaSecreta) {
	this.respostaSecreta = respostaSecreta;
    }

    public int getSenha() {
        return senha;
    }
    public void setSenha(int senha) {
        this.senha = senha;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
