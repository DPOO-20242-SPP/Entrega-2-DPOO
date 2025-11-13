package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import Eventos.Localidad;
import Oferatas.Oferta;
import Usuarios.Organizador;

public class OfertaTest {

    @Test
    void RF_oferta_activaYDentroDelRangoDebeEstarVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = ahora.minusHours(1);
        LocalDateTime fin = ahora.plusHours(1);

        Oferta oferta = new Oferta("OF1", (Organizador) null, (Localidad) null, 0.10, inicio, fin);

        assertTrue(oferta.isActiva(), "La oferta debe iniciar activa");
        assertTrue(oferta.estaVigente(), "La oferta debe estar vigente dentro del rango de fechas");
    }

    @Test
    void RF_oferta_fueraDelRangoNoDebeEstarVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = ahora.minusDays(3);
        LocalDateTime fin = ahora.minusDays(1);

        Oferta oferta = new Oferta("OF2", null, null, 0.20, inicio, fin);

        assertFalse(oferta.estaVigente(), "La oferta no debe estar vigente si el rango ya terminó");
    }

    @Test
    void RF_oferta_inactivaNoDebeEstarVigenteAunqueFechasSeanValidas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicio = ahora.minusHours(1);
        LocalDateTime fin = ahora.plusHours(1);

        Oferta oferta = new Oferta("OF3", null, null, 0.20, inicio, fin);
        oferta.setActiva(false);  

        assertFalse(oferta.estaVigente(), "La oferta inactiva no debe estar vigente aunque la fecha sea correcta");
    }
}

