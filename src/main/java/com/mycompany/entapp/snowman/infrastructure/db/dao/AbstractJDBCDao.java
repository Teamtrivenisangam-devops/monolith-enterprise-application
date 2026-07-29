/*
 * |-------------------------------------------------
 * | Copyright © 2017 Colin But. All rights reserved.
 * |-------------------------------------------------
 */
package com.mycompany.entapp.snowman.infrastructure.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractJDBCDao {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJDBCDao.class);

    private static final String DATABASE_CONNECTION_URL = System.getenv("JDBC_URL") != null ? System.getenv("JDBC_URL") : "jdbc:mysql://localhost:3306/snowman";

    private static final String DATABASE_USERNAME = System.getenv("JDBC_USERNAME") != null ? System.getenv("JDBC_USERNAME") : "username";
    private static final String DATABASE_PASSWORD = System.getenv("JDBC_PASSWORD") != null ? System.getenv("JDBC_PASSWORD") : "password";

    protected void setupDBDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.error("{}", e); // TODO should throw a business exception back up
        }
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (SQLException e) {
            LOG.error("{}", e); // TODO should throw a business exception back up
        }

        return connection;
    }
}
