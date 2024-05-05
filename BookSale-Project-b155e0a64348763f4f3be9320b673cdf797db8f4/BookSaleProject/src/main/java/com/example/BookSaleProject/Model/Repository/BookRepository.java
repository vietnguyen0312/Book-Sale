package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.util.ArrayList;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.BookType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.Entity.Book;

@Repository
public class BookRepository {
    ArrayList<Book> bookList = new ArrayList<>();
    @Autowired
    BookTypeRepository bookTypeRepository = new BookTypeRepository();


    public ArrayList<Book> getAll(){
        
        try {
            bookList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.Book");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                BookType bookType = bookTypeRepository.getByID(resultSet.getInt("booktype"));
                String date = resultSet.getString("date");
                String nxb = resultSet.getString("nxb");
                Double price = Double.parseDouble(resultSet.getString("price"));
                int SL = resultSet.getInt("SL");
                String img = resultSet.getString("img");
                String detail = resultSet.getString("detail");
                Book book = new Book(id, name, author, bookType, date, nxb, price, SL, img,detail);
                bookList.add(book);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bookList;
    }

    public Book getByID(int id){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.Book where id=?");
            prsm.setInt(1, id );
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            BookType bookType = bookTypeRepository.getByID(resultSet.getInt("booktype"));
            String date = resultSet.getString("date");
            String nxb = resultSet.getString("nxb");
            Double price = Double.parseDouble(resultSet.getString("price"));
            int SL = resultSet.getInt("SL");
            String img = resultSet.getString("img");
            String detail = resultSet.getString("detail");
            Book book = new Book(id, name, author, bookType, date, nxb, price, SL, img, detail);
            con.close();
            return book;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public boolean addNew(Book book){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Insert into BOOKSALE.Book (name,author,booktype,date,nxb,price,SL,img) values (?,?,?,?,?,?,?,?)");
            prsm.setString(1,book.getName());
            prsm.setString(2, book.getAuthor());
            prsm.setInt(3, book.getBookType().getId());
            prsm.setString(4, book.getDate());
            prsm.setString(5, book.getNxb());
            prsm.setString(6, ""+book.getPrice());
            prsm.setInt(7, book.getSL());
            prsm.setString(8, book.getImg());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return false;
    }

    public boolean update(Book book) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Update BOOKSALE.Book set name=?,author=?,booktype=?,date=?,nxb=?,price=?,SL=?,img=?,detail=? where id =?");
            prsm.setString(1,book.getName());
            prsm.setString(2, book.getAuthor());
            prsm.setInt(3, book.getBookType().getId());
            prsm.setString(4, book.getDate());
            prsm.setString(5, book.getNxb());
            prsm.setString(6, "" + book.getPrice());
            prsm.setInt(7, book.getSL());
            prsm.setString(8, book.getImg());
            prsm.setString(9, book.getDetail());
            prsm.setInt(10, book.getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

}
