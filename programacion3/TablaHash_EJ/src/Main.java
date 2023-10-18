/***
Desarrollar un Producto de Software que cumpla con las siguientes características.
    -Construir una Tabla (Array) de tamaño 10 en Java que contenga las siguientes columnas: (DNI, Apellidos, Nombres, Email, Celular, Dirección).
    -Cargue dicha tabla con datos validos y verdaderos. Se pueden utilizar los datos asociados con los compañeros del equipo de clases (Prog. III), familiares y/o conocidos.
    -Los 10 registros deben ser introducidos en la tabla desde el teclado con el uso de una interfaz gráfica (IHM).
    -Después de cargados los 10 registros, proceder a cargar una segunda Tabla, similar a la primera, pero con 4 columnas adicionales que se intercalaran entre la columna “DNI” y la columna “Apellidos”.
    -Las cuatro columnas adicionadas serán el resultado de haber calculado las 4 funciones Hash estudiadas en esta clase.
    -La columna clave a ser tomada en cuenta debe ser la del DNI.
    -Al final imprima en forma tabular las 2 Tablas, primero la tabla sin Hash y después la tabla con Hash.
*/

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // INICIALIZACION DE SCANNER
        Scanner scanner = new Scanner(System.in);
        Tabla t1 = null, t2 = null;
        int opcionInicial;
        do {
            System.out.println("Ingrese una opcion: \n" +
                    "1. Tabla 1 \n" +
                    "2. Tabla 2 \n" +
                    "3. Salir");
            opcionInicial = scanner.nextInt();
            switch (opcionInicial) {
                case 1:
                    // OPCION MENU PRIMER CONJUNTO
                    int opcionT1;
                    // VALIDADOR
                    boolean creado1 = false;
                    do {
                        imprimirMenu();
                        opcionT1 = scanner.nextInt();
                        switch (opcionT1) {
                            case 1: // CREAR CONJUNTO
                                if (!creado1) {
                                    System.out.println("INGRESAR EL SIZE DE LA TABLA: ");
                                    int sizeT1 = scanner.nextInt();
                                    t1 = new Tabla(sizeT1);
                                    creado1 = true;
                                } else {
                                    System.out.println("CONJUNTO YA CREADO!");
                                }
                                break;
                            case 2: // INGRESAR ELEMENTO A TABLA
                                System.out.println("INGRESAR ELEMENTO A AÑADIR: ");
                                System.out.println("NOMBRE: ");
                                String nombre = scanner.next();
                                System.out.println("APELLIDO: ");
                                String apellido = scanner.next();
                                System.out.println("DNI: ");
                                int dni = scanner.nextInt();
                                System.out.println("EMAIL: ");
                                String email = scanner.next();
                                System.out.println("CELULAR: ");
                                String celular = scanner.next();
                                System.out.println("DIRECCION: ");
                                String direccion = scanner.next();
                                Elemento e = new Elemento(nombre, apellido, dni, email, celular, direccion);
                                System.out.println(t1.hash(e));
                                t1.agregar(e, t1.hash(e));
                                break;
                            case 3: // IMPRIMIR TABLA
                                System.out.println(t1);
                                break;
                            case 4: // SALIR AL MENU PRINCIPAL
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("OPCION INVALIDA");
                                break;
                        }
                    } while (opcionT1 != 4);
                    break;
                case 2:
                    // OPCION MENU PRIMER CONJUNTO
                    int opcionT2;
                    // VALIDADOR
                    boolean creado2 = false;
                    do {
                        imprimirMenu();
                        opcionT2 = scanner.nextInt();
                        switch (opcionT2) {
                            case 1: // CREAR CONJUNTO
                                if (!creado2) {
                                    System.out.println("INGRESAR EL SIZE DE LA TABLA: ");
                                    int sizeT2 = scanner.nextInt();
                                    t2 = new Tabla(sizeT2);
                                    creado2 = true;
                                } else {
                                    System.out.println("CONJUNTO YA CREADO!");
                                }
                                break;
                            case 2: // INGRESAR ELEMENTO A TABLA
                                System.out.println("INGRESAR ELEMENTO A AÑADIR: ");
                                System.out.println("NOMBRE: ");
                                String nombre = scanner.next();
                                System.out.println("APELLIDO: ");
                                String apellido = scanner.next();
                                System.out.println("DNI: ");
                                int dni = scanner.nextInt();
                                System.out.println("EMAIL: ");
                                String email = scanner.next();
                                System.out.println("CELULAR: ");
                                String celular = scanner.next();
                                System.out.println("DIRECCION: ");
                                String direccion = scanner.next();
                                Elemento e = new Elemento(nombre, apellido, dni, email, celular, direccion);
                                t2.agregar(e, t2.hash(e));
                                break;
                            case 3: // IMPRIMIR TABLA
                                System.out.println(t2);
                                break;
                            case 4: // SALIR AL MENU PRINCIPAL
                                System.out.println("DE VUELTA AL MENU PRINCIPAL");
                                break;
                            default:
                                System.out.println("OPCION INVALIDA");
                                break;
                        }
                    } while (opcionT2 != 4);
                    break;
                case 3:
                    System.out.println("FIN DEL PROGRAMA");
                    break;
                default:
                    System.out.println("OPCION INVALIDA");
                    break;
            }
        } while (opcionInicial != 3);
        scanner.close();
    }

    public static void imprimirMenu(){
        System.out.println("Ingrese una opcion: \n" +
                "1. Crear tabla \n" +
                "2. Agregar elemento \n" +
                "3. Imprimir tabla \n" +
                "4. Salir");
    }
}

