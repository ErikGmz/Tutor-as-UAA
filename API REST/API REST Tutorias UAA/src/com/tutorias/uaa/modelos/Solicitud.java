package com.tutorias.uaa.modelos;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="solicitud")
public class Solicitud {
	//---Atributos---//.
	private int ID;
	private Alumno alumnoAsesorado;
	private Tutor tutorAsesorias;
	private String fechaPeticion;
	private boolean urgencia;
	private Materia materiaAsesoria;
	private String tema;
	private String descripcion;
	private String fechaAsesoria;
	private String sitio;
	private String modalidad;
	private ArrayList<Tutor> tutoresNoDisponibles;
	
	//---Getters---//.
	public int getID() { return ID; }
	public Alumno getAlumnoAsesorado() { return alumnoAsesorado; }
	public Tutor getTutorAsesorias() { return tutorAsesorias; }
	public String getFechaPeticion() { return fechaPeticion; }
	public boolean isUrgencia() { return urgencia; }
	public Materia getMateriaAsesoria() { return materiaAsesoria; }
	public String getTema() { return tema; }
	public String getDescripcion() { return descripcion; }
	public String getFechaAsesoria() { return fechaAsesoria; }
	public String getSitio() { return sitio; }
	public String getModalidad() { return modalidad; }
	public ArrayList<Tutor> getTutoresNoDisponibles() { return tutoresNoDisponibles; }
	
	//---Setters---//.
	public void setID(int iD) { ID = iD; }
	public void setAlumnoAsesorado(Alumno alumnoAsesorado) { this.alumnoAsesorado = alumnoAsesorado; }
	public void setTutorAsesorias(Tutor tutorAsesorias) { this.tutorAsesorias = tutorAsesorias; }
	public void setFechaPeticion(String fechaPeticion) { this.fechaPeticion = fechaPeticion; }
	public void setUrgencia(boolean urgencia) { this.urgencia = urgencia; }
	public void setMateriaAsesoria(Materia materiaAsesoria) { this.materiaAsesoria = materiaAsesoria; }
	public void setTema(String tema) { this.tema = tema; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	public void setFechaAsesoria(String fechaAsesoria) { this.fechaAsesoria = fechaAsesoria; }
	public void setSitio(String sitio) { this.sitio = sitio; }
	public void setModalidad(String modalidad) { this.modalidad = modalidad; }
	public void setTutoresNoDisponibles(ArrayList<Tutor> tutoresNoDisponibles) { this.tutoresNoDisponibles = tutoresNoDisponibles; }
	
	//---Constructores---//.
	public Solicitud() {
		super();
	}
	
	public Solicitud(int iD, Alumno alumnoAsesorado, Tutor tutorAsesorias, String fechaPeticion, boolean urgencia,
	Materia materiaAsesoria, String tema, String descripcion, String fechaAsesoria, String sitio,
	String modalidad, ArrayList<Tutor> tutoresNoDisponibles) {
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
		return "Solicitud [ID=" + ID + ", alumnoAsesorado=" + alumnoAsesorado + ", tutorAsesorias=" + tutorAsesorias
		+ ", fechaPeticion=" + fechaPeticion + ", urgencia=" + urgencia + ", materiaAsesoria=" + materiaAsesoria
		+ ", tema=" + tema + ", descripcion=" + descripcion + ", fechaAsesoria=" + fechaAsesoria + ", sitio="
		+ sitio + ", modalidad=" + modalidad + ", tutoresNoDisponibles=" + tutoresNoDisponibles + "]";
	}
} 