package com.projet.gestion_des_conges.services;


import com.projet.gestion_des_conges.data_transfer_object.CongeCreationDto;
import com.projet.gestion_des_conges.data_transfer_object.EmployeCreationRequest;
import com.projet.gestion_des_conges.models.*;
import com.projet.gestion_des_conges.repositories.EmployeRepository;
import com.projet.gestion_des_conges.repositories.EquipeRepository;
import com.projet.gestion_des_conges.repositories.UtilisateurRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeService {

    public final UtilisateurRepository utilisateurRepository;
    public final EmployeRepository employeRepository;
    public final EquipeRepository equipeRepository;

    @Autowired
    public EmployeService(
            UtilisateurRepository utilisateurRepository,
            EmployeRepository employeRepository,
            EquipeRepository equipeRepository) {

        this.utilisateurRepository = utilisateurRepository;
        this.employeRepository = employeRepository;
        this.equipeRepository = equipeRepository;
    }



    public Utilisateur creerEmploye(EmployeCreationRequest request , BindingResult result)  {

        Optional<Utilisateur> potentialUtilisateur = utilisateurRepository.findByEmail(request.getEmail());
        if (potentialUtilisateur.isPresent()) {
            result.rejectValue("email", "RegisterError", "Cette adresse email est déjà utilisée.");
            return null;
        }

        if (!request.getMotDePasse().equals(request.getConfPassword())) {
            result.rejectValue("confpassword", "register error", "Passwords must match");
            return null;
        }

        Optional<Equipe> equipeOptionnelle = equipeRepository.findById(request.getEquipeId());
        if (equipeOptionnelle.isEmpty()) {
            result.rejectValue("equipeId", "CreationError", "L'équipe spécifiée n'existe pas.");
        }

        if (result.hasErrors()) {
            return null;
        }

        String motDePasseHache = BCrypt.hashpw(request.getMotDePasse(), BCrypt.gensalt());
        request.setMotDePasse(motDePasseHache);

        Equipe equipeTrouvee = equipeOptionnelle.get();


        Employe nouvelEmploye = new Employe();
        nouvelEmploye.setNom(request.getNom());
        nouvelEmploye.setPrenom(request.getPrenom());
        nouvelEmploye.setEmail(request.getEmail());
        nouvelEmploye.setEquipe(equipeTrouvee);
        nouvelEmploye.setMotDePasse(motDePasseHache);
        nouvelEmploye.setDepartment(request.getDepartment());


        // Créer et lier le solde de congés (logique inchangée)
        SoldeConge soldeInitial = new SoldeConge();
        soldeInitial.setAnnee(LocalDate.now().getYear());
        soldeInitial.setTotalAnnuel(25);
        soldeInitial.setRestant(25);
        nouvelEmploye.setSoldeConge(soldeInitial);
        soldeInitial.setEmploye(nouvelEmploye);


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
     * Permet à un employé de soumettre une nouvelle demande de congé.
     */
    public Conge demanderConge(Long employeId, CongeCreationDto data, BindingResult result){
        Optional<Employe> employe = employeRepository.findById(employeId);
        if (employe.isEmpty()) {
            throw new RuntimeException("l'employer non trouvé !");
        }
        if (data.getDateDebut() != null && data.getDateFin() != null) {
            if (data.getDateDebut().isAfter(data.getDateFin())) {
                result.rejectValue("dateFin", "DateError", "La date de fin doit être après la date de début.");
            }
        }
        Conge conge = new Conge();






    }




}
