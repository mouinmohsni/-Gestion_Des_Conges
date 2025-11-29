package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {

    List<Conge> findByEmploye(Employe employe);
}
