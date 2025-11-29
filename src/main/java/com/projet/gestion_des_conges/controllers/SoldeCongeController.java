package com.projet.gestion_des_conges.controllers;

import com.projet.gestion_des_conges.models.SoldeConge;
import com.projet.gestion_des_conges.services.SoldeCongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SoldeCongeController {

    @Autowired
    SoldeCongeService soldeCongeService;

    @RequestMapping("/addSoldeConge")
    public String addSoldeConge(Model model) {
        SoldeConge soldeConge = new SoldeConge();
        model.addAttribute("soldeConge", soldeConge);
        return "new_solde_conge";
    }

    @RequestMapping(value = "/saveSoldeConge", method = RequestMethod.POST)
    public String saveSoldeConge(@ModelAttribute("SoldeCongeForm") SoldeConge soldeConge) {
        soldeCongeService.createSoldeConge(soldeConge);
        return "redirect:/allSoldeConges";
    }

    @RequestMapping("/allSoldeConges")
    public String listSoldeConges(Model model) {
        List<SoldeConge> listSoldeConges = soldeCongeService.getAllSoldeConges();
        model.addAttribute("listSoldeConges", listSoldeConges);
        return "liste_solde_conges";
    }

    @GetMapping("edit/{id}")
    public String showEditSoldeConge(@PathVariable("id") Long id, Model model) {
        SoldeConge soldeConge = soldeCongeService.getSoldeCongeById(id);
        model.addAttribute("soldeConge", soldeConge);
        return "edit_solde_conge";
    }

    @PostMapping("update/{id}")
    public String updateSoldeConge(Model model, SoldeConge soldeConge, BindingResult bindingResult, @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            soldeConge.setId(id);
            return "edit_solde_conge";
        }
        soldeCongeService.updateSoldeConge(soldeConge);
        return "redirect:/allSoldeConges";
    }

    @GetMapping("delete/{id}")
    public String deleteSoldeConge(@PathVariable("id") long id) {
        soldeCongeService.deleteSoldeConge(id);
        return "redirect:/allSoldeConges";
    }

}
