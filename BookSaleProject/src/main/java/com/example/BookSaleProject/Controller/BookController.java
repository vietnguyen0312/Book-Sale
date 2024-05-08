package com.example.BookSaleProject.Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.BookSaleProject.Model.Entity.Book;
import com.example.BookSaleProject.Model.Entity.BookType;
import com.example.BookSaleProject.Model.Service.BookService;
import com.example.BookSaleProject.Model.Service.BookTypeService;
import com.example.BookSaleProject.Model.Service.RateService;

@Controller
@RequestMapping(value = { "/book", "" })
public class BookController {

    @Autowired
    BookService bookService = new BookService();
    BookTypeService bookTypeService = new BookTypeService();
    RateService rateService = new RateService();

    HashMap<Book, Double> bookRate = new HashMap<Book, Double>();
    ArrayList<Book> bookList = new ArrayList<>();
    ArrayList<BookType> bookTypeList = bookTypeService.getAll();
    String title;

    @GetMapping(value = "/")
    public String index(Model model) {
        ArrayList<Book> bookListAll = bookService.getAll();
        bookRate.clear();
        bookListAll.sort(Comparator.comparing(Book::getDate).reversed());
        ArrayList<Book> newBookList = new ArrayList<>();
        for (Book book : bookListAll) {
            newBookList.add(book);
            if (newBookList.size() == 3) {
                break;
            }
        }
        for (Book book : newBookList) {
            bookRate.put(book, rateService.getScoreByIdBook(book));
        }

        HashMap<Book, Double> bookRateList = new HashMap<Book, Double>();

        for (Book book : bookListAll) {
            bookRateList.put(book, rateService.getScoreByIdBook(book));
        }

        HashMap<Book, Double> topRatedBooks = new HashMap<Book, Double>();
        while (topRatedBooks.size() < 3 && !bookRateList.isEmpty()) {
            Book topBook = null;
            double maxRating = Double.MIN_VALUE;
            for (Map.Entry<Book, Double> entry : bookRateList.entrySet()) {
                if (entry.getValue() > maxRating) {
                    maxRating = entry.getValue();
                    topBook = entry.getKey();
                }
            }
            if (topBook != null) {
                topRatedBooks.put(topBook, maxRating);
                bookRateList.remove(topBook);
            }
        }
        model.addAttribute("bookTypeList", bookTypeList);
        model.addAttribute("newBookList", bookRate);
        model.addAttribute("favouriteBookList", topRatedBooks);
        return "index";
    }

    @GetMapping(value = { "/getBookList/{pageNum}" })
    public String getBookList(Model model, @PathVariable(value = "pageNum") String currentPage) {
        int numPages = (int) Math.ceil((double) bookList.size() / 12);
        int[] numPage = new int[numPages];
        for (int i = 0; i < numPages; i++) {
            numPage[i] = i + 1;
        }

        ArrayList<Book> bookListPage = new ArrayList<>();
        for (int i = (Integer.parseInt(currentPage) - 1) * 12; i < Integer.parseInt(currentPage) * 12; i++) {
            if (bookList.size() <= i)
                break;
            bookListPage.add(bookList.get(i));
        }
        bookRate.clear();
        for (Book book : bookListPage) {
            bookRate.put(book, (rateService.getScoreByIdBook(book)));
        }

        ArrayList<String> nxbList = new ArrayList<>();
        ArrayList<Book> bookListAll = bookService.getAll();
        for (Book book : bookListAll) {
            if (!nxbList.contains(book.getNxb())) {
                nxbList.add(book.getNxb());
            }
        }
        model.addAttribute("BookListAll", nxbList);
        model.addAttribute("bookTypeList", bookTypeList);
        model.addAttribute("NxbList", nxbList);
        model.addAttribute("BookRate", bookRate);
        model.addAttribute("NumOfPage", numPage);
        model.addAttribute("title", title);
        model.addAttribute("currentPage", Integer.parseInt(currentPage));
        model.addAttribute("Previous", Integer.parseInt(currentPage) - 1);
        model.addAttribute("Next", Integer.parseInt(currentPage) + 1);
        return "GetBookListForCus";
    }

