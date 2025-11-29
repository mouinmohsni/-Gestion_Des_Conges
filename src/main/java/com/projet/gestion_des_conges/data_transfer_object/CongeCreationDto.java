package com.projet.gestion_des_conges.data_transfer_object;

import com.projet.gestion_des_conges.models.TypeConge;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Data
public class CongeCreationDto {


    @NotNull(message = "La date de début est obligatoire.")
    @FutureOrPresent(message = "La date de début doit être dans le futur.")
    private LocalDate dateDebut ;

    @NotNull(message = "La date de fin est obligatoire.")
    @Future(message = "La date de fin doit être dans le futur.")
    private LocalDate dateFin ;

    @NotEmpty(message = "le type est obligatoire.")
    private TypeConge type ;

    private String commentaire ;
}
