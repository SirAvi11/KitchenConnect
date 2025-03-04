package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chefs")
public class Chef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chefId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // Foreign key reference to User
    private User user;

    @OneToOne
    @JoinColumn(name = "kitchen_id", nullable = false, unique = true) // Foreign key reference to Kitchen
    private Kitchen kitchen;

    @Column(columnDefinition = "TEXT")
    private String chefProfilePicture;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(columnDefinition = "TEXT")
    private String favouriteDishes;

    // Getters and Setters

    public Kitchen getKitchen() {
        return kitchen;
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }
    
    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public String getChefProfilePicture() {
        return chefProfilePicture;
    }

    public void setChefProfilePicture(String chefProfilePicture) {
        this.chefProfilePicture = chefProfilePicture;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFavouriteDishes() {
        return favouriteDishes;
    }

    public void setFavouriteDishes(String favouriteDishes) {
        this.favouriteDishes = favouriteDishes;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

