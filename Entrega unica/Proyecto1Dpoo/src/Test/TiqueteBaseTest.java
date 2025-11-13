package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Enums.TipoTiquetes;
import Eventos.Evento;
import Eventos.Localidad;
import Tiquetes.Tiquete;
import Usuarios.Cliente;

public class TiqueteBaseTest {


    private static class TiquetePrueba extends Tiquete {

        public TiquetePrueba(String id, Evento evento, Localidad localidad, double precioBase) {
            super(id, evento, localidad, precioBase);
        }

        @Override
        public boolean puedeTransferirse() {
            return isTransferible();
        }
    }

    @Test
    void RF_tiquete_transferirSoloFuncionaSiEstaVendidoYEsTransferible() {
        Cliente c1 = new Cliente("C1", "Carlos", "c1@correo.com", "carlos1", "hash");
        Cliente c2 = new Cliente("C2", "Ana", "c2@correo.com", "ana", "hash");

        TiquetePrueba t = new TiquetePrueba("TK1", null, null, 50_000);
        t.setTransferible(true);

    
        assertEquals(TipoTiquetes.Disponible, t.getEstado(), "Debe iniciar como Disponible");

   
        boolean resultadoAntes = t.transferir(c2);
        assertFalse(resultadoAntes, "No se debe poder transferir un tiquete no vendido");

       
        t.marcarVendido(c1);
        assertEquals(TipoTiquetes.Vendido, t.getEstado());
        assertSame(c1, t.getPropietarioActual());

     
        boolean resultadoDespues = t.transferir(c2);
        assertTrue(resultadoDespues, "La transferencia debería ser exitosa");
        assertSame(c2, t.getPropietarioAsignado(), "El nuevo propietario asignado debe ser c2");
    }

    @Test
    void RF_tiquete_noSeTransfiereSiNoEsTransferible() {
        Cliente c1 = new Cliente("C1", "Carlos", "c1@correo.com", "carlos1", "hash");
        Cliente c2 = new Cliente("C2", "Ana", "c2@correo.com", "ana", "hash");

        TiquetePrueba t = new TiquetePrueba("TK2", null, null, 70_000);
        t.setTransferible(false);
        t.marcarVendido(c1);

        boolean resultado = t.transferir(c2);

        assertFalse(resultado, "No se debe poder transferir un tiquete marcado como no transferible");
        assertNull(t.getPropietarioAsignado(), "No debe haberse asignado un nuevo propietario");
    }
}
