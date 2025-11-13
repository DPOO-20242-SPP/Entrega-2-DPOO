package Eventos;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import Enums.TipoDeEvento;
import Usuarios.Organizador;

public class Evento {

    private String id;
    private String nombre;
    private TipoDeEvento tipo;
    private LocalDate fecha;
    private LocalTime hora;
    private Organizador organizador;
    private Venue venue;
    private boolean cancelado = false;
    private List<Localidad> localidades = new ArrayList<>();

    private int totalTiquetes;
    private int tiquetesVendidos;
    private double ingresosBrutosPromotor;

    public Evento(String id, String nombre, TipoDeEvento tipo, LocalDate fecha, LocalTime hora, Organizador organizador, Venue venue) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha = fecha;
        this.hora = hora;
        this.organizador = organizador;
        this.venue = venue;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoDeEvento getTipo() { return tipo; }
    public void setTipo(TipoDeEvento tipo) { this.tipo = tipo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public Organizador getOrganizador() { return organizador; }
    public void setOrganizador(Organizador organizador) { this.organizador = organizador; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public boolean isCancelado() { return cancelado; }
    public void setCancelado(boolean cancelado) { this.cancelado = cancelado; }

    public List<Localidad> getLocalidades() { return localidades; }
    public void setLocalidades(List<Localidad> localidades) { this.localidades = localidades; }

    public int getTotalTiquetes() { return totalTiquetes; }
    public void setTotalTiquetes(int totalTiquetes) { this.totalTiquetes = totalTiquetes; }

    public int getTiquetesVendidos() { return tiquetesVendidos; }
    public void setTiquetesVendidos(int tiquetesVendidos) { this.tiquetesVendidos = tiquetesVendidos; }

    public double getIngresosBrutosPromotor() { return ingresosBrutosPromotor; }
    public void setIngresosBrutosPromotor(double ingresosBrutosPromotor) { this.ingresosBrutosPromotor = ingresosBrutosPromotor; }

    
    public void agregarLocalidad(Localidad localidad) {
        localidades.add(localidad);
        totalTiquetes += localidad.getCapacidad();
    }

    public int contarTiquetesDisponibles() {
        int disponibles = 0;
        for (Localidad loc : localidades) {
            disponibles += loc.contarTiquetesDisponibles();
        }
        return disponibles;
    }

    public void cancelarEvento() {
        this.cancelado = true;
        System.out.println("Evento " + nombre + " ha sido cancelado.");
    }

    public void mostrarInfo() {
        System.out.println("Evento: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.println("Fecha: " + fecha);
        System.out.println("Hora: " + hora);
        System.out.println("Organizador: " + organizador.getName());
        System.out.println("Venue: " + venue.getNombre());
        System.out.println("Cancelado: " + cancelado);
        System.out.println("Total tiquetes: " + totalTiquetes);
        System.out.println("Tiquetes disponibles: " + contarTiquetesDisponibles());
        System.out.println("Ingresos brutos del promotor: " + ingresosBrutosPromotor);
    }

    @Override
    public String toString() {
        return "Evento{" +
               "id='" + id + '\'' +
               ", nombre='" + nombre + '\'' +
               ", tipo=" + tipo +
               ", fecha=" + fecha +
               ", hora=" + hora +
               ", organizador=" + organizador.getName() +
               ", venue=" + venue.getNombre() +
               ", cancelado=" + cancelado +
               ", totalTiquetes=" + totalTiquetes +
               ", tiquetesVendidos=" + tiquetesVendidos +
               ", ingresosBrutosPromotor=" + ingresosBrutosPromotor +
               ", localidades=" + localidades.size() +
               '}';
    }
}

