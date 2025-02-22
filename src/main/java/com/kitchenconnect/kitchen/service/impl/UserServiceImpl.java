package com.kitchenconnect.kitchen.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.repository.UserRepository;
import com.kitchenconnect.kitchen.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserServiceImpl(UserRepository userRepository){
        super();
        this.userRepository = userRepository;
    }

    public User registerUser (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
