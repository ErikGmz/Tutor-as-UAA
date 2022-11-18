package com.tutorias.uaa.servicios;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.tutorias.uaa.modelos.TutorSimplificado;
import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.SolicitudSimplificada;

@Path("/alumno")
public class ImplementacionServicioAlumno implements ServicioAlumno {
	//---Atributos---//.
	public static String nombreArchivo = "C:\\Pruebas\\registros_alumnos.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener/{id}")
	public Alumno obtenerAlumno(@PathParam("id") int ID) {
		try {
			Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(nombreArchivo, ID, new Alumno());
			if(alumno != null) {
				return alumno;
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
	public Alumno[] obtenerTodosAlumnos() {
		try {
			Map<Integer, Alumno> listaAlumnos = ArchivoJson.<Alumno>obtenerRegistros(nombreArchivo, new Alumno());
			if(listaAlumnos != null & !listaAlumnos.isEmpty()) {
				return listaAlumnos.values().toArray(new Alumno[0]);
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
	public Respuesta agregarAlumno(Alumno alumno) {
		Respuesta respuesta = new Respuesta();
		
		try {		
			//Verificar si el alumno está registrado en el servidor.
			if(alumno.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El ID del alumno no puede equivaler a 0.");
				return respuesta;
			}
			
			Alumno busquedaAlumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(nombreArchivo, alumno.getID(), new Alumno());
			if(busquedaAlumno != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el ID no. " + alumno.getID() + " ya está registrado en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<Alumno>agregarRegistro(nombreArchivo, alumno.getID(), alumno)) {
				respuesta.setEstado(true);
				respuesta.setMensaje("El alumno con el ID no. " + alumno.getID() + " fue exitosamente añadido al servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar al alumno con el ID no. " + alumno.getID() + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar/{id}")
	public Respuesta actualizarAlumno(@PathParam("id") int ID, Alumno alumno) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el alumno está registrado en el servidor.
			if(alumno.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El nuevo ID del alumno no puede equivaler a 0.");
				return respuesta;
			}
			
			Alumno busquedaAlumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(nombreArchivo, ID, new Alumno());
			if(busquedaAlumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el ID no. " + ID + " no está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si el nuevo alumno está registrado en el servidor.
			busquedaAlumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(nombreArchivo, alumno.getID(), new Alumno());
			if(busquedaAlumno != null && busquedaAlumno.getID() != ID) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el nuevo ID no. " + alumno.getID() + " ya está registrado en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<Alumno>actualizarRegistro(nombreArchivo, ID, alumno)) {
				//Actualizar los registros relacionados con el ID del alumno.
				actualizarRegistrosAlumno(ID, alumno.getID());
				
				respuesta.setEstado(true);
				respuesta.setMensaje("El alumno con el ID no. " + ID + " fue exitosamente actualizado en el servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar al alumno con el ID. no " + ID + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar/{id}")
	public Respuesta eliminarAlumno(@PathParam("id") int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el alumno está registrado en el servidor.
			Alumno busquedaAlumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(nombreArchivo, ID, new Alumno());
			if(busquedaAlumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el ID no. " + ID + " no está registrado en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<Alumno>eliminarRegistro(nombreArchivo, ID, new Alumno())) {
				//Eliminar los registros relacionados con el ID del alumno.
				eliminarRegistrosAlumno(ID);
				
				respuesta.setEstado(true);
				respuesta.setMensaje("El alumno con el ID no. " + ID + " fue exitosamente eliminado del servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar al alumno con el ID no. " + ID + " del servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}

	private void actualizarRegistrosAlumno(int IDReferencia, int IDNuevo) throws Exception {
		if(IDReferencia != IDNuevo) {
			//Actualizar los tutores con el ID del alumno eliminado.
			Map<Integer, TutorSimplificado> registrosTutores = ArchivoJson.<TutorSimplificado>obtenerRegistros(ImplementacionServicioTutor.nombreArchivo, new TutorSimplificado());
			
			registrosTutores.forEach((indice, valor) -> {
				if(valor.getIDAlumnoAsesorias() == IDReferencia) {
					valor.setIDAlumnoAsesorias(IDNuevo);
				}
			});
			ArchivoJson.<TutorSimplificado>sobreescribirArchivo(ImplementacionServicioTutor.nombreArchivo, registrosTutores, new TutorSimplificado());
					
			//Actualizar las solicitudes con el ID del alumno eliminado.
			Map<Integer, SolicitudSimplificada> registrosSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(ImplementacionServicioSolicitud.nombreArchivo, new SolicitudSimplificada());
			registrosSolicitudes.forEach((indice, valor) -> {
				if(valor.getAlumnoAsesorado() == IDReferencia) {
					valor.setAlumnoAsesorado(IDNuevo);
				}
			});
			ArchivoJson.<SolicitudSimplificada>sobreescribirArchivo(ImplementacionServicioSolicitud.nombreArchivo, registrosSolicitudes, new SolicitudSimplificada());
		}
	}
	
	private void eliminarRegistrosAlumno(int ID) throws Exception {
		//Eliminar los tutores con el ID del alumno eliminado.
		Map<Integer, TutorSimplificado> registrosTutores = ArchivoJson.<TutorSimplificado>obtenerRegistros(ImplementacionServicioTutor.nombreArchivo, new TutorSimplificado());
		List<Integer> tutoresEliminados = registrosTutores.entrySet().stream()
		.filter(valor -> valor.getValue().getIDAlumnoAsesorias() == ID)
		.map(Map.Entry::getKey)
		.collect(Collectors.toList());
		
		registrosTutores.entrySet().removeIf(registro -> registro.getValue().getIDAlumnoAsesorias() == ID);
		ArchivoJson.<TutorSimplificado>sobreescribirArchivo(ImplementacionServicioTutor.nombreArchivo, registrosTutores, new TutorSimplificado());
				
		//Eliminar las solicitudes con el ID del alumno eliminado.
		Map<Integer, SolicitudSimplificada> registrosSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(ImplementacionServicioSolicitud.nombreArchivo, new SolicitudSimplificada());
		registrosSolicitudes.entrySet().removeIf(registro -> registro.getValue().getAlumnoAsesorado() == ID);
		
		//Actualizar las solicitudes con el ID del tutor relacionado con el alumno eliminado.
		registrosSolicitudes.forEach((indice, valor) -> {
			if(tutoresEliminados.contains(valor.getTutorAsesorias())) {
				valor.setTutorAsesorias(0);
			}
			valor.getTutoresNoDisponibles().removeAll(tutoresEliminados);
		});
		ArchivoJson.<SolicitudSimplificada>sobreescribirArchivo(ImplementacionServicioSolicitud.nombreArchivo, registrosSolicitudes, new SolicitudSimplificada());
	}
}