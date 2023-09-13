package com.shop2.app.services;


import com.shop2.app.models.Fish;
import com.shop2.app.repositories.FishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FishService {
    @Autowired
    private FishRepository fishRepository;


    public Fish saveFish(Fish fish) {

        return fishRepository.save(fish);
    }


    // j'utilise iterable pour renvoyer une sequence d'objet fish que je peux itéré pour acceder individuelmnt a chaque fish
    public Iterable<Fish> findAll() {

        return fishRepository.findAll();
    }

    public Fish findById(Long id) {
        return fishRepository.findById(id).orElse(null);
    }

    public void delete(Fish fish) {
        fishRepository.delete(fish);
    }


}
