package br.ufal.ic.p2.jackut.persistence;

import br.ufal.ic.p2.jackut.models.User;

import java.io.*;
import java.util.List;

public class UserDAO {

    private static final String USERS_FILE = "database/users.xml";

    // Método para salvar os usuários
    public void save(List<User> users) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar os usuários
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
