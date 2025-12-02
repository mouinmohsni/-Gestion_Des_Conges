package com.projet.gestion_des_conges.repositories;

import com.projet.gestion_des_conges.models.Notification;
import com.projet.gestion_des_conges.models.StatutNotification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {



    List<Notification> findByDestinataireIdAndStatut(long utilisateurId , StatutNotification statut);

    List<Notification> findByDestinataireIdOrderByDateEnvoiDesc(long utilisateurId);
}
