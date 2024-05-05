package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.Bill;
import com.example.BookSaleProject.Model.Entity.User;

public interface IBillService {
    public ArrayList<Bill> getAll();
    public boolean addNew(Bill bill);
    public Bill getById(int id);
    public Bill getByIdUser(User user);
    public boolean update(Bill bill);
}
