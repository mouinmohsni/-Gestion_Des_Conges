package com.projet.gestion_des_conges.data_transfer_object;

import com.projet.gestion_des_conges.models.StatutConge;
import lombok.Data;

@Data
public class StatutCountDto {
    private StatutConge statut;
    private long count;

    public StatutCountDto(StatutConge statut, long count) {
        this.statut = statut;
        this.count = count;
    }
}
