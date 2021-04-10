package org.example.controller;

import org.example.dto.*;
import org.example.exceptions.BookShelfUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

public interface BookShelfController {

    @GetMapping("/shelf")
    String books(Model model);

    @GetMapping("/filter")
    String filter(@Valid FilterForm filterForm, BindingResult bindingResult,Model model);

    @PostMapping("/save")
    String saveBook(@Valid Book book, BindingResult bindingResult, Model model);

    @PostMapping("/remove")
    String removeBook(@Valid BookIdToRemove bookIdToRemove,
                      BindingResult bindingResult, Model model);

    @PostMapping("/remove_by_author")
    String removeBooksByAuthor(@Valid AuthorParam authorParam, BindingResult bindingResult, Model model);

    @PostMapping("/remove_by_title")
    String removeBooksByTitle(@Valid TitleParam titleParam, BindingResult bindingResult, Model model);

    @PostMapping("/remove_by_size")
    String removeBooksBySize(@Valid SizeParam sizeParam, BindingResult bindingResult, Model model);

    @PostMapping("/upload_file")
    String uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException, BookShelfUploadException;

    @GetMapping(value = "/download_file")
    ResponseEntity<byte[]> downloadFile() throws IOException, BookShelfUploadException;

}
