package com.andreiromila.dataaccess.repository;

import com.andreiromila.dataaccess.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao extends AbstractDao implements Repository<Book, Long> {

    @Override
    public List<Book> findAll() {
        final String sql = "SELECT id, title FROM book";

        /*
         * All of these interfaces extend the AutoCloseable interface, so
         * we can make use of try-with-resources functionality to make sure
         * every resource is cleaned up correctly without the need of writing
         * the code inside the "finally" block
         */
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(sql)
        ) {
            final List<Book> books = new ArrayList<>();

            while (results.next()) {
                Book book = new Book();

                book.setId(results.getLong("id"));
                book.setTitle(results.getString("title"));

                books.add(book);
            }

            return books;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RuntimeException(sqle.getMessage(), sqle);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.empty();
    }
}
