package com.projet.gestion_des_conges.services;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CongeService {

    // 1. Injection par constructeur (meilleure pratique)
    private final CongeRepository congeRepository;

    @Autowired
    public CongeService(CongeRepository congeRepository) {
        this.congeRepository = congeRepository;
    }

    public Conge createConge(Conge congeOfUser) {
        return congeRepository.save(congeOfUser);
    }


    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }

    // 3. Gestion correcte de l'Optional pour éviter les crashs
    public Optional<Conge> getCongeById(Long id) {
        return congeRepository.findById(id);
    }

    // 4. La méthode de suppression ne retourne rien (void)
    public void deleteConge(Long id) {
        // On peut vérifier si le congé existe avant de le supprimer
        if (!congeRepository.existsById(id)) {
            // Lancer une exception pour dire que le congé n'a pas été trouvé
            throw new IllegalStateException("Impossible de trouver le congé avec l'ID " + id);
        }
        congeRepository.deleteById(id);
    }
}
