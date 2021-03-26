package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @GetMapping("/filter")
    public String filter(Model model,
                         @RequestParam(value = "author") String author,
                         @RequestParam(value = "title") String title,
                         @RequestParam(value = "size") Integer size) {
        List<Book> books = bookService.filterBooks(author, title, size);
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", books);
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        if (book.getAuthor() != "" || book.getTitle() != "" || book.getSize() != null) {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
        }
        else {
            logger.info("attempt to add an empty record!");
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove) {
        if (bookService.removeBookById(bookIdToRemove)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove_by_author")
    public String removeBooksByAuthor(@RequestParam(value = "author") String author) {
        if (bookService.removeBooksByAuthor(author)) {
            return "redirect:/books/shelf";
        }
        else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove_by_title")
    public String removeBooksByTitle(@RequestParam(value = "title") String title) {
        if (bookService.removeBooksByTitle(title)) {
            return "redirect:/books/shelf";
        }
        else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove_by_size")
    public String removeBooksBySize(@RequestParam(value = "size") Integer size) {
        if (bookService.removeBooksBySize(size)) {
            return "redirect:/books/shelf";
        }
        else {
            return "redirect:/books/shelf";
        }
    }
}
