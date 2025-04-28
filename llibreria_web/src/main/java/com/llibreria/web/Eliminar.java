package com.llibreria.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que gestiona la eliminación de un libro de la base de datos.
 * Este servlet procesa las solicitudes GET y POST para eliminar un libro y sus
 * relaciones con autores y géneros.
 * 
 * <p>Este servlet se mapea a la URL "/eliminar" y procesa solicitudes GET y POST.</p>
 */
@WebServlet("/eliminar")
public class Eliminar extends HttpServlet {
    /**
     * Procesa la solicitud GET para eliminar un libro de la base de datos.
     * 
     * <p>Este método llama al método {@link #eliminarLibro(HttpServletRequest, HttpServletResponse)} 
     * para eliminar el libro solicitado.</p>
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        eliminarLibro(request, response);
    }

    /**
     * Procesa la solicitud POST para eliminar un libro de la base de datos.
     * 
     * <p>Este método llama al método {@link #eliminarLibro(HttpServletRequest, HttpServletResponse)} 
     * para eliminar el libro solicitado.</p>
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        eliminarLibro(request, response);
    }
    
    /**
     * Elimina un libro de la base de datos, junto con sus relaciones con autores y géneros.
     * Este método se encarga de ejecutar las consultas necesarias para eliminar los registros
     * correspondientes en las tablas relacionadas antes de eliminar el libro en sí.
     * Si la eliminación se realiza correctamente, se redirige a la página de consulta.
     * Si ocurre algún error, se realiza un rollback y se muestra un mensaje de error.
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    private void eliminarLibro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        String deleteLlibreAutor = "DELETE FROM llibre_autor WHERE id_llibre = ?";
        String deleteLlibreGenere = "DELETE FROM llibre_genere WHERE id_llibre = ?";
        String deleteLlibre = "DELETE FROM llibres WHERE id = ?";

        try (Connection conn = Connexio.getConnection(); PreparedStatement stmt1 = conn.prepareStatement(deleteLlibreAutor); PreparedStatement stmt2 = conn.prepareStatement(deleteLlibreGenere); PreparedStatement stmt3 = conn.prepareStatement(deleteLlibre)) {

            conn.setAutoCommit(false);

            stmt1.setInt(1, id);
            stmt1.executeUpdate();

            stmt2.setInt(1, id);
            stmt2.executeUpdate();

            stmt3.setInt(1, id);
            int rowsAffected = stmt3.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                response.sendRedirect("consulta");
            } else {
                conn.rollback();
                response.getWriter().println("No se encontró el libro con ID: " + id);
            }

        } catch (SQLException e) {
            response.getWriter().println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
}
