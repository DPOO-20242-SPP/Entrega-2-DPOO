package Usuarios;

import java.util.ArrayList;
import java.util.List;
import Tiquetes.Tiquete;
import Transacciones.Transaccion;

public class Cliente extends Usuario {

    private List<Transaccion> transacciones = new ArrayList<>();
    private List<Tiquete> tiquetes = new ArrayList<>();
    
    public Cliente(String nId, String nName, String nCorreo, String nLogin, String nPasswordH) {
        super(nId, nName, nCorreo, nLogin, nPasswordH);
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public List<Tiquete> getTiquetes() {
        return tiquetes;
    }

    public void setTiquetes(List<Tiquete> tiquetes) {
        this.tiquetes = tiquetes;
    }


    private void agregarTransaccion(Transaccion transaccion) {
        this.transacciones.add(transaccion);
    }

    
    private void agregarTiquete(Tiquete tiquete) {
        this.tiquetes.add(tiquete);
    }

    public void compra(Transaccion transaccion) {
        agregarTransaccion(transaccion); 
        for (Tiquete tiquete : transaccion.getItems()) {  
            agregarTiquete(tiquete); 
        }
    }


    public void mostrarTiquetes() {
    	System.out.println("Tiquetes comprados por " + this.getName() + ":");
    	for (Tiquete tiquete : tiquetes) {
    		System.out.println("- " + tiquete.getId()); 
    	}
    }

    public void mostrarTransacciones() {
    	System.out.println("Transacciones realizadas por " + this.getName() + ":");
    	for (Transaccion transaccion : transacciones) {
    		System.out.println("- Transacción ID: " + transaccion.getId() + ", Total: " + transaccion.getTotal());
    	}
    }
    
    @Override
    public String toString() {
      return "Cliente{" +
             "id='" + getId() + '\'' +
             ", nombre='" + getName() + '\'' +
             ", email='" + getCorreo() + '\'' +
             ", login='" + getLogin() + '\'' +
             ", saldoVirtual=" + getSaldoVirtual() + 
             ", transacciones=" + transacciones +
             ", tiquetes=" + tiquetes +
             '}';
    }
    
}

