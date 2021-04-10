package entity;

import data.Registro;

import java.io.*;

public class Usuario implements Registro {
    private int id;
    private String nome;
    private String email;
    private int senha;

    public Usuario() {}

    public Usuario(int id, String nome, String email, int senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);
        this.id = entrada.readInt();
        this.nome = entrada.readUTF();
        this.email = entrada.readUTF();
        this.senha = entrada.readInt();
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);
        saida.writeInt(this.id);
        saida.writeUTF(this.nome);
        saida.writeUTF(this.email);
        saida.writeInt(this.senha);
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

    public int getSenha() {
        return senha;
    }
    public void setSenha(int senha) {
        this.senha = senha;
    }
}
