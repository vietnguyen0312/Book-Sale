package com.example.BookSaleProject.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import java.time.LocalDateTime;

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
import com.example.BookSaleProject.Model.Service.FeedbackService;
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
    FeedbackService feedbackService = new FeedbackService();

    @GetMapping("/")
    public String index(Model model) {
        int totalBook = 0, totalSaledBook = 0, totalBill = 0, totalComment;
        ArrayList<Rate> ratingAll = rateService.getAll();

        if (ratingAll == null) {
            totalComment = 0;
        } else
            totalComment = ratingAll.size();

        if (bookService.getAll() != null) {
            for (Book book : bookService.getAll()) {
                totalBook += book.getSL();
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
        model.addAttribute("totalBook", totalBook);
        model.addAttribute("totalSaledBook", totalSaledBook);
        model.addAttribute("totalBill", totalBill);
        model.addAttribute("totalComment", totalComment);
        return "Admin/BookManage";
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
        model.addAttribute("uploadPath", uploadPath);
        model.addAttribute("book", book);
        model.addAttribute("bookTypeList", bookTypeService.getAll());
        return "Admin/Update";
    }

    @PostMapping(value = "/update")
    public String update(Model model, @ModelAttribute("book") Book book, @RequestParam("image") MultipartFile file,
            @RequestParam("nameBookType") String nameBookType) throws IOException {

        Book existingBook = bookService.getByID(book.getId());

        if (!file.isEmpty()) {
            // Delete the old image file if it exists
            if (existingBook.getImg() != null && !existingBook.getImg().isEmpty()) {
                Path oldFilePath = Paths.get(uploadPath, existingBook.getImg());
                Path oldLiveFilePath = Paths.get(liveUploadPath, existingBook.getImg());
                Files.deleteIfExists(oldFilePath);
                Files.deleteIfExists(oldLiveFilePath);
            }

            // Save the new image file
            Path fileNameAndPath = Paths.get(uploadPath, file.getOriginalFilename());
            Path liveFileNameAndPath = Paths.get(liveUploadPath, file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            Files.write(liveFileNameAndPath, file.getBytes());
            book.setImg(file.getOriginalFilename());
        } else {
            // Retain the existing image if no new file is uploaded
            book.setImg(existingBook.getImg());
        }

        // Set the book type based on the selected value
        book.setBookType(bookTypeService.getByName(nameBookType));

        // Update the book details
        bookService.update(book);

        // Add the updated book to the model
        model.addAttribute("book", book);

        // Redirect to the index or another appropriate view
        return index(model);
    }

    @GetMapping(value = "/userManage")
    public String userManage(Model model) {
        int totalUserAccount = 0, totalAdminAccount = 0;
        if (userService.getAllUser() != null) {
            for (User user : userService.getAllUser()) {
                if (!user.getRole().equals("ADMIN")) {
                    totalUserAccount++;
                } else {
                    totalAdminAccount++;
                }
            }
        }
        ArrayList<Bill> bills = billService.getAll();
        int totalOrderMonth = 0, totalOrderYear = 0;
        float[] turnoverByMonth = new float[12];

        for (Bill bill : bills) {
            float total = 0;
            if (bill.getDate().getYear() == (LocalDateTime.now().getYear())) {
                totalOrderYear++;
                if (bill.getDate().getMonth().equals(LocalDateTime.now().getMonth())) {
                    totalOrderMonth++;
                }
            }
            if (bill.getStatus().equals("Đã thanh toán")) {
                for (BillProBox billProBox : billProBoxService.getByIdBill(bill)) {
                    total += (billProBox.getSL() * billProBox.getBook().getPrice());
                }
                turnoverByMonth[bill.getDate().getMonth().getValue() - 1] += total;
            }
        }

        HashMap<User, Float> topUser = new HashMap<>();

        for (Bill bill : bills) {
            if (bill.getDate().getMonth().equals(LocalDateTime.now().getMonth())
                    && bill.getDate().getYear() == (LocalDateTime.now().getYear())
                    && bill.getStatus().equals("Đã thanh toán")) {
                float total = 0;
                for (BillProBox billProBox : billProBoxService.getByIdBill(bill)) {
                    total += (billProBox.getSL() * billProBox.getBook().getPrice());
                }
                if (topUser.get(bill.getUser()) != null) {
                    topUser.put(bill.getUser(), topUser.get(bill.getUser()) + total);
                } else {
                    topUser.put(bill.getUser(), total);
                }
            }
        }

        // Convert HashMap to a List of Map.Entry
        List<Map.Entry<User, Float>> list = new LinkedList<>(topUser.entrySet());

        // Sort the list based on values in descending order
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // Create a new HashMap to store the top 5 users
        HashMap<User, Float> top5Users = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<User, Float> entry : list) {
            if (count >= 5) break; // Exit loop after 5 elements
            top5Users.put(entry.getKey(), entry.getValue());
            count++;
        }
        
        model.addAttribute("feedbacks", feedbackService.getAll());
        model.addAttribute("topUser", top5Users);
        model.addAttribute("turnoverByMonth", turnoverByMonth);
        model.addAttribute("totalOrderMonth", totalOrderMonth);
        model.addAttribute("totalOrderYear", totalOrderYear);
        model.addAttribute("totalUserAccount", totalUserAccount);
        model.addAttribute("totalAdminAccount", totalAdminAccount);
        return "Admin/IncomeManage";
    }

}
