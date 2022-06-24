package com.andreiromila.dataaccess.repository;

import com.andreiromila.dataaccess.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao extends AbstractDao implements Repository<Book, Long> {

    public List<Book> findAllBase() {
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

    public List<Book> findAllTemplate() {
        JdbcQueryTemplate<Book> template = new JdbcQueryTemplate<>() {
            @Override
            protected Book mapItem(ResultSet results) throws SQLException {
                Book book = new Book();
                book.setId(results.getLong("id"));
                book.setTitle(results.getString("title"));
                book.setRating(results.getInt("rating"));
                return book;
            }
        };

        return template.findAll("SELECT id, title, rating FROM book");
    }

    @Override
    public List<Book> findAll() {
        JdbcMapperTemplate<Book> template = new JdbcMapperTemplate<>();

        return template.findAll("SELECT id, title, rating FROM book", results -> {
            Book book = new Book();
            book.setId(results.getLong("id"));
            book.setTitle(results.getString("title"));
            book.setRating(results.getInt("rating"));
            return book;
        });
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
    public Book insert(Book book) {

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

    @Override
    public Book update(Book book) {

        if (book.getId() == null) {
            throw new IllegalArgumentException("The book id is mandatory.");
        }

        final String query = "UPDATE book SET title = ? WHERE id = ? LIMIT 1";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, book.getTitle());
            statement.setLong(2, book.getId());
            statement.executeUpdate();

            return book;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public int[] update(List<Book> books) {

        final String query = "UPDATE book SET title = ?, rating = ? WHERE id = ? LIMIT 1";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
        ) {

            books.forEach(book -> {
                try {

                    statement.setString(1, book.getTitle());
                    statement.setInt(2, book.getRating());
                    statement.setLong(3, book.getId());

                    statement.addBatch();

                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

            });

            return statement.executeBatch();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    @Override
    public int delete(Book book) {

        final String query = "DELETE FROM book WHERE id = ? LIMIT 1";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setLong(1, book.getId());
            return statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }
}
