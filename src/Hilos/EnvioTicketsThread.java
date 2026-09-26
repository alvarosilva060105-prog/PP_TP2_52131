package Hilos;

import Modelos.EventoUniversitario;
import Actividades.Actividad;
import Modelos.Inscripcion;

public class EnvioTicketsThread extends Thread {
    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("\n>>> [HILO TICKETS] Iniciando envío concurrente de tickets...");
        for (Actividad act : evento.getActividades()) {
            for (Inscripcion ins : act.getInscripciones()) {
                if ("Confirmada".equals(ins.getEstado()) && ins.getTicket() != null) {
                    try {
                        Thread.sleep(1200); // Simulamos retardo de envío
                    } catch (InterruptedException e) {
                        System.err.println("Hilo interrumpido: " + e.getMessage());
                    }
                    ins.getTicket().enviarTicket();
                }
            }
        }
        System.out.println(">>> [HILO TICKETS] Envío finalizado exitosamente.\n");
    }
}