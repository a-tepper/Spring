package org.example.controller;

import org.example.dto.Book;
import org.example.dto.BookIdToRemove;
import org.example.exceptions.BookShelfUploadException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

public interface BookShelfController {

    @GetMapping("/shelf")
    String books(Model model);

    @GetMapping("/filter")
    String filter(Model model,
                  @RequestParam(value = "author") String author,
                  @RequestParam(value = "title") String title,
                  @RequestParam(value = "size") Integer size);

    @PostMapping("/save")
    String saveBook(@Valid Book book, BindingResult bindingResult, Model model);

    @PostMapping("/remove")
    String removeBook(@Valid BookIdToRemove bookIdToRemove,
                      BindingResult bindingResult, Model model);

    @PostMapping("/remove_by_author")
    String removeBooksByAuthor(@RequestParam(value = "author") String author);

    @PostMapping("/remove_by_title")
    String removeBooksByTitle(@RequestParam(value = "title") String title);

    @PostMapping("/remove_by_size")
    String removeBooksBySize(@RequestParam(value = "size") Integer size);

    @PostMapping("/upload_file")
    String uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException, BookShelfUploadException;
}
