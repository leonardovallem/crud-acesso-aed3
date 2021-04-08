package dal;

import data.Registro;
import entity.KeyValuePairs.RegistroKeyValuePair;
import service.impl.HashExtensivel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class DAO<T extends Registro> {
    private final Constructor<T> constructor;
    private final RandomAccessFile raf;
    private final HashExtensivel<RegistroKeyValuePair> he;

    public DAO(Constructor<T> constructor, String file) throws Exception {
        this.constructor = constructor;

        File d = new File("data");
        delete(d); d.mkdir();

        this.raf = new RandomAccessFile("data/" + file, "rw");

        he = new HashExtensivel<>(RegistroKeyValuePair.class.getConstructor(), 4,
                "data/" + file.replace(".db", "") + ".hash_d.db",
                "data/" + file.replace(".db", "") + ".hash_c.db");
    }

    public int create(T objeto) throws IOException {
        int objetoID = -1;

        raf.seek(0);
        if(raf.length() == 0) raf.writeInt(++objetoID);
        else objetoID = raf.readInt() + 1;

        objeto.setId(objetoID);
        byte[] objetoBytes = objeto.toByteArray();

        raf.seek(0);
        raf.writeInt(objetoID);

        long lastPos = raf.length();
        raf.seek(lastPos);
        raf.writeInt(objetoBytes.length);
        raf.write(objetoBytes);

        try {
            he.create(new RegistroKeyValuePair(objetoID, lastPos));
        } catch (Exception e) {
            System.out.println("ID #" + objetoID + ": " + e.getMessage());
        }

        return objetoID;
    }

    public T read(int id) throws Exception {
        T objeto = null;

        raf.seek(4);
        RegistroKeyValuePair pcv = he.read(id);
        long objPosition = pcv == null ? -1 : pcv.getPosicao();

        if(objPosition > -1) {
            raf.seek(objPosition);

            int size = raf.readInt();
            byte[] ba = new byte[size]; // instancia array de bytes do tamanho do registro

            long initialPos = raf.getFilePointer(); // guarda a posição inicial do registro
            boolean deleted = raf.readBoolean();

            raf.seek(initialPos);   // retorno à posição inicial do registro após ler ID e lápide
            if (!deleted) {
                raf.read(ba);

                objeto = constructor.newInstance();
                objeto.fromByteArray(ba);   // constroi o objeto

                raf.seek(raf.length()); // invalida a condição do while
            } else System.out.println("Registro deletado");
        }

        return objeto;
    }

    public boolean update(T updatedObject) {
        boolean status = true;

        try {
            raf.seek(4);
            long objPosition = he.read(updatedObject.getId()).getPosicao();

            if(objPosition > -1) {
                raf.seek(objPosition);

                int size = raf.readInt();
                byte[] ba = new byte[size]; // instancia array de bytes do tamanho do registro

                long initialPos = raf.getFilePointer(); // guarda a posição inicial do registro
                raf.skipBytes(4);   // pula o id do registro
                long lapidePos = raf.getFilePointer(); // guarda a posição da lápide do registro
                boolean deleted = raf.readBoolean();

                if (!deleted) {
                    byte[] updatedBa = updatedObject.toByteArray();

                    // caso o novo registro seja maior do que o original
                    if (updatedBa.length > ba.length) {
                        raf.seek(lapidePos);
                        raf.writeBoolean(true); // marca o antigo registro como excluído

                        long novoRegistroPos = raf.length();

                        raf.seek(novoRegistroPos); // vai para o final do arquivo
                        raf.writeInt(updatedBa.length); // armazena o tamanho do novo registro

                        he.update(new RegistroKeyValuePair(updatedObject.getId(), novoRegistroPos));
                    } else raf.seek(initialPos);    // volta ao inicio do registro caso o novo seja menor ou igual

                    raf.write(updatedBa);   // escreve o novo registro
                }
            }
        } catch (Exception e) {
            status = false;
            System.out.println("Erro durante a atualização do registro.");
            e.printStackTrace();
        }

        return status;
    }

    public boolean delete(int id) {
        boolean status = true;

        try {
            raf.seek(4);
            long objPosition = he.read(id).getPosicao();

            if(objPosition > -1) {
                raf.seek(objPosition);

                int size = raf.readInt();
                byte[] ba = new byte[size]; // instancia array de bytes do tamanho do registro

                long initialPos = raf.getFilePointer(); // guarda a posição inicial do registro
                raf.skipBytes(4);   // pula o id do registro
                long lapidePos = raf.getFilePointer(); // guarda a posição da lápide do registro
                boolean deleted = raf.readBoolean();

                // invalida a condição do while
                if(!deleted) {
                    raf.seek(initialPos);   // retorno à posição inicial do registro após ler lápide
                    raf.read(ba);

                    raf.seek(lapidePos);    // volta à posição da lápide e marca como true
                    raf.writeBoolean(true);

                    he.delete(id);
                }
            }
        } catch (Exception e) {
            status = false;
            System.out.println("Erro durante a remoção do registro.");
            e.printStackTrace();
        }

        return status;
    }

    private static void delete(File folder) {
        List<File> files = Arrays.asList(folder.listFiles().clone());
        files.forEach(File::delete);
        folder.delete();
    }
}
