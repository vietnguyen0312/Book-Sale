package com.example.BookSaleProject.Model.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.Cart;
import com.example.BookSaleProject.Model.Entity.User;

@Repository
public class CartRepository {
    ArrayList<Cart> cartList = new ArrayList<>();

    @Autowired
    UserRepository userRepository = new UserRepository();

    public boolean addNew(Cart cart){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Insert into BOOKSALE.Cart (idUser) values (?)");
            prsm.setInt(1,cart.getUser().getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public ArrayList<Cart> getAll(){
        try {
            cartList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.cart");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User user = userRepository.getUserById(resultSet.getInt("idUser"));
                Cart cart = new Cart(id, user);
                cartList.add(cart);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return cartList;
    }

    public Cart getByIdUser(User user){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.cart where idUser=?");
            prsm.setInt(1, user.getId() );
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            int id = resultSet.getInt("id");
            Cart cart = new Cart(id, user);
            con.close();
            return cart;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public Cart getById(int id){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.cart where id=?");
            prsm.setInt(1, id );
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            User user = userRepository.getUserById(resultSet.getInt("idUser"));
            Cart cart = new Cart(id, user);
            con.close();
            return cart;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
