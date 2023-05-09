package superior;

import hijos.Cd;

import java.util.ArrayList;

public abstract class Coleccion {
    // CREAR ARRAY LIST PARA LA COLECCIÓN
    protected ArrayList<Disco> coleccion;

    // CONSTRUCTOR
    public Coleccion() {
        this.coleccion = new ArrayList <> ();
    }

    // MÉTODOS GENERALES DE LA COLECCIÓN

    public abstract void agregarDisco();
    public void eliminarDisco(String titulo) {
        // Booleano para indicar si ya fue eliminado (Empieza en falso)
        boolean eliminado = false;
        for (int i = 0; i < coleccion.size(); i++) {
            Disco d = coleccion.get(i);
            if (d.getTitulo().equalsIgnoreCase(titulo)) {
                coleccion.remove(i);
                // Si lo encontró cambiar el booleano a true y salir del for
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            System.out.println("Eliminado correctamente.");
        } else {
            System.out.println("No se encontró ningún DVD o CD con el título ingresado.");
        }
    }
    public abstract void modificarDisco(String tituloAModificar);
    public void listarDiscos() {
        try{
            checkParaExcepcionListaVacia();
            for (int i = 0; i < coleccion.size(); i++) {
                Disco d = coleccion.get(i);
                System.out.println(d);
            }
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage() + "\n");
        }
    }
    public void listarDiscosEnPosesion() {
        try {
            checkParaExcepcionListaVacia();
            for (int i = 0; i < coleccion.size(); i++) {
                Disco d = coleccion.get(i);
                if (d.isPosesion() == true) {
                    System.out.println(d);
                }
            }
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage() + "\n");
        }
    }
    public void listarDiscosPorTiempo(int tiempo) {
        try{
            checkParaExcepcionListaVacia();
            for (int i = 0; i< coleccion.size(); i++) {
                Disco d = coleccion.get(i);
                if (d.getDuracion() < tiempo) {
                    System.out.println(d);
                }
            }
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage() + "\n");
        }
    }
    public void listarDiscosOrdenados() {
        try{
            checkParaExcepcionListaVacia();
            // Copiar la colección para no modificar el orden original
            ArrayList<Disco> copiaColeccion = new ArrayList<>(coleccion);
            int n = copiaColeccion.size();
            // Ordenar la lista
            boolean intercambio = true;
            while (intercambio) {
                // Comenzar con intercambio = false y cambiar a true si se realiza un intercambio
                intercambio = false;
                // Iterar comparando 2 elementos seguidos
                for (int i = 0; i < n - 1; i++) {
                    // Obtener los elementos
                    Disco actual = copiaColeccion.get(i);
                    Disco siguiente = copiaColeccion.get(i + 1);
                    // Si el título del elemento actual es mayor que el título del siguiente elemento (ignorando mayúsculas y minúsculas)
                    if (actual.getTitulo().compareToIgnoreCase(siguiente.getTitulo()) > 0) {
                        // Intercambiar los elementos
                        copiaColeccion.set(i, siguiente);
                        copiaColeccion.set(i + 1, actual);
                        // Establecer intercambio = true para indicar que se ha realizado un intercambio
                        intercambio = true;
                    }
                }
                // Reducir el tamaño de la lista en una unidad en cada iteración (Para no comparar al elemento más grande que se fue al final de la lista)
                n--;
            }
            // Imprimir la lista ordenada
            System.out.println("Lista de DVDs ordenados por título:");
            for (Disco d : copiaColeccion) {
                // Para cada DVD en la copia ordenada de la colección:
                // Imprimir el DVD en la consola
                System.out.println(d);
            }
            // Imprimir una línea en blanco para separar la salida de la lista de DVDs de otros mensajes
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage() + "\n");
        }
    }
    public int obtenerTotal() {
        return coleccion.size();
    }
    public int discosEnPosesion() {
        // Crear una nuevo array list donde se van a agregar todos los dvds que posea
        ArrayList<Disco> discosEnPosesion = new ArrayList<>();
        // Fijarse en la coleccion cuales poseo
        for (int i = 0; i< coleccion.size(); i++) {
            Disco d = coleccion.get(i);
            // Si lo poseo agregar a el array list
            if (d.isPosesion() == true) {
                discosEnPosesion.add(d);
            }
        }
        return discosEnPosesion.size();
    }

    // MÉTODOS PROPIOS DE CDs
    public int obtenerCantidadCd(String titulo){
        // Inicializar variable en 0 para luego asignarle la cantidad de temas si se encuentra
        int cantTemas = 0;
        for (int i = 0; i < coleccion.size(); i++) {
            Cd cd = (Cd) coleccion.get(i);
            if (cd.getTitulo().equalsIgnoreCase(titulo)) {
                cantTemas = cd.getCantTemas();
            }
        }
        return cantTemas;
    }

    // MÉTODOS AUXILIARES
    public static void imprimirMenuOpciones() {
        System.out.println("1. Ingresar información sobre un disco");
        System.out.println("2. Eliminar un disco conociendo el título");
        System.out.println("3. Modificar los valores de uno o más atributos del disco conociendo el título");
        System.out.println("4. Listar todos los discos");
        System.out.println("5. Listar los discos que tengo");
        System.out.println("6. Listar los discos que duran menos de un tiempo dado en minutos");
        System.out.println("7. Listar todos los discos ordenados por título");
        System.out.println("8. Informar la cantidad total de discos");
        System.out.println("9. Informar la cantidad de discos que tengo");
        System.out.println("10. Listar los DVD's de un determinado director (EXCLUSIVO DVD)");
        System.out.println("11. Listar los CD's de un determinado interprete (EXCLUSIVO CD)");
        System.out.println("12. Informar la cantidad de temas de un CD según el título (EXCLUSIVO CD)");
        System.out.println("13. Salir");
    }
    public void checkParaExcepcionListaVacia() throws Exception {
        if (coleccion.size() == 0){
            throw new Exception("La lista se encuentra vacia.");
        }
    }
}