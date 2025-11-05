package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String message ;
    private String type ;
    private LocalDateTime dateEnvoi;
    private String statut ;
    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire ;
    @ManyToOne
    @JoinColumn(name = "conger_id")
    private Conge conge ;

    public Notification() {
    }
}
