package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Repository.BookRepository;

@Service
public class BookService implements IBookService{
    
    @Autowired
    BookRepository bookRepository = new BookRepository();


    @Override
    public boolean update(Book book) {
        if (bookRepository.update(book)) {
            return true;
        }
        return false;
    }


    @Override
    public Book getByID(int id) {
        ArrayList<Book> bookList = getAll();
        for (Book book : bookList) {
            if (book.getId()==id) {
                return bookRepository.getByID(id);
            }
        }
        return null;
    }

    @Override
    public boolean addNew(Book book) {
        if (bookRepository.addNew(book)) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Book> getAll() {
        ArrayList<Book> bookList = bookRepository.getAll();
        if (!(bookList.isEmpty())) {
            return bookList;
        }
        return null;
    }
}
