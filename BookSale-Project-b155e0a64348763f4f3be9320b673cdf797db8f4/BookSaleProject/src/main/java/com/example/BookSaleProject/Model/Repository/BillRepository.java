package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.Bill;
import com.example.BookSaleProject.Model.Entity.User;

@Repository
public class BillRepository {
    ArrayList<Bill> bills = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    @Autowired
    UserRepository userRepository = new UserRepository();

    public ArrayList<Bill> getAll(){
        try {
            bills.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.bill");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User user = userRepository.getUserById(resultSet.getInt("idUser"));
                LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("Date"), formatter);
                String status = resultSet.getString("status");
                Bill bill = new Bill(id, user, localDateTime,status);
                bills.add(bill);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bills;
    }

    public Bill getById(int id){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.bill where id=?");
            prsm.setInt(1, id);
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            User user = userRepository.getUserById(resultSet.getInt("idUser"));
            LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("Date"), formatter);
            String status = resultSet.getString("status");
            Bill bill = new Bill(id, user, localDateTime,status);
            con.close();
            return bill;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public Bill getByIdUser(User user){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.bill where idUser=?");
            prsm.setInt(1, user.getId() );
            ResultSet resultSet = prsm.executeQuery();
            if(!resultSet.next()){
                throw new IllegalArgumentException("Cannot Find");
            }
            int id = resultSet.getInt("id");
            LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("Date"), formatter);
            String status = resultSet.getString("status");
            Bill bill = new Bill(id, user, localDateTime, status);
            con.close();
            return bill;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public boolean addNew(Bill bill){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Insert into BOOKSALE.bill (idUser,Date,status) values (?,?,?)");
            prsm.setInt(1,bill.getUser().getId());
            prsm.setString(2, bill.getDate().withNano(0).toString());
            prsm.setString(3, bill.getStatus());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public boolean update(Bill bill){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Update BOOKSALE.bill set status=? where id =?");
            prsm.setString(1, bill.getStatus());
            prsm.setInt(2, bill.getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }
}