    @GetMapping(value = { "/getBookById/{id}" })
    public String getBookById(Model model, @PathVariable(value = "id") String id) {
        ArrayList<Book> bookListAll = bookService.getAll();
        Book book = bookService.getByID(Integer.parseInt(id));
        BookType bookType = book.getBookType();
        ArrayList<Book> bookListSame = new ArrayList<>();
        for (Book book1 : bookListAll) {
            if (book1.getBookType().getId() == bookType.getId() && book1.getId() != book.getId()) {
                bookListSame.add(book1);
            }
            if (bookListSame.size() == 3) {
                break;
            }
        }
        bookRate.clear();
        for (Book book2 : bookListSame) {
            bookRate.put(book2, rateService.getScoreByIdBook(book2));
        }
        model.addAttribute("rate", rateService.getScoreByIdBook(book));
        model.addAttribute("bookTypeList", bookTypeList);
        model.addAttribute("BookRate", bookRate);
        model.addAttribute("booktype", bookType);
        model.addAttribute("book", book);
        return "BookDetail";
    }

    @GetMapping(value = "/getNewestBook")
    public String getNewestBook(Model model) {
        ArrayList<Book> bookListAll = bookService.getAll();
        bookList.clear();
        title = "SÁCH MỚI PHÁT HÀNH";
        for (Book book : bookListAll) {
            if (Integer.parseInt(book.getDate()) >= 2010)
                bookList.add(book);
        }
        return getBookList(model, "1");
    }

    @GetMapping(value = "/getFavouriteBook")
    public String getFavouriteBook(Model model) {
        ArrayList<Book> bookListAll = bookService.getAll();
        bookRate.clear();
        title = "SÁCH ĐƯỢC YÊU THÍCH";
        for (Book book : bookListAll) {
            bookRate.put(book, rateService.getScoreByIdBook(book));
        }
        bookList.clear();
        for (Map.Entry<Book, Double> entry : bookRate.entrySet()) {
            if (entry.getValue() >= 4.0) {
                bookList.add(entry.getKey());
            }
        }
        return getBookList(model, "1");
    }

    @GetMapping(value = "/getBookByType/{id}")
    public String getBookByType(Model model, @PathVariable(value = "id") String id) {
        ArrayList<Book> bookListAll = bookService.getAll();
        bookList.clear();
        title = bookTypeService.getByID(Integer.parseInt(id)).getName();
        for (Book book : bookListAll) {
            if (Integer.parseInt(id) == book.getBookType().getId()) {
                bookList.add(book);
            }
        }
        return getBookList(model, "1");
    }

    @GetMapping(value = "/getBookByNxb/{nxb}")
    public String getBookNxb(Model model, @PathVariable(value = "nxb") String nxb) {
        ArrayList<Book> bookListAll = bookService.getAll();
        bookList.clear();
        title = nxb;
        for (Book book : bookListAll) {
            if (nxb.equals(book.getNxb())) {
                bookList.add(book);
            }
        }
        return getBookList(model, "1");
    }

    @GetMapping(value = "/getAllBook")
    public String getAllBook(Model model) {
        ArrayList<Book> bookListAll = bookService.getAll();
        title = "Tất cả sản phẩm";
        bookList.clear();
        for (Book book : bookListAll) {
            bookList.add(book);
        }
        return getBookList(model, "1");
    }

    @GetMapping(value = "/search", produces = "text/plain;charset=UTF-8")
    public String searchBook(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        bookList.clear();
        title = "Kết quả tìm kiếm (" + keyword + ")";
        bookList = bookService.search(keyword);
        return getBookList(model, "1");
    }

    @GetMapping(value = "/recomendation")
    public ResponseEntity<ArrayList<Book>> recomendationBook(@RequestParam("keyword") String keyword) {
        ArrayList<Book> searchResult = bookService.search(keyword);
        return ResponseEntity.ok().body(searchResult);
    }

    @PostMapping(value = { "/updateBook" })
    public String updateBook(Model model, @RequestParam("bookFinded") String id) {

        return "";
    }

}
