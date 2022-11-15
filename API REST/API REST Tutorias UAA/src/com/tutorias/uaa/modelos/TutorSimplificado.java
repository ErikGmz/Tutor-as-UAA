package com.tutorias.uaa.modelos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="tutorSimplificado")
public class TutorSimplificado {
	//---Atributos---//.
	private int ID;
	private int IDAlumnoAsesorias;
	private ArrayList<Integer> IDsMateriasAsesorias;
	
	//---Getters---//.
	public int getID() { return ID; }
	public int getIDAlumnoAsesorias() { return IDAlumnoAsesorias; }
	public ArrayList<Integer> getIDsMateriasAsesorias() { return IDsMateriasAsesorias; }
	
	//---Setters---//.
	public void setID(int iD) { ID = iD; }
	public void setIDAlumnoAsesorias(int iDAlumnoAsesorias) { IDAlumnoAsesorias = iDAlumnoAsesorias; }
	public void setIDsMateriasAsesorias(ArrayList<Integer> iDsMateriasAsesorias) { IDsMateriasAsesorias = iDsMateriasAsesorias; }
	
	//---Constructores---//.
	public TutorSimplificado() {
		super();
	}
	
	public TutorSimplificado(int iD, int iDAlumnoAsesorias, ArrayList<Integer> iDsMateriasAsesorias) {
		super();
		ID = iD;
		IDAlumnoAsesorias = iDAlumnoAsesorias;
		IDsMateriasAsesorias = iDsMateriasAsesorias;
	}
	
	//---Métodos---//.
	@Override public String toString() {
		return "TutorSimplificado [ID=" + ID + ", IDAlumnoAsesorias=" + IDAlumnoAsesorias + ", IDsMateriasAsesorias="
		+ IDsMateriasAsesorias + "]";
	}
}