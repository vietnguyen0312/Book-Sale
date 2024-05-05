package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.BookType;

public interface IBookTypeService {
    public ArrayList<BookType> getAll();
    public BookType getByID(int id);
    public boolean addNew(BookType bookType);
    public boolean update(BookType bookType);
}
