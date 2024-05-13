package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Feedback;
import com.example.BookSaleProject.Model.Repository.FeedbackRepository;

@Service
public class FeedbackService implements IFeedbackService {
    ArrayList<Feedback> feedbacks = new ArrayList<>();

    @Autowired
    FeedbackRepository feedbackRepository = new FeedbackRepository();

    @Override
    public ArrayList<Feedback> getAll() {
        this.feedbacks = feedbackRepository.getAll();
        if (!feedbacks.isEmpty()) {
            return feedbacks;
        }
        return null;
    }

    @Override
    public Feedback getById(int id) {
        getAll();
        for (Feedback feedback : feedbacks) {
            if (feedback.getId() == id)
                return feedbackRepository.getById(id);
        }
        return null;
    }

    @Override
    public boolean addNew(Feedback feedback) {
        if (feedbackRepository.addNew(feedback)) {
            return true;
        }
        return false;
    }

}
