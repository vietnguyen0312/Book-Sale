package com.example.BookSaleProject.Model.Repository;

import java.sql.*;
import java.sql.DriverManager;
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
    @Autowired
    BookRepository bookRepository = new BookRepository();
    UserRepository userRepository = new UserRepository();
    public double getScoreByIdBook(Book book){
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
                Rate rate = new Rate(id, user, book, score);
                rateList.add(rate); 
            }
            con.close();
        
        } catch (Exception e) {
            // TODO: handle exception
        }
        double score=0;
        for (Rate rate : rateList) {
            score+=rate.getScore();
        }
        score/=rateList.size();
        return score;
    }
}
