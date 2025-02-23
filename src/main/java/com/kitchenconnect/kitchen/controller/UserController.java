package com.kitchenconnect.kitchen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        super();
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute User user, RedirectAttributes redirectAttributes ) {
        userService.registerUser(user);
        redirectAttributes.addFlashAttribute("registrationMessage", "Registration successful!");
        
        // Redirect back to the accounts page
        return "redirect:/accounts";   
    }

}
