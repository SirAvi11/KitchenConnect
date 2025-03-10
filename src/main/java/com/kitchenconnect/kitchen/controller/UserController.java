package com.kitchenconnect.kitchen.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.UserRole;
import com.kitchenconnect.kitchen.service.KitchenService;
import com.kitchenconnect.kitchen.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
public class UserController {
    private UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(UserService userService){
        super();
        this.userService = userService;
    }

    // Show login tab by default
    @GetMapping("/login")
    public String showLoginTab(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("activeTab", "login"); // Used in the frontend to set the correct tab
        return "accounts"; 
    }

    @PostMapping("/loginUser")
    public String loginUser(
        @RequestParam String username, 
        @RequestParam String password, 
        @RequestParam(name = "redirect", required = false) String redirectUrl,
        HttpSession session, 
        RedirectAttributes redirectAttributes) {
    
        // Fetch user by username or email
        Optional<User> existingUser = userService.findByUsernameOrEmail(username, username);
        if (existingUser.isPresent() && passwordEncoder.matches(password, existingUser.get().getPassword())) {
    
            // Store user in session
            session.setAttribute("loggedInUser", existingUser.get());  
    
        
            // Redirect to the specific page if a redirect URL is provided
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            } else {
                return "redirect:/dashboard";
            }
        }
    
        // Invalid login - Show error message using RedirectAttributes
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password!");
    
        return "redirect:/accounts/login";  // Redirect back to login page
    }


    // Show register tab
    @GetMapping("/register")
    public String showRegisterTab(Model model) {
        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("activeTab", "register"); 
        return "accounts"; 
    }


    @PostMapping("/save")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Validate user fields (assuming validation annotations in the User entity)
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "All fields are required!");
                return "redirect:/accounts/register";
            }
    
            // Check if user already exists (assuming userService has this method)
            Optional<User> existingUser = userService.findByUsernameOrEmail(user.getUsername(), user.getEmail());
            if (existingUser.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Username or email already taken!");
                return "redirect:/accounts/register";
            }
    
            // Save user
            //Make user a foodlover by default;
            user.setRole(UserRole.FOOD_LOVER);

            // If the user is new or providing a new password, encode it
            System.out.println("Registering user" + user.getPassword());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                System.out.println("encoding");

                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            System.out.println("after encoding user" + user.getPassword());

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            
            return "redirect:/accounts/login"; // Redirect to login page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred. Please try again.");
            return "redirect:/accounts/register"; // Stay on register page
        }
    }

    @PostMapping("/updatePersonalInformation")
    public String updateUser(@ModelAttribute User updatedUser, RedirectAttributes redirectAttributes, HttpSession session) {
        // Get the logged-in user from session
        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "No user is logged in.");
            return "redirect:/accounts/profile"; // Redirect to profile
        }

        // Update only the allowed fields
        sessionUser.setUsername(updatedUser.getUsername());
        sessionUser.setFirstname(updatedUser.getFirstname());
        sessionUser.setLastname(updatedUser.getLastname());
        sessionUser.setEmail(updatedUser.getEmail());
        sessionUser.setPhoneNumber(updatedUser.getPhoneNumber());
        sessionUser.setAddress(updatedUser.getAddress());

        // Save the updated user
        userService.saveUser(sessionUser);

        // Update session with latest user info
        session.setAttribute("loggedInUser", sessionUser);

        redirectAttributes.addFlashAttribute("successMessage", "Personal information updated successfully.");
        return "redirect:/dashboard"; // Redirect to dashboard page
    }


}
