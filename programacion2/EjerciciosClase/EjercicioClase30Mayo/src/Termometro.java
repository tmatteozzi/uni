public class Termometro {
    double tempGrados, tempKelvin;

    public Termometro(double tempGrados){
        try {
            this.tempGrados = tempGrados;
            this.tempKelvin = tempGrados + 273.15;
            if (tempKelvin < 0){
                throw new Exception("No existen temperaturas debajo de los 0 °K");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString(){
        return "Celcius: " + tempGrados + "°C\n" + "Kelvin: " + tempKelvin + "°K";
    }
}
