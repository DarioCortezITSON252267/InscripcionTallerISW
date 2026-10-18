package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Entidad que representa un taller ofertado en la Semana de Ingeniería en
 * Software (ISW).
 */
public class Taller {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"));
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final String clave;
    private final String nombre;
    private final String instructor;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final String lugar;
    private final String descripcion;
    private final int cupoMaximo;
    private int inscritos;

    public Taller(String clave, String nombre, String instructor, LocalDate fecha,
            LocalTime horaInicio, LocalTime horaFin, String lugar, String descripcion,
            int cupoMaximo, int inscritos) {
        this.clave = clave;
        this.nombre = nombre;
        this.instructor = instructor;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.cupoMaximo = cupoMaximo;
        this.inscritos = inscritos;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public String getInstructor() {
        return instructor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public int getInscritos() {
        return inscritos;
    }

    /**
     * @return lugares que aún quedan libres en el taller.
     */
    public int getCupoDisponible() {
        return cupoMaximo - inscritos;
    }

    /**
     * @return fecha del taller en formato largo, por ejemplo
     * "lunes 9 de noviembre de 2026".
     */
    public String getFechaTexto() {
        return fecha.format(FORMATO_FECHA);
    }

    /**
     * @return horario del taller, por ejemplo "09:00 - 11:00 hrs".
     */
    public String getHorario() {
        return horaInicio.format(FORMATO_HORA) + " - " + horaFin.format(FORMATO_HORA) + " hrs";
    }

    /**
     * @return true si el taller todavía tiene lugares disponibles.
     */
    public boolean hayCupo() {
        return getCupoDisponible() > 0;
    }

    /**
     * Ocupa un lugar del taller al registrar una nueva inscripción.
     */
    public void registrarInscrito() {
        if (!hayCupo()) {
            throw new IllegalStateException("El taller " + clave + " no tiene cupo disponible.");
        }
        inscritos++;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
