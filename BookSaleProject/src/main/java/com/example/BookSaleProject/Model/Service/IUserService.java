package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.User;

public interface IUserService {
    public ArrayList<User> getAllUser();
    public User getUserById(int id);
    public boolean update(User user);
    public boolean addNew(User user);
}
