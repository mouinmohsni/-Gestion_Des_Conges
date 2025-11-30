package com.projet.gestion_des_conges.services;


import com.projet.gestion_des_conges.models.CalendrierConge;
import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Equipe;
import com.projet.gestion_des_conges.repositories.CalendrierCongeRepository;
import com.projet.gestion_des_conges.repositories.SoldeCongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


    public CalendrierConge mettreAJourCalendrier(Conge conge, int annee, int mois) {
        Equipe equipe = conge.getEmploye().getEquipe();

        // Cherche si un calendrier existe déjà pour cette équipe et cette période.
        Optional<CalendrierConge> calendrierOpt = calendrierCongeRepository.findByEquipeAndAnneeAndMois(
                equipe.getNom(),
                annee,
                mois
        );

        CalendrierConge calendrier;
        if (calendrierOpt.isPresent()) {
            // S'il existe, on le récupère.
            calendrier = calendrierOpt.get();
        } else {
            // Sinon, on le crée.
            calendrier = new CalendrierConge();
            calendrier.setEquipe(equipe.getNom());
            calendrier.setAnnee(annee);
            calendrier.setMois(mois);
        }

        // On ajoute le congé à la liste du calendrier (s'il n'y est pas déjà).
        if (!calendrier.getConges().contains(conge)) {
            calendrier.getConges().add(conge);
        }

        // On sauvegarde le calendrier (création ou mise à jour).
       return calendrierCongeRepository.save(calendrier);
    }
}
