package com.kitchenconnect.kitchen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.service.ChefService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/chef")
public class ChefController {
    private ChefService chefService;

    public ChefController(ChefService chefService){
        super();
        this.chefService = chefService;
    }

    @GetMapping("/{id}")
    public String showChefPage(@PathVariable Long id, Model model) {
        Chef chef = chefService.getChefById(id);

        if (chef != null) {
            model.addAttribute("chef", chef);
            return "chef";
        }
        
        return "redirect:/"; // Redirect to homepage if kitchen not found
    }

    @PostMapping("/update-biography")
    public String updateChefBiography(@ModelAttribute("chefDetails") Chef updatedChef) {
        // Update the user in the database (including biography)
        chefService.updateChef(updatedChef);
        return "redirect:/dashboard"; // Redirect back to the profile page
    }

}
