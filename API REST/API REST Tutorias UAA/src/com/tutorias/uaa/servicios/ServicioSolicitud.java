package com.tutorias.uaa.servicios;

import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.Solicitud;
import com.tutorias.uaa.modelos.SolicitudSimplificada;

public interface ServicioSolicitud {
	public Solicitud obtenerSolicitud(int ID);
	public Solicitud[] obtenerTodasSolicitudes();
	public Solicitud[] obtenerSolicitudesAlumno(int IDAlumno);
	
	public Respuesta agregarSolicitud(SolicitudSimplificada solicitud);
	public Respuesta actualizarSolicitud(int ID, SolicitudSimplificada solicitud);
	public Respuesta eliminarSolicitud(int ID);
}