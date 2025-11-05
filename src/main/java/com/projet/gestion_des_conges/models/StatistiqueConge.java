package com.projet.gestion_des_conges.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "statistiques_conges") // Nom de table au pluriel
@NoArgsConstructor
public class StatistiqueConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int mois;

    private int annee;

    private int totalConges;

    private int congesAcceptes;

    private int congesRefuses;

    private int congesEnAttente;

    public StatistiqueConge(int mois, int annee, int totalConges, int congesAcceptes, int congesRefuses, int congesEnAttente) {
        this.mois = mois;
        this.annee = annee;
        this.totalConges = totalConges;
        this.congesAcceptes = congesAcceptes;
        this.congesRefuses = congesRefuses;
        this.congesEnAttente = congesEnAttente;
    }
}
