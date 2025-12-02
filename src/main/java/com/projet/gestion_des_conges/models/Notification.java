package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String message ;
    private String type ;
    private LocalDateTime dateEnvoi;
    @Enumerated(EnumType.STRING)
    private StatutNotification  statut ;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire ;


    public Notification() {
    }
}
