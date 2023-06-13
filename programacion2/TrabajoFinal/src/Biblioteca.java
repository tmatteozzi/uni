import java.sql.*;
import java.util.*;

public class Biblioteca {
    // Datos de MySQL para conectar a la base de datos
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
                // Insertar a la base de datos el nuevo libro
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
        System.out.println();
    }

    public void eliminarLibro(String titulo) {
        try {
            // Eliminar el libro si coincide el titulo
            String sql = "DELETE FROM Libro WHERE titulo = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, titulo);
            // Sumar 1 si se elimino
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
        System.out.println();
    }

    public void consultarPorTitulo(String titulo) {
        try {
            // Si el titulo en la base de datos es igual al parametro imprimir datos del libro
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
        System.out.println();
    }

    public void modificarLibro(String titulo, Libro nuevoLibro) {
        try {
            // Buscar el libro que tenga el titulo que se le pasa por parametro
            String sqlSelect = "SELECT id FROM Libro WHERE titulo = ?";
            PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
            selectStatement.setString(1, titulo);
            ResultSet resultSet = selectStatement.executeQuery();

            // Si encontro el libro entonces realizar la modificacion de los parametros
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
                int columnasAfectadas = updateStatement.executeUpdate();

                // Si se modifico al menos una columna, entonces se imprime que se modifico correctamente
                if (columnasAfectadas > 0) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarAutores() {
        try {
            // Listar todos los autores de la base de datos (Sin que se repitan (DISTINCT))
            String sql = "SELECT DISTINCT autores FROM Libro";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            // Crear hash set donde se van a guardar los autores de la bd
            Set<String> autores = new HashSet<>();
            while (resultSet.next()) {
                // Cargar autores al set
                String autor = resultSet.getString("autores");
                autores.add(autor);
            }
            resultSet.close();
            statement.close();
            System.out.println("Autores existentes: " + autores);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibros() {
        try {
            // Listar todos los libros de la bd
            String sql = "SELECT * FROM Libro";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorGenero(String genero) {
        try {
            // Buscar todos los libros que tengan el genero que se pasa por parametro
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
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorAutor(String autor) {
        try {
            // Listar todos los libros que tiene un autor, si el autor es igual al autor pasado por parametro
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
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorEditorial(String editorial) {
        try {
            // Listar todos los libros que tengan la editorial que se pasa por parametro
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
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorEditorialEnRangoDeAnios(String editorial, int anioInicio, int anioFin) {
        try {
            // Listar todos los libros de una determinada editorial entre el año de inicio y el año final
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
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarAutoresPorEditorial(String editorial) {
        try {
            // Listar todos los autores que tengan libros en una determinada editorial pasada por parametro
            String sql = "SELECT DISTINCT autores FROM Libro WHERE editorial = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, editorial);
            ResultSet resultSet = statement.executeQuery();
            // Crear hash set para guardar autores
            Set<String> autores = new HashSet<>();
            while (resultSet.next()) {
                // Cargar los autores
                String autor = resultSet.getString("autores");
                autores.add(autor);
            }
            resultSet.close();
            statement.close();
            System.out.println("Autores de la editorial " + editorial + ": " + autores);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorAnio(int anio) {
        try {
            // Listar todos los libros en un año determinado pasado por parametro
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
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorLetraApellidoAutores(char letraInicial) {
        try {
            // Listar libros donde la primera letra del autor (parametro) sea la primera letra del apellido
            // REGEXP para usar ^
            String sql = "SELECT * FROM Libro WHERE autores REGEXP ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Se pone ^ + letraInicial para que el autor sea el que empieze con esa letra
            statement.setString(1, "^" + letraInicial);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    public void listarLibrosPorPalabraTitulo(String palabra) {
        try {
            // Listar los libros que contengan la palabra pasada por parametro en su nombre
            String sql = "SELECT * FROM Libro WHERE titulo LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Los % % hacen que tenga que estar presente la palabra
            statement.setString(1, "%" + palabra + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(cargarResultados(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    // MÉTODOS AUXILIARES
    public boolean existeLibro(String titulo) {
        try {
            // Fijarse cuantos libros hay con el mismo titulo
            String sql = "SELECT COUNT(*) FROM Libro WHERE titulo = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            // Si hay mas de uno se devuelve true
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Libro cargarResultados(ResultSet resultSet) throws SQLException {
        // Se pasa por parametro un resultSet que sería un Libro y se crea una instancia y se devuelve
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
