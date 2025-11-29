package com.projet.gestion_des_conges.data_transfer_object;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasswordChangeDto {
    @NotEmpty
    private String ancienMotDePasse;

    @NotEmpty
    @Size(min = 6)
    private String nouveauMotDePasse;
}
