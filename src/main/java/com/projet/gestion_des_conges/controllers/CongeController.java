package com.projet.gestion_des_conges.controllers;

import com.projet.gestion_des_conges.models.Conge;
import com.projet.gestion_des_conges.services.CongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CongeController {

    @Autowired
    CongeService congeService;

    @RequestMapping("/addConge")
    public String addConge(Model model) {
        Conge conge = new Conge();
        model.addAttribute("conge", conge);
        return "new_conge";
    }

    @RequestMapping(value = "/saveConge", method = RequestMethod.POST)
    public String saveConge(@ModelAttribute("CongeForm") Conge conge) {
        congeService.createConge(conge);
        return "redirect:/all";
    }

    @RequestMapping("/allConges")
    public String listConges(Model model) {
        List<Conge> listConges = congeService.getAllConges();
        model.addAttribute("listConges", listConges);
        return "liste_conges";
    }

    @GetMapping("edit/{id}")
    public String showEditConge(@PathVariable("id") Long id, Model model) {
        Conge conge = congeService.getCongeById(id);
        model.addAttribute("conge", conge);
        return "edit_conge";
    }

    @PostMapping("update/{id}")
    public String updateConge(Model model, Conge conge, BindingResult bindingResult, @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            conge.setId(id);
            return "edit_conge";
        }
        congeService.updateConge(conge);
        return "redirect:/allConges";
    }

    @GetMapping("delete/{id}")
    public String deleteConge(@PathVariable("id") long id) {
        congeService.deleteConge(id);
        return "redirect:/allConges";
    }



}
