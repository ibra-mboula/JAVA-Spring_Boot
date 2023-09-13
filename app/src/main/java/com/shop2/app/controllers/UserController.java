package com.shop2.app.controllers;


import com.shop2.app.models.AdoptionRequest;
import com.shop2.app.models.Fish;
import com.shop2.app.models.User;
import com.shop2.app.services.AdoptionRequestService;
import com.shop2.app.services.FishService;
import com.shop2.app.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private FishService fishService;


    @Autowired
    private AdoptionRequestService adoptionRequestService;


    @GetMapping("/control")
    public String showControlPanel(HttpSession session, Model model) {

        //  USERS AUTH ETC.... ********************************************************************

        // je vérifie si le user est connecté en vérifiant la présence de la clé "user" dans la session
        if (session.getAttribute("user") == null) {
            // Redirige vers la page de connexion si le user n'est pas connecté
            return "redirect:/connexion";
        }
        User user = (User) session.getAttribute("user");
        // si mon user est un admin alors il va sur la page de control si non c'est un simple user donc il va sur home
        if (!user.getIsAdmin()) {
            return "redirect:/home";
        }
        //ici je recupere tous les users inscrit
        List<User> users = userService.findAll();
        // j'ajoute la liste d'utilisateurs au modèle pour l'affichage dans la vue
        model.addAttribute("users", users);

        // j'ajoute  l'utilisateur courant au modèle pour le badge
        model.addAttribute("user", user);


        // FISH POUR LES VISUALISER DEPUIS CONTROL ********************************************************************

        // il faut add les poissons au modèle si non on ne peut pas les visualiser deopuis control
        //je recuper ma seq de fish
        Iterable<Fish> iterableFishes = fishService.findAll();
        //je le cast en list
        List<Fish> fishes = new ArrayList<>();
        iterableFishes.forEach(fishes::add);
        //j'ajoute les info du fish au modele
        model.addAttribute("fishes", fishes);
        // j'ajoute aussi les images
        String imagesBasePath = "/images/";
        model.addAttribute("imagesBasePath", imagesBasePath);



        // Récupération des demandes d'adoption POUR LES VISUALISER DEPUIS CONTROL ********************************************************************

        // Récupérer all adoption requests
        List<AdoptionRequest> adoptionRequests = adoptionRequestService.findAll();

        // Add the adoption requests to the model
        model.addAttribute("adoptionRequests", adoptionRequests);

        // Ajout des demandes d'adoption au modèle
       // model.addAttribute("requests", requests);



        return "control";



    }

    @PostMapping("/control/delete-user")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
        return "redirect:/control";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // on verifie si le user est connecté
        if (session.getAttribute("user") == null) {
            return "redirect:/connexion";
        }
        User user = (User) session.getAttribute("user");

        // add l'utilisateur au modèle pour le badge
        model.addAttribute("user", user);

        return "home";
    }


    @PostMapping("/adoptionRequests/delete/{id}")
    public String deleteAdoptionRequest(@PathVariable("id") Long id) {
        adoptionRequestService.deleteAdoptionRequest(id);
        return "redirect:/control";
    }





}
