package Tiquetes;



import Eventos.Evento;
import Eventos.Localidad;
import Usuarios.Usuario;

public class TiqueteNormal extends Tiquete {

    protected Usuario propietarioAsignado;

    public TiqueteNormal(String id, Evento evento, Localidad localidad, double precioBase) {
        super(id, evento, localidad, precioBase);
        this.transferible = true;
        this.propietarioAsignado = null; 
    }

    @Override
    public boolean puedeTransferirse() { return true; }

    public Usuario getPropietarioAsignado() { return propietarioAsignado; }
    public void setPropietarioAsignado(Usuario u) { this.propietarioAsignado = u; }
}
