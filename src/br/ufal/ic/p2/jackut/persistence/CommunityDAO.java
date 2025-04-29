package br.ufal.ic.p2.jackut.persistence;

import br.ufal.ic.p2.jackut.models.Community;

import java.io.*;
import java.util.Map;

public class CommunityDAO {
    private static final String DIRECTORY = "database";
    private static final String COMMUNITY_FILE = DIRECTORY + "/communities.xml";

    /**
     * Salva o mapa de comunidades em um arquivo.
     *
     * @param communities Mapa de comunidades a serem salvas.
     */
    public void save(Map<String, Community> communities) {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria a pasta caso não exista
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(COMMUNITY_FILE))) {
            out.writeObject(communities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega o mapa de comunidades do arquivo.
     *
     * @return Mapa de comunidades carregado ou null se não houver dados válidos.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Community> load() {
        File file = new File(COMMUNITY_FILE);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<String, Community>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar dados de comunidades. Criando novo arquivo...");
                file.delete(); // Apaga o arquivo corrompido
            }
        }
        return null;
    }
}
