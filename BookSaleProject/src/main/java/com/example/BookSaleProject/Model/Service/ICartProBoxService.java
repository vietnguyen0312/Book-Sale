package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.CartProBox;

public interface ICartProBoxService {
    public ArrayList<CartProBox> getAll();
    public boolean delete(int id);
    public boolean update(CartProBox cartProBox);
    public boolean addNew(CartProBox cartProBox);
    public CartProBox getById(int id);
}
