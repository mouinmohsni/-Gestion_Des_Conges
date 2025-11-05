package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="conges")

public class Conge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private LocalDate dateDebut ;
    private LocalDate dateFin ;
    @Enumerated(EnumType.STRING)
    private TypeConge type ;
    @Enumerated(EnumType.STRING)
    private StatutConge statut ;
    private LocalDate dateDemande ;
    private String commentaire ;

    @OneToMany (mappedBy = "conge", cascade = CascadeType.ALL)
    private List<HistoriqueAction> historique = new ArrayList<>(); ;

    @OneToMany (mappedBy = "conge", cascade = CascadeType.ALL)
    private List<Notification> notification = new ArrayList<>();;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe ;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager validerPar ;

    public Conge() {
    }
}
