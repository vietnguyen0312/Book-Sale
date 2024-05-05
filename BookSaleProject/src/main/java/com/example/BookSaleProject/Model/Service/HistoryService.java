package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.History;
import com.example.BookSaleProject.Model.Repository.HistoryRepository;

@Service
public class HistoryService implements IHistoryService {
    ArrayList<History> histories = new ArrayList<>();

    @Autowired
    HistoryRepository historyRepository = new HistoryRepository();

    @Override
    public boolean addNew(History history) {
        if (historyRepository.addNew(history)) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<History> getAll() {
        this.histories = historyRepository.getAll();
        if (!(histories.isEmpty())) {
            return histories;
        }
        return null;
    }

    @Override
    public boolean update(History history) {
        if (historyRepository.update(history)) {
            return true;
        }
        return false;
    }
    
}
