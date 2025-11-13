package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import Eventos.Evento;
import Eventos.Localidad;
import Enums.TipoDeEvento;
import Enums.TipoLocalidad;
import Tiquetes.TiqueteDeluxe;
import Tiquetes.TiqueteMultiple;
import Tiquetes.TiqueteNormal;
import Usuarios.Cliente;
import Usuarios.Usuario;

public class TiqueteTiposTest {

    @Test
    void RF_tiqueteNormal_debeSerTransferiblePorDefecto() {
        TiqueteNormal tn = new TiqueteNormal("TN1", null, null, 30_000);
        assertTrue(tn.isTransferible(), "Un tiquete normal debe ser transferible");
    }

    @Test
    void RF_tiqueteDeluxe_noDebeSerTransferibleYPuedeGestionarBeneficios() {
        TiqueteDeluxe td = new TiqueteDeluxe("TD1", null, null, 200_000);

        assertFalse(td.isTransferible(), "Un tiquete deluxe no debe ser transferible por defecto");
        assertTrue(td.getBeneficios().isEmpty(), "Inicialmente no debería tener beneficios");

        td.agregarBeneficio("Backstage");
        td.agregarBeneficio("Bebida gratis");

        assertEquals(2, td.getBeneficios().size());
        assertTrue(td.getBeneficios().contains("Backstage"));
        assertTrue(td.getBeneficios().contains("Bebida gratis"));

        td.eliminarBeneficio("Backstage");
        assertEquals(1, td.getBeneficios().size());
        assertFalse(td.getBeneficios().contains("Backstage"));
    }

    @Test
    void RF_tiqueteMultiple_asignarTiquetesADistintosUsuarios() {
        Usuario propietario = new Cliente("C0", "Prop", "p@correo.com", "prop", "hash");
        TiqueteMultiple paquete = new TiqueteMultiple("PM1", propietario);

 
        Evento evento = new Evento("E1", "Concierto", TipoDeEvento.Musical, null, null, null, null);
        Localidad loc = new Localidad("L1", evento, "General", TipoLocalidad.General, 50_000, 2);

        TiqueteNormal t1 = new TiqueteNormal("TN1", evento, loc, 50_000);
        TiqueteNormal t2 = new TiqueteNormal("TN2", evento, loc, 50_000);

        paquete.agregarEntrada(t1);
        paquete.agregarEntrada(t2);

        Usuario u1 = new Cliente("C1", "Carlos", "c1@correo.com", "carlos1", "hash");
        Usuario u2 = new Cliente("C2", "Ana", "c2@correo.com", "ana", "hash");

        paquete.asignarTiquetesADistintosUsuarios(Arrays.asList(u1, u2));

     
        assertEquals(u1, t1.getPropietarioAsignado(), "El primer tiquete debe estar asignado al primer usuario");
        assertEquals(u2, t2.getPropietarioAsignado(), "El segundo tiquete debe estar asignado al segundo usuario");
    }
}
