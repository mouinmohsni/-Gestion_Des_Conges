package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Employe")
public class Employe extends Utilisateur {

    private String department;

    @OneToOne(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    private SoldeConge soldeConge;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    public Employe() {
        super();
    }

}
