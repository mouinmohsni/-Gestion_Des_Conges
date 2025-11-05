package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

@Entity
@Table(name="solde_conges")
public class SoldeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private  long id  ;
     private int totalAnnuel ;
     private int restant ;
     private int annee ;
    @OneToOne
    @JoinColumn(name = "employe_id", unique = true)
    private Employe employe ;

    public SoldeConge(long id, int totalAnnuel, int restant, int annee, Employe employe) {
        this.id = id;
        this.totalAnnuel = totalAnnuel;
        this.restant = restant;
        this.annee = annee;
        this.employe = employe;
    }

    public SoldeConge() {

    }
}
