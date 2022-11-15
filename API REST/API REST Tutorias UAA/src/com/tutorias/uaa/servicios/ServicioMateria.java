package com.tutorias.uaa.servicios;

import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.Materia;

public interface ServicioMateria {
	public Materia obtenerMateria(int ID);
	public Materia[] obtenerTodasMaterias();
	public Materia[] obtenerMateriasSemestre(int semestre);
	
	public Respuesta agregarMateria(Materia materia);
	public Respuesta actualizarMateria(int ID, Materia materia);
	public Respuesta eliminarMateria(int ID);
}