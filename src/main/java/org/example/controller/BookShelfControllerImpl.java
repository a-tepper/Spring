package org.example.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.example.dto.*;
import org.example.exceptions.BookShelfUploadException;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

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
        clear_fields(model);
        return "book_shelf";
    }

    @Override
    public String filter(FilterForm filterForm, BindingResult bindingResult,Model model) {
        clear_fields(model);
        if (bindingResult.hasErrors()){
            String text = bindingResult.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.joining("; "));
            model.addAttribute("errorMessage", text);
            return "book_shelf";
        }
        else {
            boolean valid = true;
            String validErrors = "";
            if (filterForm.getAuthor().length() >= 50) {
                validErrors += "author param should be < 50 chars! ";
                valid = false;
            }
            if (filterForm.getTitle().length() >= 50) {
                validErrors += "title param should be < 50 chars! ";
                valid = false;
            }
            if (filterForm.getSize() != null && filterForm.getSize() > 10000) {
                validErrors += "size param should be < 4 digits!";
                valid = false;
            }
            if (valid == false) {
                model.addAttribute("errorMessage", validErrors);
                return "book_shelf";
            }
        List<Book> books = bookService.filterBooks(filterForm.getAuthor(),
                filterForm.getTitle(), filterForm.getSize());
        model.addAttribute("filterForm", filterForm);
        model.addAttribute("bookList", books);
        return "book_shelf";
        }
    }

    @Override
    public String saveBook(Book book, BindingResult bindingResult, Model model) {
        clear_fields(model);
        if (bindingResult.hasErrors()) {
            return "book_shelf";
        }
        else {
            boolean valid = true;
            String validErrors = "";
            if (book.getAuthor().length() < 1 || book.getAuthor().length() >= 50) {
                validErrors += "author param should be not empty and < 50 chars! ";
                valid = false;
            }
            if (book.getTitle().length()  < 1 || book.getTitle().length() >= 50) {
                validErrors += "title param should be not empty and < 50 chars! ";
                valid = false;
            }
            if (book.getSize() == null || book.getSize() > 10000) {
                validErrors += "size param should be not empty and < 4 digits!";
                valid = false;
            }
            if (valid == false) {
                model.addAttribute("errorMessage", validErrors);
                return "book_shelf";
            }
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBook(BookIdToRemove bookIdToRemove,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            clear_fields(model);
            return "book_shelf";
        }
        else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    private void clear_fields(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("filterForm", new FilterForm());
        model.addAttribute("authorParam", new AuthorParam());
        model.addAttribute("titleParam", new TitleParam());
        model.addAttribute("sizeParam", new SizeParam());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
    }

    @Override
    public String removeBooksByAuthor(AuthorParam authorParam, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String text = bindingResult.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.joining("; "));
            model.addAttribute("errorMessage", text);
            clear_fields(model);
            return "redirect:/books/shelf";
        }
        else {
            if (authorParam.getName().length() < 1 || authorParam.getName().length() >= 50) {
                model.addAttribute("errorMessage", "author param is empty or > 50 chars!");
                clear_fields(model);
                return "book_shelf";
            }
            bookService.removeBooksByAuthor(authorParam.getName());
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBooksByTitle(TitleParam titleParam, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String text = bindingResult.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.joining("; "));
            model.addAttribute("errorMessage", text);
            clear_fields(model);
            return "redirect:/books/shelf";
        }
        else {
            if (titleParam.getText().length() < 1 || titleParam.getText().length() >= 50) {
                model.addAttribute("errorMessage", "title param is empty or > 50 chars!");
                clear_fields(model);
                return "book_shelf";
            }
            bookService.removeBooksByTitle(titleParam.getText());
            return "redirect:/books/shelf";
        }
    }

    @Override
    public String removeBooksBySize(@Valid SizeParam sizeParam, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String text = bindingResult.getAllErrors().stream().map(x->x.getDefaultMessage()).collect(Collectors.joining("; "));
            model.addAttribute("errorMessage", text);
            clear_fields(model);
            return "redirect:/books/shelf";
        }
        else {
            if (sizeParam.getNumberOfPages() == null || sizeParam.getNumberOfPages() >= 10000) {
                model.addAttribute("errorMessage", "size param is null or > 4 digits!");
                clear_fields(model);
                return "book_shelf";
            }
            bookService.removeBooksBySize(sizeParam.getNumberOfPages());
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
        clear_fields(model);
        return "book_shelf";
    }

    @Override
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("/images/book_icon.png");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(ContentDisposition.builder("attachment").filename("book_icon.png").build());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(httpHeaders)
                .body(IOUtils.toByteArray(in));
    }
}
