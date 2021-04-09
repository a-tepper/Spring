package org.example.controller;

import org.apache.log4j.Logger;
import org.example.dto.BookIdToRemove;
import org.example.exceptions.BookShelfUploadException;
import org.example.service.BookService;
import org.example.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
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

    @Override
    public String uploadFile(@RequestParam(value = "file")MultipartFile file) throws IOException, BookShelfUploadException {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        if (bytes.length == 0){
            logger.info("Attempt to upload an empty file");
            throw new BookShelfUploadException("Attempt to upload an empty file!");
        }

        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()){
            dir.mkdirs();
        }

        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("new file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }

    @ExceptionHandler(BookShelfUploadException.class)
    public String handleEmptyFile(Model model, BookShelfUploadException exception){
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }
}
