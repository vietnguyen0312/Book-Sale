package com.example.BookSaleProject.Model.Entity;

public class Cart {
    @Override
    public String toString() {
        return "Cart [id=" + id + ", user=" + user + "]";
    }
    public Cart(int id, User user) {
        this.id = id;
        this.user = user;
    }
    public Cart() {
    }
    private int id;
    private User user;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
