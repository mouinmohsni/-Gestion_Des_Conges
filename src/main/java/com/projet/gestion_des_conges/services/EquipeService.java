package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Equipe;
import com.projet.gestion_des_conges.repositories.EquipeRepository;

public class EquipeService {

    final EquipeRepository equipeRepository;

    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    /** todo creerEquipe
    EquipeCreationDto dto, BindingResult result
    Equipe
    Crée une nouvelle équipe. Doit vérifier que le nom n'est pas déjà pris.
     */

    /** todo findById
     Long id
     Optional<Equipe>
     Trouve une équipe par son ID. */

     /** todo findAll
     (aucun)
     List<Equipe>
     Liste toutes les équipes existantes (très utile pour le front-end).
     */

     /** todo updateEquipe
      Long id, EquipeUpdateDto dto
      Equipe
      Met à jour le nom d'une équipe.
      */


     /** todo deleteEquipe
      Long id
      void
      Supprime une équipe (attention, à n'autoriser que si l'équipe n'a plus d'employés ou de manager).
      */



}
