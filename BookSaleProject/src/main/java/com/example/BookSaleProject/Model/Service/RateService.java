package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Entity.Rate;
import com.example.BookSaleProject.Model.Repository.RateRepository;

@Service
public class RateService implements IRateService {

    @Autowired
    RateRepository rateRepository = new RateRepository();

    @Override
    public float getScoreByIdBook(Book book) {
        if(rateRepository.getScoreByIdBook(book)!=0){
            return rateRepository.getScoreByIdBook(book);
        }
        return 0;
    }

    @Override
    public ArrayList<Rate> getByIdBook(Book book) {
        ArrayList<Rate> rates = rateRepository.getByIdBook(book);
        if (!rates.isEmpty()) {
            return rates;
        }
        return null;
    }

    @Override
    public boolean addNew(Rate rate) {
        if (rateRepository.addNew(rate)) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Rate> getAll() {
        ArrayList<Rate> rates = rateRepository.getAll();
        if (!(rates.isEmpty())) {
            return rates;
        }
        return null;
    }


}
