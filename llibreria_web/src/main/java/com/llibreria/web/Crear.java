package com.llibreria.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que gestiona la creación de un nuevo libro en la base de datos. 
 * Este servlet procesa las solicitudes POST enviadas por el formulario de creación de libros,
 * insertando un nuevo registro en la tabla de libros, junto con los autores y géneros relacionados.
 * 
 * <p>Este servlet se mapea a la URL "/crear" y procesa solicitudes POST.</p>
 */
@WebServlet("/crear")
public class Crear extends HttpServlet {
    
    /**
     * Procesa la solicitud POST para crear un nuevo libro en la base de datos.
     * 
     * <p>Este método recibe los parámetros del formulario de creación de libro,
     * inserta los datos del libro en la base de datos, y luego inserta los autores y géneros
     * asociados al libro en las tablas correspondientes. Finalmente, redirige al usuario a la
     * página de consulta de libros.</p>
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String titol = request.getParameter("titol");
        String isbn = request.getParameter("isbn");
        int anyPublicacio = Integer.parseInt(request.getParameter("any_publicacio"));
        int idEditorial = Integer.parseInt(request.getParameter("editorial"));
        String[] autors = request.getParameterValues("autors");
        String[] generes = request.getParameterValues("generes");

        String insertLibroQuery = "INSERT INTO llibres (titol, isbn, any_publicacio, id_editorial) VALUES (?, ?, ?, ?)";
        try (Connection conn = Connexio.getConnection(); PreparedStatement stmt = conn.prepareStatement(insertLibroQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, titol);
            stmt.setString(2, isbn);
            stmt.setInt(3, anyPublicacio);
            stmt.setInt(4, idEditorial);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idLibro = generatedKeys.getInt(1);

                    String insertAutorQuery = "INSERT INTO llibre_autor (id_llibre, id_autor) VALUES (?, ?)";
                    try (PreparedStatement stmtAutor = conn.prepareStatement(insertAutorQuery)) {
                        for (String autor : autors) {
                            int idAutor = Integer.parseInt(autor);
                            stmtAutor.setInt(1, idLibro);
                            stmtAutor.setInt(2, idAutor);
                            stmtAutor.addBatch();
                        }
                        stmtAutor.executeBatch();
                    }

                    String insertGenereQuery = "INSERT INTO llibre_genere (id_llibre, id_genere) VALUES (?, ?)";
                    try (PreparedStatement stmtGenere = conn.prepareStatement(insertGenereQuery)) {
                        for (String genere : generes) {
                            int idGenere = Integer.parseInt(genere);
                            stmtGenere.setInt(1, idLibro);
                            stmtGenere.setInt(2, idGenere);
                            stmtGenere.addBatch();
                        }
                    }

                    response.sendRedirect("consulta");
                }
            } else {
                response.getWriter().println("Error al crear el libro.");
            }
        } catch (SQLException e) {
            response.getWriter().println("Error de conexión a la base de datos.");
        }
    }
}
