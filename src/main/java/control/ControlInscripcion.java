package control;

import modelo.Alumno;
import modelo.Inscripcion;
import modelo.InscripcionException;
import modelo.ModeloInscripcion;
import modelo.Taller;
import vista.FrmInscripcionTaller;

/**
 * Control del caso de uso "Inscribirse en Taller de Semana de
 * ISW". Recibe las peticiones de la vista, las resuelve con el modelo y le
 * indica a la vista qué debe mostrar.
 */
public class ControlInscripcion {

    private final ModeloInscripcion modelo;
    private final FrmInscripcionTaller vista;
    private Taller tallerSeleccionado;
    private Alumno alumnoConsultado;

    public ControlInscripcion(ModeloInscripcion modelo, FrmInscripcionTaller vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vista.setControl(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    public void cargarTalleres() {
        vista.mostrarTalleres(modelo.obtenerTalleres());
    }

    public void seleccionarTaller(String clave) {
        try {
            tallerSeleccionado = modelo.buscarTaller(clave);
            alumnoConsultado = null;
            vista.mostrarDetalleTaller(tallerSeleccionado);
        } catch (InscripcionException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void solicitarInscripcion() {
        if (tallerSeleccionado == null) {
            vista.mostrarError("Selecciona primero un taller de la lista.");
            return;
        }
        vista.solicitarIdAlumno();
    }

    public void consultarAlumno(String id) {
        try {
            alumnoConsultado = modelo.buscarAlumno(id);
            vista.mostrarDatosAlumno(alumnoConsultado);
        } catch (InscripcionException ex) {
            alumnoConsultado = null;
            vista.mostrarError(ex.getMessage());
        }
    }


    public void confirmarInscripcion() {
        try {
            Inscripcion inscripcion = modelo.inscribir(alumnoConsultado, tallerSeleccionado);
            alumnoConsultado = null;
            vista.mostrarTicket(inscripcion);
        } catch (InscripcionException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void reiniciarInscripcion() {
        alumnoConsultado = null;
        vista.limpiarZonaAlumno();
    }
}
