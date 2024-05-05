package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Repository.BookRepository;

@Service
public class BookService implements IBookService {

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
            if (book.getId() == id) {
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

    private static Predicate<Book> filterByName(String keyword) {
        return p -> p.getName().toLowerCase().contains(keyword.toLowerCase());
    }

    private static Predicate<Book> filterByType(String keyword) {
        return p -> p.getBookType().getName().toLowerCase().contains(keyword.toLowerCase());
    }

    private static Predicate<Book> filterByAuthor(String keyword) {
        return p -> p.getAuthor().toLowerCase().contains(keyword.toLowerCase());
    }

    private ArrayList<Book> filterBooks(String keyword, Predicate<Book> predicate) {
        ArrayList<Book> filteredBooks = new ArrayList<>();
        ArrayList<Book> ListOfBooks = getAll();
        for (Book book : ListOfBooks) { // Thay yourListOfBooks bằng danh sách thực tế của bạn
            if (predicate.test(book)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public ArrayList<Book> search(String keyword) {
        HashSet<Book> uniqueBooks = new HashSet<>(); // Set để loại bỏ các quyển sách trùng lặp
        // Lọc theo tên sách
        uniqueBooks.addAll(filterBooks(keyword, filterByName(keyword)));
        uniqueBooks.addAll(filterBooks(keyword, filterByType(keyword)));
        uniqueBooks.addAll(filterBooks(keyword, filterByAuthor(keyword)));
        return new ArrayList<>(uniqueBooks); // Chuyển Set thành ArrayList
    }
}
