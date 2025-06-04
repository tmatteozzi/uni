import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // SE LANZA INTERFAZ GRÁFICA EN HILO DE EVENTOS DE SWING
        SwingUtilities.invokeLater(() -> new VentanaSimulador().setVisible(true));
    }
}

// GRUPO: MATTEOZZI, MARZORATI, MONZALVO, SANABRIA, CENTURION Y GODOY