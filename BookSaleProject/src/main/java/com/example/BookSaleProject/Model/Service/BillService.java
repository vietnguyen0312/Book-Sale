package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.Bill;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Repository.BillRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@Service
public class BillService implements IBillService {
    ArrayList<Bill> bills = new ArrayList<>();

    @Autowired
    BillRepository billRepository = new BillRepository();

    @Override
    public ArrayList<Bill> getAll() {
        this.bills = billRepository.getAll();
        if (!(bills.isEmpty())) {
            return bills;
        }
        return null;
    }

    @Override
    public boolean addNew(Bill bill) {
        if (billRepository.addNew(bill)) {
            return true;
        }
        return false;
    }

    @Override
    public Bill getById(int id) {
        getAll();
        for (Bill bill : bills) {
            if (bill.getId() == id)
                return billRepository.getById(id);
        }
        return null;
    }

    @Override
    public Bill getByIdUser(User user) {
        getAll();
        for (Bill bill : bills) {
            if (bill.getUser().getId() == user.getId())
                return bill;
        }
        return null;
    }

    @Override
    public boolean update(Bill bill) {
        if (billRepository.update(bill)) {
            return true;
        }
        return false;
    }

    public boolean payment(double total, User user) {
        Stripe.apiKey = "sk_test_51PCLEW04f7xtY1EEz7iu04F269Zl82jzK2aYA8kK4Lpnf8v82XhWKpPHxo6gdq7xOxqZVf754m0yHGuCuSbYCfKO00ji33h87L";
        int value = (int) total;
        try {
            Map<String, Object> customerParameter = new HashMap<String, Object>();
            customerParameter.put("email", user.getEmail());
            customerParameter.put("name", user.getUsername());
            Customer newCustomer = Customer.create(customerParameter);
            String customerId = newCustomer.getId();
            Map<String, Object> retrieveParams = new HashMap<>();
            List<String> expandList = new ArrayList<>();
            expandList.add("sources");
            retrieveParams.put("expand", expandList);
            Customer customer = Customer.retrieve(
                    customerId,
                    retrieveParams,
                    null);
            Map<String, Object> cardParams = new HashMap<>();
            cardParams.put("source", "tok_visa");
            @SuppressWarnings("unused")
            Card card = (Card) customer.getSources().create(cardParams);
            Map<String, Object> chargeParam = new HashMap<String, Object>();
            chargeParam.put("amount", value);
            chargeParam.put("currency", "usd");
            chargeParam.put("customer", customer.getId());
            @SuppressWarnings("unused")
            Charge charge = Charge.create(chargeParam);

        } catch (StripeException ex) {

        }
        return true;
    }
}
