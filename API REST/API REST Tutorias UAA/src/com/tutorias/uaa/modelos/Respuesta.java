package com.tutorias.uaa.modelos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Respuesta {
	//---Atributos---//.
	private boolean estado;
	private String mensaje;
	
	//---Getters---//.
	public boolean isEstado() { return estado; }
	public String getMensaje() { return mensaje; }
	
	//---Setters---//.
	public void setEstado(boolean estado) { this.estado = estado; }
	public void setMensaje(String mensaje) { this.mensaje = mensaje; }
	
	//---Constructores---//.
	public Respuesta() {
		super();
	}
	
	public Respuesta(boolean estado, String mensaje) {
		super();
		this.estado = estado;
		this.mensaje = mensaje;
	}
}