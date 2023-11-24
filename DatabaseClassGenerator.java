import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseClassGenerator {

    public static void main(String[] args) {
        // Informations de connexion à la base de données
        String jdbcUrl = "jdbc:postgresql://localhost:5432/foncier";
        String username = "postgres";
        String password = "mdp";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Obtenir les métadonnées de la base de données
            DatabaseMetaData metaData = connection.getMetaData();

            // Récupérer les tables de la base de données
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            while (tables.next()) {
                // Récupérer le nom de la table
                String tableName = tables.getString("TABLE_NAME");

                // Récupérer les colonnes de la table
                ResultSet columns = metaData.getColumns(null, null, tableName, null);

                // Commencer la génération de la classe Java
                System.out.println("public class " + toCamelCase(tableName) + " {");

                while (columns.next()) {
                    // Récupérer le nom et le type de la colonne
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");

                    // Générer le champ de la classe Java
                    System.out.println("    private " + getJavaType(dataType) + " " + toCamelCase(columnName) + ";");
                }

                // Finir la classe Java
                System.out.println("}\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Convertir un nom de colonne en format CamelCase
    private static String toCamelCase(String input) {
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
            case "INT":
            case "SERIAL":
            case "INTEGER":
                return "int";
            case "DATE":
                return "Date";
            case "NUMERIC":
            case "DOUBLE PRECISION":
                return "double";
            default:
                return "Object";
        }
    }
}
