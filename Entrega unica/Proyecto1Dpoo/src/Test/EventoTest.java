package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import Enums.TipoDeEvento;
import Enums.TipoLocalidad;
import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;

public class EventoTest {

    @Test
    void RF_evento_agregarLocalidadesActualizaTotalTiquetes() {
        Venue venue = new Venue("V1", "Teatro ABC", "Calle 123", 500);

        Evento evento = new Evento(
                "E1",
                "Concierto",
                TipoDeEvento.Musical,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(20, 0),
                null,
                venue
        );

        Localidad l1 = new Localidad("L1", evento, "Platea", TipoLocalidad.Numerada, 100_000, 50);
        Localidad l2 = new Localidad("L2", evento, "General", TipoLocalidad.General, 50_000, 150);

        evento.agregarLocalidad(l1);
        evento.agregarLocalidad(l2);

        assertEquals(2, evento.getLocalidades().size(), "Debe haber 2 localidades asociadas al evento");
        assertEquals(200, evento.getTotalTiquetes(), "El total de tiquetes debe ser la suma de capacidades");
    }

    @Test
    void RF_evento_cancelarEventoMarcaFlagCancelado() {
        Venue venue = new Venue("V1", "Teatro ABC", "Calle 123", 500);

        Evento evento = new Evento(
                "E2",
                "Feria",
                TipoDeEvento.Cultural,
                LocalDate.of(2025, 2, 1),
                LocalTime.of(15, 0),
                null,
                venue
        );

        assertFalse(evento.isCancelado(), "Un evento recién creado no debe estar cancelado");
        evento.cancelarEvento();
        assertTrue(evento.isCancelado(), "Después de cancelar, el evento debe quedar marcado como cancelado");
    }
}
