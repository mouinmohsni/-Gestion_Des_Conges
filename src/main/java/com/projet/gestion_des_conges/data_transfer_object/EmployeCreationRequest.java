package com.projet.gestion_des_conges.data_transfer_object;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeCreationRequest {

    @NotEmpty(message = "Le nom est obligatoire.")
    private String nom;

    @NotEmpty(message = "Le prénom est obligatoire.")
    private String prenom;

    @NotEmpty(message = "L'email est obligatoire.")
    @Email(message = "Veuillez fournir une adresse email valide.")
    private String email;

    @NotEmpty(message = "Le mot de passe est obligatoire.")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères.")
    private String motDePasse;

    @NotEmpty(message = "La confirmation du mot de passe est obligatoire.")
    private String confPassword; // Le champ temporaire !

    @NotEmpty(message = "Le département est obligatoire.")
    private String department;

    private Long equipeId;
}
