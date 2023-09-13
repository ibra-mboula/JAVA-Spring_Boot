package com.shop2.app.controllers;

import com.shop2.app.models.AdoptionRequest;
import com.shop2.app.models.Fish;
import com.shop2.app.models.User;
import com.shop2.app.services.AdoptionRequestService;
import com.shop2.app.services.FishService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
@Controller
public class AdoptionRequestController {


    @Autowired
    private FishService fishService;

    @Autowired
    private AdoptionRequestService adoptionRequestService;



    @GetMapping("/home/poisson/adoption")
    public String adoptionForm(@RequestParam("fishId") Long fishId, Model model, HttpSession session) {
        // on check si le user est connecté
        if (session.getAttribute("user") == null) {
            return "redirect:/connexion";
        }
        // récupére les info du fish
        Fish fish = fishService.findById(fishId);
        if (fish == null) {
            return "redirect:/home/poisson";
        }
        // add les info du fish et du user au modèle
        User user = (User) session.getAttribute("user");
        model.addAttribute("fish", fish);
        model.addAttribute("username", user.getUsername());
        // j'ajoute  l'utilisateur courant au modèle pour le badge
        model.addAttribute("user", user);

        return "adoption";
    }

    @PostMapping("/home/poisson/adoption")
    public String submitAdoptionRequest(@RequestParam("username") String username,
                                        @RequestParam("nom") String nom,
                                        @RequestParam("prenom") String prenom,
                                        @RequestParam("adresse") String adresse,
                                        @RequestParam("email") String email,
                                        @RequestParam("motivation") String motivation,
                                        @RequestParam("fishId") Long fishId) {

        //  en fonction de l'ID du fish selectionner je le recupère
        Fish fish = fishService.findById(fishId);


        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setUsername(username);
        adoptionRequest.setNom(nom);
        adoptionRequest.setPrenom(prenom);
        adoptionRequest.setAdresse(adresse);
        adoptionRequest.setEmail(email);
        adoptionRequest.setMotivation(motivation);

        if (fish != null) {
            adoptionRequest.setFishName(fish.getName());
        }

        adoptionRequestService.save(adoptionRequest);

        return "redirect:/home/poisson";
    }



    @GetMapping("/home/MesDemandes")
    public String mesDemandes(Model model, @SessionAttribute("user") User user, HttpSession session) {

        // on check si le user est connecté
        if (session.getAttribute("user") == null) {
            return "redirect:/connexion";
        }

        // je recupère les demande du users depuis la db
        user = (User) session.getAttribute("user");
        // je check la list des demandes et je recupère uniquement celle du user concerné
        List<AdoptionRequest> adoptionRequests = adoptionRequestService.findByUsername(user.getUsername());

        // add les demandes d'adoption au modèle pour pouvoir les afficher dans la vu
        model.addAttribute("adoptionRequests", adoptionRequests);
        model.addAttribute("user", user);


        return "demandes";
    }



}
