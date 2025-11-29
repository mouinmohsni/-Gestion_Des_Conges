package com.projet.gestion_des_conges.controllers;

import com.projet.gestion_des_conges.models.CalendrierConge;
import com.projet.gestion_des_conges.models.SoldeConge;
import com.projet.gestion_des_conges.repositories.CongeRepository;
import com.projet.gestion_des_conges.services.CalendrierCongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CalendrierCongeController {

    @Autowired
    CalendrierCongeService calendrierCongeService;

    @RequestMapping("/addCalendrierConge")
    public String addSoldeConge(Model model) {
        CalendrierConge calendrierConge = new CalendrierConge();
        model.addAttribute("calendrierConge", calendrierConge);
        return "new_calendrier_conge";
    }

    @RequestMapping(value = "/saveCalendrierConge", method = RequestMethod.POST)
    public String saveCalendrierConge(@ModelAttribute("CalendrierCongeForm") CalendrierConge calendrierConge) {
        calendrierCongeService.createCalendrierConge(calendrierConge);
        return "redirect:/allCalendrierConges";
    }

    @RequestMapping("/allCalendrierConges")
    public String listCalendrierConges(Model model) {
        List<CalendrierConge> listCalendrierConges = calendrierCongeService.getAllCalendrierConges();
        model.addAttribute("listCalendrierConges", listCalendrierConges);
        return "liste_calendrier_conges";
    }

    @GetMapping("edit/{id}")
    public String showEditCalendrierConge(@PathVariable("id") Long id, Model model) {
        CalendrierConge calendrierConge = calendrierCongeService.getCalendrierCongeById(id);
        model.addAttribute("calendrierConge", calendrierConge);
        return "edit_calendrier_conge";
    }

    @PostMapping("update/{id}")
    public String updateCalendrierConge(Model model, CalendrierConge calendrierConge, BindingResult bindingResult, @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            calendrierConge.setId(id);
            return "edit_calendrier_conge";
        }
        calendrierCongeService.updateCalendrierConge(calendrierConge);
        return "redirect:/allCalendrierConges";
    }

    @GetMapping("delete/{id}")
    public String deleteCalendrierConge(@PathVariable("id") long id) {
        calendrierCongeService.deleteCalendrierConge(id);
        return "redirect:/allCalendrierConges";
    }
}
