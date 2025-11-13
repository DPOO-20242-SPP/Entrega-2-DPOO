package Usuarios;

import java.time.LocalDateTime;

public abstract class Usuario {
	
	protected String id;
	protected String name;
	protected String correo;
	protected String login;
	protected String passwordH;
	protected boolean active= true;
	protected double saldoVirtual= 0.0;
	protected LocalDateTime fechacrea= LocalDateTime.now();
	
protected Usuario (String nId, String nName, String nCorreo, String nLogin, String nPasswordH) {
	
	this.id= nId;
	this.name= nName;
	this.correo= nCorreo;
	this.login= nLogin;
	this.passwordH= nPasswordH;
	
}
	public String getId() {
		return this.id;
	}
	
	public void setId(String nId) {
		this.id= nId;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String nName) {
		this.name= nName;
	}
	
	public String getCorreo() {
		return this.correo;
	}
	
	public void setCorreo(String nCorreo) {
		this.correo= nCorreo;
	}
	
    public double getSaldoVirtual() {
        return saldoVirtual;
    }

    public void setSaldoVirtual(double saldoVirtual) {
        this.saldoVirtual = saldoVirtual;
    }
    
	public String getLogin() {
		return this.login;
	}
	
	public void setLogin(String nLogin) {
		this.login= nLogin;
	}
	
	public String getPasswordH() {
		return this.passwordH;
	}
	
	public void setPasswordH(String nPasswordH) {
		this.passwordH= nPasswordH;
	}
	
	  public boolean validarPassword(String raw) {
		    return this.passwordH.equals(Integer.toHexString(raw.hashCode()));
		  }

}
