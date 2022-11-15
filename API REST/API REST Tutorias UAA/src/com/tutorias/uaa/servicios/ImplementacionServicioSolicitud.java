package com.tutorias.uaa.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tutorias.uaa.archivos.ArchivoJson;
import com.tutorias.uaa.modelos.Alumno;
import com.tutorias.uaa.modelos.Materia;
import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.Solicitud;
import com.tutorias.uaa.modelos.SolicitudSimplificada;
import com.tutorias.uaa.modelos.Tutor;

@Path("/tutor")
public class ImplementacionServicioSolicitud implements ServicioSolicitud {
	//---Atributos---//.
	private static String nombreArchivo = "registros_solicitudes.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener")
	public Solicitud obtenerSolicitud(int ID) {
		try {
			SolicitudSimplificada solicitudSimplificada = ArchivoJson.<Integer, SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, ID);
			
			if(solicitudSimplificada != null) {
				Solicitud solicitud = new Solicitud();
				
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico("registros_alumnos.json", solicitudSimplificada.getAlumnoAsesorado());
				if(alumno == null) {
					throw new Exception();
				}
				
				// Obtener los datos de la materia referenciada en el archivo.
				Materia materia = ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico("registros_materias.json", solicitudSimplificada.getMateriaAsesoria());
				if(materia == null) {
					throw new Exception();
				}
				
				// Obtener los datos del tutor referenciado en el archivo.
				Tutor tutor = ArchivoJson.<Integer, Tutor>obtenerRegistroEspecifico("registros_tutores.json", solicitudSimplificada.getTutorAsesorias());
				
				// Obtener los datos del tutor no disponibles referenciados en el archivo.
				ArrayList<Tutor> tutoresNoDisponibles = new ArrayList<>();
				solicitudSimplificada.getTutoresNoDisponibles().forEach(IDtutor -> {
					tutoresNoDisponibles.add(ArchivoJson.<Integer, Tutor>obtenerRegistroEspecifico("registros_tutores.json", IDtutor));
				});
				
				solicitud.setID(ID);
				solicitud.setAlumnoAsesorado(alumno);
				solicitud.setTutorAsesorias(tutor);
				solicitud.setFechaPeticion(solicitudSimplificada.getFechaPeticion());
				solicitud.setUrgencia(solicitudSimplificada.isUrgencia());
				solicitud.setMateriaAsesoria(materia);
				solicitud.setTema(solicitudSimplificada.getTema());
				solicitud.setDescripcion(solicitudSimplificada.getDescripcion());
				solicitud.setFechaAsesoria(solicitudSimplificada.getFechaAsesoria());
				solicitud.setSitio(solicitudSimplificada.getSitio());
				solicitud.setModalidad(solicitudSimplificada.getModalidad());
				solicitud.setTutoresNoDisponibles(tutoresNoDisponibles);
				return solicitud;
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerTodos")
	public Solicitud[] obtenerTodasSolicitudes() {
		try {
			Map<Integer, SolicitudSimplificada> listaSolicitudes = ArchivoJson.<Integer, SolicitudSimplificada>obtenerRegistros(nombreArchivo);
			Map<Integer, Solicitud> listaSolicitudesDesglosadas = desglosarDatosSolicitudes(listaSolicitudes);
			
			if(listaSolicitudesDesglosadas != null) {
				return listaSolicitudesDesglosadas.values().toArray(new Solicitud[0]);
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerPorAlumno")
	public Solicitud[] obtenerSolicitudesAlumno(int IDAlumno) {
		try {
			Map<Integer, SolicitudSimplificada> listaSolicitudes = ArchivoJson.<Integer, SolicitudSimplificada>obtenerRegistros(nombreArchivo);
			listaSolicitudes.values().removeIf(solicitud -> solicitud.getAlumnoAsesorado() != IDAlumno);
			Map<Integer, Solicitud> listaSolicitudesDesglosadas = desglosarDatosSolicitudes(listaSolicitudes);
			
			if(listaSolicitudesDesglosadas != null) {
				return listaSolicitudesDesglosadas.values().toArray(new Solicitud[0]);
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @POST @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/agregar")
	public Respuesta agregarSolicitud(SolicitudSimplificada solicitud) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la solicitud está registrada en el servidor.
			SolicitudSimplificada busquedaSolicitud = ArchivoJson.<Integer, SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, solicitud.getID());
			if(busquedaSolicitud != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el ID no. " + solicitud.getID() + " ya está registrada en el servidor.");
			}
			
			ArchivoJson.<Integer, SolicitudSimplificada>agregarRegistro(nombreArchivo, solicitud.getID(), solicitud);
			respuesta.setEstado(true);
			respuesta.setMensaje("La solicitud fue exitosamente añadida al servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar la solicitud en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar")
	public Respuesta actualizarSolicitud(int ID, SolicitudSimplificada solicitud) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la solicitud está registrada en el servidor.
			SolicitudSimplificada busquedaSolicitud = ArchivoJson.<Integer, SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaSolicitud == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el ID no. " + ID + " no está registrada en el servidor.");
			}
			
			ArchivoJson.<Integer, SolicitudSimplificada>actualizarRegistro(nombreArchivo, ID, solicitud);
			respuesta.setEstado(true);
			respuesta.setMensaje("La solicitud fue exitosamente actualizada en el servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar la solicitud en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar")
	public Respuesta eliminarSolicitud(int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la solicitud está registrado en el servidor.
			SolicitudSimplificada busquedaSolicitud = ArchivoJson.<Integer, SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaSolicitud == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el ID no. " + ID + " no está registrada en el servidor.");
			}
			
			ArchivoJson.<Integer, SolicitudSimplificada>eliminarRegistro(nombreArchivo, ID);
			respuesta.setEstado(true);
			respuesta.setMensaje("La solicitud fue exitosamente eliminada del servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar la solicitud del servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	private Map<Integer, Solicitud> desglosarDatosSolicitudes(Map<Integer, SolicitudSimplificada> solicitudes) {
		Map<Integer, Solicitud> listaSolicitudesDesglosadas = new HashMap<>();
		solicitudes.forEach((indice, valor) -> {
			Solicitud solicitud = new Solicitud();
			
			try {
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico("registros_alumnos.json", valor.getAlumnoAsesorado());
				if(alumno == null) {
					throw new Exception();
				}
				
				// Obtener los datos de la materia referenciada en el archivo.
				Materia materia = ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico("registros_materias.json", valor.getMateriaAsesoria());
				if(materia == null) {
					throw new Exception();
				}
				
				// Obtener los datos del tutor referenciado en el archivo.
				Tutor tutor = ArchivoJson.<Integer, Tutor>obtenerRegistroEspecifico("registros_tutores.json", valor.getTutorAsesorias());
				
				// Obtener los datos del tutor no disponibles referenciados en el archivo.
				ArrayList<Tutor> tutoresNoDisponibles = new ArrayList<>();
				valor.getTutoresNoDisponibles().forEach(IDtutor -> {
					tutoresNoDisponibles.add(ArchivoJson.<Integer, Tutor>obtenerRegistroEspecifico("registros_tutores.json", IDtutor));
				});
				
				solicitud.setID(valor.getID());
				solicitud.setAlumnoAsesorado(alumno);
				solicitud.setTutorAsesorias(tutor);
				solicitud.setFechaPeticion(valor.getFechaPeticion());
				solicitud.setUrgencia(valor.isUrgencia());
				solicitud.setMateriaAsesoria(materia);
				solicitud.setTema(valor.getTema());
				solicitud.setDescripcion(valor.getDescripcion());
				solicitud.setFechaAsesoria(valor.getFechaAsesoria());
				solicitud.setSitio(valor.getSitio());
				solicitud.setModalidad(valor.getModalidad());
				solicitud.setTutoresNoDisponibles(tutoresNoDisponibles);
				listaSolicitudesDesglosadas.put(indice, solicitud);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		});
		return listaSolicitudesDesglosadas;
	}
}