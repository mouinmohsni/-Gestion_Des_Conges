package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.StatutCountDto;
import com.projet.gestion_des_conges.models.StatistiqueConge;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import com.projet.gestion_des_conges.repositories.StatistiqueCongeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class StatistiqueCongeService {
    private final StatistiqueCongeRepository statistiqueCongeRepository;
    private final CongeRepository congeRepository;

    public StatistiqueCongeService(StatistiqueCongeRepository statistiqueCongeRepository, CongeRepository congeRepository) {
        this.statistiqueCongeRepository = statistiqueCongeRepository;
        this.congeRepository = congeRepository;
    }

    public void genererStatistiquesMensuelles(int annee, int mois) {
        // 1. Appeler la requête d'agrégation
        List<StatutCountDto> counts = congeRepository.countCongesByStatutForMonth(annee, mois);

        // 2. Traiter les résultats
        long acceptes = 0;
        long refuses = 0;
        long enAttente = 0;

        for (StatutCountDto count : counts) {
            switch (count.getStatut()) {
                case VALIDE: // ou VALIDE
                    acceptes = count.getCount();
                    break;
                case REFUSE:
                    refuses = count.getCount();
                    break;
                case EN_ATTENTE:
                    enAttente = count.getCount();
                    break;

            }
        }

        long total = acceptes + refuses + enAttente;

        // 3. Chercher si des stats existent déjà pour ce mois, ou en créer de nouvelles
        Optional<StatistiqueConge> statistiqueCongeOptional = statistiqueCongeRepository.findByAnneeAndMois(annee, mois);
        StatistiqueConge stats = new StatistiqueConge();
        if(statistiqueCongeOptional.isPresent()) {
            stats = statistiqueCongeOptional.get();
        }

        stats.setAnnee(annee);
        stats.setMois(mois);
        stats.setCongesAcceptes((int) acceptes);
        stats.setCongesRefuses((int) refuses);
        stats.setCongesEnAttente((int) enAttente);
        stats.setTotalConges((int) total);

        statistiqueCongeRepository.save(stats);
    }


    public Optional<StatistiqueConge> getStatistiques(int annee, int mois) {
        return statistiqueCongeRepository.findByAnneeAndMois(annee, mois);
    }
}
