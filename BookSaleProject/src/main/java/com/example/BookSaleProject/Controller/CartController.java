package com.example.BookSaleProject.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    HashMap<CartProBox, Float> cartBookList = new HashMap<>();
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

    @PostMapping(value = "/delete")
    public ResponseEntity<String> deleteBook(@RequestParam("id") String id) {
        cartProBoxService.delete(Integer.parseInt(id));
        return ResponseEntity.ok().body("Xoá thành công");
    }

    @PostMapping(value = "/update")
    public ResponseEntity<String> updateBook(@RequestParam("id") String id, @RequestParam("quantity") String quantity) {
        CartProBox cartProBox = cartProBoxService.getById(Integer.parseInt(id));
        cartProBox.setSL(Integer.parseInt(quantity));
        cartProBoxService.update(cartProBox);
        return ResponseEntity.ok().body("Cập nhật thành công");
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addBook(@RequestParam("id") String id, HttpServletRequest request,
            @RequestParam("SL") String SL) {
        System.out.println(SL);
        int idBook = Integer.parseInt(id);
        if (cartProBoxService.getByIdBook(bookService.getByID(idBook)) != null) {
            CartProBox cartProBox = cartProBoxService.getByIdBook(bookService.getByID(idBook));
            if (cartProBox.getSL() + Integer.parseInt(SL) <= cartProBox.getBook().getSL()) {
                cartProBox.setSL(cartProBox.getSL() + Integer.parseInt(SL));
                cartProBoxService.update(cartProBox);
            } else {
                return ResponseEntity.badRequest().body("Số lượng đặt hàng vượt quá số lượng có sẵn trong kho!");
            }
        } else {
            HttpSession session = request.getSession();
            User userSession = userService.getUserByEmail(session.getAttribute("userEmail").toString());
            CartProBox cartProBox = new CartProBox(0, cartService.getByIdUser(userSession), bookService.getByID(idBook),
                    Integer.parseInt(SL));
            cartProBoxs.add(cartProBox);
            cartProBoxService.addNew(cartProBox);
        }
        return ResponseEntity.ok().body("Thêm thành công");
    }
}
