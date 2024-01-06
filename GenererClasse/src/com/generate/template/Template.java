package com.generate.template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import com.generate.config.Config;

import java.util.ArrayList;

public class Template {

    public static String changeTemplate(Config config, String templatePath, String packageName, String table, Connection c) throws SQLException {
        String generate = Template.loadTemplate(templatePath);
        String importStatement = "";
        String attributes = "";
        String dataType = "";
        String getters_setters = "";
        // String set = "";
        int countAttributes = 0;
        int countImports = 0;
        DatabaseMetaData metaData = c.getMetaData();

        List<String> imports = new ArrayList<>();

        ResultSet tables = metaData.getTables(null, null, table, new String[]{"TABLE", "VIEW"});
        ResultSet columns = null;
        String tableName = "";
        while (tables.next()) {
            tableName = tables.getString("TABLE_NAME");
            generate = generate.replace("##PACKAGE##", config.getPackageName());

            columns = metaData.getColumns(null, null, tableName, null);

            while (columns.next()) {
                dataType = columns.getString("TYPE_NAME");
                addImportsForType(imports, dataType, config);
            }

            for (String importState : imports) {
                if (countImports == 0) {
                    config.setImportStatement(config.getImportStatement().replace("${importName}n", "${importName}" + countImports));
                } else {
                    config.setImportStatement(config.getImportStatement().replace("${importName}" + (countImports - 1), "${importName}" + countImports));
                }
                importStatement += config.getImportStatement() + "\n";
                countImports++;
            }

            generate = generate.replace("##IMPORT##", importStatement);

            generate = generate.replace("##CLASS_DECLARATION##", config.getClassDeclaration());

            columns.beforeFirst();
            while (columns.next()) {
                dataType = columns.getString("TYPE_NAME");
                if (countAttributes == 0) {
                    System.out.println("Type du colonne " + dataType);
                    config.setAttributes(config.getAttributes().replace("${attributesType}n", "${attributesType}" + countAttributes));
                    config.setAttributes(config.getAttributes().replace("${attributesName}n", "${attributesName}" + countAttributes));
                    config.setGettersSetters(config.getGettersSetters().replace("${attributesType}n", "${attributesType}" + countAttributes));
                    config.setGettersSetters(config.getGettersSetters().replace("${attributesName}n", "${attributesName}" + countAttributes));
                    config.setGettersSetters(config.getGettersSetters().replace("${attributesNameMaj}n", "${attributesNameMaj}" + countAttributes));
                } else {
                    System.out.println("Type du colonne " + dataType);
                    config.setAttributes(config.getAttributes().replace("${attributesType}" + (countAttributes - 1), "${attributesType}" + countAttributes));
                    config.setAttributes(config.getAttributes().replace("${attributesName}" + +(countAttributes - 1), "${attributesName}" + countAttributes));
                    config.setGettersSetters(config.getGettersSetters().replace("${attributesType}" + +(countAttributes - 1), "${attributesType}" + countAttributes));
                    config.setGettersSetters(config.getGettersSetters().replace("${attributesName}" + +(countAttributes - 1), "${attributesName}" + countAttributes));
                    config.setGettersSetters(config.getGettersSetters().replace("${attributesNameMaj}" + +(countAttributes - 1), "${attributesNameMaj}" + countAttributes));
                }
                attributes += "    " + config.getAttributes() + "\n";
                getters_setters += "    " + config.getGettersSetters() + "\n";
                countAttributes++;
            }
            generate = generate.replace("##ATTRIBUTES##", attributes);

            generate = generate.replace("##GETTERS_SETTERS##", getters_setters);

            generate = generate.replace("##END_OF_CLASS##", config.getEndOfClass());
        }
        return generate;
    }

    public static String changeVariable(Config config, String templatePath, String packageName, String table, Connection c) throws SQLException {
        String generate = Template.changeTemplate(config, templatePath, packageName, table, c);
        String dataType = "";
        String columnName = "";
        int countAttributes = 0;
        int countImports = 0;
        DatabaseMetaData metaData = c.getMetaData();

        List<String> imports = new ArrayList<>();

        ResultSet tables = metaData.getTables(null, null, table, new String[]{"TABLE", "VIEW"});
        ResultSet columns = null;
        String tableName = "";
        generate = generate.replace("${packageName}", packageName);
        while (tables.next()) {
            tableName = tables.getString("TABLE_NAME");
            generate = generate.replace("${className}", toCamelCaseMaj(tableName));
            columns = metaData.getColumns(null, null, tableName, null);

            while (columns.next()) {
                dataType = columns.getString("TYPE_NAME");
                addImportsForType(imports, dataType, config);
            }

            for (String importState : imports) {
                generate = generate.replace("${importName}" + countImports, importState);
                countImports++;
            }

            columns.beforeFirst();
            while (columns.next()) {
                columnName = columns.getString("COLUMN_NAME");
                dataType = columns.getString("TYPE_NAME");
                generate = generate.replace("${attributesName}" + countAttributes, toCamelCaseMin(columnName));
                generate = generate.replace("${attributesType}" + countAttributes, getType(dataType, config));
                generate = generate.replace("${attributesNameMaj}" + countAttributes, toCamelCaseMaj(columnName));
                countAttributes++;
            }
        }
        return generate;
    }

    public static void saveToFile(String content, String filePath, String directory) {
        try {
            // Concaténer le chemin du dossier et le chemin du fichier pour obtenir le chemin complet
            String fullPath = Paths.get(directory, toCamelCaseMaj(filePath)).toString();
    
            // Créer le dossier (et ses parents) si nécessaire
            Files.createDirectories(Paths.get(fullPath).getParent());
    
            // Écrire le contenu du fichier
            Files.write(Paths.get(fullPath), content.getBytes());
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

    public static String loadTemplate(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void addImportsForType(List<String> imports, String dataType, Config config) {
        switch (dataType.toUpperCase()) {
            case "DATE":
                imports.add(config.getImportState().get(0));
                break;
            case "TIMESTAMP":
            case "DATETIME":
                imports.add(config.getImportState().get(2));
                break;
            case "TIME":
                imports.add(config.getImportState().get(1));
                break;
            default:
                break;
        }
    }

    private static String getType(String sqlType, Config config) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
            case "TEXT":
            case "LONGTEXT":
                return config.getAttributesTypes().get(1);
            case "INT4":
            case "INT":
            case "SERIAL":
            case "BIT":
            case "BIGINT UNSIGNED":
            case "INT UNSIGNED":
            case "INTEGER":
                return config.getAttributesTypes().get(0);
            case "DATE":
                return config.getAttributesTypes().get(4);
            case "TIMESTAMP":
            case "DATETIME":
                return config.getAttributesTypes().get(6);
            case "TIME":
                return config.getAttributesTypes().get(5);
            case "NUMERIC":
            case "DECIMAL":
            case "DOUBLE PRECISION":
                return config.getAttributesTypes().get(2);
            case "FLOAT8":
            case "FLOAT UNSIGNED":
                return config.getAttributesTypes().get(3);
            default:
                return config.getAttributesTypes().get(7);
        }
    }
}
