package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Cart;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Repository.CartRepository;


@Service
public class CartService implements ICartService {

    ArrayList<Cart> carts = new ArrayList<>();

    @Autowired
    CartRepository cartRepository = new CartRepository();

    @Override
    public boolean addNew(Cart cart) {
        if (cartRepository.addNew(cart)) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Cart> getAll() {
        this.carts = cartRepository.getAll();
        if (!(carts.isEmpty())) {
            return carts;
        }
        return null;
    }

    @Override
    public Cart getByIdUser(User user) {
        getAll();
        for (Cart cart : carts) {
            if (cart.getUser().getId() == user.getId())
                return cart;
        }
        return null;
    }

    @Override
    public Cart getById(int id) {
        getAll();
        for(Cart cart:carts){
            if(cart.getId()==id)
                return cartRepository.getById(id);
            }
        return null;
    }
    
}
