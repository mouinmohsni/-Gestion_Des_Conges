package com.projet.gestion_des_conges.services;


import com.projet.gestion_des_conges.data_transfer_object.CalendrierCongeDto;
import com.projet.gestion_des_conges.data_transfer_object.CalendrierUpdateDto;
import com.projet.gestion_des_conges.models.CalendrierConge;
import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.Equipe;
import com.projet.gestion_des_conges.models.StatutConge;
import com.projet.gestion_des_conges.repositories.CalendrierCongeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CalendrierCongeService {


    private final CalendrierCongeRepository calendrierCongeRepository;

    public CalendrierCongeService(CalendrierCongeRepository calendrierCongeRepository) {
        this.calendrierCongeRepository = calendrierCongeRepository;
    }


    public CalendrierConge getCalendrierCongeById(Long id) {
        Optional<CalendrierConge> calendrierCongeOptional = calendrierCongeRepository.findById(id);
        if (calendrierCongeOptional.isEmpty()) {
            throw new RuntimeException("Calendrier conge n'existe pas");
        }
        CalendrierConge calendrierConge = calendrierCongeOptional.get();
        return calendrierConge ;
    }

    public CalendrierConge createCalendrierConge(CalendrierCongeDto data) {
        Optional<CalendrierConge> calendrierCongeOptional=calendrierCongeRepository.findByEquipeAndAnneeAndMois(data.getEquipe(), data.getAnnee(), data.getMois());
        if (calendrierCongeOptional.isPresent()) {
            throw new IllegalStateException("Un calendrier pour " + data.getMois() + "/" + data.getAnnee() + " existe déjà.");
        }

        CalendrierConge  calendrierConge = new CalendrierConge();
        calendrierConge.setMois(data.getMois());
        calendrierConge.setAnnee(data.getAnnee());
        calendrierConge.setEquipe(data.getEquipe());

        return calendrierCongeRepository.save(calendrierConge);
    }

    public List<CalendrierConge> getAllCalendrierConges() {
        return calendrierCongeRepository.findAll();
    }

    public CalendrierConge updateCalendrierConge(long calendrierId, CalendrierUpdateDto data) {
        Optional<CalendrierConge> calendrierCongeOptional=calendrierCongeRepository.findById(calendrierId);
        if (calendrierCongeOptional.isEmpty()) {
            throw new RuntimeException("Calendrier conge n'existe pas");
        }
        CalendrierConge calendrierConge = calendrierCongeOptional.get();
        if (calendrierConge.getMois() != data.getMois()) {
            calendrierConge.setMois(data.getMois());
        }
        if (calendrierConge.getAnnee() != data.getAnnee()) {
            calendrierConge.setAnnee(data.getAnnee());
        }
        if (!calendrierConge.getEquipe().equals(data.getEquipe()) ) {
            calendrierConge.setEquipe(data.getEquipe());
        }


        return  calendrierCongeRepository.saveAndFlush(calendrierConge);
    }



    public CalendrierConge mettreAJourCalendrier(Conge conge, int annee, int mois) {
        if (conge.getStatut() != StatutConge.VALIDE) {
            throw new IllegalStateException("le conger n'est pas valide");
        }

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

    public void retirerCongeDuCalendrier(Conge congeAnnule) {

        Equipe equipe = congeAnnule.getEmploye().getEquipe();
        if (equipe == null) {
            throw new IllegalStateException("le conger n'est pas valide");

        }
        int annee = congeAnnule.getDateDebut().getYear();
        int mois = congeAnnule.getDateDebut().getMonthValue();
        String nomEquipe = equipe.getNom();

        Optional<CalendrierConge> calendrierOpt = calendrierCongeRepository.findByEquipeAndAnneeAndMois(nomEquipe, annee, mois);
        if (calendrierOpt.isEmpty()) {
            throw new IllegalStateException("le calendrier n'existe pas");
        }
        CalendrierConge calendrier = calendrierOpt.get();
        boolean removed = calendrier.getConges().remove(congeAnnule);

        if (removed) {
            calendrierCongeRepository.save(calendrier);
        }

    }


    public void deleteCalendrierConge(Long id) {
        Optional<CalendrierConge> calendrierConge = calendrierCongeRepository.findById(id);
        if (calendrierConge.isEmpty()) {
            throw new RuntimeException("Calendrier conge n'existe pas");
        }

        calendrierCongeRepository.deleteById(id);
    }
}
