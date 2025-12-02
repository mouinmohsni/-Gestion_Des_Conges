package com.projet.gestion_des_conges.data_transfer_object;

import lombok.Data;

@Data
public class SoldeCreationDto {

    private Long employeId;
    private int annee;
    private int joursAlloues;
}

