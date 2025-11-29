package com.projet.gestion_des_conges.data_transfer_object;

import jakarta.validation.constraints.*;
import lombok.Data;



    @Data
    public class ProfilUpdateDto {
        @NotEmpty
        private String nom;
        @NotEmpty
        private String prenom;
    }


