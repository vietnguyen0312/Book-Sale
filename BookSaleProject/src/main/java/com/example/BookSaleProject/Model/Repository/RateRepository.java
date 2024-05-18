package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.BookSaleProject.Model.BaseConnection;
import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Entity.Rate;
import com.example.BookSaleProject.Model.Entity.User;

@Repository
public class RateRepository {
    ArrayList<Rate> rateList = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Autowired
    BookRepository bookRepository = new BookRepository();
    UserRepository userRepository = new UserRepository();

    public float getScoreByIdBook(Book book) {
        try {
            rateList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from booksale.rate where idBook=?");
            prsm.setInt(1, book.getId());
            ResultSet resultSet = prsm.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User user = userRepository.getUserById(resultSet.getInt("idUser"));
                int score = resultSet.getInt("rateScore");
                String comment = resultSet.getString("comment");
                LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("Date"), formatter);
                Rate rate = new Rate(id, user, book, score, comment, localDateTime);
                rateList.add(rate);
            }
            con.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
        if (rateList.isEmpty()||rateList == null) {
            return 0;
        }
        float score = 0;
        for (Rate rate : rateList) {
            score += rate.getScore();
        }
        score /= rateList.size();
        return score;
    }

    public ArrayList<Rate> getByIdBook(Book book) {
        ArrayList<Rate> rates = new ArrayList<>();
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con.prepareStatement("Select * from BOOKSALE.rate where idBook=?");
            prsm.setInt(1, book.getId());
            ResultSet resultSet = prsm.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User user = userRepository.getUserById(resultSet.getInt("idUser"));
                float score = resultSet.getInt("rateScore");
                String comment = resultSet.getString(("comment"));
                LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("Date"), formatter);
                Rate rate = new Rate(id, user, book, score, comment, localDateTime);
                rates.add(rate);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return rates;
    }

    public boolean addNew(Rate rate) {
        try {
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            PreparedStatement prsm = con
                    .prepareStatement("Insert into BOOKSALE.rate (idUser,idBook,rateScore,comment,Date) values (?,?,?,?,?)");
            prsm.setInt(1, rate.getUser().getId());
            prsm.setInt(2, rate.getBook().getId());
            prsm.setInt(3, (int)rate.getScore());
            prsm.setString(4, rate.getComment());
            prsm.setString(5, rate.getDate().withNano(0).toString());
            int result = prsm.executeUpdate();
            con.close();
            return result > 0;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    public ArrayList<Rate> getAll() {
        try {
            rateList.clear();
            Class.forName(BaseConnection.nameClass);
            Connection con = DriverManager.getConnection(BaseConnection.url, BaseConnection.username,
                    BaseConnection.password);
            Statement stml = con.createStatement();
            ResultSet resultSet = stml.executeQuery("Select * from BOOKSALE.rate");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User user = userRepository.getUserById(resultSet.getInt("idUser"));
                float score = resultSet.getInt("rateScore");
                Book book = bookRepository.getByID(resultSet.getInt("idBook"));
                String comment = resultSet.getString(("comment"));
                LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("Date"), formatter);
                Rate rate = new Rate(id, user, book, score, comment, localDateTime);
                rateList.add(rate);
            }
            con.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return rateList;
    }
}
