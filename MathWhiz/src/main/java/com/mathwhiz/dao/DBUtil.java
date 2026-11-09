package com.mathwhiz.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database utility providing robust JDBC connection management for MySQL.
 *
 * Supports:
 * - External configuration via db.properties
 * - Automatic port fallback (tries 3306, then 3307 for local XAMPP)
 * - Safe driver registration
 */
public class DBUtil {

    private static String url = "jdbc:mysql://localhost:3306/math_whiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String fallbackUrl = "jdbc:mysql://localhost:3307/math_whiz_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String user = "root";
    private static String password = "";
    private static String driver = "com.mysql.cj.jdbc.Driver";

    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                if (props.getProperty("db.url") != null) url = props.getProperty("db.url").trim();
                if (props.getProperty("db.fallbackUrl") != null) fallbackUrl = props.getProperty("db.fallbackUrl").trim();
                if (props.getProperty("db.user") != null) user = props.getProperty("db.user").trim();
                if (props.getProperty("db.password") != null) password = props.getProperty("db.password").trim();
                if (props.getProperty("db.driver") != null) driver = props.getProperty("db.driver").trim();
            }
        } catch (Exception e) {
            System.err.println("[DBUtil] Warning: Could not read db.properties, using defaults. " + e.getMessage());
        }

        // Environment variable override if present
        if (System.getenv("DB_PASSWORD") != null) {
            password = System.getenv("DB_PASSWORD");
        }
        if (System.getenv("DB_USER") != null) {
            user = System.getenv("DB_USER");
        }
        if (System.getenv("DB_URL") != null) {
            url = System.getenv("DB_URL");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] Error: MySQL JDBC Driver not found in classpath: " + driver);
        }
    }

    /**
     * Establishes a JDBC connection to MySQL.
     * Tries the primary URL first; if connection fails, tries the fallback URL (e.g. port 3307).
     *
     * @return active java.sql.Connection
     * @throws SQLException if both primary and fallback connections fail
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException primaryEx) {
            if (fallbackUrl != null && !fallbackUrl.isEmpty() && !fallbackUrl.equals(url)) {
                try {
                    return DriverManager.getConnection(fallbackUrl, user, password);
                } catch (SQLException fallbackEx) {
                    throw new SQLException("Failed to connect to MySQL on primary (" + url +
                            ") and fallback (" + fallbackUrl + "): " + fallbackEx.getMessage(), primaryEx);
                }
            }
            throw primaryEx;
        }
    }
}
