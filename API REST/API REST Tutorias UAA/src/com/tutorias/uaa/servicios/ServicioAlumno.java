package com.tutorias.uaa.servicios;

import com.tutorias.uaa.modelos.Alumno;
import com.tutorias.uaa.modelos.Respuesta;

public interface ServicioAlumno {
	public Alumno obtenerAlumno(int ID);
	public Alumno[] obtenerTodosAlumnos();
	
	public Respuesta agregarAlumno(Alumno alumno);
	public Respuesta actualizarAlumno(int ID, Alumno alumno);
	public Respuesta eliminarAlumno(int ID);
}