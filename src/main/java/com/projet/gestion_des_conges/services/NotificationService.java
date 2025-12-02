package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Notification;
import com.projet.gestion_des_conges.models.StatutNotification;
import com.projet.gestion_des_conges.models.Utilisateur;
import com.projet.gestion_des_conges.repositories.NotificationRepository;
import com.projet.gestion_des_conges.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {


    private final NotificationRepository notificationRepository;


    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void creerNotification(Utilisateur destinataire, String message, String type) {
        if (destinataire == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setMessage(message);
        notification.setType(type);
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setStatut(StatutNotification.NON_LUE);

        notificationRepository.save(notification);
    }

    public List<Notification> findNotificationsPourUtilisateur(Long utilisateurId) {
        return notificationRepository.findByDestinataireIdOrderByDateEnvoiDesc(utilisateurId);
    }

    public List<Notification> findNotificationsNonLues(Long utilisateurId) {
        return notificationRepository.findByDestinataireIdAndStatut(utilisateurId, StatutNotification.NON_LUE);
    }


    public void changerStatutNotification(Long notificationId, Long utilisateurId , StatutNotification statut) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if(notificationOptional.isEmpty()) {
            throw new RuntimeException("Notification non trouvée.");
        }
        Notification notification = notificationOptional.get();

        if (notification.getDestinataire().getId()!= utilisateurId) {
            throw new IllegalStateException("Action non autorisée : cette notification ne vous appartient pas.");
        }

        notification.setStatut(statut);

        notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId,Long utilisateurId) {

        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        if(notificationOptional.isEmpty()) {
            throw new IllegalStateException("Notification non trouvée");
        }
        Notification notification = notificationOptional.get();
        if (notification.getDestinataire().getId()!= utilisateurId) {
            throw new IllegalStateException("Action non autorisée : vous ne pouvez pas supprimer cette notification.");
        }

        notificationRepository.delete(notification);
    }



}
