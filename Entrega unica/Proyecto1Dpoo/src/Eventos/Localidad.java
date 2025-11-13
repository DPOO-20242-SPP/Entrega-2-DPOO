package Eventos;


import java.util.ArrayList;
import java.util.List;
import Enums.TipoLocalidad;
import Enums.TipoTiquetes;
import Tiquetes.Tiquete;

public class Localidad {

    private String id;
    private Evento evento;
    private String nombre; 
    private TipoLocalidad tipo;
    private double precioPublico;
    private int capacidad;
    private List<Tiquete> tiquetes = new ArrayList<>();

    public Localidad(String id, Evento evento, String nombre, TipoLocalidad tipo, double precioPublico, int capacidad) {
        this.id = id;
        this.evento = evento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioPublico = precioPublico;
        this.capacidad = capacidad;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoLocalidad getTipo() { return tipo; }
    public void setTipo(TipoLocalidad tipo) { this.tipo = tipo; }

    public double getPrecioPublico() { return precioPublico; }
    public void setPrecioPublico(double precioPublico) { this.precioPublico = precioPublico; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public List<Tiquete> getTiquetes() { return tiquetes; }
    public void setTiquetes(List<Tiquete> tiquetes) { this.tiquetes = tiquetes; }


    public int contarTiquetesDisponibles() {
        int count = 0;
        for (Tiquete t : tiquetes) {
            if (t.getEstado() == TipoTiquetes.Disponible) {
                count++;
            }
        }
        return count;
    }

    public void mostrarInfo() {
        System.out.println("Localidad: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.println("Precio: " + precioPublico);
        System.out.println("Capacidad: " + capacidad);
        System.out.println("Tiquetes disponibles: " + contarTiquetesDisponibles());
    }

    @Override
    public String toString() {
        return "Localidad{" +
               "id='" + id + '\'' +
               ", nombre='" + nombre + '\'' +
               ", tipo=" + tipo +
               ", precioPublico=" + precioPublico +
               ", capacidad=" + capacidad +
               ", tiquetes=" + tiquetes.size() +
               '}';
    }
}


