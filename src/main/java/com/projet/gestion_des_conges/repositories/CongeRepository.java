package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.data_transfer_object.StatutCountDto;
import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Employe;
import com.projet.gestion_des_conges.models.StatutConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {

    List<Conge> findByEmploye(Employe employe);
    List<Conge> findByStatut(StatutConge statut);

    @Query("SELECT c FROM Conge c WHERE c.employe.equipe.id = :equipeId AND c.statut = :statut")
    List<Conge> findByEquipeIdAndStatut(@Param("equipeId") Long equipeId, @Param("statut") StatutConge statut);

    @Query("SELECT new com.projet.gestion_des_conges.data_transfer_object.StatutCountDto(c.statut, COUNT(c)) " +
            "FROM Conge c " +
            "WHERE YEAR(c.dateDebut) = :annee AND MONTH(c.dateDebut) = :mois " +
            "GROUP BY c.statut")
    List<StatutCountDto> countCongesByStatutForMonth(@Param("annee") int annee, @Param("mois") int mois);

}
