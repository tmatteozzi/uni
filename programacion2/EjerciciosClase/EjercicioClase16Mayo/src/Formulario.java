import javax.swing.*;
import java.awt.event.*;

public class Formulario extends JFrame implements ActionListener {
    private JTextField textField1, textField2;
    private JLabel label1, label2, label3;
    private JButton button1;
    public Formulario(){
        setLayout(null);
        // LABEL USUARIO
        label1 = new JLabel("Usuario: ");
        label1.setBounds(10,10,100,30);
        add(label1);
        // TEXTFIELD USUARIO
        textField1 = new JTextField();
        textField1.setBounds(120,10,150,20);
        add(textField1);
        // LABEL CONTRASEÑA
        label2 = new JLabel("Contraseña");
        label2.setBounds(10,40,100,30);
        add(label2);
        // TEXTFIELD CONTRASEÑA
        textField2 = new JTextField();
        textField2.setBounds(120,40,150,20);
        add(textField2);
        // BOTON
        button1 = new JButton("Ingresar");
        button1.setBounds(150,80,100,30);
        add(button1);
        button1.addActionListener(this);
        // IMPRIMIR CONTRASEÑA Y USUARIO
        label3 = new JLabel();
        label3.setBounds(10, 120, 250, 30);
        add(label3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            String usuario = textField1.getText();
            String contrasena = textField2.getText();
            label3.setText("El usuario es: " + usuario + ", y la contraseña: " + contrasena + ".");
        }
    }
}
