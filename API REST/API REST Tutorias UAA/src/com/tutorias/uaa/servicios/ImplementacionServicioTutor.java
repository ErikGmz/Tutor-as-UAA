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
import com.tutorias.uaa.modelos.Tutor;
import com.tutorias.uaa.modelos.TutorSimplificado;

@Path("/tutor")
public class ImplementacionServicioTutor implements ServicioTutor {
	//---Atributos---//.
	private static String nombreArchivo = "registros_tutores.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener")
	public Tutor obtenerTutor(int ID) {
		try {
			TutorSimplificado tutorSimplificado = ArchivoJson.<Integer, TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, ID);
			
			if(tutorSimplificado != null) {
				Tutor tutor = new Tutor();
				
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico("registros_alumnos.json", tutorSimplificado.getIDAlumnoAsesorias());
				if(alumno == null) {
					throw new Exception();
				}
				
				// Obtener los datos de las materias que imparte el tutor, referenciados en el archivo.
				ArrayList<Materia> materiasAsesorias = new ArrayList<>();
				tutorSimplificado.getIDsMateriasAsesorias().forEach(IDmateria -> {
					materiasAsesorias.add(ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico("registros_materias.json", IDmateria));
				});
				
				tutor.setID(tutorSimplificado.getID());
				tutor.setAlumnoAsesorias(alumno);
				tutor.setMateriasAsesorias(materiasAsesorias);
				return tutor;
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
	public Tutor[] obtenerTodosTutores() {
		try {
			Map<Integer, TutorSimplificado> listaTutores = ArchivoJson.<Integer, TutorSimplificado>obtenerRegistros(nombreArchivo);
			Map<Integer, Tutor> listaSolicitudesDesglosadas = desglosarDatosTutores(listaTutores);
			
			if(listaSolicitudesDesglosadas != null) {
				return listaSolicitudesDesglosadas.values().toArray(new Tutor[0]);
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
	public Respuesta agregarTutor(TutorSimplificado tutor) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el tutor está registrado en el servidor.
			TutorSimplificado busquedaTutor = ArchivoJson.<Integer, TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, tutor.getID());
			if(busquedaTutor != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el ID no. " + tutor.getID() + " ya está registrado en el servidor.");
			}
			
			ArchivoJson.<Integer, TutorSimplificado>agregarRegistro(nombreArchivo, tutor.getID(), tutor);
			respuesta.setEstado(true);
			respuesta.setMensaje("El tutor fue exitosamente añadido al servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar al tutor en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar")
	public Respuesta actualizarTutor(int ID, TutorSimplificado tutor) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el tutor está registrado en el servidor.
			TutorSimplificado busquedaTutor = ArchivoJson.<Integer, TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaTutor == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el ID no. " + ID + " no está registrado en el servidor.");
			}
			
			ArchivoJson.<Integer, TutorSimplificado>actualizarRegistro(nombreArchivo, ID, tutor);
			respuesta.setEstado(true);
			respuesta.setMensaje("El tutor fue exitosamente actualizado en el servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar al tutor en el servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar")
	public Respuesta eliminarTutor(int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el tutor está registrado en el servidor.
			TutorSimplificado busquedaTutor = ArchivoJson.<Integer, TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, ID);
			if(busquedaTutor == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el ID no. " + ID + " no está registrado en el servidor.");
			}
			
			ArchivoJson.<Integer, TutorSimplificado>eliminarRegistro(nombreArchivo, ID);
			respuesta.setEstado(true);
			respuesta.setMensaje("El tutor fue exitosamente eliminado del servidor.");
			return respuesta;
		} 
		catch(Exception e) {
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar al tutor del servidor."
			+ e.getMessage());
			return respuesta;
		}
	}
	
	private Map<Integer, Tutor> desglosarDatosTutores(Map<Integer, TutorSimplificado> tutores) {
		Map<Integer, Tutor> listaTutoresDesglosados = new HashMap<>();
		tutores.forEach((indice, valor) -> {
			Tutor tutor = new Tutor();
			
			try {
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Integer, Alumno>obtenerRegistroEspecifico("registros_alumnos.json", valor.getIDAlumnoAsesorias());
				if(alumno == null) {
					throw new Exception();
				}
				
				// Obtener los datos de las materias que imparte el tutor, referenciados en el archivo.
				ArrayList<Materia> materiasAsesorias = new ArrayList<>();
				valor.getIDsMateriasAsesorias().forEach(IDmateria -> {
					materiasAsesorias.add(ArchivoJson.<Integer, Materia>obtenerRegistroEspecifico("registros_materias.json", IDmateria));
				});
				
				tutor.setID(valor.getID());
				tutor.setAlumnoAsesorias(alumno);
				tutor.setMateriasAsesorias(materiasAsesorias);
				listaTutoresDesglosados.put(indice, tutor);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		});
		return listaTutoresDesglosados;
	}
}