package Usuarios;

import java.util.EnumMap;
import java.util.Map;

import Eventos.Evento;
import Eventos.Venue;
import Enums.TipoDeEvento;  
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {
  
  private double porcentajeServicioGeneral = 0.0; 
  private Map<TipoDeEvento, Double> porcentajeServicioPorTipo = new EnumMap<>(TipoDeEvento.class); 
  
  private double costoEmisionFijo = 0.0; 
  private List<Venue> venuesPendientesAprobacion = new ArrayList<>();  

  public Administrador(String id, String nombre, String email, String login, String passwordPlano) {
    super(id, nombre, email, login, passwordPlano);
  }

  public double getPorcentajeServicioGeneral() {
    return porcentajeServicioGeneral;
  }

  public void setPorcentajeServicioGeneral(double porcentajeServicioGeneral) {
    this.porcentajeServicioGeneral = porcentajeServicioGeneral;
  }

  public Map<TipoDeEvento, Double> getPorcentajeServicioPorTipo() {
    return porcentajeServicioPorTipo;
  }

  public void setPorcentajeServicioPorTipo(Map<TipoDeEvento, Double> porcentajeServicioPorTipo) {
    this.porcentajeServicioPorTipo = porcentajeServicioPorTipo;
  }

  public double getCostoEmisionFijo() {
    return costoEmisionFijo;
  }

  public void setCostoEmisionFijo(double costoEmisionFijo) {
    this.costoEmisionFijo = costoEmisionFijo;
  }

  public List<Venue> getVenuesPendientesAprobacion() {
    return venuesPendientesAprobacion;
  }

  public void setVenuesPendientesAprobacion(List<Venue> venuesPendientesAprobacion) {
    this.venuesPendientesAprobacion = venuesPendientesAprobacion;
  }

  public void agregarVenuePendiente(Venue venue) {
    this.venuesPendientesAprobacion.add(venue);
  }

  public void aprobarVenue(Venue venue) {
    if (this.venuesPendientesAprobacion.contains(venue)) {
      this.venuesPendientesAprobacion.remove(venue);
      System.out.println("Venue aprobado: " + venue.getNombre());
    } else {
      System.out.println("Este venue no está en la lista de pendientes.");
    }
  }

  public void fijarPorcentajeServicio(TipoDeEvento tipoEvento, double porcentaje) {
    this.porcentajeServicioPorTipo.put(tipoEvento, porcentaje);
  }

  public double calcularPorcentajeServicio(TipoDeEvento tipoEvento) {
    if (this.porcentajeServicioPorTipo.containsKey(tipoEvento)) {
      return this.porcentajeServicioPorTipo.get(tipoEvento);
    } else {
      return this.porcentajeServicioGeneral; 
    }
  }

  public double calcularCostoTotal(double precioTiquete, TipoDeEvento tipoEvento) {
    double porcentajeServicio = calcularPorcentajeServicio(tipoEvento);
    double recargo = precioTiquete * porcentajeServicio;
    return precioTiquete + recargo + costoEmisionFijo;
  }

  public void mostrarVenuesPendientes() {
    System.out.println("Venues pendientes de aprobación:");
    for (Venue venue : venuesPendientesAprobacion) {
      System.out.println("- " + venue.getNombre());
    }
  }
}


