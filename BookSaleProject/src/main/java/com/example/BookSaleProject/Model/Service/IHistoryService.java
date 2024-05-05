package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.History;

public interface IHistoryService {
    public boolean addNew(History history);
    public ArrayList<History> getAll();
    public boolean update(History history);
}
