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
import com.tutorias.uaa.modelos.SolicitudSimplificada;
import com.tutorias.uaa.modelos.Tutor;
import com.tutorias.uaa.modelos.TutorSimplificado;

@Path("/tutor")
public class ImplementacionServicioTutor implements ServicioTutor {
	//---Atributos---//.
	public static String nombreArchivo = "C:\\Pruebas\\registros_tutores.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener/{id}")
	public Tutor obtenerTutor(@PathParam("id") int ID) {
		try {
			TutorSimplificado tutorSimplificado = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, ID, new TutorSimplificado());
			
			if(tutorSimplificado != null) {
				Tutor tutor = new Tutor();
				
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, tutorSimplificado.getIDAlumnoAsesorias(), new Alumno());
				if(alumno == null) {
					return null;
				}
				
				// Obtener los datos de las materias que imparte el tutor, referenciados en el archivo.
				ArrayList<Materia> materiasAsesorias = new ArrayList<>();
				tutorSimplificado.getIDsMateriasAsesorias().forEach(IDmateria -> {
					materiasAsesorias.add(ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, IDmateria, new Materia()));
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
			Map<Integer, TutorSimplificado> listaTutores = ArchivoJson.<TutorSimplificado>obtenerRegistros(nombreArchivo, new TutorSimplificado());
			
			if(listaTutores != null && !listaTutores.isEmpty()) {
				Map<Integer, Tutor> listaSolicitudesDesglosadas = desglosarDatosTutores(listaTutores);
				
				if(listaSolicitudesDesglosadas != null) {
					return listaSolicitudesDesglosadas.values().toArray(new Tutor[0]);
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
	public Respuesta agregarTutor(TutorSimplificado tutor) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			if(tutor.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El ID del tutor no puede equivaler a 0.");
				return respuesta;
			}
			
			//Verificar si el tutor está registrado en el servidor.
			TutorSimplificado busquedaTutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, tutor.getID(), new TutorSimplificado());
			if(busquedaTutor != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el ID no. " + tutor.getID() + " ya está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si el alumno especificado está registrado en el servidor.
			Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, tutor.getIDAlumnoAsesorias(), new Alumno());
			if(alumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El alumno asesorado con el ID no. " + tutor.getIDAlumnoAsesorias() + " no está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si las materias especificadas están registradas en el servidor.
			for(Integer IDmateria : tutor.getIDsMateriasAsesorias()) {
				Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, IDmateria, new Materia());
				if(materia == null) {
					respuesta.setEstado(false);
					respuesta.setMensaje("La materia de asesoría con el ID no. " + IDmateria + " no está registrada en el servidor.");
					return respuesta;
				}
			}
			
			if(ArchivoJson.<TutorSimplificado>agregarRegistro(nombreArchivo, tutor.getID(), tutor)) {
				respuesta.setEstado(true);
				respuesta.setMensaje("El tutor con el ID no. " + tutor.getID() + " fue exitosamente añadido al servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar al tutor con el ID no. " + tutor.getID() + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar/{id}")
	public Respuesta actualizarTutor(@PathParam("id") int ID, TutorSimplificado tutor) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			if(tutor.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El nuevo ID del tutor no puede equivaler a 0.");
				return respuesta;
			}
			
			//Verificar si el tutor está registrado en el servidor.
			TutorSimplificado busquedaTutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, ID, new TutorSimplificado());
			if(busquedaTutor == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el ID no. " + ID + " no está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si el nuevo ID del tutor está registrado en el servidor.
			busquedaTutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, tutor.getID(), new TutorSimplificado());
			if(busquedaTutor != null && tutor.getID() != ID) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el nuevo ID no. " + tutor.getID() + " ya está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si el alumno especificado está registrado en el servidor.
			Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, tutor.getIDAlumnoAsesorias(), new Alumno());
			if(alumno == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El nuevo alumno asesorado con el ID no. " + tutor.getIDAlumnoAsesorias() + " no está registrado en el servidor.");
				return respuesta;
			}
			
			//Verificar si las materias especificadas están registradas en el servidor.
			for(Integer IDmateria : tutor.getIDsMateriasAsesorias()) {
				Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, IDmateria, new Materia());
				if(materia == null) {
					respuesta.setEstado(false);
					respuesta.setMensaje("La nueva materia de asesoría con el ID no. " + IDmateria + " no está registrada en el servidor.");
					return respuesta;
				}
			}
			
			if(ArchivoJson.<TutorSimplificado>actualizarRegistro(nombreArchivo, ID, tutor)) {
				//Actualizar los registros relacionados con el ID del tutor.
				actualizarRegistrosTutor(ID, tutor.getID());
				
				respuesta.setEstado(true);
				respuesta.setMensaje("El tutor con el ID no. " + ID + " fue exitosamente actualizado en el servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
			
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar al tutor con el ID no. " + ID + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar/{id}")
	public Respuesta eliminarTutor(@PathParam("id") int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si el tutor está registrado en el servidor.
			TutorSimplificado busquedaTutor = ArchivoJson.<TutorSimplificado>obtenerRegistroEspecifico(nombreArchivo, ID, new TutorSimplificado());
			if(busquedaTutor == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El tutor con el ID no. " + ID + " no está registrado en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<TutorSimplificado>eliminarRegistro(nombreArchivo, ID, new TutorSimplificado())) {
				//Eliminar los registros relacionados con el ID del tutor.
				eliminarRegistrosTutor(ID);
				
				respuesta.setEstado(true);
				respuesta.setMensaje("El tutor con el ID no. " + ID + " fue exitosamente eliminado del servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar al tutor con el ID no. " + ID + " del servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	public static Map<Integer, Tutor> desglosarDatosTutores(Map<Integer, TutorSimplificado> tutores) {
		Map<Integer, Tutor> listaTutoresDesglosados = new HashMap<>();
		tutores.forEach((indice, valor) -> {
			Tutor tutor = new Tutor();
			
			try {
				// Obtener los datos del alumno referenciado en el archivo.
				Alumno alumno = ArchivoJson.<Alumno>obtenerRegistroEspecifico(ImplementacionServicioAlumno.nombreArchivo, valor.getIDAlumnoAsesorias(), new Alumno());
				if(alumno == null) {
					throw new Exception();
				}
				
				// Obtener los datos de las materias que imparte el tutor, referenciados en el archivo.
				ArrayList<Materia> materiasAsesorias = new ArrayList<>();
				valor.getIDsMateriasAsesorias().forEach(IDmateria -> {
					materiasAsesorias.add(ArchivoJson.<Materia>obtenerRegistroEspecifico(ImplementacionServicioMateria.nombreArchivo, IDmateria, new Materia()));
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
	
	private void actualizarRegistrosTutor(int IDReferencia, int IDNuevo) throws Exception {
		if(IDReferencia != IDNuevo) {	
			//Actualizar las solicitudes con el ID del tutor actualizado.
			Map<Integer, SolicitudSimplificada> registrosSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(ImplementacionServicioSolicitud.nombreArchivo, new SolicitudSimplificada());
			registrosSolicitudes.forEach((indice, valor) -> {
				if(valor.getTutorAsesorias() == IDReferencia) {
					valor.setTutorAsesorias(IDNuevo);
				}
				valor.getTutoresNoDisponibles().replaceAll(valorID -> valorID == IDReferencia ? IDNuevo : valorID);
			});
			ArchivoJson.<SolicitudSimplificada>sobreescribirArchivo(ImplementacionServicioSolicitud.nombreArchivo, registrosSolicitudes, new SolicitudSimplificada());
		}
	}
	
	private void eliminarRegistrosTutor(int ID) throws Exception {
		//Actualizar las solicitudes con el ID del tutor actualizado.
		Map<Integer, SolicitudSimplificada> registrosSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(ImplementacionServicioSolicitud.nombreArchivo, new SolicitudSimplificada());
		registrosSolicitudes.forEach((indice, valor) -> {
			if(valor.getTutorAsesorias() == ID) {
				valor.setTutorAsesorias(0);
			}
			valor.getTutoresNoDisponibles().removeIf(valorID -> valorID == ID);
		});
		ArchivoJson.<SolicitudSimplificada>sobreescribirArchivo(ImplementacionServicioSolicitud.nombreArchivo, registrosSolicitudes, new SolicitudSimplificada());
	}
}