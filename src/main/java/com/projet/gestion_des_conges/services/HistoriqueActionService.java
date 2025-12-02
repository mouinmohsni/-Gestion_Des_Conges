package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.models.HistoriqueAction;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import com.projet.gestion_des_conges.repositories.HistoriqueActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoriqueActionService {

    private final HistoriqueActionRepository historiqueActionRepository;
    private final CongeRepository congeRepository;

    public HistoriqueActionService(HistoriqueActionRepository historiqueActionRepository, CongeRepository congeRepository) {
        this.historiqueActionRepository = historiqueActionRepository;
        this.congeRepository = congeRepository;
    }


    public List<HistoriqueAction> findHistoriquePourUnConge(Long congeId) {
        Optional<Conge> congeOptional = congeRepository.findById(congeId);
        if (congeOptional.isEmpty()) {
            throw new RuntimeException("Congé non trouvé avec l'ID : " + congeId);
        }
        Conge conge = congeOptional.get();

        return historiqueActionRepository.findByCongeOrderByDateActionAsc(conge);
    }

    public List<HistoriqueAction> findActionsParUtilisateur(Long utilisateurId) {
        return historiqueActionRepository.findByUtilisateurIdOrderByDateActionDesc(utilisateurId);
    }

    public List<HistoriqueAction> findActionsParTypeEtPeriode(String actionType, LocalDateTime debut, LocalDateTime fin) {
        return historiqueActionRepository.findByActionAndDateActionBetween(actionType, debut, fin);
    }
}
