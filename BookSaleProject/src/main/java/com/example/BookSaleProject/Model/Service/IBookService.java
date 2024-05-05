package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.Book;

public interface IBookService {
    public boolean update(Book book);
    public ArrayList<Book> getAll();
    public Book getByID(int id);
    public boolean addNew(Book book);
}
