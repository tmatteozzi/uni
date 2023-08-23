import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("INGRESAR LA PALABRA A CHEQUEAR");
        String palabraAChequear = scanner.next();
        Verificador verificador = new Verificador();
        if(verificador.esPalindromo(palabraAChequear)){
            System.out.println(palabraAChequear.toUpperCase() + " ES PALINDROMO");
        } else {
            System.out.println(palabraAChequear.toUpperCase() + " NO ES PALINDROMO");
        }
    }
}
