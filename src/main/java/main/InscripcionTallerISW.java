package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import control.ControlInscripcion;
import modelo.ModeloInscripcion;
import vista.FrmInscripcionTaller;

/**
 * Punto de entrada. Crea el modelo, la vista y el control (MVC) y arranca el
 * caso de uso "Inscribirse en Taller de Semana de ISW".
 */
public class InscripcionTallerISW {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
           
        }
        SwingUtilities.invokeLater(() -> {
            ModeloInscripcion modelo = new ModeloInscripcion();
            FrmInscripcionTaller vista = new FrmInscripcionTaller();
            ControlInscripcion control = new ControlInscripcion(modelo, vista);
            control.iniciar();
        });
    }
}
