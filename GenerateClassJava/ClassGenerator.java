package com.classe.generated;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ClassGenerator {
    public String generate(String packageName, Connection c) throws SQLException {
        String generate = "";
        DatabaseMetaData metaData = c.getMetaData();

        Set<String> imports = new HashSet<>();

        // Récupérer les tables de la base de données
        ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });

        while (tables.next()) {
            generate = "";
            generate += "package " + packageName + ";\n";
            generate += "\n";
            // Récupérer le nom de la table
            String tableName = tables.getString("TABLE_NAME");

            // Récupérer les colonnes de la table
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            // Parcourir les colonnes pour détecter les types nécessitant des imports
            while (columns.next()) {
                String dataType = columns.getString("TYPE_NAME");

                // Ajouter les imports nécessaires
                addImportsForType(imports, dataType);
            }
            for (String importStatement : imports) {
                generate += importStatement + "\n";
            }
            generate += "\n";

            // Commencer la génération de la classe Java
            generate += "public class " + toCamelCaseMaj(tableName) + " {\n";

            columns.beforeFirst(); // Réinitialiser le curseur
            while (columns.next()) {
                // Récupérer le nom et le type de la colonne
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                // System.out.println("Type du colonne " + dataType);

                // Générer le champ de la classe Java
                generate += "    private " + getJavaType(dataType) + " " + toCamelCaseMin(columnName) + ";\n";
            }
            generate += "\n";

            // Générer les getters et setters
            columns.beforeFirst(); // Réinitialiser le curseur
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");

                generate += getter(getJavaType(dataType),
                        toCamelCaseMin(columnName));
                generate += setter(getJavaType(dataType),
                        toCamelCaseMin(columnName));
            }
            generate += "}\n";
            // Finir la classe Java
            writeToFile(toCamelCaseMaj(tableName) + ".java", generate);
        }
        return generate;
    }

    public static void writeToFile(String fileName, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toCamelCaseMaj(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

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

    private static String toCamelCaseMin(String input) {
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

    // Obtenir le type Java correspondant à un type SQL
    private static String getJavaType(String sqlType) {
        // Ajouter davantage de correspondances au besoin
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
                return "String";
            case "INT4":
            case "SERIAL":
            case "INTEGER":
                return "int";
            case "DATE":
                return "Date";
            case "TIMESTAMP":
                return "Timestamp";
            case "NUMERIC":
            case "DOUBLE PRECISION":
                return "double";
            case "FLOAT8":
                return "float";
            default:
                return "Object";
        }
    }

    private static void addImportsForType(Set<String> imports, String dataType) {
        switch (dataType.toUpperCase()) {
            case "DATE":
                imports.add("import java.sql.Date;");
                break;
            case "TIMESTAMP":
                imports.add("import java.sql.Timestamp;");
                break;
            default:
                break;
        }
    }

    private static String getter(String type, String name) {
        String getter = "    public " + type + " get" + name.substring(0, 1).toUpperCase() + name.substring(1)
                + "() {\n";
        getter += "        return this." + name + ";\n";
        getter += "    }\n";
        return getter;
    }

    private static String setter(String type, String name) {
        String getter = "    public void set" + name.substring(0, 1).toUpperCase() + name.substring(1) + "(" + type
                + " "
                + name + ") {\n";
        getter += "        this." + name + " = " + name + ";\n";
        getter += "    }\n";
        return getter;
    }
}
