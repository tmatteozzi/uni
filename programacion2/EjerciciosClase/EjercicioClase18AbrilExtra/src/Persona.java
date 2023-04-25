public abstract class Persona {
    // Atributos
    protected String nombre;
    protected int edad, dni, peso;
    protected char sexo;
    protected double altura;
    // Constructor por defecto
    public Persona(){}
    // Constructor con nombre, edad y sexo
    public Persona(String nombre, int edad, char sexo){
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
    }
    // Constructor con todos los atributos
    public Persona(String nombre, int edad, int dni, int peso, double altura, char sexo) {
        this.nombre = nombre;
        this.edad = edad;
        this.dni = dni;
        this.peso = peso;
        this.altura = altura;
        this.sexo = sexo;
    }
    // Getters & Setters
    public char getSexo() {
        return sexo;
    }

    // Método abstracto indice masa corporal
    public abstract void calcularIMC();
    public boolean esMayorDeEdad() {
        if (edad < 18){
            System.out.println("No es mayor de edad");
        } else {
            System.out.println("Es mayor de edad");
        }
        return false;
    }
    public char comprobarSexo(char sexo) {
        if (sexo == 'h' || sexo == 'm') {
            System.out.println("El sexo es correcto");
        } else {
            sexo = 'h';
            System.out.println("El sexo es correcto");
        }
        return sexo;
    }
}
