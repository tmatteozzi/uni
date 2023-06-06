import java.sql.*;
import java.util.*;

public class Biblioteca {
    private String user = "root";
    private String pass = "root";
    private String nombreBD = "biblioteca";
    private String url = "jdbc:mysql://localhost:3306/" + nombreBD + "?useUnicode=true&use"
            + "JDBCCompilantTimeZoneShift=true&useLegacyDatetimeCode=false&" + "serverTimezone=UTC";
    private Connection connection;

    public Biblioteca() {
        try {
            // Establecer la conexión a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            if (connection!=null){
                System.out.println("Conexion exitosa a la BD: " + nombreBD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void agregarLibro(Libro libro) {
        try {
            if (!existeLibro(libro.getTitulo())) {
                // Ejecutar la consulta de inserción
                String sql = "INSERT INTO Libro (titulo, isbn, editorial, genero, autores, cantidadPaginas, anioEdicion) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, libro.getTitulo());
                statement.setString(2, libro.getIsbn());
                statement.setString(3, libro.getEditorial());
                statement.setString(4, libro.getGenero());
                statement.setString(5, String.join(",", libro.getAutores()));
                statement.setInt(6, libro.getCantidadPaginas());
                statement.setInt(7, libro.getAnioEdicion());
                statement.executeUpdate();
                statement.close();
                System.out.println("El libro ha sido agregado correctamente.");
            } else {
                throw new Exception("El libro ya existe en la biblioteca.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void eliminarLibro(String titulo) {
        try {
            String sql = "DELETE FROM Libro WHERE titulo = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, titulo);
            int columnasBorradas = statement.executeUpdate();
            statement.close();
            if (columnasBorradas > 0) {
                System.out.println("Eliminado correctamente.");
            } else {
                throw new Exception("No se encontró ningún libro con el título ingresado.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void consultarPorTitulo(String titulo) {
        try {
            String sql = "SELECT * FROM Libro WHERE titulo = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Leer los datos del resultado y mostrarlos
                System.out.println(cargarResultados(resultSet));
            } else {
                throw new Exception("El libro no se encuentra en la biblioteca.");
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void modificarLibro(String titulo, Libro nuevoLibro) {
        try {
            String sqlSelect = "SELECT id FROM Libro WHERE titulo = ?";
            PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
            selectStatement.setString(1, titulo);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String sqlUpdate = "UPDATE Libro SET titulo = ?, isbn = ?, editorial = ?, genero = ?, autores = ?, cantidadPaginas = ?, anioEdicion = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate);
                updateStatement.setString(1, nuevoLibro.getTitulo());
                updateStatement.setString(2, nuevoLibro.getIsbn());
                updateStatement.setString(3, nuevoLibro.getEditorial());
                updateStatement.setString(4, nuevoLibro.getGenero());
                updateStatement.setString(5, String.join(", ", nuevoLibro.getAutores()));
                updateStatement.setInt(6, nuevoLibro.getCantidadPaginas());
                updateStatement.setInt(7, nuevoLibro.getAnioEdicion());
                updateStatement.setInt(8, id);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("El libro ha sido modificado correctamente.");
                } else {
                    throw new Exception("No se pudo modificar el libro en la base de datos.");
                }

                updateStatement.close();
            } else {
                throw new Exception("El libro no existe en la biblioteca.");
            }
            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void listarAutores() {
        try {
            String sql = "SELECT DISTINCT autores FROM Libro";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            Set<String> autores = new HashSet<>();
            while (resultSet.next()) {
                String autor = resultSet.getString("autores");
                autores.add(autor);
            }
            resultSet.close();
            statement.close();
            System.out.println("Autores existentes: " + autores);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibros() {
        try {
            String sql = "SELECT * FROM Libro";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorGenero(String genero) {
        try {
            String sql = "SELECT * FROM Libro WHERE genero = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, genero);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorAutor(String autor) {
        try {
            String sql = "SELECT * FROM Libro WHERE FIND_IN_SET(?, autores) > 0";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, autor);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorEditorial(String editorial) {
        try {
            String sql = "SELECT * FROM Libro WHERE editorial = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, editorial);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorEditorialEnRangoDeAnios(String editorial, int anioInicio, int anioFin) {
        try {
            String sql = "SELECT * FROM Libro WHERE editorial = ? AND anioEdicion BETWEEN ? AND ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, editorial);
            statement.setInt(2, anioInicio);
            statement.setInt(3, anioFin);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarAutoresPorEditorial(String editorial) {
        try {
            String sql = "SELECT DISTINCT autores FROM Libro WHERE editorial = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, editorial);
            ResultSet resultSet = statement.executeQuery();
            Set<String> autores = new HashSet<>();
            while (resultSet.next()) {
                String autor = resultSet.getString("autores");
                autores.add(autor);
            }
            resultSet.close();
            statement.close();
            System.out.println("Autores de la editorial " + editorial + ": " + autores);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorAnio(int anio) {
        try {
            String sql = "SELECT * FROM Libro WHERE anioEdicion = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, anio);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorLetraApellidoAutores(char letraInicial) {
        try {
            String sql = "SELECT * FROM Libro WHERE autores REGEXP ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "^" + letraInicial);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void listarLibrosPorPalabraTitulo(String palabra) {
        try {
            String sql = "SELECT * FROM Libro WHERE titulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + palabra + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS AUXILIARES
    public boolean existeLibro(String titulo) {
        try {
            String sql = "SELECT COUNT(*) FROM Libro WHERE titulo = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Libro cargarResultados(ResultSet resultSet) throws SQLException {
        String titulo = resultSet.getString("titulo");
        String isbn = resultSet.getString("isbn");
        String editorial = resultSet.getString("editorial");
        String genero = resultSet.getString("genero");
        String[] autores = resultSet.getString("autores").split(",");
        int cantidadPaginas = resultSet.getInt("cantidadPaginas");
        int anioEdicion = resultSet.getInt("anioEdicion");
        return new Libro(titulo, autores, cantidadPaginas, isbn, anioEdicion, editorial, genero);
    }
    public static void imprimirMenu() {
        System.out.println("1. Dar de alta un libro");
        System.out.println("2. Dar de baja un libro");
        System.out.println("3. Consultar por un libro de un determinado título");
        System.out.println("4. Modificar los datos de un libro");
        System.out.println("5. Listar todos los autores existentes");
        System.out.println("6. Listar todos los libros existentes");
        System.out.println("7. Listar todos los libros de un género determinado");
        System.out.println("8. Listar todos los libros que posee un autor determinado");
        System.out.println("9. Listar todos los libros de una editorial determinada.");
        System.out.println("10. Listar todos los libros de una editorial determinada en un rango de años de edición");
        System.out.println("11. Listar todos los autores de una determinada editorial");
        System.out.println("12. Listar todos los libros que fueron editados en un determinado año");
        System.out.println("13. Listar todos los libros de los autores cuyos apellidos comienzan con una letra determinada");
        System.out.println("14. Listar todos los libros cuyos títulos contengan una palabra determinada.");
        System.out.println("15. Salir");
    }
}
