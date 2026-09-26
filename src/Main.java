import Actividades.*;
import Certificacion.Certificable;
import Excepciones.CupoExcedidoExcepcion;
import Hilos.EnvioTicketsThread;
import Modelos.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    UTN FRM - TRABAJO PRÁCTICO 2 - POO");
        System.out.println("==================================================\n");

        Estudiantes e1 = new Estudiantes("50123", "Álvaro Silva");
        Estudiantes e2 = new Estudiantes("50124", "María González");
        Estudiantes e3 = new Estudiantes("50125", "Lucas Rodríguez");

        Sala salaPrincipal = new Sala(1, "Aula Magna");

        EventoUniversitario evento = new EventoUniversitario("EVT-2026", "Congreso de Tecnología UTN", 4500.0, false);
        evento.asignarSala(salaPrincipal);

        Charla charla = new Charla(101, "Introducción a IA", 2, "Dr. Pérez");
        Taller taller = new Taller(102, "Taller de Git y GitHub", 1, true); // Cupo restringido a 1
        Curso curso = new Curso(103, "Curso Intensivo Java", 5, 20);

        evento.getActividades().add(charla);
        evento.getActividades().add(taller);
        evento.getActividades().add(curso);

        System.out.println("--- 1. MANEJO DE EXCEPCIONES (CUPO EXCEDIDO) ---");
        try {
            System.out.println("Inscribiendo a Álvaro en Taller (Cupo máximo: 1)...");
            Inscripcion ins1 = taller.inscribir(e1);
            ins1.confirmarInscripcion(); // Confirmamos para generar Ticket
            System.out.println("-> Inscripción exitosa.");

            System.out.println("Intentando inscribir a María en el mismo Taller...");
            Inscripcion ins2 = taller.inscribir(e2); // Esto lanzará CupoExcedidoExcepcion
        } catch (CupoExcedidoExcepcion e) {
            System.err.println("EXCEPCIÓN ATRAPADA: " + e.getMessage());
        } finally {
            System.out.println("Bloque try-catch-finally de inscripción finalizado.");
        }

        try {
            Inscripcion insCharla1 = charla.inscribir(e1);
            insCharla1.confirmarInscripcion();

            Inscripcion insCurso2 = curso.inscribir(e2);
            insCurso2.confirmarInscripcion();

            Inscripcion insCurso3 = curso.inscribir(e3); // Queda en estado 'Pendiente'
        } catch (CupoExcedidoExcepcion e) {
            System.err.println("Error de inscripción: " + e.getMessage());
        }

        System.out.println("\n--- 2. FILTRADO CON GENERICS Y CÁLCULO CON WILDCARDS ---");
        List<Curso> listaCursos = evento.filtrarActividadesPorTipo(Curso.class);
        List<Taller> listaTalleres = evento.filtrarActividadesPorTipo(Taller.class);

        System.out.println("Cantidad de cursos creados: " + listaCursos.size());
        System.out.println("Cantidad de talleres creados: " + listaTalleres.size());

        double costoMaterialesCursos = evento.calcularCostoMateriales(listaCursos);
        System.out.println("Costo total de materiales para Cursos: $" + costoMaterialesCursos);

        System.out.println("\n--- 3. EMISIÓN DE CERTIFICADOS (INTERFACES) ---");
        for (Actividad act : evento.getActividades()) {
            if (act instanceof Certificable) {
                Certificable certificable = (Certificable) act;
                for (Inscripcion ins : act.getInscripciones()) {
                    System.out.println(certificable.generarCertificado(ins.getEstudiante()));
                }
            } else {
                System.out.println("La actividad '" + act.getTitulo() + "' de tipo " + act.getTipo() + " no emite certificados.");
            }
        }

        System.out.println("\n--- 4. PERSISTENCIA DE OBJETOS ---");
        if (evento.persistirEvento()) {
            System.out.println("Evento guardado en disco correctamente.");
        }

        EventoUniversitario eventoRecuperado = EventoUniversitario.recuperarEvento("EVT-2026");
        if (eventoRecuperado != null) {
            System.out.println("Evento recuperado desde archivo: " + eventoRecuperado.getTitulo());
        }
        System.out.println("\n--- 5. ENVÍO CONCURRENTE DE TICKETS (HILOS) ---");
        EnvioTicketsThread hiloTickets = new EnvioTicketsThread(evento);
        hiloTickets.start(); // Inicia la ejecución en un hilo separado
        for (int i = 1; i <= 3; i++) {
            System.out.println("[HILO PRINCIPAL] Mostrando resumen/interfaz del sistema... (" + i + "/3)");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}