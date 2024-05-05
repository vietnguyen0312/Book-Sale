package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import com.example.BookSaleProject.Model.Entity.BillProBox;

public interface IBillProBoxService {
    public ArrayList<BillProBox> getAll();
    public boolean addNew(BillProBox billProBox);
    public BillProBox getById(int id);
}
