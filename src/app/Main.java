package app;

import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación SpeedFast CRUD.
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                    new VentanaPrincipal();

            ventana.setVisible(true);
        });
    }
}