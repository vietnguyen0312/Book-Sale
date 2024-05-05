package com.example.BookSaleProject.Model.Entity;


public class Book {
    @Override
    public String toString() {
        return "Book [id=" + id + ", name=" + name + ", author=" + author + ", bookType=" + bookType + ", date=" + date
                + ", nxb=" + nxb + ", price=" + price + ", SL=" + SL + ", img=" + img + ", detail=" + detail + "]";
    }
    public Book(int id, String name, String author, BookType bookType, String date, String nxb, double price, int sL,
            String img, String detail) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.bookType = bookType;
        this.date = date;
        this.nxb = nxb;
        this.price = price;
        SL = sL;
        this.img = img;
        this.detail = detail;
    }
    public Book() {
    }
    private int id;
    private String name;
    private String author;
    private BookType bookType;
    private String date;
    private String nxb;
    private double price;
    private int SL;
    private String img;
    private String detail;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public BookType getBookType() {
        return bookType;
    }
    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getNxb() {
        return nxb;
    }
    public void setNxb(String nxb) {
        this.nxb = nxb;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getSL() {
        return SL;
    }
    public void setSL(int sL) {
        SL = sL;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    
}
