package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.BillProBox;
import com.example.BookSaleProject.Model.Repository.BillProBoxRepository;

@Service
public class BillProBoxService implements IBillProBoxService{
    ArrayList<BillProBox> billProBoxs = new ArrayList<>();

    @Autowired
    BillProBoxRepository billProBoxRepository = new BillProBoxRepository();

    @Override
    public ArrayList<BillProBox> getAll() {
        this.billProBoxs = billProBoxRepository.getAll();
        if (!(billProBoxs.isEmpty())) {
            return billProBoxs;
        }
        return null;
    }

    @Override
    public boolean addNew(BillProBox billProBox) {
        if (billProBoxRepository.addNew(billProBox)) {
            return true;
        }
        return false;
    }

    @Override
    public BillProBox getById(int id) {
        getAll();
        for (BillProBox billProBox : billProBoxs) {
            if (billProBox.getId() == id)
                return billProBoxRepository.getById(id);
        }
        return null;
    }
    
}
