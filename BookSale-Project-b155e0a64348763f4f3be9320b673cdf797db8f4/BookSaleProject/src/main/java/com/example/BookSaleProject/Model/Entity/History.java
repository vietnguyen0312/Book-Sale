package com.example.BookSaleProject.Model.Entity;


public class History {
    
    public History() {
    }
    private int id;
    private Bill bill;
    String detail;
    public History(int id, Bill bill, String detail) {
        this.id = id;
        this.bill = bill;
        this.detail = detail;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public Bill getBill() {
        return bill;
    }
    public void setBill(Bill bill) {
        this.bill = bill;
    }
    @Override
    public String toString() {
        return "History [id=" + id + ", bill=" + bill + ", detail=" + detail + "]";
    }
    
}
