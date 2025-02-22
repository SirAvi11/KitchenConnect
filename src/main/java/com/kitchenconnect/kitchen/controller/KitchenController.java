package com.kitchenconnect.kitchen.controller;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.service.KitchenService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kitchens")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;

    @GetMapping
    public String showAllKitchens(Model model) {
        List<Kitchen> allKitchens = kitchenService.getAllKitchens();

        model.addAttribute("kitchens", allKitchens);
        return "kitchens";
    }

    @GetMapping("/{id}")
    public String showKitchenDetails(@PathVariable Long id, Model model) {
        Kitchen kitchen = kitchenService.getKitchenById(id);
        if (kitchen != null) {
            model.addAttribute("kitchen", kitchen);
            return "kitchenpage";
        }
        return "redirect:/"; // Redirect to homepage if kitchen not found
    }
}


