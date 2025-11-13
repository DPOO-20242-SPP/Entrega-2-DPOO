package Usuarios;

import java.util.ArrayList;
import java.util.List;
import Eventos.Evento;
import Oferatas.Oferta; 

public class Organizador extends Usuario {
  
    private List<Evento> eventos = new ArrayList<Evento>();  
    private List<Oferta> ofertas = new ArrayList<Oferta>();      
 
    public Organizador(String nId, String nName, String nCorreo, String nLogin, String nPasswordH) {
        super(nId, nName, nCorreo, nLogin, nPasswordH);
    }
    
    public List<Evento> getEventos() { return eventos; }
    public void setEventos(List<Evento> eventos) { this.eventos = eventos; }
    
    public List<Oferta> getOfertas() { return ofertas; }
    public void setOfertas(List<Oferta> ofertas) { this.ofertas = ofertas; }
    
    public void agregarEvento(Evento evento) {
        this.eventos.add(evento);  
    }

    public void agregarOferta(Oferta oferta) {
        this.ofertas.add(oferta);  
    }

    public void mostrarEventos() {
        System.out.println("Eventos creados por " + this.getName() + ":");
        for (Evento evento : eventos) {
            System.out.println("- " + evento.getNombre()); 
        }
    }

    public void mostrarOfertas() {
        System.out.println("Ofertas creadas por " + this.getName() + ":");
        for (Oferta oferta : ofertas) {
            
            System.out.println("- " + oferta.getId());  
        }
    }
}


