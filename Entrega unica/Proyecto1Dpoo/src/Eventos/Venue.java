package Eventos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Venue {

    private String id;
    private String nombre;
    private String direccion;
    private int capacidadMaxima;
    private boolean aprobado;
    private List<Evento> eventosProgramados = new ArrayList<>();

    public Venue(String id, String nombre, String direccion, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        this.aprobado = false;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }

    public List<Evento> getEventosProgramados() { return eventosProgramados; }
    public void setEventosProgramados(List<Evento> eventosProgramados) { this.eventosProgramados = eventosProgramados; }


    public void agregarEvento(Evento evento) {
        eventosProgramados.add(evento);
    }

    public boolean estaDisponible(LocalDate fecha, LocalTime hora) {
        for (Evento e : eventosProgramados) {
            if (e.getFecha().equals(fecha) && e.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }

    public void mostrarInfo() {
        System.out.println("Venue: " + nombre);
        System.out.println("Dirección: " + direccion);
        System.out.println("Capacidad máxima: " + capacidadMaxima);
        System.out.println("Aprobado: " + aprobado);
        System.out.println("Eventos programados: " + eventosProgramados.size());
    }

    @Override
    public String toString() {
        return "Venue{" +
               "id='" + id + '\'' +
               ", nombre='" + nombre + '\'' +
               ", direccion='" + direccion + '\'' +
               ", capacidadMaxima=" + capacidadMaxima +
               ", aprobado=" + aprobado +
               ", eventosProgramados=" + eventosProgramados.size() +
               '}';
    }
}

