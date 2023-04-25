public class Main {
    public static void main(String[] args) {
        // ANIMAL
        Animal animal = new Perro("Olivia");
        // RELOJ
        Reloj reloj1 = new Despertador("Desp");
        Reloj reloj2 = new Cucu("Cuc");
        // RUIDOSOS
        Ruidosos Vec [] = {animal, reloj1, reloj2};
        for (int i = 0; i < Vec.length; i++) {
            Vec[i].hablar();
        }
    }
}
