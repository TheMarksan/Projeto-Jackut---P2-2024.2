/**
 * Classe responsável pela persistência das sessões no sistema.
 * As sessões são armazenadas e recuperadas a partir de um arquivo XML.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.persistence;

import java.io.*;
import java.util.List;

public class SessionDAO {

    private static final String SESSIONS_FILE = "database/sessions.xml";

    /**
     * Salva a lista de sessões em um arquivo.
     *
     * @param sessions Lista de sessões a serem salvas.
     */
    public void save(List<String> sessions) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SESSIONS_FILE))) {
            out.writeObject(sessions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de sessões do arquivo.
     *
     * @return Lista de sessões carregadas ou null se não houver dados válidos.
     */
    public List<String> load() {
        File file = new File(SESSIONS_FILE);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (List<String>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar dados de sessões. Criando novo arquivo...");
                file.delete(); // Apaga o arquivo corrompido
            }
        }
        return null;
    }
}
