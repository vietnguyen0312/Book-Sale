package com.example.BookSaleProject.Model.Entity;

public class CartProBox {
    
    @Override
    public String toString() {
        return "CartProBox [id=" + id + ", cart=" + cart + ", book=" + book + ", SL=" + SL + "]";
    }
    public CartProBox(int id, Cart cart, Book book, int sL) {
        this.id = id;
        this.cart = cart;
        this.book = book;
        SL = sL;
    }
    public CartProBox() {
    }
    int id;
    private Cart cart;
    private Book book;
    private int SL;
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public int getSL() {
        return SL;
    }
    public void setSL(int sL) {
        SL = sL;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}
