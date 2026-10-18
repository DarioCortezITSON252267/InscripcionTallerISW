package modelo;

/**
 * Excepción de negocio que se lanza cuando una consulta o una inscripción no
 * puede realizarse (alumno inexistente, taller sin cupo, inscripción
 * duplicada, etc.).
 */
public class InscripcionException extends Exception {

    public InscripcionException(String mensaje) {
        super(mensaje);
    }
}
