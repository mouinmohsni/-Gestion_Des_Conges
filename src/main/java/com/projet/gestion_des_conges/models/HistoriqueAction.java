package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="historique_action")
public class HistoriqueAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String action ;
    private LocalDateTime dateAction ;
    private String detailsAction ;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur ;
    @ManyToOne
    @JoinColumn(name = "conge_id")
    private Conge conge ;
}
