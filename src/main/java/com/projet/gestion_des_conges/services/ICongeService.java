package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Conge;

import java.util.List;

public interface ICongeService {

    Conge getCongeById(Long id);

    Conge createConge(Conge conge);

    List<Conge> getAllConges();

    Conge updateConge(Conge conge);

    void deleteConge(Long id);
}
