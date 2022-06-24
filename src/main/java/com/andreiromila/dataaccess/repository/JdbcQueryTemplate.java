package com.andreiromila.dataaccess.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcQueryTemplate<T> extends AbstractDao {

    public JdbcQueryTemplate() {
    }

    /**
     * Executes the query and returns all results
     *
     * @param query {@link String} The SQL query to be executed
     * @return The list of rows
     */
    public List<T> findAll(String query) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet results = statement.executeQuery();
        ) {
            final List<T> methodResult = new ArrayList<>();
            while (results.next()) {
                // Transform the results to concrete pojo
                methodResult.add(mapItem(results));
            }
            return methodResult;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    protected abstract T mapItem(ResultSet results) throws SQLException;
}
