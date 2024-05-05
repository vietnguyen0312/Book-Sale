package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.Cart;
import com.example.BookSaleProject.Model.Entity.User;

public interface ICartService {
    public boolean addNew(Cart cart);
    public ArrayList<Cart> getAll();
    public Cart getByIdUser(User user);
    public Cart getById(int id);
}
