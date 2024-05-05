package com.example.BookSaleProject.Model.Entity;

import java.time.LocalDateTime;

public class Bill {
    
    
    public Bill() {
    }
    private int id;
    private User user;
    private LocalDateTime date;
    private String status;

    public Bill(int id, User user, LocalDateTime date, String status) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.status = status;
    }
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
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Bill [id=" + id + ", user=" + user + ", date=" + date + ", status=" + status + "]";
    }
}
