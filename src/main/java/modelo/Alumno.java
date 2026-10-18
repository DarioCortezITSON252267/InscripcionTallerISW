package modelo;

/**
 * Entidad que representa a un alumno que puede inscribirse a los talleres.
 */
public class Alumno {

    private final String id;
    private final String nombre;
    private final int semestre;
    private final String programa;
    private final String correo;

    public Alumno(String id, String nombre, int semestre, String programa, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.semestre = semestre;
        this.programa = programa;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSemestre() {
        return semestre;
    }

    public String getPrograma() {
        return programa;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
