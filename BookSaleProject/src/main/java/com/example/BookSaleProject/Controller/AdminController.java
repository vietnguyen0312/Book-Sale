package com.example.BookSaleProject.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${liveUpload.path}")
    private String liveUploadPath;

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

    @GetMapping(value = "/toAddBook")
    public String showAddBook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        model.addAttribute("bookTypeList", bookTypeService.getAll());
        return "Admin/AddBook";
    }

    @PostMapping("/addBook")
    public String addBook(Model model, @ModelAttribute("book") Book book, @RequestParam("image") MultipartFile file,
            @RequestParam("nameBookType") String nameBookType) throws IOException {

        // Save image file
        if (!file.isEmpty()) {
            Path fileNameAndPath = Paths.get(uploadPath, file.getOriginalFilename());
            Path liveFileNameAndPath = Paths.get(liveUploadPath, file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            Files.write(liveFileNameAndPath, file.getBytes());
            book.setImg(file.getOriginalFilename());
        }

        // Set book type
        BookType bookType = bookTypeService.getByName(nameBookType);
        if (bookType != null) {
            book.setBookType(bookType);
        }

        // Save book
        bookService.addNew(book);

        return index(model);
    }

    @GetMapping(value = "/recomendation")
    public ResponseEntity<ArrayList<Book>> recomendationBook(@RequestParam("keyword") String keyword) {
        ArrayList<Book> searchResult = bookService.search(keyword);
        return ResponseEntity.ok().body(searchResult);
    }

    @GetMapping(value = "/toUpdate/{id}")
    public String toUpdate(Model model, @PathVariable("id") String id) {
        Book book = bookService.getByID(Integer.parseInt(id));
        model.addAttribute("book", book);
        return "Update";
    }

    @PostMapping(value = "/update")
    public String update(Model model, @ModelAttribute("book") Book book, @RequestParam("image") MultipartFile file) {
        
        bookService.update(book);
        return index(model);
    }
}
