package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.data_transfer_object.StatutCountDto;
import com.projet.gestion_des_conges.models.StatistiqueConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatistiqueCongeRepository extends JpaRepository<StatistiqueConge, Long> {

    Optional<StatistiqueConge>  findByAnneeAndMois(int annee, int mois);


}
