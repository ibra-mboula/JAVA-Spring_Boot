package com.shop2.app.repositories;


import com.shop2.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //optional c'est pour eviter les erreur du type null exeption dans le cas ou
    // un user n'est pas retourné il va juste indiquer Optional.empty()
    Optional<User> findByUsername(String username); // méthode qui va servir à chercher les users

}