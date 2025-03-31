/**
 * Classe responsável pela persistência dos usuários no sistema.
 * Os usuários são armazenados e recuperados a partir de um arquivo XML.
 *
 * @author SeuNome
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.persistence;

import br.ufal.ic.p2.jackut.models.User;

import java.io.*;
import java.util.List;

public class UserDAO {

    private static final String DIRECTORY = "database";
    private static final String USERS_FILE = DIRECTORY + "/users.xml";

    /**
     * Salva a lista de usuários em um arquivo.
     *
     * @param users Lista de usuários a serem salvos.
     */
    public void save(List<User> users) {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria a pasta caso não exista
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de usuários do arquivo.
     *
     * @return Lista de usuários carregada ou null se não houver dados válidos.
     */
    public List<User> load() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (List<User>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar dados de usuários. Criando novo arquivo...");
                file.delete(); // Apaga o arquivo corrompido
            }
        }
        return null;
    }
}
