package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.ManagerCreationRequest;
import com.projet.gestion_des_conges.data_transfer_object.UpdateStatutCongeDto;
import com.projet.gestion_des_conges.models.*;
import com.projet.gestion_des_conges.repositories.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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
    final SoldeCongeRepository soldeCongeRepository ;
    final CalendrierCongeRepository calendrierCongeRepository ;
    final CalendrierCongeService calendrierCongeService;

    public ManagerService(ManagerRepository managerRepository,
                          UtilisateurRepository utilisateurRepository,
                          EquipeRepository equipeRepository,
                          CongeRepository congeRepository,
                          SoldeCongeRepository soldeCongeRepository,
                          CalendrierCongeRepository calendrierCongeRepository,
                          CalendrierCongeService calendrierCongeService) {
        this.managerRepository = managerRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.equipeRepository = equipeRepository;
        this.congeRepository = congeRepository;
        this.soldeCongeRepository = soldeCongeRepository;
        this.calendrierCongeRepository = calendrierCongeRepository;
        this.calendrierCongeService = calendrierCongeService;
    }


    /**
     * ceation d'un nouveau manager
     * @param data
     * @param result
     * @return
     */

    public Manager creerManager(ManagerCreationRequest data , BindingResult result) {
        Optional<Utilisateur> potentialManager = utilisateurRepository.findByEmail(data.getEmail());
        if (potentialManager.isPresent()) {
            result.rejectValue("email", "RegisterError", "Cette adresse email est déjà utilisée.");

        }
        if (!data.getMotDePasse().equals(data.getConfPassword())) {
            result.rejectValue("confpassword", "register error", "Passwords must match");

        }
        Equipe equipe ;
        Optional<Equipe> equipeOptionnelle = equipeRepository.findById(data.getEquipeId());
        equipe = equipeOptionnelle.orElse(null);

        if (result.hasErrors()) {
            return null;
        }
        String motDePasseHache = BCrypt.hashpw(data.getMotDePasse(), BCrypt.gensalt());


        Manager manager = new Manager();
        manager.setEmail(data.getEmail());
        manager.setNom(data.getNom());
        manager.setPrenom(data.getPrenom());
        manager.setMotDePasse(motDePasseHache);
        manager.setEquipe(equipe);

        return managerRepository.save(manager);

    }






    public Conge approuverRefuserConge(long managerId , long idconge , UpdateStatutCongeDto data, BindingResult bindingResult) {
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
            throw new RuntimeException("Ce congé ne peut plus être approuvé.");
        }

        StatutConge newStatu  ;
        SoldeConge soldeConge = conge.getEmploye().getSoldeConge();
        if (data.getStatut().equals(StatutConge.VALIDE)) {
            long nbJour = ChronoUnit.DAYS.between(conge.getDateDebut(), conge.getDateFin()) + 1;
            int reste = soldeConge.getRestant();
            reste -= (int) nbJour;
            soldeConge.setRestant(reste);
            newStatu=StatutConge.VALIDE;

            int annee = conge.getDateDebut().getYear();
            int mois = conge.getDateDebut().getMonthValue();
            calendrierCongeService.mettreAJourCalendrier(conge, annee, mois);
            soldeCongeRepository.save(soldeConge);


        }else if (data.getStatut() == StatutConge.REFUSE){
            newStatu=StatutConge.REFUSE;
        }else{
            throw new IllegalArgumentException("Statut invalide : " + data.getStatut());
        }
        conge.setStatut(newStatu);
        conge.setValiderPar(manager);

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
