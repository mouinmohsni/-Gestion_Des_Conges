package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.CalendrierConge;
import com.projet.gestion_des_conges.models.SoldeConge;

import java.util.List;

public interface ISoldeCongeService {
    SoldeConge getSoldeCongeById(Long id);

    SoldeConge createSoldeConge(SoldeConge soldeConge);

    List<SoldeConge> getAllSoldeConges();

    SoldeConge updateSoldeConge(SoldeConge soldeConge);

    void deleteSoldeConge(Long id);
}
