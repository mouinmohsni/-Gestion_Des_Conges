package com.projet.gestion_des_conges.models;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="calendrier_conge")
public class CalendrierConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id ;
    private  int mois ;
    private  int annee ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "calendrier_conge_items", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "calendrier_id"),
            inverseJoinColumns = @JoinColumn(name = "conge_id")
    )
    private List<Conge> conges = new ArrayList<>();

    private String equipe ;
}
