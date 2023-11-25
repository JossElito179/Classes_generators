package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConf {
    String jdbcUrl = "jdbc:postgresql://localhost:5432/generic_class";
    String username = "postgres";
    String password = "mdp";

    public Connection connect() throws SQLException {
        Connection con = DriverManager.getConnection(jdbcUrl, username, password);
        return con;
    }
}
