/**
 * Classe respons�vel pela persist�ncia dos usu�rios no sistema.
 * Os usu�rios s�o armazenados e recuperados a partir de um arquivo XML.
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
     * Salva a lista de usu�rios em um arquivo.
     *
     * @param users Lista de usu�rios a serem salvos.
     */
    public void save(List<User> users) {
        File dir = new File(DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria a pasta caso n�o exista
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de usu�rios do arquivo.
     *
     * @return Lista de usu�rios carregada ou null se n�o houver dados v�lidos.
     */
    public List<User> load() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (List<User>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar dados de usu�rios. Criando novo arquivo...");
                file.delete(); // Apaga o arquivo corrompido
            }
        }
        return null;
    }
}
