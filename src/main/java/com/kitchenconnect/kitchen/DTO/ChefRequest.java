package com.kitchenconnect.kitchen.DTO;

public class ChefRequest {
    private String biography;
    private String favouriteDishes;
    private String chefProfilePicture;

    // Constructor
    public ChefRequest(String biography, String favouriteDishes, String chefProfilePicture) {
        this.biography = biography;
        this.favouriteDishes = favouriteDishes;
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

    public String getChefProfilePicture() {
        return chefProfilePicture;
    }

    public void setChefProfilePicture(String chefProfilePicture) {
        this.chefProfilePicture = chefProfilePicture;
    }

    

}
