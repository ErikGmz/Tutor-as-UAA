package com.tutorias.uaa.servicios;

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
import com.tutorias.uaa.modelos.Materia;
import com.tutorias.uaa.modelos.Respuesta;

@Path("/materia")
public class ImplementacionServicioMateria implements ServicioMateria {
	//---Atributos---//.
	private static String nombreArchivo = "registros_materias.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener")
	public Materia obtenerMateria(int ID) {
		try {
			return ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico(nombreArchivo, ID);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerTodos")
	public Materia[] obtenerTodasMaterias() {
		try {
			return ArchivoJson.<Integer, Materia>obtenerRegistros(nombreArchivo).values().toArray(new Materia[0]);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerPorSemestre")
	public Materia[] obtenerMateriasSemestre(int semestre) {
		try {
			Map<Integer, Materia> listaMaterias = ArchivoJson.<Integer, Materia>obtenerRegistros(nombreArchivo);
			listaMaterias.values().removeIf(materia -> materia.getSemestre() != semestre);
			return listaMaterias.values().toArray(new Materia[0]);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override @POST @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/agregar")
	public Respuesta agregarMateria(Materia materia) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la materia está registrada en el servidor.
			Materia busquedaMateria = ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico(nombreArchivo, materia.getID());
			if(busquedaMateria != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el ID no. " + materia.getID() + " ya está registrada en el servidor.");
			}
			
			ArchivoJson.<Integer, Materia>agregarRegistro(nombreArchivo, materia.getID(), materia);
			respuesta.setEstado(true);
			respuesta.setMensaje("La materia fue exitosamente añadida al servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar la materia en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar")
	public Respuesta actualizarMateria(int ID, Materia materia) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el alumno está registrado en el servidor.
			Materia busquedaMateria = ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaMateria == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el ID no. " + ID + " no está registrada en el servidor.");
			}
			
			ArchivoJson.<Integer, Materia>actualizarRegistro(nombreArchivo, ID, materia);
			respuesta.setEstado(true);
			respuesta.setMensaje("La materia fue exitosamente actualizada en el servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar la materia en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar")
	public Respuesta eliminarMateria(int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la materia está registrada en el servidor.
			Materia busquedaMateria = ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaMateria == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el ID no. " + ID + " no está registrada en el servidor.");
			}
			
			ArchivoJson.<Integer, Materia>eliminarRegistro(nombreArchivo, ID);
			respuesta.setEstado(true);
			respuesta.setMensaje("La materia fue exitosamente eliminada del servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar la materia del servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
}