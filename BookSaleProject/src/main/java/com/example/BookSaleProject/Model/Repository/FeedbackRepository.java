package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.Feedback;

@Repository
public class FeedbackRepository {
    ArrayList<Feedback> feedbacks = new ArrayList<>();

    public ArrayList<Feedback> getAll() {
        try {
            feedbacks.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.feedback");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String comment = resultSet.getString("comment");
                Feedback feedback = new Feedback(id, name, email, comment);
                feedbacks.add(feedback);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return feedbacks;
    }

    public Feedback getById(int id) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.feedback where id=?");
            prsm.setInt(1, id);
            ResultSet resultSet = prsm.executeQuery();
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Cannot Find");
            }
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String comment = resultSet.getString("comment");
            Feedback feedback = new Feedback(id, name, email, comment);
            con.close();
            return feedback;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public boolean addNew(Feedback feedback) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con
                    .prepareStatement("Insert into BOOKSALE.feedback (name,email,comment) values (?,?,?)");
            prsm.setString(1, feedback.getName());
            prsm.setString(2, feedback.getEmail());
            prsm.setString(3, feedback.getComment());
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
