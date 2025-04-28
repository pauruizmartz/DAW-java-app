package com.llibreria.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para probar la conexión a una base de datos MySQL.
 *
 * <p>
 * Esta clase contiene un método principal que intenta establecer una conexión a
 * la base de datos con las credenciales especificadas y muestra un mensaje en
 * la consola indicando si la conexión fue exitosa o si ocurrió algún error.</p>
 */
public class TestDBConnection {

    /**
     * Método principal que prueba la conexión a una base de datos MySQL.
     *
     * <p>
     * Este método establece la conexión con la base de datos utilizando la URL,
     * el usuario y la contraseña proporcionados. Si la conexión es exitosa, se
     * imprime un mensaje indicando que la conexión fue exitosa, de lo
     * contrario, se muestra un mensaje de error.</p>
     *
     * @param args Argumentos de la línea de comandos (no se utilizan en este
     *             caso).
     */
    public static void main(String[] args) {
        // Parámetros de conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Si la conexión es exitosa, se imprime este mensaje
            System.out.println("Conexión exitosa a la base de datos.");

            // Ejemplo de uso de la variable connection para realizar una consulta
            try (Statement statement = connection.createStatement()) {
                // Realizamos una consulta para verificar si la conexión es funcional
                String query = "SELECT 1";
                statement.executeQuery(query);
                System.out.println("Consulta ejecutada correctamente.");
            }

        } catch (SQLException e) {
            // Si ocurre un error al intentar conectar, se imprime este mensaje
            System.err.println("Error al conectar con la base de datos.");
        }
    }
}
