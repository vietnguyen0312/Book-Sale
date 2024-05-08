package com.example.BookSaleProject.Model.Service;

import com.example.BookSaleProject.Model.Entity.Book;

public interface IRateService {
    public float getScoreByIdBook(Book book);
}
