package com.llibreria.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que maneja las solicitudes HTTP GET para la ruta "/hello".
 * Este servlet simplemente responde con un mensaje HTML que saluda a los usuarios.
 * 
 * <p>Este servlet está mapeado a la URL "/hello" y responde a las solicitudes GET.</p>
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    /**
     * Procesa la solicitud GET y responde con un mensaje HTML que saluda al usuario.
     * 
     * <p>Este método establece el tipo de contenido como "text/html" y luego envía un
     * mensaje de saludo en formato HTML al cliente.</p>
     * 
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Hola, Llibreriaaa!</h1>");
    }
}
