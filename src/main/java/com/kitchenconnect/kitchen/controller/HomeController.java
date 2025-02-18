package com.kitchenconnect.kitchen.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kitchenconnect.kitchen.entity.Role;
import com.kitchenconnect.kitchen.entity.User;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/kitchens")
    public String kitchens() {
        return "kitchens";
    }

    @GetMapping("/kitchenpage")
    public String kitchenpage() {
        return "kitchenpage";
    }
    

    // Show the registration and login page
    @GetMapping("/accounts")
    public String showAccountsPage(Model model) {
        model.addAttribute("user", new User()); // Ensure User class has the necessary fields
        return "accounts"; // This should match the name of your HTML file without the .html extension
    }
}
