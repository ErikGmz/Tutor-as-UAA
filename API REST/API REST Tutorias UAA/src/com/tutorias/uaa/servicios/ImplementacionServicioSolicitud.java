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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tutorias.uaa.archivos.ArchivoJson;
import com.tutorias.uaa.modelos.Alumno;
import com.tutorias.uaa.modelos.Materia;
import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.Solicitud;
import com.tutorias.uaa.modelos.SolicitudSimplificada;
import com.tutorias.uaa.modelos.Tutor;
import com.tutorias.uaa.modelos.TutorSimplificado;

@Path("/solicitud")
public class ImplementacionServicioSolicitud implements ServicioSolicitud {
	//---Atributos---//.
	public static String nombreArchivo = "C:\\Pruebas\\registros_solicitudes.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener/{id}")
	public Solicitud obtenerSolicitud(@PathParam("id") int ID) {
		try {
			SolicitudSimplificada solicitudSimplificada = ArchivoJson.<SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, ID, new SolicitudSimplificada());
			
			if(solicitudSimplificada != null) {
				Solicitud solicitud = new Solicitud();
				
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, solicitudSimplificada.getAlumnoAsesorado(), new Alumno());
				System.out.println(alumno);
				if(alumno == null) {
					return null;
				}
				
				// Obtener los datos de la materia referenciada en el archivo.
				Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, solicitudSimplificada.getMateriaAsesoria(), new Materia());
				if(materia == null) {
					return null;
				}
				
				// Obtener los datos del tutor referenciado en el archivo.
				TutorSimplificado tutorAuxiliar = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(ImplementacionServicioTutor.nombreArchivo, solicitudSimplificada.getTutorAsesorias(), new TutorSimplificado());
				Tutor tutor = new Tutor();
				
				if(tutorAuxiliar != null) {
					//Obtener los datos del alumno referenciado en el archivo.
					Alumno alumnoAuxiliar = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, tutorAuxiliar.getIDAlumnoAsesorias(), new Alumno());
					
					//Obtener los datos de las materias que imparte el tutor, referenciados en el archivo.
					ArrayList<Materia> materiasAsesorias = new ArrayList<>();
					tutorAuxiliar.getIDsMateriasAsesorias().forEach(IDmateria -> {
						materiasAsesorias.add(ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, IDmateria, new Materia()));
					});
					
					tutor.setID(tutorAuxiliar.getID());
					tutor.setAlumnoAsesorias(alumnoAuxiliar);
					tutor.setMateriasAsesorias(materiasAsesorias);
				}
				
				// Obtener los datos de los tutores no disponibles referenciados en el archivo.
				Map<Integer, TutorSimplificado> tutoresSimplificados = ArchivoJson.<TutorSimplificado>obtenerRegistros(ImplementacionServicioTutor.nombreArchivo, new TutorSimplificado());
				ArrayList<Tutor> tutoresNoDisponibles = new ArrayList<>(ImplementacionServicioTutor.desglosarDatosTutores(tutoresSimplificados).values());
				tutoresNoDisponibles.removeIf(registro -> !solicitudSimplificada.getTutoresNoDisponibles().contains(registro.getID()));
				
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
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerTodas")
	public Solicitud[] obtenerTodasSolicitudes() {
		try {
			Map<Integer, SolicitudSimplificada> listaSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(nombreArchivo, new SolicitudSimplificada());
			if(listaSolicitudes != null && !listaSolicitudes.isEmpty()) {
				Map<Integer, Solicitud> listaSolicitudesDesglosadas = desglosarDatosSolicitudes(listaSolicitudes);
			
				if(listaSolicitudesDesglosadas != null) {
					return listaSolicitudesDesglosadas.values().toArray(new Solicitud[0]);
				}
				else {
					return null;
				}
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
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerPorAlumno/{idAlumno}")
	public Solicitud[] obtenerSolicitudesAlumno(@PathParam("idAlumno") int IDAlumno) {
		try {
			Map<Integer, SolicitudSimplificada> listaSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(nombreArchivo, new SolicitudSimplificada());
			if(listaSolicitudes != null && !listaSolicitudes.isEmpty()) {
				listaSolicitudes.values().removeIf(solicitud -> solicitud.getAlumnoAsesorado() != IDAlumno);
				Map<Integer, Solicitud> listaSolicitudesDesglosadas = desglosarDatosSolicitudes(listaSolicitudes);
				
				if(listaSolicitudesDesglosadas != null) {
					return listaSolicitudesDesglosadas.values().toArray(new Solicitud[0]);
				}
				else {
					return null;
				}
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
			if(solicitud.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El ID de la solicitud no puede equivaler a 0.");
				return respuesta;
			}
			
			//Verificar si la solicitud está registrada en el servidor.
			SolicitudSimplificada busquedaSolicitud = ArchivoJson.<SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, solicitud.getID(), new SolicitudSimplificada());
			if(busquedaSolicitud != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el ID no. " + solicitud.getID() + " ya está registrada en el servidor.");
				return respuesta;
			}
			
			//Verificar si el alumno especificado está registrado en el servidor.
			Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, solicitud.getAlumnoAsesorado(), new Alumno());
			if(alumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno asesorado con el ID no. " + solicitud.getAlumnoAsesorado() + " no está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si el tutor especificado está registrado en el servidor.
			if(solicitud.getTutorAsesorias() != 0) {
				TutorSimplificado tutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(ImplementacionServicioTutor.nombreArchivo, solicitud.getTutorAsesorias(), new TutorSimplificado());
				if(tutor == null || tutor.getID() == 0) {
					respuesta.setEstado(false);
					respuesta.setMensaje("El tutor de asesorías con el ID no. " + solicitud.getTutorAsesorias() + " no está registrado en el servidor.");
					return respuesta;
				}
			}
			
			//Verificar si la materia especificada está registrada en el servidor.
			Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, solicitud.getMateriaAsesoria(), new Materia());
			if(materia == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La nueva materia de asesoría con el ID no. " + solicitud.getMateriaAsesoria() + " no está registrada en el servidor.");
				return respuesta;
			}
			
			//Verificar si los tutores no disponibles están registrados en el servidor.
			for(Integer IDTutor : solicitud.getTutoresNoDisponibles()) {
				TutorSimplificado tutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(ImplementacionServicioTutor.nombreArchivo, IDTutor, new TutorSimplificado());
				if(tutor == null) {
					respuesta.setEstado(false);
					respuesta.setMensaje("El tutor no disponible de asesorías con el ID no. " + IDTutor + " no está registrado en el servidor.");
					return respuesta;
				}
			}
			
			if(ArchivoJson.<SolicitudSimplificada>agregarRegistro(nombreArchivo, solicitud.getID(), solicitud)) {
				respuesta.setEstado(true);
				respuesta.setMensaje("La solicitud con el ID no. " + solicitud.getID() + " fue exitosamente añadida al servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar la solicitud con el ID no. " + solicitud.getID() + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar/{id}")
	public Respuesta actualizarSolicitud(@PathParam("id") int ID, SolicitudSimplificada solicitud) {
		Respuesta respuesta = new Respuesta();
		
		try {
			if(solicitud.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El nuevo ID de la solicitud no puede equivaler a 0.");
				return respuesta;
			}
			
			//Verificar si la solicitud está registrada en el servidor.
			SolicitudSimplificada busquedaSolicitud = ArchivoJson.<SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, ID, new SolicitudSimplificada());
			if(busquedaSolicitud == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el ID no. " + ID + " no está registrada en el servidor.");
				return respuesta;
			}
			
			//Verificar si la nueva solicitud está registrada en el servidor.
			busquedaSolicitud = ArchivoJson.<SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, solicitud.getID(), new SolicitudSimplificada());
			if(busquedaSolicitud != null && solicitud.getID() != ID) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el nuevo ID no. " + solicitud.getID() + " ya está registrada en el servidor.");
				return respuesta;
			}
			
			//Verificar si el alumno especificado está registrado en el servidor.
			Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, solicitud.getAlumnoAsesorado(), new Alumno());
			if(alumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno asesorado con el ID no. " + solicitud.getAlumnoAsesorado() + " no está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si el tutor especificado está registrado en el servidor.
			if(solicitud.getTutorAsesorias() != 0) {
				TutorSimplificado tutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(ImplementacionServicioTutor.nombreArchivo, solicitud.getTutorAsesorias(), new TutorSimplificado());
				if(tutor == null || tutor.getID() == 0) {
					respuesta.setEstado(false);
					respuesta.setMensaje("El tutor de asesorías con el ID no. " + solicitud.getTutorAsesorias() + " no está registrado en el servidor.");
					return respuesta;
				}
			}
			
			//Verificar si la materia especificada está registrada en el servidor.
			Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, solicitud.getMateriaAsesoria(), new Materia());
			if(materia == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La nueva materia de asesoría con el ID no. " + solicitud.getMateriaAsesoria() + " no está registrada en el servidor.");
				return respuesta;
			}
			
			//Verificar si los tutores no disponibles están registrados en el servidor.
			for(Integer IDTutor : solicitud.getTutoresNoDisponibles()) {
				TutorSimplificado tutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(ImplementacionServicioTutor.nombreArchivo, IDTutor, new TutorSimplificado());
				if(tutor == null) {
					respuesta.setEstado(false);
					respuesta.setMensaje("El tutor no disponible de asesorías con el ID no. " + IDTutor + " no está registrado en el servidor.");
					return respuesta;
				}
			}
			
			if(ArchivoJson.<SolicitudSimplificada>actualizarRegistro(nombreArchivo, ID, solicitud)) {
				respuesta.setEstado(true);
				respuesta.setMensaje("La solicitud con el ID no. " + ID + " fue exitosamente actualizada en el servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
			
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar la solicitud con el ID no. " + ID + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar/{id}")
	public Respuesta eliminarSolicitud(@PathParam("id") int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la solicitud está registrado en el servidor.
			SolicitudSimplificada busquedaSolicitud = ArchivoJson.<SolicitudSimplificada>obtenerRegistroEspecifico(nombreArchivo, ID, new SolicitudSimplificada());
			if(busquedaSolicitud == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La solicitud con el ID no. " + ID + " no está registrada en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<SolicitudSimplificada>eliminarRegistro(nombreArchivo, ID, new SolicitudSimplificada())) {
				respuesta.setEstado(true);
				respuesta.setMensaje("La solicitud con el ID no. " + ID + " fue exitosamente eliminada del servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar la solicitud con el ID no. " + ID + " del servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	private Map<Integer, Solicitud> desglosarDatosSolicitudes(Map<Integer, SolicitudSimplificada> solicitudes) {
		Map<Integer, Solicitud> listaSolicitudesDesglosadas = new HashMap<>();
		solicitudes.forEach((indice, valor) -> {
			Solicitud solicitud = new Solicitud();
			
			try {
				//Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, valor.getAlumnoAsesorado(), new Alumno());
				if(alumno == null) {
					throw new Exception();
				}
				
				//Obtener los datos de la materia referenciada en el archivo.
				Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, valor.getMateriaAsesoria(), new Materia());
				if(materia == null) {
					throw new Exception();
				}
				
				//Obtener los datos del tutor referenciado en el archivo.
				TutorSimplificado tutorAuxiliar = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(ImplementacionServicioTutor.nombreArchivo, valor.getTutorAsesorias(), new TutorSimplificado());
				Tutor tutor = new Tutor();
				
				if(tutorAuxiliar != null) {
					//Obtener los datos del alumno referenciado en el archivo.
					Alumno alumnoAuxiliar = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, tutorAuxiliar.getIDAlumnoAsesorias(), new Alumno());
					
					//Obtener los datos de las materias que imparte el tutor, referenciados en el archivo.
					ArrayList<Materia> materiasAsesorias = new ArrayList<>();
					tutorAuxiliar.getIDsMateriasAsesorias().forEach(IDmateria -> {
						materiasAsesorias.add(ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, IDmateria, new Materia()));
					});
					
					tutor.setID(tutorAuxiliar.getID());
					tutor.setAlumnoAsesorias(alumnoAuxiliar);
					tutor.setMateriasAsesorias(materiasAsesorias);
				}
				
				// Obtener los datos de los tutores no disponibles referenciados en el archivo.
				Map<Integer, TutorSimplificado> tutoresSimplificados = ArchivoJson.<TutorSimplificado>obtenerRegistros(ImplementacionServicioTutor.nombreArchivo, new TutorSimplificado());
				ArrayList<Tutor> tutoresNoDisponibles = new ArrayList<>(ImplementacionServicioTutor.desglosarDatosTutores(tutoresSimplificados).values());
				tutoresNoDisponibles.removeIf(registro -> !valor.getTutoresNoDisponibles().contains(registro.getID()));
				
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