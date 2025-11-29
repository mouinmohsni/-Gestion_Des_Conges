package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Employe;
import com.projet.gestion_des_conges.models.SoldeConge;
import com.projet.gestion_des_conges.repositories.SoldeCongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoldeCongeService implements ISoldeCongeService {

    @Autowired
    private SoldeCongeRepository soldeCongeRepository;

    @Override
    public SoldeConge getSoldeCongeById(Long id) {
        return soldeCongeRepository.findById(id).get();
    }

    @Override
    public SoldeConge createSoldeConge(SoldeConge soldeConge) {
        return soldeCongeRepository.save(soldeConge);
    }

    @Override
    public List<SoldeConge> getAllSoldeConges() {
        return soldeCongeRepository.findAll();
    }

    @Override
    public SoldeConge updateSoldeConge(SoldeConge soldeConge) {
        return soldeCongeRepository.saveAndFlush(soldeConge);
    }

    @Override
    public void deleteSoldeConge(Long id) {
        soldeCongeRepository.deleteById(id);
    }

    public SoldeConge findByEmploye(Employe employe){
        return soldeCongeRepository.findByEmploye(employe);

    }
}
