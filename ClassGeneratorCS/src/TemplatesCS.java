// import java.io.File;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dataConfig.Connectiondb;

/**
 * Templates
 */
public class TemplatesCS {
    

    public static void initializeClass(Connectiondb connection,String path,String tableName) throws IOException{
            String creatingTable="";
            String creatingAttribute="";
            String finalcase="";
            String importation="";
            String packages="";
            ArrayList<String> fields=new ArrayList<>();
            ArrayList<String> type=new ArrayList<>();
        try{
        DatabaseMetaData metaData = connection.getConnection().getMetaData();
                File directory = new File(path);
                if (!directory.exists()) {
                    directory.mkdir();
                }
                File file = new File(directory, tableName+".cs");
                if (!file.exists()) {
                    file.createNewFile();
                } 
                // Récupérer les colonnes de la table
                ResultSet columns = metaData.getColumns(null, null, tableName, null);

                // Commencer la génération de la classe Java
                creatingTable+="\n public class " + toCamelCase(tableName) + " {\n";
                while (columns.next()) {
                    // Récupérer le nom et le type de la colonne
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");

                    // Générer le champ de la classe CS
                    fields.add(toCamelCase(columnName));
                    type.add(getCSType(dataType));
                    creatingAttribute+="\n\n public " + getCSType(dataType) + " " + toCamelCase(columnName) + " { get ; set } \n";
                }
                    importation= generateImportation(type);
                    packages=generateTemplatesPackage(path);
                // Finir la classe Java
                finalcase="}\n"+"}";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String content=packages+importation+creatingTable+creatingAttribute+finalcase;
        writeToFile(path+"/"+tableName+".cs", content);
    }

    public static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                result.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                capitalizeNext = false;
            }
        }

        return result.toString();
    }

    public static String getCSType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
                return "string";
            case "INT4":
            case "INT32":
            case "SERIAL":
            case "INTEGER":
            case "LONG":
                return "int";
            case "DATE":
                return "Date";
            case "NUMERIC":
            case "DOUBLE PRECISION":
                return "double";
            case "DECIMAL":
                return "Decimal";
            case "FLOAT8":
                return "float";
            case "TIME":
                return  "Time";
            case "TIMESTAMP":
                return "Datetime";
            default:
                return "Object";
        }
    }

    public static String generateTemplatesPackage(String path){
        return "namespace " + path + "{\n\n";
    }

    public static String generateImportation(ArrayList<String> types){
        String importation="";
        for (String string : types) {
            if (string.equals("DATE") || string.equals("Time") || string.equals("Timestamp") || string.equals("Datetime")) {
                importation+="using System.Data; \r\n" +
                            " using System.Data.SqlClient;\n";
            }
        }
        return importation ;
    }

        public static void writeToFile(String fileName, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(content);
            System.out.println("non");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}