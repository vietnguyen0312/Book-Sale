package com.example.BookSaleProject.Model.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Repository.RateRepository;

@Service
public class RateService implements IRateService {

    @Autowired
    RateRepository rateRepository = new RateRepository();

    @Override
    public double getScoreByIdBook(Book book) {
        if(rateRepository.getScoreByIdBook(book)!=0){
            return rateRepository.getScoreByIdBook(book);
        }
        return 0;
    }

}
