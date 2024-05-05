package com.example.BookSaleProject.Model.Service;

import com.example.BookSaleProject.Model.Entity.Book;

public interface IRateService {
    public double getScoreByIdBook(Book book);
}
