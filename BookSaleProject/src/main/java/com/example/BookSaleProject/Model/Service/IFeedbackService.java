package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.Feedback;

public interface IFeedbackService {
    public ArrayList<Feedback> getAll();
    public Feedback getById(int id);
    public boolean addNew(Feedback feedback);
}
