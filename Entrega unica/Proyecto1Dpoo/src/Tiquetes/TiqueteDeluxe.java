package Tiquetes;

import java.util.ArrayList;
import java.util.List;
import Eventos.Evento;
import Eventos.Localidad;

public class TiqueteDeluxe extends Tiquete {

    private List<String> beneficios = new ArrayList<>();

    public TiqueteDeluxe(String id, Evento evento, Localidad localidad, double precioBase) {
        super(id, evento, localidad, precioBase);
        this.transferible = false;
    }

    @Override
    public boolean puedeTransferirse() { return false; }

    public List<String> getBeneficios() { return beneficios; }
    public void setBeneficios(List<String> beneficios) { this.beneficios = beneficios; }
    public void agregarBeneficio(String beneficio) { beneficios.add(beneficio); }
    public void eliminarBeneficio(String beneficio) { beneficios.remove(beneficio); }

    @Override
    public String toString() {
        return "TiqueteDeluxe{" +
               "id='" + id + '\'' +
               ", precioBase=" + precioBase +
               ", numeroAsiento=" + numeroAsiento +
               ", estado=" + estado +
               ", beneficios=" + beneficios +
               '}';
    }
}
