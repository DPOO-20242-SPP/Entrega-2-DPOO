package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import Enums.TipoDeEvento;
import Eventos.Evento;
import Eventos.Venue;

public class VenueTest {

    @Test
    void RF_venue_sinEventosDebeEstarDisponibleSiempre() {
        Venue venue = new Venue("V1", "Teatro ABC", "Calle 123", 1000);

        assertTrue(venue.estaDisponible(LocalDate.of(2025, 1, 1), LocalTime.of(20, 0)),
                   "Un venue sin eventos programados debe estar disponible");
    }

    @Test
    void RF_venue_noDisponibleMismaFechaYHoraDeEvento() {
        Venue venue = new Venue("V1", "Teatro ABC", "Calle 123", 1000);

        Evento evento = new Evento(
                "E1",
                "Concierto",
                TipoDeEvento.Musical,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(20, 0),
                null,
                venue
        );

        venue.agregarEvento(evento);

        assertFalse(venue.estaDisponible(LocalDate.of(2025, 1, 1), LocalTime.of(20, 0)),
                    "Si hay un evento a esa fecha/hora, el venue no debe estar disponible");
    }

    @Test
    void RF_venue_disponibleOtraHoraUOtroDia() {
        Venue venue = new Venue("V1", "Teatro ABC", "Calle 123", 1000);

        Evento evento = new Evento(
                "E1",
                "Concierto",
                TipoDeEvento.Musical,
                LocalDate.of(2025, 1, 1),
                LocalTime.of(20, 0),
                null,
                venue
        );
        venue.agregarEvento(evento);

        assertTrue(venue.estaDisponible(LocalDate.of(2025, 1, 1), LocalTime.of(18, 0)),
                   "Otra hora del mismo día debería estar disponible");

        assertTrue(venue.estaDisponible(LocalDate.of(2025, 1, 2), LocalTime.of(20, 0)),
                   "Otro día a la misma hora debería estar disponible");
    }
}
