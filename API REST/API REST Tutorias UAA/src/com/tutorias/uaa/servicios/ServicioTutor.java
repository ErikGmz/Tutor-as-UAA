package com.tutorias.uaa.servicios;

import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.Tutor;
import com.tutorias.uaa.modelos.TutorSimplificado;

public interface ServicioTutor {
	public Tutor obtenerTutor(int ID);
	public Tutor[] obtenerTodosTutores();
	
	public Respuesta agregarTutor(TutorSimplificado tutor);
	public Respuesta actualizarTutor(int ID, TutorSimplificado tutor);
	public Respuesta eliminarTutor(int ID);
}