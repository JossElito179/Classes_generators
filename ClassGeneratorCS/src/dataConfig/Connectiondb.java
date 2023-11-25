package dataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectiondb {

    private String jdbcUrl = "jdbc:postgresql://localhost:5432/g_stock";
    private String username = "postgres";
    private String password = "dbapg";

    public Connectiondb() {
    }

    public Connectiondb(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }


    public String getJdbcUrl() {
        return jdbcUrl;
    }
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        return connection;
    }
}
