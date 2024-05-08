package com.example.BookSaleProject.Model.Entity;

public class Rate {
    @Override
    public String toString() {
        return "Rate [id=" + id + ", user=" + user + ", book=" + book + ", score=" + score + "]";
    }
    public Rate(int id, User user, Book book, float score) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.score = score;
    }
    public Rate() {
    }
    private int id;
    private User user;
    private Book book;
    private float score;
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
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }
}
