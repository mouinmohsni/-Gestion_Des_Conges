package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
}
