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
                float score = getScoreByIdBook(book);
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
}
