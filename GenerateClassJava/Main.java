package main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.classe.generated.ClassGenerator;

import connexion.DatabaseConf;

public class Main {

    public static void main(String[] args) throws SQLException {
        ClassGenerator classgenerator = new ClassGenerator();
        DatabaseConf connect = new DatabaseConf();
        Connection c = connect.connect();
        System.out.println(classgenerator.generate("model", c));
    }
}
