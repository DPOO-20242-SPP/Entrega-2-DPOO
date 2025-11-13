package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Usuarios.Administrador;
import Usuarios.Organizador;

public class OrganizadorAprobacionTest {

    @Test
    void HU5_registroPromotorDebeQuedarPendienteAprobacion() {
        Organizador org = new Organizador("O1", "Pedro", "p@correo.com", "pedro", "hash");

    
        assertEquals("pendiente", org.getEstadoAprobacion(),
                "Un promotor recién creado debe quedar en estado 'pendiente'");
    }

    @Test
    void HU1_adminPuedeAprobarPromotorPendiente() {
        Administrador admin = new Administrador("A1", "Admin", "admin@correo.com", "admin", "hash");
        Organizador org = new Organizador("O1", "Pedro", "p@correo.com", "pedro", "hash");

        admin.aprobarPromotor(org);

        assertEquals("aprobado", org.getEstadoAprobacion(),
                "Después de aprobar, el estado del promotor debe ser 'aprobado'");
    }

    @Test
    void HU1_adminPuedeRechazarPromotorPendiente() {
        Administrador admin = new Administrador("A1", "Admin", "admin@correo.com", "admin", "hash");
        Organizador org = new Organizador("O2", "Luis", "l@correo.com", "luis", "hash");

        admin.rechazarPromotor(org);

        assertEquals("rechazado", org.getEstadoAprobacion(),
                "Después de rechazar, el estado del promotor debe ser 'rechazado'");
    }
}
