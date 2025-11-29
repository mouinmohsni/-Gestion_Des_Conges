package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.CalendrierConge;
import com.projet.gestion_des_conges.models.Conge;

import java.util.List;

public interface ICalendrierCongeService {

    CalendrierConge getCalendrierCongeById(Long id);

    CalendrierConge createCalendrierConge(CalendrierConge conge);

    List<CalendrierConge> getAllCalendrierConges();

    CalendrierConge updateCalendrierConge(CalendrierConge conge);

    void deleteCalendrierConge(Long id);
}
