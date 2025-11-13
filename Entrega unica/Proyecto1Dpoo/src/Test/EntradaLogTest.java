package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import log.EntradaLog;

public class EntradaLogTest {

    @Test
    void HU4_crearEntradaLogDebeAlmacenarDatosCorrectos() {
        LocalDateTime ahora = LocalDateTime.now();
        EntradaLog log = new EntradaLog(ahora, "INFO", "Usuario C1 realizó una compra");

        assertEquals("INFO", log.getTipo());
        assertEquals("Usuario C1 realizó una compra", log.getDescripcion());
        assertNotNull(log.getFechaHora());
    }

    @Test
    void HU4_dosEntradasConMismosDatosDebenSerIguales() {
        LocalDateTime t = LocalDateTime.now();

        EntradaLog l1 = new EntradaLog(t, "ERROR", "Algo falló");
        EntradaLog l2 = new EntradaLog(t, "ERROR", "Algo falló");

        assertEquals(l1, l2, "Dos logs con mismos datos deberían ser equals");
        assertEquals(l1.hashCode(), l2.hashCode(), "Y tener el mismo hashCode");
    }
}
