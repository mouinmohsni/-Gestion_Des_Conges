package com.projet.gestion_des_conges.data_transfer_object;

import lombok.Data;

@Data
public class CalendrierUpdateDto {
    private int annee;
    private int mois;
    private String equipe;
}
