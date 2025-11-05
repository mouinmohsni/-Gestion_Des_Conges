package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="utilisateurs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany (mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<HistoriqueAction> historiqueActions;

    public Utilisateur() {

    }

    public Utilisateur(String nom, String prenom, String email, String motDePasse, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }


}
