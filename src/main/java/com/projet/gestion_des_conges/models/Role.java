package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nomRole;
    @OneToMany (mappedBy = "role", cascade = CascadeType.ALL)
    private List<Utilisateur> utilisateurs ;
}
