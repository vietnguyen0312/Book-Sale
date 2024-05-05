package com.example.BookSaleProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Random;

import com.example.BookSaleProject.Model.Entity.Cart;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Service.BillService;
import com.example.BookSaleProject.Model.Service.CartService;
import com.example.BookSaleProject.Model.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = { "/user" })
public class UserController {

    @Autowired
    UserService userService = new UserService();
    BillService billService = new BillService();
    CartService cartService = new CartService();


    @GetMapping(value = "/")
    public String showLogin(Model model, HttpServletRequest request, HttpSession session) {
        Cookie[] cookies = request.getCookies();
        User user = new User();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userCookie".equals(cookie.getName())) {
                    String userStr = cookie.getValue();
                    session.setAttribute("userEmail", userStr);
                    session.setAttribute("userName", userService.getUserByEmail(userStr).getUsername());
                    return "redirect:/";
                }
            }
        }
        model.addAttribute("user", user);
        return "login";
    }

    @PostMapping(value = "/login")
    public String toLogin(@ModelAttribute("user") User user1, Model model,
            HttpServletResponse response, HttpServletRequest request, HttpSession session,
            @RequestParam(value = "rememberme", required = false) Boolean rememberme) {
        user1.setAddress(null);
        user1.setId(0);
        user1.setRole(0);
        user1.setSdt(null);
        user1.setUsername(null);
        boolean flag = userService.toLogin(user1);
        if (Boolean.TRUE.equals(rememberme)) {
            if (flag) {
                Cookie cookie = new Cookie("userCookie", user1.getEmail());
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);
                session.setAttribute("userEmail", user1.getEmail());
                session.setAttribute("userName", userService.getUserByEmail(user1.getEmail()).getUsername());
                return "redirect:/";
            }
        } else if (!Boolean.TRUE.equals(rememberme)) {
            if (flag) {
                session.setAttribute("userEmail", user1.getEmail());
                session.setAttribute("userName", userService.getUserByEmail(user1.getEmail()).getUsername());
                return "redirect:/";
            }
        } else {
            return showLogin(model, request, session);
        }
        return showLogin(model, request, session);
    }

    @GetMapping(value = "/logout")
    public String logOut(Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userCookie".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        return "redirect:/";
    }

    @GetMapping(value = "/showRegist")
    public String showRegist(Model model, String messages) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        return "Register";
    }

    @PostMapping(value = "/register")
    public String toRegister(Model model, @ModelAttribute("user") User user1,
            @RequestParam(name = "passAgain") String pass) {
        user1.setRole(2);
        user1.setId(0);
        user1.setAddress(null);
        if (pass.equals(user1.getPassword())) {
            if (userService.getInvalidAttributes(user1).isEmpty() || userService.getInvalidAttributes(user1) == null) {
                Random random = new Random();
                int randomNumber = random.nextInt(90000) + 10000;
                model.addAttribute("code", randomNumber);
                userService.sendMail(user1.getEmail(), "Code Login for you",
                        randomNumber + "");
                model.addAttribute("user", user1);
                return "SubmitCode";
            } else {
                String err = "";
                for (String error : userService.getInvalidAttributes(user1)) {
                    err += error + " ";
                }
                return showRegist(model, err);
            }
        } else {
            String mess = "errorpassword";
            return showRegist(model, mess);
        }
    }

    @PostMapping(value = "/registerSucess")
    public String registerSucess(Model model, @ModelAttribute(name = "user") User user) {
        userService.addNew(user);
        cartService.addNew(new Cart(0, userService.getUserByEmail(user.getEmail())));
        model.addAttribute("Message", "Đăng kí thành công");
        return "login";
    }

    @GetMapping(value = "/userInfo")
    public String userInfo(Model model) {
        return "UserInfo";
    }
}
