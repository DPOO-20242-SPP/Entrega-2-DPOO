package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Enums.MedioPago;
import Transacciones.Transaccion;
import Tiquetes.TiqueteNormal;
import Usuarios.Cliente;

public class HistorialClienteTest {

    @Test
    void HU12_y_HU14_historialDebeMostrarTodasLasTransaccionesDelCliente() {
        Cliente cliente = new Cliente("C1", "Carlos", "c@correo.com", "carlos", "hash");

        Transaccion t1 = new Transaccion("T1", cliente, MedioPago.Tarjeta);
        t1.agregarTiquete(new TiqueteNormal("TK1", null, null, 50_000));
        t1.calcularTotales(0.10, 5_000, 0, 0);
        cliente.compra(t1);

        Transaccion t2 = new Transaccion("T2", cliente, MedioPago.PSE);
        t2.agregarTiquete(new TiqueteNormal("TK2", null, null, 100_000));
        t2.calcularTotales(0.10, 5_000, 0, 0);
        cliente.compra(t2);

        assertEquals(2, cliente.getTransacciones().size(),
                "El historial del cliente debe incluir todas sus transacciones");
    }

    @Test
    void HU8_resumenVentasPuedeCalcularIngresosTotalesDeCliente() {
        Cliente cliente = new Cliente("C1", "Carlos", "c@correo.com", "carlos", "hash");

        Transaccion t1 = new Transaccion("T1", cliente, MedioPago.Tarjeta);
        t1.agregarTiquete(new TiqueteNormal("TK1", null, null, 50_000));
        t1.calcularTotales(0.10, 5_000, 0, 0);
        cliente.compra(t1);

        Transaccion t2 = new Transaccion("T2", cliente, MedioPago.PSE);
        t2.agregarTiquete(new TiqueteNormal("TK2", null, null, 100_000));
        t2.calcularTotales(0.10, 5_000, 0, 0);
        cliente.compra(t2);


        double suma = cliente.getTransacciones()
                             .stream()
                             .mapToDouble(Transaccion::getTotal)
                             .sum();

        assertTrue(suma > 0, "El resumen de ventas debe reflejar un ingreso total positivo");
    }
}
