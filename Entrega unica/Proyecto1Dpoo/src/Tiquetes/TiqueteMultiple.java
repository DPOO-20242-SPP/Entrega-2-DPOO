package Tiquetes;


import java.util.ArrayList;
import java.util.List;
import Usuarios.Usuario;

public class TiqueteMultiple {

    private String id;
    private Usuario propietarioActual; 
    private List<TiqueteNormal> entradas = new ArrayList<>();
    private boolean transferibleParcial = true;
    private boolean bloqueado = false;

    public TiqueteMultiple(String id, Usuario propietarioActual) {
        this.id = id;
        this.propietarioActual = propietarioActual;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getPropietarioActual() { return propietarioActual; }
    public void setPropietarioActual(Usuario propietarioActual) { this.propietarioActual = propietarioActual; }

    public List<TiqueteNormal> getEntradas() { return entradas; }
    public void setEntradas(List<TiqueteNormal> entradas) { this.entradas = entradas; }

    public boolean isTransferibleParcial() { return transferibleParcial; }
    public void setTransferibleParcial(boolean transferibleParcial) { this.transferibleParcial = transferibleParcial; }

    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }


    public void agregarEntrada(TiqueteNormal t) {
        entradas.add(t);
    }

    public void asignarTiquetesADistintosUsuarios(List<Usuario> listaUsuarios) {
        if (listaUsuarios.size() != entradas.size()) {
            System.out.println("Error: la cantidad de usuarios no coincide con la cantidad de tiquetes.");
            return;
        }

        for (int i = 0; i < entradas.size(); i++) {
            TiqueteNormal t = entradas.get(i);
            Usuario u = listaUsuarios.get(i);
            t.setPropietarioAsignado(u); 
        }

        System.out.println("Tiquetes asignados correctamente a cada usuario.");
    }

    @Override
    public String toString() {
        return "PaqueteMultiple{" +
               "id='" + id + '\'' +
               ", propietarioActual=" + propietarioActual.getName() +
               ", entradas=" + entradas.size() +
               ", transferibleParcial=" + transferibleParcial +
               ", bloqueado=" + bloqueado +
               '}';
    }
}
