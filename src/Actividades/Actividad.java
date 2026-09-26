package Actividades;

import Modelos.Estudiantes;
import Modelos.Inscripcion;
import Excepciones.CupoExcedidoExcepcion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {
    private int id;
    private String titulo;
    private int cupoMaximo;
    public static final int CUPO_MINIMO = 5;

    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Actividad(int id, String titulo, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getCupoMaximo() { return cupoMaximo; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }

    public Inscripcion inscribir(Estudiantes estudiante) throws CupoExcedidoExcepcion {
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoExcepcion("No hay cupo disponible en la actividad: " + titulo);
        }
        Inscripcion inscripcion = new Inscripcion(estudiante);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void mostrarInscripciones() {
        System.out.println("Inscripciones para: " + titulo);
        for (Inscripcion ins : inscripciones) {
            System.out.println("- Estudiante: " + ins.getEstudiante() + " | Estado: " + ins.getEstado());
        }
    }

    public final void mostrarIdentificacion() {
        System.out.println("ID: " + id + " - Actividad: " + titulo + " [" + getTipo() + "]");
    }

    public abstract double calcularCostoMateriales();
    public abstract String getTipo();
}