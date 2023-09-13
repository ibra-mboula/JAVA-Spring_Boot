package com.shop2.app.controllers;

import com.shop2.app.models.User;
import com.shop2.app.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/connexion")
    public String connexion() {
        return "connexion";
    }

    @PostMapping("/connexion")
    // connexion demande les value de username et password qui seront fourni par le user
    // je déclare également un type HttpSession pour gerer le suivi de session
    public String connexion(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {

        // on verifie que le user name saisi appartient à la db
        Optional<User> optionalUser = userService.findByUsername(username);

        System.out.println("optionalUser contr" +optionalUser);

        // si le username est dans la db et son mdp est correcte je return une page
        if (optionalUser.isPresent() && userService.checkPassword(password, optionalUser.get().getPassword())) {
            //je recupere son nom pour le suivis de ssession
            session.setAttribute("user", optionalUser.get());
            System.out.println("Session ID: " + session.getId());


            if (optionalUser.get().getIsAdmin()) {
                return "redirect:/control";
            } else {
                return "redirect:/home";
            }
        } else {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            return "connexion";
        }
    }

    @GetMapping("/")
    public String welcome(@ModelAttribute User user) {
        userService.save(user); // pas forcement utile a ce stade du code, vu que j'arrive sur la page de connexion
        return "redirect:/connexion";
    }


    @GetMapping("/inscription")
    public String inscription(Model model) {

        // je cree un nouvelle objet user vide qui sera fourni a la vu pour le formulaire d'inscription
        model.addAttribute("user", new User());
        return "inscription";
    }



    @PostMapping("/inscription")
    public String inscription(@ModelAttribute User user, Model model) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        System.out.printf(String.valueOf(existingUser));

        if (existingUser.isPresent()) {
            model.addAttribute("error", "Ce nom d'utilisateur est déjà utilisé.");
            model.addAttribute("user", user); // ajout de l'utilisateur dans le modèle pour que les champs du formulaire soient pré-remplis
            return "inscription";
        } else {
            userService.save(user);
            return "redirect:/connexion";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/connexion";
    }


}
