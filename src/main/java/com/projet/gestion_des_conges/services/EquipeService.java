package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.EquipeDto;
import com.projet.gestion_des_conges.models.Employe;
import com.projet.gestion_des_conges.models.Equipe;
import com.projet.gestion_des_conges.repositories.EquipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EquipeService {

    final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    /**
    EquipeCreationDto dto, BindingResult result
    Equipe
    Crée une nouvelle équipe. Doit vérifier que le nom n'est pas déjà pris.
     */
    public Equipe CreateEquipe(EquipeDto data, BindingResult result) {

        Optional<Equipe> potentialEquipe = equipeRepository.findByNom(data.getNom());
        if (potentialEquipe.isPresent()) {
            result.rejectValue("nom", "nom.existe");
        }
        if (result.hasErrors()) {
            return null;
        }

        Equipe newEquipe = potentialEquipe.get() ;
        return equipeRepository.save(newEquipe);
    }

    /**
     Long id
     Optional<Equipe>
     Trouve une équipe par son ID. */
    public Optional<Equipe> FindById(Long id) {
        return equipeRepository.findById(id);
    }

     /**
     (aucun)
     List<Equipe>
     Liste toutes les équipes existantes (très utile pour le front-end).
     */
     public List<Equipe> FindAll() {
         return equipeRepository.findAll();
     }

     /**
      Long id, EquipeDto dto
      Equipe
      Met à jour le nom d'une équipe.
      */

     public Equipe UpdateEquipe(Long equipeId, EquipeDto data, BindingResult result) {
         Optional<Equipe> exsiteEquipe = equipeRepository.findByNom(data.getNom());
         if (exsiteEquipe.isPresent()) {
             result.rejectValue("nom", "nom.existe");
         }
         if (result.hasErrors()) {
             return null;
         }
         Optional<Equipe> potentialEquipe = equipeRepository.findById(equipeId);
         if (potentialEquipe.isEmpty()) {
             throw new RuntimeException("l'equipe non trouvé !");
         }
         Equipe newEquipe = potentialEquipe.get();
         newEquipe.setNom(newEquipe.getNom());
         return equipeRepository.save(newEquipe);
     }



     /**
      Long id
      void
      Supprime une équipe (attention, à n'autoriser que si l'équipe n'a plus d'employés ou de manager).
      */

    public void deleteEquipe(Long equipeId){
        Optional<Equipe> potentialEquipe = equipeRepository.findById(equipeId);
        if (potentialEquipe.isEmpty()) {
            throw new RuntimeException("l'equipe non trouvé !");
        }
        Equipe newEquipe = potentialEquipe.get();
        equipeRepository.delete(newEquipe);
    }







}
