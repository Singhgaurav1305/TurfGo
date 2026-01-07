package com.turf.management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "turfs")
public class Turf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Double price;
    private String type;
    private String status;

    // --- MAIN FIX IS HERE ---
    // 'LONGTEXT' allows up to 4GB of text data (Perfect for Base64 images)
    @Column(columnDefinition = "LONGTEXT") 
    private String imageUrl;

    // --- GETTERS AND SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- Methods for Image URL ---
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // --- Compatibility Methods (Kept for your existing code) ---
    public String getImage() { return imageUrl; }
    public void setImage(String image) { this.imageUrl = image; }
}