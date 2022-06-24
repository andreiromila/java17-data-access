package com.andreiromila.dataaccess.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapperTemplate<T> extends AbstractDao {

    @FunctionalInterface
    public interface Mapper<T> {
        /**
         * Better implementation of the mapper - use a lambda
         *
         * @param resultSet {@link ResultSet} The result set from the database
         * @return The concrete pojo
         * @throws SQLException If the mapper cannot 
         */
        T map(ResultSet resultSet) throws SQLException;
    }

    /**
     * Executes the query and returns all results
     *
     * @param query {@link String} The SQL query to be executed
     * @return The list of rows
     */
    public List<T> findAll(String query, Mapper<T> mapper) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet results = statement.executeQuery();
        ) {
            final List<T> methodResult = new ArrayList<>();
            while (results.next()) {
                // Transform the results to concrete pojo
                methodResult.add(mapper.map(results));
            }
            return methodResult;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }
}
