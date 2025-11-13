package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import Enums.MedioPago;
import Enums.TipoTiquetes;
import Transacciones.Transaccion;
import Tiquetes.TiqueteNormal;
import Usuarios.Cliente;

public class TransaccionTest {

    @Test
    void RF_compra_agregarTiqueteDebeAgregarItemsALaLista() {
        Cliente cliente = new Cliente("C1", "Carlos", "c@correo.com", "carlos", "hash");
        Transaccion tx = new Transaccion("T1", cliente, MedioPago.Tarjeta);

        assertEquals(0, tx.getItems().size(), "Al inicio no debería haber ítems en la transacción");


        TiqueteNormal t1 = new TiqueteNormal("TK1", null, null, 100_000);
        TiqueteNormal t2 = new TiqueteNormal("TK2", null, null, 50_000);

        tx.agregarTiquete(t1);
        tx.agregarTiquete(t2);

        List<?> items = tx.getItems();
        assertEquals(2, items.size(), "Después de agregar dos tiquetes debe haber 2 ítems");
    }

    @Test
    void RF_compra_calcularTotalesDebeSumarSubtotalRecargoEmisionYDescuento() {
        Cliente cliente = new Cliente("C1", "Carlos", "c@correo.com", "carlos", "hash");
        Transaccion tx = new Transaccion("T2", cliente, MedioPago.PSE);

        TiqueteNormal t1 = new TiqueteNormal("TK1", null, null, 100_000);
        TiqueteNormal t2 = new TiqueteNormal("TK2", null, null, 200_000);

        tx.agregarTiquete(t1);
        tx.agregarTiquete(t2);

        double porcentajeServicio = 0.10;
        double costoEmisionFijo = 5_000;  
        double descuento = 1_000;

        tx.calcularTotales(porcentajeServicio, costoEmisionFijo, descuento, 0.0);

        double esperadoSubtotal = 300_000;
        double esperadoRecargo = esperadoSubtotal * porcentajeServicio; 
        double esperadoCostoEmision = costoEmisionFijo * 2;             
        double esperadoTotal = esperadoSubtotal + esperadoRecargo + esperadoCostoEmision - descuento;

        assertEquals(esperadoSubtotal, tx.getSubtotal(), 0.0001, "Subtotal incorrecto");
        assertEquals(esperadoRecargo, tx.getRecargoServicio(), 0.0001, "Recargo de servicio incorrecto");
        assertEquals(esperadoCostoEmision, tx.getCostoEmision(), 0.0001, "Costo de emisión incorrecto");
        assertEquals(descuento, tx.getDescuento(), 0.0001, "Descuento incorrecto");
        assertEquals(esperadoTotal, tx.getTotal(), 0.0001, "Total calculado incorrecto");
    }

    @Test
    void RF_compra_confirmarDebeMarcarTiquetesComoVendidosAlCliente() {
        Cliente cliente = new Cliente("C1", "Carlos", "c@correo.com", "carlos", "hash");
        Transaccion tx = new Transaccion("T3", cliente, MedioPago.SaldoVirtual);

        TiqueteNormal t1 = new TiqueteNormal("TK1", null, null, 50_000);
        TiqueteNormal t2 = new TiqueteNormal("TK2", null, null, 80_000);

        tx.agregarTiquete(t1);
        tx.agregarTiquete(t2);

        tx.confirmar();

        assertEquals(TipoTiquetes.Vendido, t1.getEstado(), "El tiquete 1 debería quedar Vendido");
        assertEquals(TipoTiquetes.Vendido, t2.getEstado(), "El tiquete 2 debería quedar Vendido");
        assertSame(cliente, t1.getPropietarioActual(), "El cliente debe ser propietario del tiquete 1");
        assertSame(cliente, t2.getPropietarioActual(), "El cliente debe ser propietario del tiquete 2");
    }
}
