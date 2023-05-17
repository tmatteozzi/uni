import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Formulario formulario = new Formulario();
        formulario.setBounds(0,0,500,200);
        formulario.setResizable(false);
        formulario.setVisible(true);
        formulario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}