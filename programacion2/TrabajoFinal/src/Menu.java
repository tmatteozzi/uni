import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    private Biblioteca biblioteca;

    public Menu() {
        biblioteca = new Biblioteca();
        crearGUI();
    }

    private void crearGUI() {
        /**                                     CREACIÓN DEL GUI                                                     **/
        setTitle("Menú Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton agregarLibroButton = new JButton("Agregar Libro");
        JButton eliminarLibroButton = new JButton("Eliminar Libro");
        JButton consultarTituloButton = new JButton("Consultar por Título");
        JButton modificarLibroButton = new JButton("Modificar Libro");
        JButton listarAutoresButton = new JButton("Listar Autores");
        JButton listarLibrosButton = new JButton("Listar Libros");
        JButton listarPorGeneroButton = new JButton("Listar Libros por Género");
        JButton listarPorAutorButton = new JButton("Listar Libros por Autor");
        JButton listarPorEditorialButton = new JButton("Listar Libros por Editorial");
        JButton listarPorEditorialRangoButton = new JButton("Listar Libros por Editorial en Rango de Años");
        JButton listarAutoresEditorialButton = new JButton("Listar Autores por Editorial");
        JButton listarPorAnioButton = new JButton("Listar Libros por Año");
        JButton listarPorLetraApellidoButton = new JButton("Listar Libros por Letra del Apellido de los Autores");
        JButton listarPorPalabraTituloButton = new JButton("Listar Libros por Palabra en el Título");
        JButton salirButton = new JButton("Salir");
        JTextArea resultadoTextArea = new JTextArea();

        // AGREGAR LOS BOTONES Y EL TEXT AREA
        add(agregarLibroButton);
        add(eliminarLibroButton);
        add(consultarTituloButton);
        add(modificarLibroButton);
        add(listarAutoresButton);
        add(listarLibrosButton);
        add(listarPorGeneroButton);
        add(listarPorAutorButton);
        add(listarPorEditorialButton);
        add(listarPorEditorialRangoButton);
        add(listarAutoresEditorialButton);
        add(listarPorAnioButton);
        add(listarPorLetraApellidoButton);
        add(listarPorPalabraTituloButton);
        add(salirButton);
        add(resultadoTextArea);

        pack();
        setVisible(true);

        /**                                     FUNCIONAMIENTO DE BOTONES                                           **/

        // AGREGAR LIBRO
        agregarLibroButton.addActionListener(e -> {
            String titulo = JOptionPane.showInputDialog("Ingrese el título del libro: ");
            String inputAutores = JOptionPane.showInputDialog("Ingresar el apellido del autor. Si hay más autores, separar con ','.");
            String[] autores = inputAutores.split(",");
            int cantidadPaginas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de páginas del libro"));
            String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro: ");
            int anioEdicion = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de edición del libro"));
            String editorial = JOptionPane.showInputDialog("Ingrese la editorial del libro: ");
            String genero = JOptionPane.showInputDialog("Ingrese el género del libro: ");
            biblioteca.agregarLibro(new Libro(titulo, autores, cantidadPaginas, isbn, anioEdicion, editorial, genero));
            JOptionPane.showMessageDialog(null, "Libro agregado correctamente");
        });

        // ELIMINAR LIBRO
        eliminarLibroButton.addActionListener(e -> {
            String tituloAEliminar = JOptionPane.showInputDialog("Ingrese el título del libro a eliminar: ");
            biblioteca.eliminarLibro(tituloAEliminar);
            JOptionPane.showMessageDialog(null, "Libro eliminado correctamente");
        });

        // CONSULTAR POR TÍTULO
        consultarTituloButton.addActionListener(e -> {
            String tituloAConsultar = JOptionPane.showInputDialog("Ingrese el título del libro a consultar:");
            resultadoTextArea.setText(biblioteca.consultarPorTitulo(tituloAConsultar));
        });

        // MODIFICAR LIBRO
        modificarLibroButton.addActionListener(e -> {
            String tituloAModificar = JOptionPane.showInputDialog("Ingrese el título del libro a modificar: ");
            String tituloModificado = JOptionPane.showInputDialog("Ingrese el nuevo título del libro: ");
            String inputAutoresModificado = JOptionPane.showInputDialog("Ingresar el nuevo apellido del autor. Si hay más autores, separar con ','.");
            String[] autoresModificados = inputAutoresModificado.split(",");
            int cantidadPaginasModificado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de páginas del libro."));
            String isbnModificado = JOptionPane.showInputDialog("Ingrese el nuevo ISBN del libro: ");
            int anioEdicionModificado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el nuevo año de edición del libro"));
            String editorialModificado = JOptionPane.showInputDialog("Ingrese la nueva editorial del libro: ");
            String generoModificado = JOptionPane.showInputDialog("Ingrese el nuevo género del libro: ");
            biblioteca.modificarLibro(tituloAModificar, new Libro(tituloModificado, autoresModificados, cantidadPaginasModificado, isbnModificado, anioEdicionModificado, editorialModificado, generoModificado));
            JOptionPane.showMessageDialog(null, "Libro modificado correctamente");
        });

        // LISTAR AUTORES
        listarAutoresButton.addActionListener(e -> resultadoTextArea.setText(biblioteca.listarAutores()));

        // LISTAR LIBROS
        listarLibrosButton.addActionListener(e -> resultadoTextArea.setText(biblioteca.listarLibros()));

        // LISTAR LIBROS POR GÉNERO
        listarPorGeneroButton.addActionListener(e -> {
            String generoAListar = JOptionPane.showInputDialog("Ingrese el género para listar: ");
            resultadoTextArea.setText(biblioteca.listarLibrosPorGenero(generoAListar));
        });

        // LISTAR LIBROS POR AUTOR
        listarPorAutorButton.addActionListener(e -> {
            String autorAListar = JOptionPane.showInputDialog("Ingrese el autor para listar: ");
            resultadoTextArea.setText(biblioteca.listarLibrosPorAutor(autorAListar));
        });

        // LISTAR LIBROS POR EDITORIAL
        listarPorEditorialButton.addActionListener(e -> {
            String editorialAListar = JOptionPane.showInputDialog("Ingrese la editorial para listar: ");
            resultadoTextArea.setText(biblioteca.listarLibrosPorEditorial(editorialAListar));
        });

        // LISTAR LIBROS POR EDITORIAL POR RANGO DE AÑOS
        listarPorEditorialRangoButton.addActionListener(e -> {
            String editorialAListarPorRango = JOptionPane.showInputDialog("Ingrese la editorial para listar entre años: ");
            int anioInicio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de inicio: "));
            int anioFin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de finalización: "));
            resultadoTextArea.setText(biblioteca.listarLibrosPorEditorialEnRangoDeAnios(editorialAListarPorRango, anioInicio, anioFin));
        });

        // LISTAR AUTORES POR EDITORIAL
        listarAutoresEditorialButton.addActionListener(e -> {
            String editorialParaListarAutores = JOptionPane.showInputDialog("Ingresar la editorial para listar autores: ");
            resultadoTextArea.setText(biblioteca.listarAutoresPorEditorial(editorialParaListarAutores));
        });

        // LISTAR LIBROS POR AÑO
        listarPorAnioButton.addActionListener(e -> {
            int anioAListar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año para listar: "));
            resultadoTextArea.setText(biblioteca.listarLibrosPorAnio(anioAListar));
        });

        // LISTAR LIBROS POR LA PRIMERA LETRA DEL APELLIDO DEL AUTOR
        listarPorLetraApellidoButton.addActionListener(e -> {
            String apellidoParaChar = JOptionPane.showInputDialog("Ingresar la primera letra del apellido para listar: ");
            char primeraLetra = apellidoParaChar.charAt(0);
            resultadoTextArea.setText(biblioteca.listarLibrosPorLetraApellidoAutores(primeraLetra));
        });

        // LISTAR LIBROS QUE INCLUYAN LA PALABRA INGRESADA EN EL TÍTULO
        listarPorPalabraTituloButton.addActionListener(e -> {
            String palabra = JOptionPane.showInputDialog("Ingresar la palabra que debe contener el título para listar: ");
            resultadoTextArea.setText(biblioteca.listarLibrosPorPalabraTitulo(palabra));
        });

        // BUTTON DE SALIR
        salirButton.addActionListener(e -> System.exit(0));
    }
}
