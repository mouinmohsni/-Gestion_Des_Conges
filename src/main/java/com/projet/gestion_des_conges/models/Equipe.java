package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", unique = true)
    private Manager manager;

    @OneToMany(mappedBy = "equipe")
    private List<Employe> employes;
}