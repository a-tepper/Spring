package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepo.retreiveAll().stream().filter(e -> author.equals(e.getAuthor())).collect(Collectors.toList());
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepo.retreiveAll().stream().filter(e -> title.equals(e.getTitle())).collect(Collectors.toList());
    }

    public List<Book> getBooksBySize(Integer size) {
        return bookRepo.retreiveAll().stream().filter(e -> size.equals(e.getSize())).collect(Collectors.toList());
    }

    public boolean removeBooksByAuthor(String author) {
        for (Book book : getBooksByAuthor(author)) {
            if (bookRepo.removeItemById(book.getId())) {
                logger.info("removed book " + book);
            } else {
                logger.info("failed to remove book " + book);
            }
        }
        return true;
    }


    public boolean removeBooksByTitle(String title) {
        for (Book book : getBooksByTitle(title)) {
            if (bookRepo.removeItemById(book.getId())) {
                logger.info("removed book " + book);
            } else {
                logger.info("failed to remove book " + book);
            }
        }
        return true;
    }

    public boolean removeBooksBySize(Integer size) {
        for (Book book : getBooksBySize(size)) {
            if (bookRepo.removeItemById(book.getId())) {
                logger.info("removed book " + book);
            } else {
                logger.info("failed to remove book " + book);
            }
        }
        return true;
    }
}
