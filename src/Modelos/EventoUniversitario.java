package Modelos;

import Actividades.Actividad;
import Excepciones.CupoExcedidoExcepcion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoUniversitario implements Serializable {
    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos = 0;

    private Sala sala;
    private List<Actividad> actividades;

    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;
        this.actividades = new ArrayList<>();
        cantidadEventos++;
    }

    public EventoUniversitario(EventoUniversitario otro) {
        this.id = otro.id;
        this.titulo = otro.titulo;
        this.costoBase = otro.costoBase;
        this.gratuito = otro.gratuito;
        this.sala = otro.sala;
        this.actividades = new ArrayList<>(otro.actividades);
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public List<Actividad> getActividades() { return actividades; }
    public static int getCantidadEventos() { return cantidadEventos; }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    public double calcularCostoEstimado() {
        return gratuito ? 0 : costoBase;
    }



    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> filtradas = new ArrayList<>();
        for (Actividad act : actividades) {
            if (tipo.isInstance(act)) {
                filtradas.add(tipo.cast(act));
            }
        }
        return filtradas;
    }

    public double calcularCostoMateriales(List<? extends Actividad> listaActividades) {
        double total = 0;
        for (Actividad act : listaActividades) {
            total += act.calcularCostoMateriales();
        }
        return total;
    }

    public void mostrarDatos() {
        System.out.println("Evento: " + titulo + " (ID: " + id + ")");
        if (sala != null) {
            System.out.println("Sala: " + sala.getNombre());
        }
        System.out.println("Cantidad de actividades: " + actividades.size());
    }

    public boolean persistirEvento() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(id + ".dat"))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            System.err.println("Error al serializar el evento: " + e.getMessage());
            return false;
        }
    }

    public static EventoUniversitario recuperarEvento(String id) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(id + ".dat"))) {
            return (EventoUniversitario) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de evento no encontrado: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase no encontrada al deserealizar: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de lectura al recuperar evento: " + e.getMessage());
        }
        return null;
    }
}