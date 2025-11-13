package Tiquetes;

import java.time.LocalDateTime;
import Eventos.Evento;
import Eventos.Localidad;
import Enums.TipoTiquetes;
import Usuarios.Usuario;

public abstract class Tiquete {

    protected String id;
    protected Evento evento;
    protected Localidad localidad;
    protected TipoTiquetes estado = TipoTiquetes.Disponible;
    protected boolean transferible;
    protected Integer numeroAsiento;         
    protected Usuario propietarioActual;      
    protected Usuario propietarioAsignado;
    protected double precioBase;             
    protected LocalDateTime fechaEmision = LocalDateTime.now();

    protected Tiquete(String id, Evento evento, Localidad localidad, double precioBase) {
        this.id = id;
        this.evento = evento;
        this.localidad = localidad;
        this.precioBase = precioBase;
        this.transferible = true;
        this.propietarioActual = null;
        this.propietarioAsignado = null;
    }

    public String getId() { return id; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Localidad getLocalidad() { return localidad; }
    public void setLocalidad(Localidad localidad) { this.localidad = localidad; }

    public TipoTiquetes getEstado() { return estado; }
    public void setEstado(TipoTiquetes estado) { this.estado = estado; }

    public boolean isTransferible() { return transferible; }
    public void setTransferible(boolean transferible) { this.transferible = transferible; }

    public Integer getNumeroAsiento() { return numeroAsiento; }
    public void setNumeroAsiento(Integer numeroAsiento) { this.numeroAsiento = numeroAsiento; }

    public Usuario getPropietarioActual() { return propietarioActual; }
    public void setPropietarioActual(Usuario propietarioActual) { this.propietarioActual = propietarioActual; }

    public Usuario getPropietarioAsignado() { return propietarioAsignado; }
    public void setPropietarioAsignado(Usuario propietarioAsignado) { this.propietarioAsignado = propietarioAsignado; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public abstract boolean puedeTransferirse();

    public void marcarVendido(Usuario comprador) {
        this.estado = TipoTiquetes.Vendido;
        this.propietarioActual = comprador;
    }

    public boolean transferir(Usuario nuevoPropietario) {
        if (puedeTransferirse() && estado == TipoTiquetes.Vendido) {
            this.propietarioAsignado = nuevoPropietario;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Tiquete{" +
               "id='" + id + '\'' +
               ", evento=" + evento.getNombre() +
               ", localidad=" + localidad.getNombre() +
               ", estado=" + estado +
               ", transferible=" + transferible +
               ", numeroAsiento=" + numeroAsiento +
               ", propietarioActual=" + (propietarioActual != null ? propietarioActual.getName() : "N/A") +
               ", propietarioAsignado=" + (propietarioAsignado != null ? propietarioAsignado.getName() : "N/A") +
               ", precioBase=" + precioBase +
               ", fechaEmision=" + fechaEmision +
               '}';
    }
}


