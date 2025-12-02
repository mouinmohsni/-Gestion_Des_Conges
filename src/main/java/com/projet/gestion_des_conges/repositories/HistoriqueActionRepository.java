package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.HistoriqueAction;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction, Long> {

    List<HistoriqueAction> findByCongeOrderByDateActionAsc(Conge conge);
    List<HistoriqueAction> findByUtilisateurIdOrderByDateActionDesc( long utilisateurId);
    List<HistoriqueAction>findByActionAndDateActionBetween(String actionType, LocalDateTime debut, LocalDateTime  fin);

}
