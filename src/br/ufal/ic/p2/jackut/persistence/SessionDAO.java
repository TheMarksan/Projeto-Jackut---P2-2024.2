package br.ufal.ic.p2.jackut.persistence;

import java.io.*;
import java.util.List;

public class SessionDAO {

    private static final String SESSIONS_FILE = "database/sessions.xml";

    // M�todo para salvar as sess�es
    public void save(List<String> sessions) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SESSIONS_FILE))) {
            out.writeObject(sessions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // M�todo para carregar as sess�es
    public List<String> load() {
        File file = new File(SESSIONS_FILE);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (List<String>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar dados de sess�es. Criando novo arquivo...");
                file.delete(); // Apaga o arquivo corrompido
            }
        }
        return null;
    }
}
