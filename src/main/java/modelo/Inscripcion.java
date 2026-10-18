package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad que registra la inscripción de un alumno en un taller. Sus datos son
 * los que se muestran en el ticket de inscripción.
 */
public class Inscripcion {

    private static final DateTimeFormatter FORMATO_REGISTRO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String folio;
    private final Alumno alumno;
    private final Taller taller;
    private final LocalDateTime fechaRegistro;

    public Inscripcion(String folio, Alumno alumno, Taller taller, LocalDateTime fechaRegistro) {
        this.folio = folio;
        this.alumno = alumno;
        this.taller = taller;
        this.fechaRegistro = fechaRegistro;
    }

    public String getFolio() {
        return folio;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public Taller getTaller() {
        return taller;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * @return fecha y hora de registro, por ejemplo "18/10/2026 10:35".
     */
    public String getFechaRegistroTexto() {
        return fechaRegistro.format(FORMATO_REGISTRO);
    }
}
