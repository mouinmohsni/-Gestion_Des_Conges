package com.projet.gestion_des_conges.data_transfer_object;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EquipeDto {

    @NotEmpty(message = "Le nom de l'équipe est obligatoire.")
    private String nom;
}
