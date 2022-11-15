package com.tutorias.uaa.modelos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="materia")
public class Materia {
	//---Atributos---//.
	private int ID;
	private String nombre;
	private int semestre;
	
	//---Getters---//.
	public int getID() { return ID; }
	public String getNombre() { return nombre; }
	public int getSemestre() { return semestre; }
	
	//---Setters---//.
	public void setID(int iD) { ID = iD; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public void setSemestre(int semestre) { this.semestre = semestre; }
	
	//---Constructores---//.
	public Materia() {
		super();
	}
	
	public Materia(int iD, String nombre, int semestre) {
		super();
		ID = iD;
		this.nombre = nombre;
		this.semestre = semestre;
	}
	
	//---Métodos---//.
	@Override public String toString() {
		return "Materia [ID=" + ID + ", nombre=" + nombre + ", semestre=" + semestre + "]";
	}
}