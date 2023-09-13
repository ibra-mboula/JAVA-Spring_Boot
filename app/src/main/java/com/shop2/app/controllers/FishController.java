package com.shop2.app.controllers;


import com.shop2.app.models.Fish;
import com.shop2.app.models.User;
import com.shop2.app.services.FishService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FishController {
    @Autowired
    private FishService fishService;

    @PostMapping("/uploadFish")
    public String uploadFish(@RequestParam("image") MultipartFile file, @RequestParam("name") String name, @RequestParam("description") String description) {
        // pour enregistrer l'image du poisson de manière static dans un dossier du code pour qu'il puisse bien l'afficher
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/main/resources/static/images/" + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // je cree un nouveau fish et je lui passe ce que je'ai recuperé depuis mon html
        Fish fish = new Fish(name, description, file.getOriginalFilename());

        // je MAJ la db car ce service est lié au repository
        fishService.saveFish(fish);
        return "redirect:/control";
    }

    @PostMapping("/fish/delete/{id}")
    public String deleteFish(@PathVariable Long id) {
        // je pars chercher le fish correspondant à l'id
        Fish fish = fishService.findById(id);

        //s'il existe je le delete
        if (fish != null) {
            // je delete l'objet Fish de la db
            fishService.delete(fish);
        }

        // redirect de l'utilisateur vers la page de contrôle pour qu'il reste sur la meme page
        return "redirect:/control";
    }

    // pour pouvoir consulter les fish ajouté depuis mon control panel
    @GetMapping("/home/poisson")
    public String displayFishList(HttpSession session, Model model) {
        // je check si l'utilisateur est connecté en consultant la clé de session
        if (session.getAttribute("user") == null) {
            return "redirect:/connexion";
        }
        User user = (User) session.getAttribute("user");

        // je l'ajoute au mlodèle pour le badje
        model.addAttribute("user", user);

        // je recupere ma sequence de poissons
        Iterable<Fish> iterableFishes = fishService.findAll();

        // je dois convertir mes iterable en list >> Iterable<Fish> en List<Fish>
        List<Fish> fishes = new ArrayList<>();
        iterableFishes.forEach(fishes::add);

        // j'ajoute la liste de poissons au modèle pour pouvoir les afficher correctement avec th dans mon html
        model.addAttribute("fishes", fishes);

        String imagesBasePath = "/images/";
        model.addAttribute("imagesBasePath", imagesBasePath);

        return "poisson";
    }

    @PutMapping("/fish/edit/{id}")
    public String editFish(@PathVariable Long id, @RequestParam("name") String name, @RequestParam("description") String description) {
        Fish fish = fishService.findById(id);
        if (fish != null) {
            fish.setName(name);
            fish.setDescription(description);
            fishService.saveFish(fish);
        }
        return "redirect:/control";
    }



}