package com.example.BookSaleProject.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.BookSaleProject.Model.Entity.Bill;
import com.example.BookSaleProject.Model.Entity.BillProBox;
import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Entity.BookType;
import com.example.BookSaleProject.Model.Entity.Rate;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Service.BillProBoxService;
import com.example.BookSaleProject.Model.Service.BillService;
import com.example.BookSaleProject.Model.Service.BookService;
import com.example.BookSaleProject.Model.Service.BookTypeService;
import com.example.BookSaleProject.Model.Service.RateService;
import com.example.BookSaleProject.Model.Service.UserService;

@Controller
@RequestMapping({ "/admin" })
public class AdminController {

    @Autowired
    UserService userService = new UserService();
    BillService billService = new BillService();
    BillProBoxService billProBoxService = new BillProBoxService();
    RateService rateService = new RateService();
    BookService bookService = new BookService();
    BookTypeService bookTypeService = new BookTypeService();

    @GetMapping("/")
    public String index(Model model) {
        int totalAccount = 0, totalSaledBook = 0, totalBill = 0, totalComment;
        ArrayList<Rate> ratingAll = rateService.getAll();

        if (ratingAll == null) {
            totalComment = 0;
        } else
            totalComment = ratingAll.size();

        if (userService.getAllUser() != null) {
            for (User user : userService.getAllUser()) {
                if (!user.getRole().equals("ADMIN")) {
                    totalAccount++;
                }
            }
        }

        HashMap<String, Integer> hBookType = new HashMap<>();
        for (BookType bookType : bookTypeService.getAll()) {
            hBookType.put(bookType.getName(), 0);
        }

        if (billService.getAll() != null) {
            for (Bill bill : billService.getAll()) {
                totalBill++;
                for (BillProBox billProBox : billProBoxService.getByIdBill(bill)) {
                    totalSaledBook += billProBox.getSL();
                    String bookType = billProBox.getBook().getBookType().getName();
                    if (bill.getStatus().equals("Đã thanh toán")) {
                        hBookType.put(bookType, hBookType.get(bookType) + billProBox.getSL());
                    }
                    
                }
            }
        }
        model.addAttribute(("bookTypeChart"), hBookType);
        model.addAttribute("bookList", bookService.getAll());
        model.addAttribute("totalAccount", totalAccount);
        model.addAttribute("totalSaledBook", totalSaledBook);
        model.addAttribute("totalBill", totalBill);
        model.addAttribute("totalComment", totalComment);
        return "Admin/AdminDashBoard";
    }

    @PostMapping(value = "/addBook")
    public ResponseEntity<String> addBook(@RequestBody Book book,@RequestParam("booktype") int idBookType) {
        book.setBookType(bookTypeService.getByID(idBookType));
        return ResponseEntity.ok().body("Success");
    }
}
