package com.andreiromila.dataaccess;

import com.andreiromila.dataaccess.model.Book;
import com.andreiromila.dataaccess.repository.BookDao;
import com.andreiromila.dataaccess.repository.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {

        Repository<Book, Long> repository = new BookDao();
        List<Book> books = repository.findAll();

        books.forEach(book -> {
            System.out.println("Id: " + book.getId());
            System.out.println("Title: " + book.getTitle());
        });

        System.out.println("\n\n");

        Optional<Book> firstBook = repository.findById(2L);
        firstBook.ifPresent(book -> {
            System.out.println("The first book is:");
            System.out.println("\t Id: " + book.getId());
            System.out.println("\t Title: " + book.getTitle());
        });

        // Create a new book
//        Book insertBook = new Book();
//        insertBook.setTitle("Reactive Systems in Java: Resilient, Event-Driven Architecture with Quarkus");
//        insertBook = repository.insert(insertBook);
//
//        System.out.println(insertBook.getId());
//        System.out.println(insertBook.getTitle());

        // Update the boot with id 6
        Book updateBook = new Book();
        updateBook.setId(6L);
        updateBook.setTitle("Effective Java: Third Edition");
        updateBook = repository.update(updateBook);

        System.out.println("Updated book:");
        System.out.println("\t Id: " + updateBook.getId());
        System.out.println("\t Title: " + updateBook.getTitle());

        // Set the rating for every book
        books.forEach(book -> book.setRating(10));
        int[] records = repository.update(books);
        System.out.println(Arrays.toString(records));

        // Try to remove the book with id 2
        Book deleteBook = new Book();
        deleteBook.setId(2L);
        int affectedRows = repository.delete(deleteBook);
        System.out.println("Affected rows: " + affectedRows);

    }
}