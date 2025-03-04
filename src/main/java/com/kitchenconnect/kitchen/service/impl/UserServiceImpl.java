package com.kitchenconnect.kitchen.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.repository.ChefRepository;
import com.kitchenconnect.kitchen.repository.UserRepository;
import com.kitchenconnect.kitchen.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ChefRepository chefRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserServiceImpl(UserRepository userRepository, ChefRepository chefRepository){
        super();
        this.userRepository = userRepository;
        this.chefRepository = chefRepository;
    }

    public User saveUser (User user) {
        // Encode password before saving
        // Save User in Database
        User savedUser = userRepository.save(user);

        // If User is a CHEF, also create a Chef entry
        // if ("CHEF".equalsIgnoreCase(user.getRole())) {
        //     Chef chef = new Chef();
        //     chef.setUser(savedUser); // Link Chef to User
        //     chefRepository.save(chef); // Save in Chef Table
        // }

        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    
}
