package org.example.controller;

import org.apache.log4j.Logger;
import org.example.dto.BookIdToRemove;
import org.example.service.BookService;
import org.example.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookShelfControllerImpl implements BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfControllerImpl.class);
    private BookService bookService;

    @Autowired
    public BookShelfControllerImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @Override
    public String filter(Model model, String author, String title, Integer size) {
        List<Book> books = bookService.filterBooks(author, title, size);
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", books);
        return "book_shelf";
    }

    @Override
    public String saveBook(Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        }
        else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBook(BookIdToRemove bookIdToRemove,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        }
        else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBooksByAuthor(String author) {
        if (bookService.removeBooksByAuthor(author)) {
            return "redirect:/books/shelf";
        }
        else {
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBooksByTitle(String title) {
        if (bookService.removeBooksByTitle(title)) {
            return "redirect:/books/shelf";
        }
        else {
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBooksBySize(Integer size) {
        if (bookService.removeBooksBySize(size)) {
            return "redirect:/books/shelf";
        }
        else {
            return "redirect:/books/shelf";
        }
    }
}
