package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.SoldeCreationDto;
import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Employe;
import com.projet.gestion_des_conges.models.SoldeConge;
import com.projet.gestion_des_conges.models.TypeConge;
import com.projet.gestion_des_conges.repositories.EmployeRepository;
import com.projet.gestion_des_conges.repositories.SoldeCongeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SoldeCongeService {

    
    private final SoldeCongeRepository soldeCongeRepository;
    private final EmployeRepository employeRepository;

    public SoldeCongeService(SoldeCongeRepository soldeCongeRepository, EmployeRepository employeRepository) {
        this.soldeCongeRepository = soldeCongeRepository;
        this.employeRepository = employeRepository;
    }


    public SoldeConge getSoldeCongeById(Long id) {
        Optional<SoldeConge> soldeConge = soldeCongeRepository.findById(id);
        if (soldeConge.isEmpty()) {
            throw new RuntimeException("SoldeConge not found");
        }

        return soldeConge.get();
    }

    
    public SoldeConge createSoldeConge(SoldeCreationDto data) {
        Optional<Employe> employeOptional = employeRepository.findById(data.getEmployeId());
       if(employeOptional.isEmpty() ){
           throw new IllegalArgumentException("L'employé fourni est invalide ou non sauvegardé.");

       }
       Employe employe = employeOptional.get();
       int anneeActuelle = LocalDate.now().getYear();
       int annee = data.getAnnee();
       int joursAlloues = data.getJoursAlloues();

        if (annee < anneeActuelle - 1 || annee > anneeActuelle + 1) {
            throw new IllegalArgumentException("L'année " + annee + " est invalide. Elle doit être proche de l'année actuelle.");
        }

        if (joursAlloues <= 0 || joursAlloues > 60) {
            throw new IllegalArgumentException("Le nombre de jours alloués (" + joursAlloues + ") est invalide. Il doit être positif et réaliste.");
        }

        Optional<SoldeConge> soldeExistant = soldeCongeRepository.findByEmployeAndAnnee(employe, annee);
        if (soldeExistant.isPresent()) {
            throw new IllegalStateException("Un solde de congés existe déjà pour l'employé " + employe.getNom() + " pour l'année " + annee + ".");
        }


        SoldeConge nouveauSolde = new SoldeConge();
        nouveauSolde.setEmploye(employe);
        nouveauSolde.setAnnee(annee);
        nouveauSolde.setTotalAnnuel(joursAlloues);
        nouveauSolde.setRestant(joursAlloues);
        return soldeCongeRepository.save(nouveauSolde);
    }

    
    public List<SoldeConge> getAllSoldeConges() {
        return soldeCongeRepository.findAll();
    }

    
    public SoldeConge updateSoldeConge(long soldeCongeId,SoldeCreationDto data) {



        Optional<SoldeConge> soldeCongeOptional=soldeCongeRepository.findById(soldeCongeId);
        if(soldeCongeOptional.isEmpty()){
            throw new RuntimeException("SoldeConge not found");
        }
        SoldeConge soldeConge = soldeCongeOptional.get();
        Optional<Employe> employeOptional = employeRepository.findById(data.getEmployeId());
        if(employeOptional.isPresent()){
            Employe employe = employeOptional.get();
            if(!soldeConge.getEmploye().equals(employe)){
                soldeConge.setEmploye(employe);
            }
        }
        if(soldeConge.getAnnee() != data.getAnnee()){
            soldeConge.setAnnee(data.getAnnee());
        }
        if(soldeConge.getTotalAnnuel() != data.getJoursAlloues()){
            soldeConge.setTotalAnnuel(data.getJoursAlloues());
        }

        return soldeCongeRepository.save(soldeConge);
    }

    public void debiterSolde(Conge congeApprouve) {

        if (congeApprouve.getType() != TypeConge.ANNUEL) {
            return;
        }

        long duree = ChronoUnit.DAYS.between(congeApprouve.getDateDebut(), congeApprouve.getDateFin()) + 1;

        Employe employe = congeApprouve.getEmploye();

        int anneeConge = congeApprouve.getDateDebut().getYear();

            Optional<SoldeConge> soldeCongeOptional = soldeCongeRepository.findByEmployeAndAnnee(employe, anneeConge);
            if(soldeCongeOptional.isEmpty()) {
                throw  new RuntimeException("Aucun solde trouvé pour l'employé " + employe.getId() + " pour l'année " + anneeConge);

            }
            SoldeConge soldeConge = soldeCongeOptional.get();

        // 4. Vérifier et débiter
        if (soldeConge.getRestant() < duree) {
            throw new IllegalStateException("Solde insuffisant pour approuver ce congé.");
        }
            soldeConge.setRestant(soldeConge.getRestant() - (int)duree);
        soldeCongeRepository.save(soldeConge);
    }

    
    public void deleteSoldeConge(Long soldeCongeId) {
        Optional<SoldeConge> soldeCongeOptional=soldeCongeRepository.findById(soldeCongeId);
        if(soldeCongeOptional.isEmpty()){
            throw new RuntimeException("SoldeConge not found");
        }

        soldeCongeRepository.deleteById(soldeCongeId);
    }

    public  SoldeConge findByEmploye(Employe employe, int annee){

        return soldeCongeRepository.findByEmployeAndAnnee(employe ,annee).get();

    }
    public List<SoldeConge> findByAllEmploye(Employe employe){

        return soldeCongeRepository.findByEmploye(employe);

    }
}
