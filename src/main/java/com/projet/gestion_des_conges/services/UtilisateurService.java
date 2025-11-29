package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.data_transfer_object.LoginRequest;
import com.projet.gestion_des_conges.data_transfer_object.PasswordChangeDto;
import com.projet.gestion_des_conges.data_transfer_object.ProfilUpdateDto;
import com.projet.gestion_des_conges.models.Utilisateur;
import com.projet.gestion_des_conges.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;


@Service
@Transactional
public class UtilisateurService {

    public final UtilisateurRepository utilisateurRepository;


    @Autowired
    public UtilisateurService(UtilisateurRepository UtilisateurRepository) {
        this.utilisateurRepository = UtilisateurRepository;
    }

    /**
     * Recherche un utilisateur par son email.
     * @param email L'email de l'utilisateur à trouver.
     * @return Un Optional contenant l'utilisateur s'il est trouvé.
     */
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    // login
    /**
     * login.
     * @param loginRequest objet qui vient du controller qui contient l'emai et le mot de passe .
     * @return Un Optional contenant l'utilisateur s'il est trouvé.
     */
    public Utilisateur login(LoginRequest loginRequest, BindingResult result) {

        Optional<Utilisateur> potentialUtilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail());
        if (potentialUtilisateur.isEmpty()) {
            result.rejectValue("email", "login error", "Email ou mot de passe incorrect");
            return null;
        }
         Utilisateur utilisateur = potentialUtilisateur.get();
        if (!BCrypt.checkpw(utilisateur.getMotDePasse(), loginRequest.getMotDePasse())) {
            result.rejectValue("email", "login error", "Email ou mot de passe incorrect");
            return null;
        }else{
            return utilisateur;
        }
    }

    public Utilisateur updateProfil(long id , ProfilUpdateDto newUtilisateur) {
        Optional<Utilisateur> optionalUtilisateur  = utilisateurRepository.findById(id);
        if (optionalUtilisateur.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé !");
        }
        Utilisateur utilisateurExistant = optionalUtilisateur.get();
        utilisateurExistant.setNom(newUtilisateur.getNom());
        utilisateurExistant.setPrenom(newUtilisateur.getPrenom());

        return utilisateurRepository.save(utilisateurExistant);
    }


    public Utilisateur updatePassword(long id , PasswordChangeDto newPassword) {
        Optional<Utilisateur> optionalUtilisateur  = utilisateurRepository.findById(id);
        if (optionalUtilisateur.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé !");
        }
        Utilisateur utilisateurExistant = optionalUtilisateur.get();
        if(!BCrypt.checkpw(utilisateurExistant.getMotDePasse(), newPassword.getAncienMotDePasse())){
            throw new IllegalStateException("L'ancien mot de passe est incorrect.");
        }

        String nouveauMotDePasseHache = BCrypt.hashpw(newPassword.getNouveauMotDePasse(), BCrypt.gensalt());
        utilisateurExistant.setMotDePasse(nouveauMotDePasseHache);


        return utilisateurRepository.save(utilisateurExistant);
    }

    public void desactiverUtilisateur (long id){
        Optional<Utilisateur> optionalUtilisateur  = utilisateurRepository.findById(id);
        if (optionalUtilisateur.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé !");
        }
        Utilisateur utilisateurExistant = optionalUtilisateur.get();
        utilisateurExistant.setActif(false);

        utilisateurRepository.save(utilisateurExistant);


    }

    public void supprimerUtilisateur (long id){
        Optional<Utilisateur> optionalUtilisateur  = utilisateurRepository.findById(id);
        if (optionalUtilisateur.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé !");
        }
        Utilisateur utilisateurExistant = optionalUtilisateur.get();

        utilisateurRepository.delete(utilisateurExistant);


    }






}
