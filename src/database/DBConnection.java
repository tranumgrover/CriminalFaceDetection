package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/criminal_detection";
            String user = "root";
            String password = "password";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database Connected Successfully");
            return conn;

        } catch (Exception e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
            return null;
        }
    }
}