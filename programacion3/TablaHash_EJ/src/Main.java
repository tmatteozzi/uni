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
        // CREAR TABLA SIN HASH (10 filas, 6 columnas)
        String[][] tablaSinHash = new String[10][6];
        // CARGAR TABLA
        System.out.println("Ingrese los datos para la tabla sin Hash:");
        cargarTabla(scanner, tablaSinHash);

        // CREAR TABLA CON HASH (10 filas, 10 columnas (4 columnas adicionales - Una por cada metodo hash))
        String[][] tablaConHash = new String[tablaSinHash.length][10];
        // INSERTAR VALORES HASH DE LA PRIMERA TABLA EN LA SEGUNDA
        calcularHash(tablaSinHash, tablaConHash);

        // IMPRIMIR TABLAS
        System.out.println("TABLA SIN HASH");
        imprimirTabla(tablaSinHash);
        System.out.println("TABLA CON HASH");
        imprimirTabla(tablaConHash);
    }

    // METODO PARA CARGAR ELEMENTOS DE LA TABLA
    public static void cargarTabla(Scanner scanner, String[][] tabla) {
        // CARGAR DATOS POR CADA FILA EN LA TABLA
        for (int i = 0; i < tabla.length; i++) {
            System.out.println("PERSONA " + (i + 1) + ":");
            System.out.print("DNI: ");
            tabla[i][0] = scanner.nextLine();
            System.out.print("NOMBRE: ");
            tabla[i][1] = scanner.nextLine();
            System.out.print("APELLIDO: ");
            tabla[i][2] = scanner.nextLine();
            System.out.print("MAIL: ");
            tabla[i][3] = scanner.nextLine();
            System.out.print("CELULAR: ");
            tabla[i][4] = scanner.nextLine();
            System.out.print("DIRECCION: ");
            tabla[i][5] = scanner.nextLine();
            System.out.println();
        }
    }

    // METODO PARA CALCULAR LOS 4 TIPOS DE HASH
    public static void calcularHash(String[][] tablaSinHash, String[][] tablaConHash) {
        for (int i = 0; i < tablaSinHash.length; i++) {
            // EXTRAER EL DNI DE CADA PERSONA
            String dni = tablaSinHash[i][0];
            // CALCULAR LOS DISTINTOS TIPOS DE HASH
            String hash1 = aritModular(dni, tablaSinHash.length);
            String hash2 = plegamiento(dni, tablaSinHash.length);
            String hash3 = mitadDelCuadrado(dni, tablaSinHash.length);
            String hash4 = multiplicacion(dni, tablaSinHash.length);
            // INSERTAR LOS VALORES A LA TABLA (PRIMERO EL DNI Y DSPUES LOS HASH)
            tablaConHash[i][0] = dni;
            tablaConHash[i][1] = hash1;
            tablaConHash[i][2] = hash2;
            tablaConHash[i][3] = hash3;
            tablaConHash[i][4] = hash4;
            // PONER EL RESTO DE DATOS A LA TABLA
            for (int j = 1; j < 6; j++) {
                tablaConHash[i][j + 4] = tablaSinHash[i][j];
            }
        }
    }

    // METODO ARITMETICA MODULAR
    public static String aritModular(String dni, int sizeTabla) {
        int dnint = Integer.parseInt(dni);
        int posicion = dnint % sizeTabla;
        return String.valueOf(posicion);
    }

    // METODO PLEGAMIENTO
    public static String plegamiento(String dni, int sizeTabla) {
        String auxClave = dni;
        String auxTamTab = String.valueOf(sizeTabla - 1);
        int posicion = 0;
        String parte = "";
        for(int i = 0; i < auxClave.length(); i++){
            parte += auxClave.charAt(i);
            if(parte.length() == auxTamTab.length()){
                posicion += Integer.parseInt(parte);
                parte = "";
            } else if (i == auxClave.length() - 1) {
                posicion += Integer.parseInt(parte);
            }
        }
        return String.valueOf(posicion);
    }

    // METODO MITAD DEL CUADRADO
    public static String mitadDelCuadrado(String dni, int sizeTabla) {
        String auxClave = String.valueOf((long) Math.pow(Integer.parseInt(dni), 2));
        String auxTamTab = String.valueOf(sizeTabla - 1);
        int inicio = (auxClave.length()/2) - 1;
        String posicion = "";
        for(int i=inicio; i < inicio + auxTamTab.length(); i++){
            posicion += auxClave.charAt(i);
        }
        return posicion;
    }

    // METODO DE MULTIPLICACION
    public static String multiplicacion(String dni, int sizeTabla) {
        int dnint = Integer.parseInt(dni);
        double A = 0.61680339887;
        double d = A * dnint - Math.floor(A * dnint);
        return String.valueOf(Math.round(sizeTabla * d));
    }

    // IMPRIMIR LA TABLA
    public static void imprimirTabla(String[][] tabla) {
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.print(tabla[i][j] + "|\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}
