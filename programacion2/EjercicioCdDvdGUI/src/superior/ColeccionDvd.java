package superior;

import hijos.Dvd;

import javax.swing.*;
import java.util.Scanner;

public class ColeccionDvd extends Coleccion implements Listable{
    // INICIAR SCANNER PARA INPUT
    static Scanner teclado = new Scanner(System.in);

    // CONSTRUCTOR
    public ColeccionDvd(){
        super();
        Disco a = new Dvd("Piratas del Caribe", "Espen Sandverg", "Acción", 163,"Excelente", true);
        Disco b = new Dvd("El Padrino", "Francis Ford Coppola", "Drama", 175, "Obra maestra del cine", false);
        Disco c = new Dvd("Forrest Gump", "Robert Zemeckis", "Comedia dramática", 142, "Una historia conmovedora", false);
        Disco d = new Dvd("The Matrix", "The Wachowskis", "Ciencia ficción", 136, "Revolucionaria en su tiempo", true);
        Disco e = new Dvd("La La Land", "Damien Chazelle", "Musical romántico", 128, "Una carta de amor al cine musical", true);
        coleccion.add(a);
        coleccion.add(b);
        coleccion.add(c);
        coleccion.add(d);
        coleccion.add(e);
    }

    @Override
    public void agregarDisco() {
        String titulo = JOptionPane.showInputDialog("Ingrese el título del DVD: ");
        String director = JOptionPane.showInputDialog("Ingrese el nombre del director: ");
        String genero = JOptionPane.showInputDialog("Ingrese el género del DVD: ");
        int duracion = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la duración del DVD: "));
        String comentario = JOptionPane.showInputDialog("Ingrese un comentario: ");
        int resultado = JOptionPane.showConfirmDialog(null, "¿Usted posee el disco?", "Posesión", JOptionPane.YES_NO_OPTION);
        boolean posesion;
        if (resultado == JOptionPane.YES_OPTION) {
            posesion = true;
        } else {
            posesion = false;
        }

        coleccion.add(new Dvd(titulo, director, genero, duracion, comentario, posesion));
        System.out.println("DVD agregado correctamente.\n");
    }
    @Override
    public void modificarDisco(String tituloAModificar) {
        boolean modificado = false;

        for (int i = 0; i < coleccion.size(); i++) {
            Disco d = coleccion.get(i);
            if (d.getTitulo().equalsIgnoreCase(tituloAModificar)) {
                String titulo = JOptionPane.showInputDialog("Ingrese el título del DVD: ");
                String director = JOptionPane.showInputDialog("Ingrese el nombre del director: ");
                String genero = JOptionPane.showInputDialog("Ingrese el género del DVD: ");
                int duracion = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la duración del DVD: "));
                String comentario = JOptionPane.showInputDialog("Ingrese un comentario: ");
                int resultado = JOptionPane.showConfirmDialog(null, "¿Usted posee el disco?", "Posesión", JOptionPane.YES_NO_OPTION);
                boolean posesion;
                if (resultado == JOptionPane.YES_OPTION) {
                    posesion = true;
                } else {
                    posesion = false;
                }

                d.setTitulo(titulo);
                d.setGenero(genero);
                d.setDuracion(duracion);
                d.setPosesion(posesion);
                d.setComentario(comentario);
                ((Dvd) d).setDirector(director);

                modificado = true;
                break;
            }
        }
        if (modificado) {
            System.out.println("DVD modificado correctamente.");
        } else {
            System.out.println("No se encontró un DVD con el título ingresado.");
        }
    }
    @Override
    public void listarDiscoPorDirectorOInterprete(String directorOInterprete) {
        System.out.println("Lista de los DVD's que tengo del interprete '" + directorOInterprete + "'");
        for (int i = 0; i< coleccion.size(); i++) {
            Dvd dvd = (Dvd) coleccion.get(i);
            if (dvd.getDirector().equals(directorOInterprete)) {
                System.out.println(dvd);
            }
        }
        System.out.println();
    }
}