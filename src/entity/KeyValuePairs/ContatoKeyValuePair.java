package entity.KeyValuePairs;

import service.RegistroHashExtensivel;

import java.io.*;

public class ContatoKeyValuePair implements RegistroHashExtensivel<ContatoKeyValuePair> {

    private String telefone;
    private int id;
    private static final short TAMANHO = 44;

    public ContatoKeyValuePair() {
        this("", -1);
    }

    public ContatoKeyValuePair(String e, int i) {
        try {
            this.telefone = e
                    .replace("-", "")
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", "");
            this.id = i;
            if (e.length() + 4 > TAMANHO)
                throw new Exception("Número de caracteres do telefone maior que o permitido. Os dados serão cortados.");
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return this.telefone.hashCode();
    }

    public short size() {
        return TAMANHO;
    }

    public String toString() {
        return this.telefone + ";" + this.id;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(telefone);
        dos.writeInt(id);
        byte[] bs = baos.toByteArray();
        byte[] bs2 = new byte[TAMANHO];
        for (int i = 0; i < TAMANHO; i++)
            bs2[i] = ' ';
        for (int i = 0; i < bs.length && i < TAMANHO; i++)
            bs2[i] = bs[i];
        return bs2;
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.telefone = dis.readUTF();
        this.id = dis.readInt();
    }

}