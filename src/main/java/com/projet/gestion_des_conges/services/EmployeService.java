package com.projet.gestion_des_conges.services;


import com.projet.gestion_des_conges.data_transfer_object.CongeCreationDto;
import com.projet.gestion_des_conges.data_transfer_object.EmployeCreationRequest;
import com.projet.gestion_des_conges.models.*;
import com.projet.gestion_des_conges.repositories.EmployeRepository;
import com.projet.gestion_des_conges.repositories.EquipeRepository;
import com.projet.gestion_des_conges.repositories.SoldeCongeRepository;
import com.projet.gestion_des_conges.repositories.UtilisateurRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeService {

    public final UtilisateurRepository utilisateurRepository;
    public final EmployeRepository employeRepository;
    public final EquipeRepository equipeRepository;
    public final CongeService congeService;
    private final SoldeCongeService soldeCongeService;

    @Autowired
    public EmployeService(
            UtilisateurRepository utilisateurRepository,
            EmployeRepository employeRepository,
            EquipeRepository equipeRepository,
            CongeService congeService,
            SoldeCongeService soldeCongeService) {

        this.utilisateurRepository = utilisateurRepository;
        this.employeRepository = employeRepository;
        this.equipeRepository = equipeRepository;
        this.congeService = congeService;
        this.soldeCongeService = soldeCongeService;
    }


    /**
     *Crée un employé, hache le mdp, initialise le SoldeConge.
     * @param request
     * @return Utilisateur
     */
    public Employe creerEmploye(EmployeCreationRequest request )  {

        Optional<Utilisateur> potentialUtilisateur = utilisateurRepository.findByEmail(request.getEmail());
        if (potentialUtilisateur.isPresent()) {
            throw  new IllegalArgumentException( "Cette adresse email est déjà utilisée.");

        }

        if (!request.getMotDePasse().equals(request.getConfPassword())) {
            throw new IllegalArgumentException( "Passwords must match");
        }

        Optional<Equipe> equipeOptionnelle = equipeRepository.findById(request.getEquipeId());
        if (equipeOptionnelle.isEmpty()) {
            equipeOptionnelle= null ;
        }


        String motDePasseHache = BCrypt.hashpw(request.getMotDePasse(), BCrypt.gensalt());


        Equipe equipeTrouvee = equipeOptionnelle.get();


        Employe nouvelEmploye = new Employe();
        nouvelEmploye.setNom(request.getNom());
        nouvelEmploye.setPrenom(request.getPrenom());
        nouvelEmploye.setEmail(request.getEmail());
        nouvelEmploye.setEquipe(equipeTrouvee);
        nouvelEmploye.setMotDePasse(motDePasseHache);
        nouvelEmploye.setDepartment(request.getDepartment());


        SoldeConge soldeInitial = new SoldeConge();
        soldeInitial.setAnnee(LocalDate.now().getYear());
        soldeInitial.setTotalAnnuel(25);
        soldeInitial.setRestant(25);
        nouvelEmploye.setSoldeConge(soldeInitial);



        return employeRepository.save(nouvelEmploye);
    }

    /**
     * Récupère un utilisateur par son ID.
     * @return Un Optional, pour gérer le cas où l'utilisateur n'est pas trouvé.
     */
    public Optional<Employe> findById(Long id) {
        return employeRepository.findById(id);
    }

    /**
     * Récupère tous les utilisateurs.
     * Utile pour une interface d'administration.
     */
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }



    /**
     * Récupère l'historique de toutes les demandes de congé d'un employé.
     * @param employeId
     * @return  List<Conge>
     */
    public  List<Conge> consulterMesConges(long employeId ){
        Optional<Employe> employe = employeRepository.findById(employeId);
        if (employe.isEmpty()) {
            throw new RuntimeException("l'employer non trouvé !");
        }
        Employe thisEmploye = employe.get();

        return congeService.findByEmploye(thisEmploye);

    };

    /**
     * Récupère le solde de congés actuel de l'employé.
     * @param employeId
     * @return SoldeConge
     */
    public  SoldeConge consulterMonSolde(long employeId ,int annee ){
        Optional<Employe> employe = employeRepository.findById(employeId);
        if (employe.isEmpty()) {
            throw new RuntimeException("l'employer non trouvé !");
        }
        Employe thisEmploye = employe.get();

        return  soldeCongeService.findByEmploye(thisEmploye,annee);
    };

    /**
     *  Assigner ou desassigner un employer a une equipe
     * @param employeId
     * @param EquipeId
     * @return
     */
    public Employe changerEquipe(Long employeId, Long EquipeId) {
        Optional <Employe> employe = employeRepository.findById(employeId);
        if (employe.isEmpty()) {
            throw new RuntimeException("l'employer non trouvé !");
        }
        Employe thisEmploye = employe.get();
        if(EquipeId == null){
            thisEmploye.setEquipe(null);
        }else {
            Optional<Equipe> equipe = equipeRepository.findById(EquipeId);
            if (equipe.isEmpty()) {
                throw  new RuntimeException("Équipe non trouvée");
            }
            thisEmploye.setEquipe(equipe.get());


        }
        return employeRepository.save(thisEmploye);
    }




}
