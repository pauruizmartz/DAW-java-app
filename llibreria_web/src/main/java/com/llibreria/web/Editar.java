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
 * Servlet que gestiona la edición de un libro existente en la base de datos.
 * Este servlet procesa las solicitudes POST enviadas por el formulario de edición de libros,
 * actualizando los datos del libro, sus autores y géneros relacionados.
 * 
 * <p>Este servlet se mapea a la URL "/editar" y procesa solicitudes POST.</p>
 */
@WebServlet("/editar")
public class Editar extends HttpServlet {

    /**
     * Procesa la solicitud POST para editar un libro en la base de datos.
     * 
     * <p>Este método recibe los parámetros del formulario de edición de libro, 
     * actualiza los datos del libro en la base de datos, y luego actualiza los autores 
     * y géneros asociados al libro en las tablas correspondientes. Finalmente, 
     * redirige al usuario a la página de consulta de libros.</p>
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String titol = request.getParameter("titol");
        String isbn = request.getParameter("isbn");
        int anyPublicacio = Integer.parseInt(request.getParameter("any_publicacio"));
        int editorialId = Integer.parseInt(request.getParameter("editorial"));

        String[] autors = request.getParameterValues("autors");
        String[] generes = request.getParameterValues("generes");

        String updateQuery = "UPDATE llibres SET titol = ?, isbn = ?, any_publicacio = ?, id_editorial = ? WHERE id = ?";

        try (Connection conn = Connexio.getConnection(); PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, titol);
            stmt.setString(2, isbn);
            stmt.setInt(3, anyPublicacio);
            stmt.setInt(4, editorialId);
            stmt.setInt(5, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                updateAuthorsAndGenres(conn, id, autors, generes);
                response.sendRedirect("consulta");
            } else {
                response.getWriter().println("Error al editar el libro.");
            }
        } catch (SQLException e) {
            response.getWriter().println("Error de conexión a la base de datos.");
        }
    }
    
    /**
     * Actualiza los autores y géneros asociados a un libro en la base de datos.
     * Primero elimina los autores y géneros actuales, luego inserta los nuevos.
     * 
     * @param conn La conexión a la base de datos.
     * @param libroId El ID del libro a actualizar.
     * @param autors Los IDs de los autores asociados al libro.
     * @param generes Los IDs de los géneros asociados al libro.
     * @throws SQLException Si ocurre un error con las consultas SQL.
     */
    private void updateAuthorsAndGenres(Connection conn, int libroId, String[] autors, String[] generes) throws SQLException {
        String deleteAuthorsQuery = "DELETE FROM llibre_autor WHERE id_llibre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteAuthorsQuery)) {
            stmt.setInt(1, libroId);
            stmt.executeUpdate();
        }

        if (autors != null) {
            String insertAuthorsQuery = "INSERT INTO llibre_autor (id_llibre, id_autor) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertAuthorsQuery)) {
                for (String autorId : autors) {
                    stmt.setInt(1, libroId);
                    stmt.setInt(2, Integer.parseInt(autorId));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }

        String deleteGenresQuery = "DELETE FROM llibre_genere WHERE id_llibre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteGenresQuery)) {
            stmt.setInt(1, libroId);
            stmt.executeUpdate();
        }

        if (generes != null) {
            String insertGenresQuery = "INSERT INTO llibre_genere (id_llibre, id_genere) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertGenresQuery)) {
                for (String genereId : generes) {
                    stmt.setInt(1, libroId);
                    stmt.setInt(2, Integer.parseInt(genereId));
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }
    }
}
