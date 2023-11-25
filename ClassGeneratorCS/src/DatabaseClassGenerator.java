import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataConfig.Connectiondb;

public class DatabaseClassGenerator {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/g_stock";
        String username = "postgres";
        String password = "dbapg";
        try{
            Connectiondb connex=new Connectiondb();
            TemplatesCS.initializeClass(connex,"models","entre_stock");
        } catch(Exception e){
        }
    }

}
