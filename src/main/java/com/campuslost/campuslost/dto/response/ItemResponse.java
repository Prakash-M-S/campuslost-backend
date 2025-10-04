package com.campuslost.campuslost.dto.response;

import com.campuslost.campuslost.entity.Item;
import java.time.LocalDateTime;

public class ItemResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String status;
    private String location;
    private LocalDateTime datePosted;
    private String imageUrl;
    private String contactInfo;
    private String userName;
    private String userEmail;

    // Constructors
    public ItemResponse() {}

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.category = item.getCategory();
        this.status = item.getStatus();
        this.location = item.getLocation();
        this.datePosted = item.getDatePosted();
        this.imageUrl = item.getImageUrl();
        this.contactInfo = item.getContactInfo();
        this.userName = item.getUser().getName();
        this.userEmail = item.getUser().getEmail();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
