package br.ufal.ic.p2.jackut.persistence;

import java.io.*;
import java.util.List;

public class SessionDAO {

    private static final String SESSIONS_FILE = "database/sessions.xml";

    // Método para salvar as sessões
    public void save(List<String> sessions) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SESSIONS_FILE))) {
            out.writeObject(sessions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar as sessões
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
