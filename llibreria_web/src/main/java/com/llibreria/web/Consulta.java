package com.llibreria.web;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet que gestiona la consulta y presentación de los libros almacenados
 * en la base de datos. Esta clase se encarga de generar una página HTML
 * con una tabla que muestra los detalles de los libros, incluyendo su título,
 * ISBN, año de publicación, editorial, autores y géneros.
 * 
 * <p>Este servlet se mapea a la URL "/consulta" y procesa solicitudes GET.</p>
 */ 
@WebServlet("/consulta")
public class Consulta extends HttpServlet {
    
    /**
     * Procesa la solicitud GET y genera una página HTML con una tabla que muestra
     * la información de los libros almacenados en la base de datos. La consulta
     * recupera el ID, título, ISBN, año de publicación, editorial, autores y géneros
     * de los libros, y muestra esta información en una tabla utilizando HTML y Bootstrap.
     *
     * <p>Si ocurre un error al acceder a la base de datos, se muestra un mensaje de error.</p>
     *
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Read</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");
            out.println("<h1 class='mb-4'>Read</h1>");
            out.println("<table class='table table-striped'>");
            out.println("<thead class='table-dark'>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Título</th>");
            out.println("<th>ISBN</th>");
            out.println("<th>Año de Publicación</th>");
            out.println("<th>Editorial</th>");
            out.println("<th>Autores</th>");
            out.println("<th>Géneros<th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            try (Connection conn = Connexio.getConnection()) {
                String query = "SELECT l.id, l.titol, l.isbn, l.any_publicacio, e.nom AS editorial, "
                        + "GROUP_CONCAT(DISTINCT a.nom SEPARATOR ', ') AS autors, "
                        + "GROUP_CONCAT(DISTINCT g.nom SEPARATOR ', ') AS generes "
                        + "FROM llibres l "
                        + "LEFT JOIN editorials e ON l.id_editorial = e.id "
                        + "LEFT JOIN llibre_autor la ON l.id = la.id_llibre "
                        + "LEFT JOIN autors a ON la.id_autor = a.id "
                        + "LEFT JOIN llibre_genere lg ON l.id = lg.id_llibre "
                        + "LEFT JOIN generes g ON lg.id_genere = g.id "
                        + "GROUP BY l.id";
                try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        out.println("<tr>");
                        out.println("<td>" + rs.getInt("id") + "</td>");
                        out.println("<td>" + rs.getString("titol") + "</td>");
                        out.println("<td>" + rs.getString("isbn") + "</td>");
                        out.println("<td>" + rs.getInt("any_publicacio") + "</td>");
                        out.println("<td>" + rs.getString("editorial") + "</td>");
                        out.println("<td>" + rs.getString("autors") + "</td>");
                        out.println("<td>" + rs.getString("generes") + "</td>");
                        out.println("</tr>");
                    }
                }
            } catch (SQLException e) {
                out.println("<tr><td colspan='4'>Error al acceder a la base de datos</td></tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
