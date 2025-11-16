package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.SoldeConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldeCongeRepository extends JpaRepository<SoldeConge, Long> {
}
