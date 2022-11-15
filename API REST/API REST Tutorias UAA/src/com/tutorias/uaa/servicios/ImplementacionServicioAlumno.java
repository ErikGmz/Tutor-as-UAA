package com.tutorias.uaa.servicios;

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
import com.tutorias.uaa.modelos.Respuesta;

@Path("/alumno")
public class ImplementacionServicioAlumno implements ServicioAlumno {
	//---Atributos---//.
	private static String nombreArchivo = "registros_alumnos.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener")
	public Alumno obtenerAlumno(int ID) {
		try {
			return ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico(nombreArchivo, ID);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerTodos")
	public Alumno[] obtenerTodosAlumnos() {
		try {
			return ArchivoJson.<Integer, Alumno>obtenerRegistros(nombreArchivo).values().toArray(new Alumno[0]);
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
			Alumno busquedaAlumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico(nombreArchivo, alumno.getID());
			if(busquedaAlumno != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el ID no. " + alumno.getID() + " ya está registrado en el servidor.");
			}
			
			ArchivoJson.<Integer, Alumno>agregarRegistro(nombreArchivo, alumno.getID(), alumno);
			respuesta.setEstado(true);
			respuesta.setMensaje("El alumno fue exitosamente añadido al servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar al alumno en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar")
	public Respuesta actualizarAlumno(int ID, Alumno alumno) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el alumno está registrado en el servidor.
			Alumno busquedaAlumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaAlumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el ID no. " + ID + " no está registrado en el servidor.");
			}
			
			ArchivoJson.<Integer, Alumno>actualizarRegistro(nombreArchivo, ID, alumno);
			respuesta.setEstado(true);
			respuesta.setMensaje("El alumno fue exitosamente actualizado en el servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar al alumno en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar")
	public Respuesta eliminarAlumno(int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el alumno está registrado en el servidor.
			Alumno busquedaAlumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaAlumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno con el ID no. " + ID + " no está registrado en el servidor.");
			}
			
			ArchivoJson.<Integer, Alumno>eliminarRegistro(nombreArchivo, ID);
			respuesta.setEstado(true);
			respuesta.setMensaje("El alumno fue exitosamente eliminado del servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar al alumno del servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
}