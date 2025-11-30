package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Employe;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import com.projet.gestion_des_conges.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CongeService {

    private final CongeRepository congeRepository;
    private final EmployeRepository employeRepository; // On en aura besoin

    // 1. Injection par constructeur
    public CongeService(CongeRepository congeRepository, EmployeRepository employeRepository) {
        this.congeRepository = congeRepository;
        this.employeRepository = employeRepository;
    }



    public Conge getCongeById(Long id) {
        return congeRepository.findById(id).get();
    }


    public Conge createConge(Conge conge) {
        return congeRepository.save(conge);
    }


    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }


    public Conge updateConge(Conge conge) {
        return congeRepository.saveAndFlush(conge);
    }


    public void deleteConge(Long id) {
        congeRepository.deleteById(id);
    }

    public List<Conge> findByEmploye(Employe employe){
        return congeRepository.findByEmploye(employe);
    }
}
