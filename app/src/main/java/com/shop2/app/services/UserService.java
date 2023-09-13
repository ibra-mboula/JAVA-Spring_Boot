package com.shop2.app.services;


import com.shop2.app.models.User;
import com.shop2.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired // pour demander a spring d' injecter automatiquement une instance de userRepo dans userService
    private UserRepository userRepository;


    //cherche un username depuis ma database à partir du nom donnée
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    //pour recuper la liste des user depuis la db
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //supprimer un users
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private String encodePassword(String password) {
        // Générer un sel aléatoire pour le mot de passe
        String salt = BCrypt.gensalt();

        // Hache le mot de passe en utilisant le sel generer plus haut et le nbr de tours, il est à 10 par defaut

        return BCrypt.hashpw(password, salt);
    }

    public boolean checkPassword(String currentPassword, String encodedPassword) {
        // je check si le mdp en utilisant le mdp haché de la db
        return BCrypt.checkpw(currentPassword, encodedPassword);
    }


    public User save(User user) {
        // Encoder le mot de passe avant de l'enregistrer, il va me return un mdp haché
        String encodedPassword = encodePassword(user.getPassword());

        //ici je hache le mdp entrer par l'utilisateur
        user.setPassword(encodedPassword);

        //je MAJ dans la db
        return userRepository.save(user);
    }






}
