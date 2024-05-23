package com.example.BookSaleProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Random;

import com.example.BookSaleProject.Model.Entity.Cart;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Service.BillService;
import com.example.BookSaleProject.Model.Service.BookTypeService;
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
                    user = userService.getUserByEmail(userStr);
                    session.setAttribute("userEmail", userStr);
                    session.setAttribute("userName", user.getUsername());
                    session.setAttribute("roleUser", user.getRole());
                    if (user.getRole().equals("ADMIN")) {
                        return "redirect:/admin/";
                    }
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
        user1.setRole(null);
        user1.setSdt(null);
        user1.setUsername(null);
        boolean flag = userService.toLogin(user1);
        user1 = userService.getUserByEmail(user1.getEmail());
        if (Boolean.TRUE.equals(rememberme)) {
            if (flag) {
                Cookie cookie = new Cookie("userCookie", user1.getEmail());
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);
                session.setAttribute("userEmail", user1.getEmail());
                session.setAttribute("userName", user1.getUsername());
                session.setAttribute("roleUser", user1.getRole());
                if (user1.getRole().equals("ADMIN")) {
                    return "redirect:/admin/";
                }
                return "redirect:/";
            }
        } else if (!Boolean.TRUE.equals(rememberme)) {
            if (flag) {
                session.setAttribute("userEmail", user1.getEmail());
                session.setAttribute("userName", user1.getUsername());
                session.setAttribute("roleUser", user1.getRole());
                if (user1.getRole().equals("ADMIN")) {
                    return "redirect:/admin/";
                }
                return "redirect:/";
            }
        } else {
            return showLogin(model, request, session);
        }
        model.addAttribute("Message", "ERROR");
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
        session.removeAttribute("roleUser");
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
        user1.setRole("USER");
        user1.setId(0);
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

    @GetMapping(value = "/showMissPassword")
    public String showMissPassword(Model model) {
        return "MissPassword";
    }

    @GetMapping(value = "/missPassword")
    public ResponseEntity<String> missPassword(@RequestParam("email") String email) {
        ArrayList<User> users = userService.getAllUser();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                Random random = new Random();
                int randomNumber = random.nextInt(90000) + 10000;
                userService.sendMail(user.getEmail(), "Code Login for you",
                        randomNumber + "");
                return ResponseEntity.ok().body("" + randomNumber);
            }
        }
        return ResponseEntity.badRequest().body("Tài khoản không tồn tại");
    }

    @PostMapping(value = "/changePass")
    public ResponseEntity<String> refreshPassword(@RequestParam(name = "newPassword") String newPassword,
            @RequestParam("email") String email) {
        if (userService.checkValidatePass(newPassword)) {
            User user = userService.getUserByEmail(email);
            user.setPassword(newPassword);
            userService.update(user);
            return ResponseEntity.ok().body("/user/");
        }
        return ResponseEntity.badRequest().body("Mật khẩu quá ngắn");
    }

    @GetMapping(value = "/userInfo")
    public String userInfo(Model model, HttpServletRequest request) {
        BookTypeService bookTypeService = new BookTypeService();
        HttpSession session = request.getSession();
        User user = userService.getUserByEmail(session.getAttribute("userEmail").toString());
        model.addAttribute("user", user);
        model.addAttribute("bookTypeList", bookTypeService.getAll());
        return "UserInfor";
    }

    @GetMapping(value = "/error")
    public String showError() {
        return "403";
    }

    @PostMapping(value = "/updateUser")
    public String updateUser(Model model, @ModelAttribute("user") User userUpdate, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getUserByEmail(session.getAttribute("userEmail").toString());
        session.setAttribute("userName", userUpdate.getUsername());
        user.setAddress(userUpdate.getAddress());
        user.setUsername(userUpdate.getUsername());
        userService.update(user);
        return userInfo(model, request);
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("newPassword") String newPassword,
            @RequestParam("currentPassword") String currentPassword,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getUserByEmail(session.getAttribute("userEmail").toString());
        if (user.getPassword().equals(currentPassword)) {
            if (userService.checkValidatePass(newPassword)) {
                user.setPassword(newPassword);
                userService.update(user);
                return ResponseEntity.ok().body("Success");
            }
            return ResponseEntity.badRequest().body("Mật khẩu quá ngắn");
        }
        return ResponseEntity.badRequest().body("Mật khẩu hiện tại không chính xác");

    }
}
