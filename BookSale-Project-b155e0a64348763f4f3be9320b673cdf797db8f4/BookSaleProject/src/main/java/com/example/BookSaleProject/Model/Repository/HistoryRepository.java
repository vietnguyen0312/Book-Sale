package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.Bill;
import com.example.BookSaleProject.Model.Entity.History;

@Repository
public class HistoryRepository {
    ArrayList<History> histories = new ArrayList<>();

    @Autowired
    BillRepository billRepository = new BillRepository();

    public ArrayList<History> getAll(){
        try {
            histories.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.history");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Bill bill = billRepository.getById(resultSet.getInt("idBill"));
                String detail = resultSet.getString("detail");
                History history = new History(id, bill, detail);
                histories.add(history);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return histories;
    }

    public boolean addNew(History history){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con
                    .prepareStatement("Insert into BOOKSALE.history (idBill,detail) values (?,?)");
            prsm.setInt(1,history.getBill().getId());
            prsm.setString(2, history.getDetail());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return false;
    }

    public boolean update(History history){
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Update BOOKSALE.history set idBill=? where id =?");
            prsm.setInt(1, history.getBill().getId());
            prsm.setInt(2, history.getId());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }
}
