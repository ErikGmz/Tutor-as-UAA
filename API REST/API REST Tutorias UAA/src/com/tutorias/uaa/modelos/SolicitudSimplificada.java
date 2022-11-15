package com.tutorias.uaa.modelos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="solicitudSimplificada")
public class SolicitudSimplificada {
	//---Atributos---//.
	private int ID;
	private int alumnoAsesorado;
	private int tutorAsesorias;
	private String fechaPeticion;
	private boolean urgencia;
	private int materiaAsesoria;
	private String tema;
	private String descripcion;
	private String fechaAsesoria;
	private String sitio;
	private String modalidad;
	private ArrayList<Integer> tutoresNoDisponibles;
	
	//---Getters---//.
	public int getID() { return ID; }
	public int getAlumnoAsesorado() { return alumnoAsesorado; }
	public int getTutorAsesorias() { return tutorAsesorias; }
	public String getFechaPeticion() { return fechaPeticion; }
	public boolean isUrgencia() { return urgencia; }
	public int getMateriaAsesoria() { return materiaAsesoria; }
	public String getTema() { return tema; }
	public String getDescripcion() { return descripcion; }
	public String getFechaAsesoria() { return fechaAsesoria; }
	public String getSitio() { return sitio; }
	public String getModalidad() { return modalidad; }
	public ArrayList<Integer> getTutoresNoDisponibles() { return tutoresNoDisponibles; }
	
	//---Setters---//.
	public void setID(int iD) { ID = iD; }
	public void setAlumnoAsesorado(int alumnoAsesorado) { this.alumnoAsesorado = alumnoAsesorado; }
	public void setTutorAsesorias(int tutorAsesorias) { this.tutorAsesorias = tutorAsesorias; }
	public void setFechaPeticion(String fechaPeticion) { this.fechaPeticion = fechaPeticion; }
	public void setUrgencia(boolean urgencia) { this.urgencia = urgencia; }
	public void setMateriaAsesoria(int materiaAsesoria) { this.materiaAsesoria = materiaAsesoria; }
	public void setTema(String tema) { this.tema = tema; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	public void setFechaAsesoria(String fechaAsesoria) { this.fechaAsesoria = fechaAsesoria; }
	public void setSitio(String sitio) { this.sitio = sitio; }
	public void setModalidad(String modalidad) { this.modalidad = modalidad; }
	public void setTutoresNoDisponibles(ArrayList<Integer> tutoresNoDisponibles) { this.tutoresNoDisponibles = tutoresNoDisponibles; }
	
	//---Constructores---//.
	public SolicitudSimplificada() {
		super();
	}
	
	public SolicitudSimplificada(int iD, int alumnoAsesorado, int tutorAsesorias, String fechaPeticion,
	boolean urgencia, int materiaAsesoria, String tema, String descripcion, String fechaAsesoria, String sitio,
	String modalidad, ArrayList<Integer> tutoresNoDisponibles) {
		super();
		ID = iD;
		this.alumnoAsesorado = alumnoAsesorado;
		this.tutorAsesorias = tutorAsesorias;
		this.fechaPeticion = fechaPeticion;
		this.urgencia = urgencia;
		this.materiaAsesoria = materiaAsesoria;
		this.tema = tema;
		this.descripcion = descripcion;
		this.fechaAsesoria = fechaAsesoria;
		this.sitio = sitio;
		this.modalidad = modalidad;
		this.tutoresNoDisponibles = tutoresNoDisponibles;
	}
	
	//---Métodos---//.
	@Override public String toString() {
		return "SolicitudSimplificada [ID=" + ID + ", alumnoAsesorado=" + alumnoAsesorado + ", tutorAsesorias="
		+ tutorAsesorias + ", fechaPeticion=" + fechaPeticion + ", urgencia=" + urgencia + ", materiaAsesoria="
		+ materiaAsesoria + ", tema=" + tema + ", descripcion=" + descripcion + ", fechaAsesoria="
		+ fechaAsesoria + ", sitio=" + sitio + ", modalidad=" + modalidad + ", tutoresNoDisponibles="
		+ tutoresNoDisponibles + "]";
	}
}