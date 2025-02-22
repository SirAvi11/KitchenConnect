package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chefs")
public class Chef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chefId;

    @OneToOne(mappedBy = "chef", cascade = CascadeType.ALL)
    private Kitchen kitchen;

    @Column(columnDefinition = "TEXT")
    private String chefProfilePicture;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(columnDefinition = "TEXT")
    private String favouriteDishes;

    // Getters and Setters
    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public String getChefProfilePicture() {
        return chefProfilePicture;
    }

    public void setChefProfilePicture(String chefProfilePicture) {
        this.chefProfilePicture = chefProfilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}

