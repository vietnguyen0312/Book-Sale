package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Entity.Cart;
import com.example.BookSaleProject.Model.Entity.CartProBox;

@Repository
public class CartProBoxRepository {
    ArrayList<CartProBox> cartProBoxs = new ArrayList<>();

    @Autowired
    BookRepository bookRepository = new BookRepository();
    CartRepository cartRepository = new CartRepository();

    public ArrayList<CartProBox> getAll() {
        try {
            cartProBoxs.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.cartprobox");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Book book = bookRepository.getByID(resultSet.getInt("idBook"));
                Cart cart = cartRepository.getById(resultSet.getInt("idCart"));
                int SL = resultSet.getInt("SL");
                CartProBox cartProBox = new CartProBox(id, cart, book, SL);
                cartProBoxs.add(cartProBox);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return cartProBoxs;
    }

    public boolean delete(int id) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Delete from BookSale.cartprobox where id=?");
            prsm.setInt(1, id);
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public boolean update(CartProBox cartProBox) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Update BOOKSALE.cartprobox set SL=? where id =?");
            prsm.setInt(1, cartProBox.getSL());
            prsm.setInt(2, cartProBox.getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public boolean addNew(CartProBox cartProBox) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con
                    .prepareStatement("Insert into BOOKSALE.cartprobox (idCart,idBook,SL) values (?,?,?)");
            prsm.setInt(1, cartProBox.getCart().getId());
            prsm.setInt(2, cartProBox.getBook().getId());
            prsm.setInt(3, cartProBox.getSL());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return false;
    }

    public CartProBox getById(int id) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.cartprobox where id=?");
            prsm.setInt(1, id);
            ResultSet resultSet = prsm.executeQuery();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Cannot Find");
            }
            Cart cart = cartRepository.getById(resultSet.getInt("idCart"));
            Book book = bookRepository.getByID(resultSet.getInt("idBook"));
            int SL = resultSet.getInt("SL");
            CartProBox cartProBox = new CartProBox(id, cart, book, SL);
            con.close();
            return cartProBox;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
