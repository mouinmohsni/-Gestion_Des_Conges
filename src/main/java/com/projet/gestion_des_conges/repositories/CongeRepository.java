package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.Conge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {
}
