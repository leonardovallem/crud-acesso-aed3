package entity.KeyValuePairs;

import service.RegistroHashExtensivel;

import java.io.*;

public class RegistroKeyValuePair implements RegistroHashExtensivel<RegistroKeyValuePair> {

    private int id;
    private long posicao;

    public RegistroKeyValuePair() {
        this(-1, -1);
    }

    public RegistroKeyValuePair(int id, long posicao) {
        try {
            this.id = id;
            this.posicao = posicao;
            if (id < -1) throw new Exception("Valor de ID inválido.");
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    public long getPosicao() {
        return posicao;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public short size() {
        return 4+8; // 4 bytes (ID) + 8 byets (posição)
    }

    public String toString() {
        return this.id + " : " + this.posicao;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeLong(posicao);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.posicao = dis.readLong();
    }

}