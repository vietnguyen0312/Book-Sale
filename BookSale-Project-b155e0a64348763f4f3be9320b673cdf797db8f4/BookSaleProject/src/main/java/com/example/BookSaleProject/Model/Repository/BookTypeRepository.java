package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.BookType;

@Repository
public class BookTypeRepository {
    ArrayList<BookType> bookTypesList = new ArrayList<>();
    public ArrayList<BookType> getAll(){
        try {
            bookTypesList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.BookType");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                BookType bookType = new BookType(id,name);
                bookTypesList.add(bookType);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bookTypesList;
    }

    public BookType getByID(int id){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.BookType where id=?");
            prsm.setInt(1, id );
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            String name = resultSet.getString("name");
            BookType bookType = new BookType(id, name);
            con.close();
            return bookType;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public boolean addNew(BookType bookType){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Insert into BOOKSALE.BookType (name) values (?)");
            prsm.setString(1,bookType.getName());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return false;
    }
    
    public boolean update(BookType bookType) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Update BOOKSALE.BookType set name=? where id =?");
            prsm.setString(1, bookType.getName());
            prsm.setInt(2, bookType.getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }
}
