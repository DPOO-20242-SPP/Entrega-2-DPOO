package Reembolsos;



import java.time.LocalDateTime;
import Tiquetes.Tiquete;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Usuario;

public class Reembolso {

    private String id;
    private Administrador aprobador; 
    private Usuario solicitante;
    private Tiquete tiqueteAReembolsar;
    private double valorBase;
    private double valorCargoEmision;
    private double valorServicio;
    private double valorAcreditado;
    private String motivo;
    private String estado; 
    private LocalDateTime fechaSolicitud = LocalDateTime.now();
    private LocalDateTime fechaResolucion; 

    public Reembolso(String id, Usuario solicitante, Cliente c, Tiquete tiqueteAReembolsar, double d, double e, double f, double g, String motivo, String string, LocalDateTime localDateTime) {
        this.id = id;
        this.solicitante = solicitante;
        this.tiqueteAReembolsar = tiqueteAReembolsar;
        this.motivo = motivo;
        this.estado = "PENDIENTE";
        this.valorBase = tiqueteAReembolsar.getPrecioBase();
        this.valorCargoEmision = 0.0; 
        this.valorServicio = 0.0; 
        this.valorAcreditado = 0.0; 
    }

    public String getId() {
        return id;
    }

    public Administrador getAprobador() {
        return aprobador;
    }

    public void setAprobador(Administrador aprobador) {
        this.aprobador = aprobador;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Tiquete getTiqueteAReembolsar() {
        return tiqueteAReembolsar;
    }

    public void setTiqueteAReembolsar(Tiquete tiqueteAReembolsar) {
        this.tiqueteAReembolsar = tiqueteAReembolsar;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public double getValorCargoEmision() {
        return valorCargoEmision;
    }

    public void setValorCargoEmision(double valorCargoEmision) {
        this.valorCargoEmision = valorCargoEmision;
    }

    public double getValorServicio() {
        return valorServicio;
    }

    public void setValorServicio(double valorServicio) {
        this.valorServicio = valorServicio;
    }

    public double getValorAcreditado() {
        return valorAcreditado;
    }

    public void setValorAcreditado(double valorAcreditado) {
        this.valorAcreditado = valorAcreditado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }


    public void aprobar(Administrador aprobador, double valorAcreditado) {
        this.aprobador = aprobador;
        this.estado = "APROBADO";
        this.valorAcreditado = valorAcreditado;
        this.fechaResolucion = LocalDateTime.now();
        double nuevoSaldo = solicitante.getSaldoVirtual() + valorAcreditado;
        solicitante.setSaldoVirtual(nuevoSaldo);
    }

    public void rechazar(Administrador aprobador) {
        this.aprobador = aprobador;
        this.estado = "RECHAZADO";
        this.valorAcreditado = 0.0;
        this.fechaResolucion = LocalDateTime.now();
    }

    public boolean estaPendiente() {
        return estado.equals("PENDIENTE");
    }

    @Override
    public String toString() {
        return "Reembolso{" +
               "id='" + id + '\'' +
               ", aprobador=" + (aprobador != null ? aprobador.getName() : "N/A") +
               ", solicitante=" + solicitante.getName() +
               ", tiqueteAReembolsar=" + tiqueteAReembolsar.getId() +
               ", valorBase=" + valorBase +
               ", valorCargoEmision=" + valorCargoEmision +
               ", valorServicio=" + valorServicio +
               ", valorAcreditado=" + valorAcreditado +
               ", motivo='" + motivo + '\'' +
               ", estado='" + estado + '\'' +
               ", fechaSolicitud=" + fechaSolicitud +
               ", fechaResolucion=" + fechaResolucion +
               '}';
    }

	public void setEstado(String string) {
		// TODO Auto-generated method stub
		
	}

	public Usuario getAdmin() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getTiquete() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getFecha() {
		// TODO Auto-generated method stub
		return null;
	}
}
