package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "kitchen_id", nullable = false)
    private Kitchen kitchen;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Prevents circular reference
    private List<MenuItem> menuItems;

    @Transient // This field is not persisted in the database
    private int itemCount = 0; // Default to 0

    private int position;

    // Constructors
    public Category() {
        // Default constructor
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Category(String name, Kitchen kitchen) {
        this.name = name;
        this.kitchen = kitchen;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    // Calculate itemCount dynamically based on the size of menuItems
    public int getItemCount() {
        return menuItems != null ? menuItems.size() : 0;
    }

    // Setter for itemCount (if needed, though it's not recommended to set it manually)
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}