package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fachada del modelo. Concentra los datos de talleres, alumnos
 * e inscripciones y aplica las reglas de negocio del caso de uso "Inscribirse
 * en Taller de Semana de ISW". Los datos se cargan en memoria para simular la
 * base de datos de la escuela.
 */
public class ModeloInscripcion {

    private final List<Taller> talleres;
    private final List<Alumno> alumnos;
    private final List<Inscripcion> inscripciones;
    private int consecutivoFolio;

    public ModeloInscripcion() {
        talleres = new ArrayList<>();
        alumnos = new ArrayList<>();
        inscripciones = new ArrayList<>();
        consecutivoFolio = 0;
        cargarDatos();
    }

    public List<Taller> obtenerTalleres() {
        return Collections.unmodifiableList(talleres);
    }

    public Taller buscarTaller(String clave) throws InscripcionException {
        for (Taller taller : talleres) {
            if (taller.getClave().equals(clave)) {
                return taller;
            }
        }
        throw new InscripcionException("No existe un taller con la clave " + clave + ".");
    }

    public Alumno buscarAlumno(String id) throws InscripcionException {
        if (id == null || id.isBlank()) {
            throw new InscripcionException("Ingresa el ID del alumno.");
        }
        for (Alumno alumno : alumnos) {
            if (alumno.getId().equals(id.trim())) {
                return alumno;
            }
        }
        throw new InscripcionException("No se encontró ningún alumno con el ID " + id.trim() + ".");
    }

    public Inscripcion inscribir(Alumno alumno, Taller taller) throws InscripcionException {
        if (alumno == null || taller == null) {
            throw new InscripcionException("Selecciona un taller y consulta al alumno antes de confirmar.");
        }
        if (estaInscrito(alumno, taller)) {
            throw new InscripcionException(alumno.getNombre() + " ya está inscrito(a) en el taller \""
                    + taller.getNombre() + "\".");
        }
        if (!taller.hayCupo()) {
            throw new InscripcionException("El taller \"" + taller.getNombre() + "\" ya no tiene cupo disponible.");
        }
        Inscripcion inscripcion = new Inscripcion(generarFolio(), alumno, taller, LocalDateTime.now());
        taller.registrarInscrito();
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    private boolean estaInscrito(Alumno alumno, Taller taller) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getAlumno() == alumno && inscripcion.getTaller() == taller) {
                return true;
            }
        }
        return false;
    }

    private String generarFolio() {
        consecutivoFolio++;
        return String.format("SISW-%d-%04d", LocalDate.now().getYear(), consecutivoFolio);
    }


    private void cargarDatos() {
        talleres.add(new Taller("ISW-T01", "Desarrollo de APIs REST con Spring Boot",
                "Ing. Laura Méndez Ochoa", LocalDate.of(2026, 11, 9),
                LocalTime.of(9, 0), LocalTime.of(12, 0), "Laboratorio LV-301",
                "Construcción de un servicio web REST desde cero: controladores, "
                + "persistencia con JPA y pruebas con Postman.", 25, 18));
        talleres.add(new Taller("ISW-T02", "Pruebas automatizadas con JUnit 5 y Mockito",
                "Mtro. Carlos Ibarra Ruiz", LocalDate.of(2026, 11, 10),
                LocalTime.of(10, 0), LocalTime.of(13, 0), "Laboratorio LV-205",
                "Pruebas unitarias, dobles de prueba (mocks) y cobertura de código "
                + "aplicadas a un proyecto Java real.", 20, 12));
        talleres.add(new Taller("ISW-T03", "Introducción a Docker y contenedores",
                "Ing. Andrea Valenzuela Soto", LocalDate.of(2026, 11, 11),
                LocalTime.of(16, 0), LocalTime.of(19, 0), "Laboratorio LV-110",
                "Imágenes, contenedores, volúmenes y Docker Compose para levantar "
                + "una aplicación con base de datos.", 30, 30));
        talleres.add(new Taller("ISW-T04", "Diseño de interfaces con Figma",
                "Lic. Mariana Robles Félix", LocalDate.of(2026, 11, 12),
                LocalTime.of(9, 0), LocalTime.of(11, 30), "Aula AV-1305",
                "Prototipado de pantallas, componentes reutilizables y pruebas de "
                + "usabilidad con usuarios.", 35, 21));
        talleres.add(new Taller("ISW-T05", "Git y GitHub para trabajo en equipo",
                "Ing. Jesús Armenta Leyva", LocalDate.of(2026, 11, 13),
                LocalTime.of(12, 0), LocalTime.of(14, 0), "Laboratorio LV-301",
                "Ramas, pull requests, revisión de código y resolución de conflictos "
                + "en un repositorio compartido.", 25, 9));
        talleres.add(new Taller("ISW-T06", "Patrones de diseño aplicados en Java",
                "Dr. Ricardo Gaxiola Peña", LocalDate.of(2026, 11, 13),
                LocalTime.of(16, 0), LocalTime.of(19, 0), "Laboratorio LV-205",
                "MVC, Observer, Strategy y Factory implementados paso a paso en "
                + "una aplicación de escritorio.", 20, 19));

        alumnos.add(new Alumno("00000245731", "Ana Sofía Ramírez López", 5,
                "Ingeniería en Software", "ana.ramirez245731@potros.itson.edu.mx"));
        alumnos.add(new Alumno("00000251122", "Luis Fernando Castro Gil", 3,
                "Ingeniería en Software", "luis.castro251122@potros.itson.edu.mx"));
        alumnos.add(new Alumno("00000238410", "María José Valdez Torres", 7,
                "Ingeniería en Software", "maria.valdez238410@potros.itson.edu.mx"));
        alumnos.add(new Alumno("00000260057", "Diego Alonso Yocupicio Ruiz", 1,
                "Ingeniería en Software", "diego.yocupicio260057@potros.itson.edu.mx"));
        alumnos.add(new Alumno("00000229983", "Karla Patricia Félix Soto", 9,
                "Ingeniería en Ciencias Computacionales", "karla.felix229983@potros.itson.edu.mx"));
        alumnos.add(new Alumno("00000247765", "Jorge Iván Moreno Leyva", 5,
                "Ingeniería en Software", "jorge.moreno247765@potros.itson.edu.mx"));
    }
}
