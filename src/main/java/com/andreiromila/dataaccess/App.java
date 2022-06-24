package com.andreiromila.dataaccess;

import com.andreiromila.dataaccess.model.Book;
import com.andreiromila.dataaccess.repository.BookDao;
import com.andreiromila.dataaccess.repository.Repository;

import java.util.List;

public class App {
    public static void main(String[] args) {

        Repository<Book, Long> repository = new BookDao();
        List<Book> books = repository.findAll();

        books.forEach(book -> {
            System.out.println("Id: " + book.getId());
            System.out.println("Title: " + book.getTitle());
        });
    }
}