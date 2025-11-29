package com.projet.gestion_des_conges.data_transfer_object;

import jakarta.validation.constraints.*;
import lombok.Data;



@Data
public class LoginRequest {

    @NotEmpty(message = "L'email est obligatoire.")
    @Email(message = "Veuillez entrer une adresse email valide.")
    private String email;

    @NotEmpty(message = "Le mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères.")
    private String motDePasse;
}
