package Oferatas;

import java.time.LocalDateTime;
import Eventos.Localidad;
import Usuarios.Organizador;

public class Oferta {
    
    private String id;
    private Organizador creador;
    private Localidad localidadObjetivo; 
    private double porcentajeDescuento; 
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private boolean activa;

    public Oferta(String id, Organizador creador, Localidad localidadObjetivo, double porcentajeDescuento,
                  LocalDateTime inicio, LocalDateTime fin) {
        this.id = id;
        this.creador = creador;
        this.localidadObjetivo = localidadObjetivo;
        this.porcentajeDescuento = porcentajeDescuento;
        this.inicio = inicio;
        this.fin = fin;
        this.activa = true; 
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Organizador getCreador() { return creador; }
    public void setCreador(Organizador creador) { this.creador = creador; }

    public Localidad getLocalidadObjetivo() { return localidadObjetivo; }
    public void setLocalidadObjetivo(Localidad localidadObjetivo) { this.localidadObjetivo = localidadObjetivo; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public boolean estaVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        return activa && (ahora.isAfter(inicio) || ahora.isEqual(inicio)) &&
                        (ahora.isBefore(fin) || ahora.isEqual(fin));
    }

    @Override
    public String toString() {
        return "Oferta{" +
               "id='" + id + '\'' +
               ", creador=" + creador.getName() +
               ", localidadObjetivo=" + localidadObjetivo.getNombre() +
               ", porcentajeDescuento=" + porcentajeDescuento +
               ", inicio=" + inicio +
               ", fin=" + fin +
               ", activa=" + activa +
               '}';
    }
}


