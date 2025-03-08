package com.kitchenconnect.kitchen.entity;

public class CategoryRequest {
    private String name;

    // Default constructor (required for deserialization)
    public CategoryRequest() {}

    // Parameterized constructor
    public CategoryRequest(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
