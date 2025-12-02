package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.CongeCreationDto;
import com.projet.gestion_des_conges.data_transfer_object.CongeUpdateDto;
import com.projet.gestion_des_conges.models.*;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import com.projet.gestion_des_conges.repositories.EmployeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CongeService {

    private final CongeRepository congeRepository;
    private final EmployeRepository employeRepository;
    private final NotificationService  notificationService ;

    public CongeService(CongeRepository congeRepository,
                        EmployeRepository employeRepository,
                        NotificationService  notificationService) {
        this.congeRepository = congeRepository;
        this.employeRepository = employeRepository;
        this.notificationService = notificationService;

    }


    /**
     * Permet à un employé de soumettre une nouvelle demande de congé.
     * @param employeId
     * @param data
     * @return
     */
    public Conge createConge(Long employeId, CongeCreationDto data){
        Optional<Employe> employe = employeRepository.findById(employeId);
        if (employe.isEmpty()) {
            throw new RuntimeException("l'employer non trouvé !");
        }
        Employe thisEmploye = employe.get();
        if (data.getDateDebut() != null && data.getDateFin() != null) {
            if (data.getDateDebut().isAfter(data.getDateFin())) {
                throw new RuntimeException("La date de fin doit être après la date de début.");
            }
        }

        int nbJourRest = thisEmploye.getSoldeConge().getRestant();
        long dureeDuConge = ChronoUnit.DAYS.between(data.getDateDebut(), data.getDateFin()) + 1;
        if ( nbJourRest < dureeDuConge) {
            throw new RuntimeException("nombre des jour n'est pas suffisant");
        }
        Conge conge = new Conge();
        conge.setDateDebut(data.getDateDebut());
        conge.setDateFin(data.getDateFin());
        conge.setEmploye(thisEmploye);
        conge.setDateDemande(LocalDate.now());
        conge.setCommentaire(data.getCommentaire());
        conge.setStatut(StatutConge.EN_ATTENTE);
        conge.setType(data.getType());

        HistoriqueAction action = new HistoriqueAction();
        action.setAction("CREATION_DEMANDE");
        action.setDateAction(LocalDateTime.now());
        action.setUtilisateur(thisEmploye);
        action.setConge(conge);
        action.setDetailsAction("creation de la demande de conge de type "+data.getType() +"pour l'employe "+thisEmploye.getNom());
        conge.getHistorique().add(action);

        Manager manager = thisEmploye.getEquipe().getManager();
        String message = "Nouvelle demande de congé de " + thisEmploye.getPrenom() + " en attente de validation.";
        notificationService.creerNotification(manager, message, "NOUVELLE_DEMANDE_CONGE");

        return congeRepository.save(conge);

    }



    public Optional<Conge> getCongeById(Long id) {
        return congeRepository.findById(id);
    }


    /*public Conge createConge(Conge conge) {
        return congeRepository.save(conge);
    }*/


    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }


    public Conge updateConge(long congeId, long employeId, CongeUpdateDto data , BindingResult bindingResult) {


        Optional<Employe> employeOptional = employeRepository.findById(employeId);
        if (employeOptional.isEmpty()) {
            throw new RuntimeException("Manager non trouvé !");
        }
        Employe employe = employeOptional.get();
        Optional<Conge> congeOptional = congeRepository.findById(congeId);
        if (congeOptional.isEmpty()) {
            throw new RuntimeException("Conger non trouver");
        }
        Conge congeAModifier = congeOptional.get();
        if (congeAModifier.getEmploye().getId()!=employeId){
            throw new IllegalStateException("Cet employer n'a pas le droit de valider ce congé.");
        }

        if(congeAModifier.getStatut() != StatutConge.EN_ATTENTE) {
            throw new IllegalStateException("Ce congé ne peut plus être modifié.");
        }

        if (data.getDateDebut().isAfter(data.getDateFin())) {
            throw new IllegalArgumentException("La nouvelle date de fin doit être après la nouvelle date de début.");
        }

        // 3. Mettre à jour les champs de l'entité avec les données du DTO.
        // On ne modifie QUE ce qui est autorisé.
        congeAModifier.setDateDebut(data.getDateDebut());
        congeAModifier.setDateFin(data.getDateFin());
        congeAModifier.setType(data.getType());
        congeAModifier.setCommentaire(data.getCommentaire());

        HistoriqueAction action = new HistoriqueAction();
        action.setAction("MODIFICATION_DEMANDE");
        action.setDateAction(LocalDateTime.now());
        action.setUtilisateur(employe);
        action.setConge(congeAModifier);
        action.setDetailsAction("Mise à jour des dates/type/commentaire par l'employé.");
        congeAModifier.getHistorique().add(action);

        Manager manager = employe.getEquipe().getManager();
        String message = "L'employer " + employe.getPrenom() + " a modifier sont conger : en attente de validation.";
        notificationService.creerNotification(manager, message, "CONGER_MODIFIER");

        return congeRepository.saveAndFlush(congeAModifier);
    }


    public void deleteConge(Long congeId) {
        Optional<Conge> congeOptional = congeRepository.findById(congeId);
        if (congeOptional.isEmpty()) {
            throw new RuntimeException("Conger non trouver");
        }


        congeRepository.deleteById(congeId);
    }

    public List<Conge> findByEmploye(Employe employe){
        long employeId = employe.getId();
        Optional<Employe> employeOptional = employeRepository.findById(employeId);
        if (employeOptional.isEmpty()) {
            throw new RuntimeException("Manager non trouvé !");
        }
        return congeRepository.findByEmploye(employe);
    }

    public void annulerConge(Long congeId, Long employeId) {
        // 1. Récupérer les objets nécessaires
        Optional<Conge> congeOptional = congeRepository.findById(congeId);
        if(congeOptional.isEmpty()) {
            throw new RuntimeException("Congé non trouvé avec l'ID : " + congeId);
        }
        Conge conge = congeOptional.get();

        Optional<Employe> employeOptional = employeRepository.findById(employeId);
        if(employeOptional.isEmpty()){
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + employeId);
        }
        Employe employe = employeOptional.get();


        if (conge.getStatut() != StatutConge.EN_ATTENTE) {
            throw new IllegalStateException("Ce congé ne peut plus être annulé car il a déjà été traité.");
        }

        conge.setStatut(StatutConge.ANNULE);

        HistoriqueAction action = new HistoriqueAction();
        action.setAction("ANNULATION_DEMANDE");
        action.setDateAction(LocalDateTime.now());
        action.setUtilisateur(employe);
        action.setConge(conge);
        action.setDetailsAction("La demande de congé a été annulée.");

        conge.getHistorique().add(action);

        Manager manager = employe.getEquipe().getManager();
        String message = "L'employer " + employe.getPrenom() + " a modifier sont conger : en attente de validation.";
        notificationService.creerNotification(manager, message, "CONGER_ANNULER");

        congeRepository.save(conge);
    }

}
