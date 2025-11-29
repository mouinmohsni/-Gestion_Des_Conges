package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Employe;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CongeService implements ICongeService {

    @Autowired
    private CongeRepository congeRepository;

    @Override
    public Conge getCongeById(Long id) {
        return congeRepository.findById(id).get();
    }

    @Override
    public Conge createConge(Conge conge) {
        return congeRepository.save(conge);
    }

    @Override
    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }

    @Override
    public Conge updateConge(Conge conge) {
        return congeRepository.saveAndFlush(conge);
    }

    @Override
    public void deleteConge(Long id) {
        congeRepository.deleteById(id);
    }

    public List<Conge> findByEmploye(Employe employe){
        return congeRepository.findByEmploye(employe);
    }
}
