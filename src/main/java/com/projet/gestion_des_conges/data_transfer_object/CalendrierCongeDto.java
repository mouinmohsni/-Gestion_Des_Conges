package com.projet.gestion_des_conges.data_transfer_object;

import com.projet.gestion_des_conges.models.Conge;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalendrierCongeDto {

    @NotNull(message = "Le mois est obligatoire.")
    private  int mois ;
    @NotNull(message = "L'année  est obligatoire.")
    private  int annee ;
    @NotNull(message = "le nom de l'equipe  est obligatoire.")
    private String equipe ;

    private Conge conge ;
}
