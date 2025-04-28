package com.llibreria.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona la conexión a la base de datos MySQL.
 * Proporciona un método estático para obtener una conexión con la base de datos.
 * 
 * <p>La conexión se realiza a la base de datos 'llibres' en el servidor local
 * (localhost) utilizando el usuario 'root' y sin contraseña.</p>
 * 
 * <p>En caso de no encontrar el driver JDBC, se lanzará una excepción SQLException.</p>
 */
public class Connexio {

    /**
     * URL de la base de datos MySQL a la que se conecta.
     * Especifica el servidor local y la base de datos 'llibres'.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/llibres";

    /**
     * Nombre de usuario para acceder a la base de datos.
     * En este caso, se utiliza el usuario por defecto 'root'.
     */
    private static final String USER = "root";
    
    /**
     * Contraseña del usuario para acceder a la base de datos.
     * En este caso, no se especifica contraseña, por lo que se deja vacía.
     */
    private static final String PASSWORD = "";

    /**
     * Obtiene una conexión con la base de datos MySQL.
     * 
     * <p>Este método intenta cargar el driver JDBC para MySQL y, si tiene éxito,
     * devuelve una conexión a la base de datos configurada. En caso de que no se
     * encuentre el driver, se lanzará una SQLException.</p>
     * 
     * @return Una conexión activa a la base de datos.
     * @throws SQLException Si ocurre un error al intentar obtener la conexión,
     *                     o si no se encuentra el driver JDBC.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver JDBC", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
