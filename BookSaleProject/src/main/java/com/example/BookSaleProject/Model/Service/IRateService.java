package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Entity.Rate;

public interface IRateService {
    public float getScoreByIdBook(Book book);
    public ArrayList<Rate> getByIdBook(Book book);
}
