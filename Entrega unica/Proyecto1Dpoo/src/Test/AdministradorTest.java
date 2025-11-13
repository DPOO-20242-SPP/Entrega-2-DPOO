package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Enums.TipoDeEvento;
import Usuarios.Administrador;

public class AdministradorTest {

    @Test
    void RF_costos_adminUsaPorcentajeEspecificoCuandoExiste() {
        Administrador admin = new Administrador("A1", "Admin", "admin@correo.com", "admin", "hash");

        admin.setPorcentajeServicioGeneral(0.05); // 5% general
        admin.fijarPorcentajeServicio(TipoDeEvento.Musical, 0.10); // 10% Musical

        double pMusical = admin.calcularPorcentajeServicio(TipoDeEvento.Musical);
        assertEquals(0.10, pMusical, 0.0001, "Debe usar el porcentaje específico para tipo Musical");

        double pCultural = admin.calcularPorcentajeServicio(TipoDeEvento.Cultural);
        assertEquals(0.05, pCultural, 0.0001, "Debe usar el porcentaje general cuando no hay específico");
    }

    @Test
    void RF_costos_calcularCostoTotalDebeIncluirRecargoYEmisionFija() {
        Administrador admin = new Administrador("A1", "Admin", "admin@correo.com", "admin", "hash");

        admin.setPorcentajeServicioGeneral(0.0);
        admin.fijarPorcentajeServicio(TipoDeEvento.Musical, 0.20); // 20%
        admin.setCostoEmisionFijo(5_000);

        double precioBase = 100_000;
        double costoTotal = admin.calcularCostoTotal(precioBase, TipoDeEvento.Musical);

        double esperado = precioBase + (precioBase * 0.20) + 5_000; // 100k + 20k + 5k = 125k

        assertEquals(esperado, costoTotal, 0.0001, "Costo total calculado incorrecto");
    }
}
