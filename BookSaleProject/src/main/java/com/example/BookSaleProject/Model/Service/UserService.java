package com.example.BookSaleProject.Model.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Repository.UserRepository;

@Service
public class UserService implements IUserService {

    ArrayList<User> userList = new ArrayList<>();

    @Autowired
    UserRepository userRepository = new UserRepository();
    

    @Override
    public ArrayList<User> getAllUser() {
        this.userList = userRepository.getAllUser();
        if (!(userList.isEmpty())) {
            return userList;
        }
        return null;
    }

    @Override
    public User getUserById(int id) {
        getAllUser();
        for (User user : userList) {
            if (user.getId() == id)
                return userRepository.getUserById(id);
        }
        return null;
    }

    @Override
    public boolean update(User user) {
        if (userRepository.update(user)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addNew(User user) {
        if (userRepository.addNew(user)) {
            return true;
        }
        return false;
    }

    public User getUserByEmail(String email) {
        getAllUser();
        for (User user : userList) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public boolean toLogin(User user) {
        getAllUser();
        for (User user1 : userList) {
            if (user1.getEmail().equals(user.getEmail()) && user1.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private static final Predicate<String> EMAIL_VALIDATOR = email -> email
            .matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$");

    private static final Predicate<String> PASSWORD_VALIDATOR = password -> password.length() >= 8;

    private static final Predicate<String> PHONE_VALIDATOR = phone -> phone.matches("^\\d{10}$");

    public ArrayList<String> getInvalidAttributes(User user) {
        ArrayList<String> invalidAttributes = new ArrayList<>();
        HashSet<User> users = new HashSet<>();
        users.addAll(getAllUser());
        if (!users.add(user)) {
            invalidAttributes.add("duplicate");
        }
        if (!EMAIL_VALIDATOR.test(user.getEmail())) {
            invalidAttributes.add("email");
        }
        if (!PASSWORD_VALIDATOR.test(user.getPassword())) {
            invalidAttributes.add("password");
        }
        if (!PHONE_VALIDATOR.test(user.getSdt())) {
            invalidAttributes.add("sdt");
        }
        return invalidAttributes;
    }
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vietnguyen0312@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Sent successfully....");
    }
}
