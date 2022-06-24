package com.andreiromila.dataaccess.repository;

import com.andreiromila.dataaccess.model.Book;

import java.sql.*;
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

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {

        final String sql = "SELECT id, title FROM book WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            // Add the query parameters
            statement.setLong(1, id);

            // We need a second try-with-resources because of the setLong method above
            try (ResultSet results = statement.executeQuery()) {

                if (results.next()) {
                    Book book = new Book();

                    book.setId(results.getLong("id"));
                    book.setTitle(results.getString("title"));

                    return Optional.of(book);
                }

                return Optional.empty();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }

    }

    @Override
    public Book save(Book book) {

        final String sql = "INSERT INTO book (title) VALUES (?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, book.getTitle());
            statement.executeUpdate();

            try (
                    // This will return the generated keys
                    ResultSet results = statement.getGeneratedKeys()
            ) {
                if (results.next()) {
                    book.setId(results.getLong(1));
                }
            }

            return book;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }

    }
}
