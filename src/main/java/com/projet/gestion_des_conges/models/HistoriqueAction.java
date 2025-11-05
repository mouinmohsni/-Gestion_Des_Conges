package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="historique_action")
public class HistoriqueAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String action ;
    private LocalDateTime dateAction ;
    private String detailsAction ;
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur ;
    @ManyToOne
    @JoinColumn(name = "conge_id")
    private Conge conge ;
}
