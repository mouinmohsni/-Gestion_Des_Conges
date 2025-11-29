package com.projet.gestion_des_conges.services;


import com.projet.gestion_des_conges.models.CalendrierConge;
import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.repositories.CalendrierCongeRepository;
import com.projet.gestion_des_conges.repositories.SoldeCongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendrierCongeService implements ICalendrierCongeService {

    @Autowired
    private CalendrierCongeRepository calendrierCongeRepository;


    @Override
    public CalendrierConge getCalendrierCongeById(Long id) {
        return calendrierCongeRepository.findById(id).get();
    }

    @Override
    public CalendrierConge createCalendrierConge(CalendrierConge conge) {
        return calendrierCongeRepository.save(conge);
    }

    @Override
    public List<CalendrierConge> getAllCalendrierConges() {
        return calendrierCongeRepository.findAll();
    }

    @Override
    public CalendrierConge updateCalendrierConge(CalendrierConge conge) {
        return  calendrierCongeRepository.saveAndFlush(conge);
    }

    @Override
    public void deleteCalendrierConge(Long id) {
        calendrierCongeRepository.deleteById(id);
    }
}
