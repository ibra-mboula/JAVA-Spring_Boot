package com.shop2.app.models;

import jakarta.persistence.*; // pour la persistance de données et sert à utiliser @Entity, @Table ect..

@Entity // pour dire que User est une entité persistante, elle sera mappée à une table dans la db.
@Table(name = "user") // le nom de la table dans ma db, mon entité users sera relié à cette table

public class User {

    @Id // clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pour dire que c'est ma db qui generer automatiquement les id
    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    private String password;
    private boolean isAdmin; // 1 ou 0

}
