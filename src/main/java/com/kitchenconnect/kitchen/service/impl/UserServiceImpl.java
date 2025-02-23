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

    public User registerUser (User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save User in Database
        User savedUser = userRepository.save(user);

        // If User is a CHEF, also create a Chef entry
        if ("CHEF".equalsIgnoreCase(user.getRole())) {
            Chef chef = new Chef();
            chef.setUser(savedUser); // Link Chef to User
            chefRepository.save(chef); // Save in Chef Table
        }

        return savedUser;
    }

    public Optional<User> loginUser (String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User loadUserByUsername(String username){
            return null;
        // Optional<User> user =  userRepository.findByUsername(username);

        // if(user.isPresent()){
        //     var userObj = user.get();
        //     return User.builder().username(userObj.getUsername()).password(userObj.getPassword());
        // }
    }

    
}
