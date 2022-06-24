package com.andreiromila.dataaccess.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractDao {

    protected Connection getConnection() throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/library";
        final String username = "root";
        final String password = "password";

        // This gets a connection to the database
        // The DriverManager will take a look at our classpath and instantiate
        // a new java.sql.Driver using the com.mysql.cj.jdbc.Driver class
        return DriverManager.getConnection(url, username, password);
    }

}
