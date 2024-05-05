package com.example.BookSaleProject.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.BookSaleProject.Model.Entity.CartProBox;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Service.BookService;
import com.example.BookSaleProject.Model.Service.BookTypeService;
import com.example.BookSaleProject.Model.Service.CartProBoxService;
import com.example.BookSaleProject.Model.Service.CartService;
import com.example.BookSaleProject.Model.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = { "/cart" })
public class CartController {

    @Autowired
    CartService cartService = new CartService();
    CartProBoxService cartProBoxService = new CartProBoxService();
    UserService userService = new UserService();
    BookService bookService = new BookService();
    BookTypeService bookTypeService = new BookTypeService();

    HashMap<CartProBox, Double> cartBookList = new HashMap<>();
    ArrayList<CartProBox> cartProBoxs = new ArrayList<>();

    @GetMapping(value = "/viewCart")
    public String viewCart(Model model, HttpServletRequest request) {
        cartBookList.clear();
        cartProBoxs.clear();
        model.addAttribute("bookTypeList", bookTypeService.getAll());
        model.addAttribute("title", "Giỏ hàng");
        HttpSession session = request.getSession();
        if (cartProBoxService.getAll() == null) {
            model.addAttribute("check", 0);
            return "Cart";
        }
        ArrayList<CartProBox> cartProBoxAll = cartProBoxService.getAll();
        for (CartProBox cartProBox : cartProBoxAll) {
            int userIdFromSession = userService.getUserByEmail(session.getAttribute("userEmail").toString()).getId();
            if (cartProBox.getCart().getUser().getId() == userIdFromSession) {
                cartProBoxs.add(cartProBox);
            }
        }
        for (CartProBox cartProBox : cartProBoxs) {
            cartBookList.put(cartProBox, cartProBox.getSL() * cartProBox.getBook().getPrice());
        }
        if (cartBookList == null) {
            model.addAttribute("check", 0);
        }
        model.addAttribute("cartBookList", cartBookList);
        return "Cart";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteBook(Model model, HttpServletRequest request, @PathVariable(value = "id") String id) {
        for (CartProBox cartProBox : cartProBoxs) {
            if (cartProBox.getId() == Integer.parseInt(id)) {
                cartProBoxService.delete(Integer.parseInt(id));
                break;
            }
        }
        return viewCart(model, request);
    }

    @PostMapping(value = "/update/{id}")
    public String updateBook(Model model, HttpServletRequest request, @PathVariable(value = "id") String id,
            @RequestParam(name = "quantity") String SL) {
        for (CartProBox cartProBox : cartProBoxs) {
            if (cartProBox.getId() == Integer.parseInt(id)) {
                cartProBox.setSL(Integer.parseInt(SL));
                cartProBoxService.update(cartProBox);
                break;
            }
        }
        return viewCart(model, request);
    }

    @PostMapping(value = "/add/{id}")
    public String addBook(Model model, @PathVariable(value = "id") String id, HttpServletRequest request,
            @RequestParam(name = "SL") String SL) {
        int idBook = Integer.parseInt(id);
        for (CartProBox cartProBox : cartProBoxs) {
            if (cartProBox.getBook().getId() == idBook) {
                cartProBox.setSL(cartProBox.getSL() + Integer.parseInt(SL));
                cartProBoxService.update(cartProBox);
                return viewCart(model, request);
            }
        }
        HttpSession session = request.getSession();
        User userSession = userService.getUserByEmail(session.getAttribute("userEmail").toString());
        CartProBox cartProBox = new CartProBox(0, cartService.getByIdUser(userSession), bookService.getByID(idBook),
                Integer.parseInt(SL));
        cartProBoxs.add(cartProBox);
        cartProBoxService.addNew(cartProBox);
        return viewCart(model, request);
    }
}
