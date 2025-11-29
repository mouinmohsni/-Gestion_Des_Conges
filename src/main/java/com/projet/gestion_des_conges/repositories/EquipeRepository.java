package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.Equipe;
import com.projet.gestion_des_conges.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    Optional<Equipe> findByNom(String nom);

}
