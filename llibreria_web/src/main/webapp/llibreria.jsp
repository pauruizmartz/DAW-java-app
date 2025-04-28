<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Llibreria</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
    <div class="container mt-5">
        <h1 class="text-center mb-4">Lista de Libros</h1>

        <div class="text-center">
            <button class="btn btn-primary mb-4" onclick="showCreateForm()">Añadir Nuevo Libro</button>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>ISBN</th>
                        <th>Año de Publicación</th>
                        <th>Editorial</th>
                        <th>Autores</th>
                        <th>Géneros</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        String url = "jdbc:mysql://localhost:3306/llibres"; 
                        String user = "root"; 
                        String password = ""; 
                        Connection conn = null; 
                        PreparedStatement stmt = null; 
                        ResultSet rs = null;
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            conn = DriverManager.getConnection(url, user, password);
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
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) { 
                    %>
                    <tr>
                        <td><%= rs.getInt("id") %></td>
                        <td><%= rs.getString("titol") %></td>
                        <td><%= rs.getString("isbn") %></td>
                        <td><%= rs.getInt("any_publicacio") %></td>
                        <td><%= rs.getString("editorial") %></td>
                        <td><%= rs.getString("autors") %></td>
                        <td><%= rs.getString("generes") %></td>
                        <td>
                            <button class="btn btn-warning" onclick="editBook(<%= rs.getInt("id") %>, '<%= rs.getString("titol") %>', '<%= rs.getString("isbn") %>', <%= rs.getInt("any_publicacio") %>, '<%= rs.getString("editorial") %>', '<%= rs.getString("autors") %>', '<%= rs.getString("generes") %>')">Editar</button>
                            <a href="eliminar?id=<%= rs.getInt("id") %>" class="btn btn-danger" onclick="return confirm('¿Estás seguro de que deseas eliminar este libro?')">Eliminar</a>
                        </td>
                    </tr>
                    <% 
                            } 
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (rs != null) rs.close();
                                if (stmt != null) stmt.close();
                                if (conn != null) conn.close();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>

        <div id="createForm" class="mt-5" style="display: none;">
            <h2>Añadir Nuevo Libro</h2>
            <form action="crear" method="POST">
                <div class="form-group">
                    <label for="titol">Título:</label>
                    <input type="text" id="titol" name="titol" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="isbn">ISBN:</label>
                    <input type="text" id="isbn" name="isbn" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="any_publicacio">Año de Publicación:</label>
                    <input type="number" id="any_publicacio" name="any_publicacio" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="editorial">Editorial:</label>
                    <select name="editorial" id="editorial" class="form-control" required>
                        <option value="1">Penguin Random House</option>
                        <option value="2">HarperCollins</option>
                        <option value="3">Simon & Schuster</option>
                        <option value="4">Macmillan Publishers</option>
                        <option value="5">Hachette Livre</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="autors">Autores:</label>
                    <select name="autors" id="autors" class="form-control" multiple required>
                        <option value="1">J.K. Rowling</option>
                        <option value="2">George R.R. Martin</option>
                        <option value="3">Dan Brown</option>
                        <option value="4">J.R.R. Tolkien</option>
                        <option value="5">Suzanne Collins</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="generes">Géneros:</label>
                    <select name="generes" id="generes" class="form-control" multiple required>
                        <option value="1">Fantasía</option>
                        <option value="2">Ciència Ficció</option>
                        <option value="3">Thriller</option>
                        <option value="4">Ficció Històrica</option>
                        <option value="5">Juvenil</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-success mt-3">Crear Libro</button>
            </form>
        </div>

        <div id="editForm" class="mt-5" style="display: none;">
            <h2>Editar Libro</h2>
            <form action="editar" method="POST">
                <input type="hidden" id="editId" name="id">
                
                <div class="form-group">
                    <label for="editTitol">Título:</label>
                    <input type="text" id="editTitol" name="titol" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="editIsbn">ISBN:</label>
                    <input type="text" id="editIsbn" name="isbn" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="editAnyPublicacio">Año de Publicación:</label>
                    <input type="number" id="editAnyPublicacio" name="any_publicacio" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="editEditorial">Editorial:</label>
                    <select name="editorial" id="editEditorial" class="form-control" required>
                        <option value="1">Penguin Random House</option>
                        <option value="2">HarperCollins</option>
                        <option value="3">Simon & Schuster</option>
                        <option value="4">Macmillan Publishers</option>
                        <option value="5">Hachette Livre</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="editAutors">Autores:</label>
                    <select name="autors" id="editAutors" class="form-control" multiple required>
                        <option value="1">J.K. Rowling</option>
                        <option value="2">George R.R. Martin</option>
                        <option value="3">Dan Brown</option>
                        <option value="4">J.R.R. Tolkien</option>
                        <option value="5">Suzanne Collins</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="editGeneres">Géneros:</label>
                    <select name="generes" id="editGeneres" class="form-control" multiple required>
                        <option value="1">Fantasía</option>
                        <option value="2">Ciència Ficció</option>
                        <option value="3">Thriller</option>
                        <option value="4">Ficció Històrica</option>
                        <option value="5">Juvenil</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary mt-3">Actualizar Libro</button>
            </form>
        </div>
        
    </div>

    <script>
        function showCreateForm() {
            document.getElementById('createForm').style.display = 'block';
        }

        function editBook(id, titol, isbn, anyPublicacio, editorial, autors, generes) {
            document.getElementById('editForm').style.display = 'block';
            document.getElementById('editId').value = id;
            document.getElementById('editTitol').value = titol;
            document.getElementById('editIsbn').value = isbn;
            document.getElementById('editAnyPublicacio').value = anyPublicacio;
            document.getElementById('editEditorial').value = editorial;
        }
    </script>

</body>

</html>
