package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.User;

@Repository
public class UserRepository {
    ArrayList<User> userList = new ArrayList<>();

    public ArrayList<User> getAllUser(){
        try {
            userList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.user");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String sdt = resultSet.getString("sdt");
                String address = resultSet.getString("address");
                int role = resultSet.getInt("Role");
                User user = new User(id, userName, password, email, sdt, address, role);
                userList.add(user);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return userList;
    }



    public User getUserById(int id){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.user where id=?");
            prsm.setInt(1, id );
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            String userName = resultSet.getString("username");
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");
            String sdt = resultSet.getString("sdt");
            String address = resultSet.getString("address");
            int role = resultSet.getInt("Role");
            User user = new User(id, userName, password, email, sdt, address, role);
            return user;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public boolean update(User user){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement
            ("Update BOOKSALE.BookType set username=?,password=?,sdt=?,address=? where id =?");
            prsm.setString(1, user.getUsername());
            prsm.setString(2, user.getPassword());
            prsm.setString(3, user.getSdt());
            prsm.setString(4, user.getAddress());
            prsm.setInt(5, user.getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public boolean addNew(User user){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Insert into BOOKSALE.user (username,password,email,sdt,address,Role) value (?,?,?,?,?,?)");
            prsm.setString(1, user.getUsername());
            prsm.setString(2, user.getPassword());
            prsm.setString(3, user.getEmail());
            prsm.setString(4, user.getSdt());
            prsm.setString(5, user.getAddress());
            prsm.setInt(6, 2);
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return false;
    }
}
