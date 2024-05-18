package com.example.BookSaleProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.BookSaleProject.Model.Entity.Bill;
import com.example.BookSaleProject.Model.Entity.BillProBox;
import com.example.BookSaleProject.Model.Entity.User;
import com.example.BookSaleProject.Model.Service.BillProBoxService;
import com.example.BookSaleProject.Model.Service.BillService;
import com.example.BookSaleProject.Model.Service.BookService;
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

    @GetMapping("/")
    public String index(Model model) {
        int totalAccount = 0, totalSaledBook = 0, totalBill = 0, totalComment = rateService.getAll().size();

        for (User user : userService.getAllUser()) {
            if (!user.getRole().equals("ADMIN")) {
                totalAccount++;
            }
        }

        for (Bill bill : billService.getAll()) {
            totalBill++;
            for (BillProBox billProBox : billProBoxService.getByIdBill(bill)) {
                totalSaledBook += billProBox.getSL();
            }
        }
        model.addAttribute("bookList", bookService.getAll());
        model.addAttribute("totalAccount", totalAccount);
        model.addAttribute("totalSaledBook", totalSaledBook);
        model.addAttribute("totalBill", totalBill);
        model.addAttribute("totalComment", totalComment);
        return "Admin/AdminDashBoard";
    }
}
