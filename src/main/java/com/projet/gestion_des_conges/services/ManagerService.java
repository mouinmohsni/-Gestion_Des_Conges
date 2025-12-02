package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.ManagerCreationRequest;
import com.projet.gestion_des_conges.data_transfer_object.UpdateStatutCongeDto;
import com.projet.gestion_des_conges.models.*;
import com.projet.gestion_des_conges.repositories.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ManagerService {

    public final ManagerRepository managerRepository;
    public final UtilisateurRepository utilisateurRepository;
    public final EquipeRepository equipeRepository;
    final CongeRepository congeRepository;
    final CalendrierCongeRepository calendrierCongeRepository ;
    final CalendrierCongeService calendrierCongeService;
    private final SoldeCongeService soldeCongeService;
    private final NotificationService notificationService;

    public ManagerService(ManagerRepository managerRepository,
                          UtilisateurRepository utilisateurRepository,
                          EquipeRepository equipeRepository,
                          CongeRepository congeRepository,
                          CalendrierCongeRepository calendrierCongeRepository,
                          CalendrierCongeService calendrierCongeService,
                          SoldeCongeService soldeCongeService,
                          NotificationService notificationService) {
        this.managerRepository = managerRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.equipeRepository = equipeRepository;
        this.congeRepository = congeRepository;
        this.calendrierCongeRepository = calendrierCongeRepository;
        this.calendrierCongeService = calendrierCongeService;
        this.soldeCongeService = soldeCongeService;
        this.notificationService =notificationService;
    }


    /**
     * ceation d'un nouveau manager
     * @param data
     * @return
     */

    public Manager creerManager(ManagerCreationRequest data ) {
        Optional<Utilisateur> potentialManager = utilisateurRepository.findByEmail(data.getEmail());
        if (potentialManager.isPresent()) {
            throw  new RuntimeException( "Cette adresse email est déjà utilisée.");

        }
        if (!data.getMotDePasse().equals(data.getConfPassword())) {
            throw  new RuntimeException( "Passwords must match");

        }
        Equipe equipe ;
        Optional<Equipe> equipeOptionnelle = equipeRepository.findById(data.getEquipeId());
        equipe = equipeOptionnelle.orElse(null);


        String motDePasseHache = BCrypt.hashpw(data.getMotDePasse(), BCrypt.gensalt());


        Manager manager = new Manager();
        manager.setEmail(data.getEmail());
        manager.setNom(data.getNom());
        manager.setPrenom(data.getPrenom());
        manager.setMotDePasse(motDePasseHache);
        manager.setEquipe(equipe);

        return managerRepository.save(manager);

    }






    public Conge approuverRefuserConge(long managerId , long idconge , UpdateStatutCongeDto data) {
        Optional<Manager> managerOptional = managerRepository.findById(managerId);
        if (managerOptional.isEmpty()) {
            throw new RuntimeException("Manager non trouvé !");
        }
        Manager manager = managerOptional.get();
        Optional<Conge> congeOptional = congeRepository.findById(idconge);
        if (congeOptional.isEmpty()) {
            throw new RuntimeException("Conger non trouver");
        }
        Conge conge = congeOptional.get();
        if (manager.getEquipe() == null || !manager.getEquipe().equals(conge.getEmploye().getEquipe())){
            throw new RuntimeException("Ce manager n'a pas le droit de valider ce congé.");
        }

        if(conge.getStatut() != StatutConge.EN_ATTENTE) {
            throw new IllegalStateException("Action impossible : le congé n'est plus en attente. Statut actuel : " + conge.getStatut());
        }


        StatutConge newStatu  ;
        if (data.getStatut().equals(StatutConge.VALIDE)) {
            soldeCongeService.debiterSolde(conge);
            newStatu=StatutConge.VALIDE;
            int annee = conge.getDateDebut().getYear();
            int mois = conge.getDateDebut().getMonthValue();
            CalendrierConge calendrier =calendrierCongeService.mettreAJourCalendrier(conge, annee, mois);


        }else if (data.getStatut() == StatutConge.REFUSE){
            newStatu=StatutConge.REFUSE;
        }else{
            throw new IllegalArgumentException("Statut invalide : " + data.getStatut());
        }
        conge.setStatut(newStatu);
        conge.setValiderPar(manager);

        HistoriqueAction action = new HistoriqueAction();
        action.setAction("DEMANDE CONGE EST "+manager.getNom());
        action.setDateAction(LocalDateTime.now());
        action.setUtilisateur(manager);
        action.setConge(conge);
        action.setDetailsAction("Mise à jour le statut du conger par le manager");

        conge.getHistorique().add(action);

        Employe employe = conge.getEmploye();
        String statutReponse = (conge.getStatut() == StatutConge.VALIDE) ? "approuvée" : "refusée";
        String message = "Votre demande de congé du " + conge.getDateDebut() + " a été " + statutReponse + ".";
        notificationService.creerNotification(employe, message, "REPONSE_DEMANDE_CONGE");


        return  congeRepository.save(conge);
    }





    public List<Conge> listerCongesEnAttente(long managerId){
        Optional<Manager> managerOptional = managerRepository.findById(managerId);
        if (managerOptional.isEmpty()) {
            throw new RuntimeException("Manager non trouvé !");
        }
        Manager manager = managerOptional.get();

        if (manager.getEquipe() == null) {
            return new ArrayList<>();
        }
        long equipeID =manager.getEquipe().getId();

        return congeRepository.findByEquipeIdAndStatut(equipeID,StatutConge.EN_ATTENTE);



    }

     public Optional<CalendrierConge> voirCalendrierEquipe(Long managerId, int annee, int mois){
         Optional<Manager> managerOptional = managerRepository.findById(managerId);
         if (managerOptional.isEmpty()) {
             throw new RuntimeException("Manager non trouvé !");
         }
         Manager manager = managerOptional.get();

         if ( annee < 2000 || mois < 1 || mois > 12 ){
             throw new IllegalArgumentException("l'anner et mois sont obligatoire");
         }

         Equipe equipe = manager.getEquipe();
         if (equipe == null){
             throw new RuntimeException("le m'anager n'as pas encore d'equipe");
         }

         return calendrierCongeRepository.findByEquipeAndAnneeAndMois(equipe.getNom(),annee,mois) ;



     }

}
