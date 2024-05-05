package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.CartProBox;
import com.example.BookSaleProject.Model.Repository.CartProBoxRepository;


@Service
public class CartProBoxService implements ICartProBoxService {
    ArrayList<CartProBox> cartProBoxs = new ArrayList<>();

    @Autowired
    CartProBoxRepository cartProBoxRepository = new CartProBoxRepository();

    @Override
    public ArrayList<CartProBox> getAll() {
        this.cartProBoxs = cartProBoxRepository.getAll();
        if (!(cartProBoxs.isEmpty())) {
            return cartProBoxs;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (cartProBoxRepository.delete(id)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(CartProBox cartProBox) {
        if (cartProBoxRepository.update(cartProBox)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addNew(CartProBox cartProBox) {
        if (cartProBoxRepository.addNew(cartProBox)) {
            return true;
        }
        return false;
    }

    @Override
    public CartProBox getById(int id) {
        getAll();
        for (CartProBox cartProBox : cartProBoxs) {
            if (cartProBox.getId() == id)
                return cartProBoxRepository.getById(id);
        }
        return null;
    }
}