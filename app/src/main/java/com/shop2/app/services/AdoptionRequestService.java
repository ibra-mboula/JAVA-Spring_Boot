package com.shop2.app.services;

import com.shop2.app.models.AdoptionRequest;
import com.shop2.app.repositories.AdoptionRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionRequestService {

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    public void save(AdoptionRequest adoptionRequest) {
        adoptionRequestRepository.save(adoptionRequest);
    }


    public List<AdoptionRequest> findByUsername(String username) {
        return adoptionRequestRepository.findByUsername(username);
    }

    public List<AdoptionRequest> findAll() {
        return adoptionRequestRepository.findAll();
    }


    @Transactional
    public void deleteAdoptionRequest(Long id) {
        adoptionRequestRepository.deleteById(id);
    }


}
