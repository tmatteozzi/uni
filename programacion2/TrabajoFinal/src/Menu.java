import javax.swing.*;
import java.util.Scanner;

public class Menu {
    public Menu() {
        nuevoMenu();
    }
    public void nuevoMenu() {
        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        boolean salir = false;
        while (!salir){
            Biblioteca.imprimirMenu();
            int opcion = scanner.nextInt();
            switch (opcion){
                case 1:
                    String titulo = JOptionPane.showInputDialog("Ingrese el título del libro: ");
                    String inputAutores = JOptionPane.showInputDialog("Ingresar el nombre del autor. Si hay más autores, separar con ','.");
                    String[] autores = inputAutores.split(",");
                    int cantidadPaginas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de páginas del libro"));
                    String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro: ");
                    int anioEdicion = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de edición del libro"));
                    String editorial = JOptionPane.showInputDialog("Ingrese la editorial del libro: ");
                    String genero = JOptionPane.showInputDialog("Ingrese el género del libro: ");
                    biblioteca.agregarLibro(new Libro(titulo, autores, cantidadPaginas, isbn, anioEdicion, editorial, genero));
                    break;
                case 2:
                    String tituloAEliminar = JOptionPane.showInputDialog("Ingrese el título del libro a eliminar: ");
                    biblioteca.eliminarLibro(tituloAEliminar);
                    break;
                case 3:
                    String tituloAConsultar = JOptionPane.showInputDialog("Ingrese el título del libro a consultar: ");
                    biblioteca.consultarPorTitulo(tituloAConsultar);
                    break;
                case 4:
                    String tituloAModificar = JOptionPane.showInputDialog("Ingrese el título del libro a modificar: ");
                    String tituloModificado = JOptionPane.showInputDialog("Ingrese el nuevo título del libro: ");
                    String inputAutoresModificado = JOptionPane.showInputDialog("Ingresar el nuevo nombre del autor. Si hay más autores, separar con ','.");
                    String[] autoresModificados = inputAutoresModificado.split(",");
                    int cantidadPaginasModificado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de páginas del libro."));
                    String isbnModificado = JOptionPane.showInputDialog("Ingrese el nuevo ISBN del libro: ");
                    int anioEdicionModificado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el nuevo año de edición del libro"));
                    String editorialModificado = JOptionPane.showInputDialog("Ingrese la nueva editorial del libro: ");
                    String generoModificado = JOptionPane.showInputDialog("Ingrese el nuevo género del libro: ");
                    biblioteca.modificarLibro(tituloAModificar,new Libro(tituloModificado, autoresModificados, cantidadPaginasModificado, isbnModificado, anioEdicionModificado, editorialModificado, generoModificado));
                    break;
                case 5:
                    biblioteca.listarAutores();
                    break;
                case 6:
                    biblioteca.listarLibros();
                    break;
                case 7:
                    String generoAListar = JOptionPane.showInputDialog("Ingrese el género para listar: ");
                    biblioteca.listarLibrosPorGenero(generoAListar);
                    break;
                case 8:
                    String autorAListar = JOptionPane.showInputDialog("Ingrese el autor para listar: ");
                    biblioteca.listarLibrosPorAutor(autorAListar);
                    break;
                case 9:
                    String editorialAListar = JOptionPane.showInputDialog("Ingrese la editorial para listar: ");
                    biblioteca.listarLibrosPorEditorial(editorialAListar);
                    break;
                case 10:
                    String editorialAListarPorRango = JOptionPane.showInputDialog("Ingrese la editorial para listar entre años: ");
                    int anioInicio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de inicio: "));
                    int anioFin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año de finalización: "));
                    biblioteca.listarLibrosPorEditorialEnRangoDeAnios(editorialAListarPorRango, anioInicio, anioFin);
                    break;
                case 11:
                    String editorialParaListarAutores = JOptionPane.showInputDialog("Ingresar le editorial para listar autores: ");
                    biblioteca.listarAutoresPorEditorial(editorialParaListarAutores);
                    break;
                case 12:
                    int anioAListar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año para listar: "));
                    biblioteca.listarLibrosPorAnio(anioAListar);
                    break;
                case 13:
                    String apellidoParaChar = JOptionPane.showInputDialog("Ingresar la primera letra del apellido para listar: ");
                    char primeraLetra = apellidoParaChar.charAt(0);
                    biblioteca.listarLibrosPorLetraApellidoAutores(primeraLetra);
                    break;
                case 14:
                    String palabra = JOptionPane.showInputDialog("Ingresar la palabra que debe contener el título para listar: ");
                    biblioteca.listarLibrosPorPalabraTitulo(palabra);
                    break;
                case 15:
                    salir = true;
                    break;
            }
        }
    }
}
