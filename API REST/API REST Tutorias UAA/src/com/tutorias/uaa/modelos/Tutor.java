package com.tutorias.uaa.modelos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="tutor")
public class Tutor {
	//---Atributos---//.
	private int ID;
	private Alumno alumnoAsesorias;
	private ArrayList<Materia> materiasAsesorias;
	
	//---Getters---//.
	public int getID() { return ID; }
	public Alumno getAlumnoAsesorias() { return alumnoAsesorias; }
	public ArrayList<Materia> getMateriasAsesorias() { return materiasAsesorias; }
	
	//---Setters---//.
	public void setID(int iD) { ID = iD; }
	public void setAlumnoAsesorias(Alumno alumnoAsesorias) { this.alumnoAsesorias = alumnoAsesorias; }
	public void setMateriasAsesorias(ArrayList<Materia> materiasAsesorias) { this.materiasAsesorias = materiasAsesorias; }
	
	//---Constructores---//.
	public Tutor() {
		super();
	}
	
	public Tutor(int iD, Alumno alumnoAsesoria, ArrayList<Materia> materiasAsesorias) {
		super();
		ID = iD;
		this.alumnoAsesorias = alumnoAsesoria;
		this.materiasAsesorias = materiasAsesorias;
	}
	
	//---Métodos---//.
	@Override public String toString() {
		return "Tutor [ID=" + ID + ", alumnoAsesorias=" + alumnoAsesorias + ", materiasAsesorias=" + materiasAsesorias
		+ "]";
	}
}