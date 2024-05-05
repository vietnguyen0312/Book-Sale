package com.example.BookSaleProject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({"/client"})
public class ClientController {
    UserService userService = new UserService();

    @GetMapping("/contact")
    public String showContact(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = new User();
        if (session.getAttribute("userEmail")!=null) {
            user = userService.getUserByEmail(session.getAttribute("userEmail").toString());
        }
        model.addAttribute("user", user);
        return "Contact";
    }
}
