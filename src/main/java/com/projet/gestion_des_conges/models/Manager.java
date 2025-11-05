package com.projet.gestion_des_conges.models;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Manager extends Utilisateur {

    @OneToOne(mappedBy = "manager")
    private Equipe  equipe;

    @OneToMany(mappedBy = "validerPar", fetch = FetchType.LAZY)
    private List<Conge> congesValides = new ArrayList<>();

    public Manager() {
        super();
    }
}
