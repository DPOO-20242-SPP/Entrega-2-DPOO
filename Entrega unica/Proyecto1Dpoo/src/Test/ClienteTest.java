package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import Enums.MedioPago;
import Tiquetes.TiqueteNormal;
import Transacciones.Transaccion;
import Usuarios.Cliente;

public class ClienteTest {

    @Test
    void RF_compra_clienteDebeRegistrarTransaccionYTiquetes() {
        Cliente cliente = new Cliente("C1", "Carlos", "c@correo.com", "carlos", "hash");
        Transaccion tx = new Transaccion("T1", cliente, MedioPago.Tarjeta);

        TiqueteNormal t1 = new TiqueteNormal("TK1", null, null, 40_000);
        TiqueteNormal t2 = new TiqueteNormal("TK2", null, null, 60_000);

        tx.agregarTiquete(t1);
        tx.agregarTiquete(t2);

     
        cliente.compra(tx);

        List<Transaccion> transacciones = cliente.getTransacciones();
        assertEquals(1, transacciones.size(), "El cliente debe tener 1 transacción registrada");
        assertSame(tx, transacciones.get(0), "La transacción registrada debe ser la misma");

        List<?> tiquetesCliente = cliente.getTiquetes();
        assertEquals(2, tiquetesCliente.size(), "El cliente debe tener 2 tiquetes registrados");
        assertTrue(tiquetesCliente.contains(t1), "Debe contener el tiquete 1");
        assertTrue(tiquetesCliente.contains(t2), "Debe contener el tiquete 2");
    }
}
