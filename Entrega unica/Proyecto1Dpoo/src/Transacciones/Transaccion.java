package Transacciones;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Enums.MedioPago;
import Tiquetes.Tiquete;
import Usuarios.Cliente;

public class Transaccion {

    private String id;
    private Cliente cliente;
    private LocalDateTime fecha = LocalDateTime.now();
    private List<Tiquete> items = new ArrayList<>();
    private MedioPago medioPago;

    private double subtotal;
    private double recargoServicio; 
    private double costoEmision;    
    private double descuento;       
    private double total;

    private boolean contabilizaIngreso = true; 

    public Transaccion(String id, Cliente cliente, MedioPago medioPago) {
        this.id = id;
        this.cliente = cliente;
        this.medioPago = medioPago;
    }


    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public LocalDateTime getFecha() { return fecha; }
    public List<Tiquete> getItems() { return items; }
    public void setItems(List<Tiquete> items) { this.items = items; }
    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }
    public double getSubtotal() { return subtotal; }
    public double getRecargoServicio() { return recargoServicio; }
    public double getCostoEmision() { return costoEmision; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public boolean isContabilizaIngreso() { return contabilizaIngreso; }
    public void setContabilizaIngreso(boolean contabilizaIngreso) { this.contabilizaIngreso = contabilizaIngreso; }



    public void agregarTiquete(Tiquete tiquete) {
        items.add(tiquete);
    }

    public void calcularTotales(double porcentajeServicio, double costoEmisionFijo, double descuentoAplicado, double d) {
        subtotal = 0.0;
        for (Tiquete t : items) {
            subtotal += t.getPrecioBase();  
        }
        recargoServicio = subtotal * porcentajeServicio;  
        costoEmision = costoEmisionFijo * items.size();   
        descuento = descuentoAplicado;                   
        total = subtotal + recargoServicio + costoEmision - descuento;
    }

    public void confirmar() {
        for (Tiquete t : items) {
            t.marcarVendido(cliente);  
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaccion{");
        sb.append("id='").append(id).append('\'');
        sb.append(", cliente=").append(cliente.getName());  // CORRECCIÓN: getNombre()
        sb.append(", fecha=").append(fecha);
        sb.append(", items=").append(items.size());
        sb.append(", subtotal=").append(subtotal);
        sb.append(", recargoServicio=").append(recargoServicio);
        sb.append(", costoEmision=").append(costoEmision);
        sb.append(", descuento=").append(descuento);
        sb.append(", total=").append(total);
        sb.append(", contabilizaIngreso=").append(contabilizaIngreso);
        sb.append('}');
        return sb.toString();
    }
}

