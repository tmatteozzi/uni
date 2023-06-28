import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private Biblioteca biblioteca;

    public Menu() {
        biblioteca = new Biblioteca();
        createGUI();
    }

    private void createGUI() {
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
        resultadoTextArea.setEditable(false);
        resultadoTextArea.setBounds(10, 200, 480, 300);

        // Agregar ActionListener para el botón "Agregar Libro"
        agregarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        // Agregar ActionListener para el botón "Eliminar Libro"
        eliminarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tituloAEliminar = JOptionPane.showInputDialog("Ingrese el título del libro a eliminar: ");
                biblioteca.eliminarLibro(tituloAEliminar);
                JOptionPane.showMessageDialog(null, "Libro eliminado correctamente");
            }
        });

        // Agregar ActionListener para el botón "Consultar por Título"
        consultarTituloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tituloAConsultar = JOptionPane.showInputDialog("Ingrese el título del libro a consultar:");
                biblioteca.consultarPorTitulo(tituloAConsultar);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Modificar Libro"
        modificarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        // Agregar ActionListener para el botón "Listar Autores"
        listarAutoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                biblioteca.listarAutores();
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
                resultadoTextArea.setText(biblioteca.listarAutores());
            }
        });

        // Agregar ActionListener para el botón "Listar Libros"
        listarLibrosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                biblioteca.listarLibros();
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Género"
        listarPorGeneroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String generoAListar = JOptionPane.showInputDialog("Ingrese el género para listar: ");
                biblioteca.listarLibrosPorGenero(generoAListar);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Autor"
        listarPorAutorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String autorAListar = JOptionPane.showInputDialog("Ingrese el autor para listar: ");
                biblioteca.listarLibrosPorAutor(autorAListar);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Editorial"
        listarPorEditorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String editorialAListar = JOptionPane.showInputDialog("Ingrese la editorial para listar: ");
                biblioteca.listarLibrosPorEditorial(editorialAListar);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Editorial en Rango de Años"
        listarPorEditorialRangoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String editorialAListarPorRango = JOptionPane.showInputDialog("Ingrese la editorial para listar entre años: ");
                int anioInicio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de inicio: "));
                int anioFin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de finalización: "));
                biblioteca.listarLibrosPorEditorialEnRangoDeAnios(editorialAListarPorRango, anioInicio, anioFin);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Autores por Editorial"
        listarAutoresEditorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String editorialParaListarAutores = JOptionPane.showInputDialog("Ingresar la editorial para listar autores: ");
                biblioteca.listarAutoresPorEditorial(editorialParaListarAutores);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Año"
        listarPorAnioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int anioAListar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año para listar: "));
                biblioteca.listarLibrosPorAnio(anioAListar);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Letra del Apellido de los Autores"
        listarPorLetraApellidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String apellidoParaChar = JOptionPane.showInputDialog("Ingresar la primera letra del apellido para listar: ");
                char primeraLetra = apellidoParaChar.charAt(0);
                biblioteca.listarLibrosPorLetraApellidoAutores(primeraLetra);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Listar Libros por Palabra en el Título"
        listarPorPalabraTituloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String palabra = JOptionPane.showInputDialog("Ingresar la palabra que debe contener el título para listar: ");
                biblioteca.listarLibrosPorPalabraTitulo(palabra);
                // Aquí puedes mostrar el resultado en un cuadro de diálogo o en un área de texto
            }
        });

        // Agregar ActionListener para el botón "Salir"
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

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
    }
}
