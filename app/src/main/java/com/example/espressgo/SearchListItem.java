package com.example.espressgo;

import models.Shop;
import models.User;

public class SearchListItem {
    private String title;
    private User user;
    private Shop shop;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setShop(Shop shop) {
        this.shop = shop;
    }
    public Shop getShop() {
        return shop;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
    public String userOrShop() {
        if (user != null) {
            return "Follow";
        }
        if (shop != null) {
            return "Shop Page";
        }
        return null;
    }
}
