package com.tutorias.uaa.servicios;

import java.util.Arrays;
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
import com.tutorias.uaa.modelos.Materia;
import com.tutorias.uaa.modelos.Respuesta;
import com.tutorias.uaa.modelos.SolicitudSimplificada;
import com.tutorias.uaa.modelos.TutorSimplificado;

@Path("/materia")
public class ImplementacionServicioMateria implements ServicioMateria {
	//---Atributos---//.
	public static String nombreArchivo = "C:\\Pruebas\\registros_materias.json";
	
	//---Métodos---//.
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtener/{id}")
	public Materia obtenerMateria(@PathParam("id") int ID) {
		try {
			Materia materia = ArchivoJson.<Materia>obtenerRegistroEspecifico(nombreArchivo, ID, new Materia());
			if(materia != null) {
				return materia;
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
	public Materia[] obtenerTodasMaterias() {
		try {
			Map<Integer, Materia> listaMaterias = ArchivoJson.<Materia>obtenerRegistros(nombreArchivo, new Materia());
			if(listaMaterias != null && !listaMaterias.isEmpty()) {
				return listaMaterias.values().toArray(new Materia[0]);
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
	
	@Override @GET @Produces(MediaType.APPLICATION_JSON) @Path("/obtenerPorSemestre/{semestre}")
	public Materia[] obtenerMateriasSemestre(@PathParam("semestre") int semestre) {
		try {
			Map<Integer, Materia> listaMaterias = ArchivoJson.<Materia>obtenerRegistros(nombreArchivo, new Materia());
			if(listaMaterias != null && !listaMaterias.isEmpty()) {
				listaMaterias.values().removeIf(materia -> materia.getSemestre() != semestre);
				return listaMaterias.values().toArray(new Materia[0]);
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
	public Respuesta agregarMateria(Materia materia) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la materia está registrada en el servidor.
			if(materia.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El ID de la materia no puede equivaler a 0.");
				return respuesta;
			}
			
			Materia busquedaMateria = ArchivoJson.<Materia>obtenerRegistroEspecifico(nombreArchivo, materia.getID(), new Materia());
			if(busquedaMateria != null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el ID no. " + materia.getID() + " ya está registrada en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<Materia>agregarRegistro(nombreArchivo, materia.getID(), materia)) {
				respuesta.setEstado(true);
				respuesta.setMensaje("La materia con el ID no. " + materia.getID() + " fue exitosamente añadida al servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de registrar la materia con el ID no. " + materia.getID() + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @PUT @Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON) @Path("/actualizar/{id}")
	public Respuesta actualizarMateria(@PathParam("id") int ID, Materia materia) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la materia está registrado en el servidor.
			if(materia.getID() == 0) {
				respuesta.setEstado(false);
				respuesta.setMensaje("El nuevo ID de la materia no puede equivaler a 0.");
				return respuesta;
			}
			
			Materia busquedaMateria = ArchivoJson.<Materia>obtenerRegistroEspecifico(nombreArchivo, ID, new Materia());
			if(busquedaMateria == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el ID no. " + ID + " no está registrada en el servidor.");
				return respuesta;
			}
			
			//Verificar si la materia nueva está registrado en el servidor.
			busquedaMateria = ArchivoJson.<Materia>obtenerRegistroEspecifico(nombreArchivo, materia.getID(), new Materia());
			if(busquedaMateria != null && materia.getID() != ID) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el nuevo ID no. " + materia.getID() + " ya está registrada en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<Materia>actualizarRegistro(nombreArchivo, ID, materia)) {
				//Actualizar los registros relacionados con el ID de la materia.
				actualizarRegistrosMateria(ID, materia.getID());
				
				respuesta.setEstado(true);
				respuesta.setMensaje("La materia con el ID no. " + ID + " fue exitosamente actualizada en el servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de actualizar la materia con el ID no. " + ID + " en el servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	@Override @DELETE @Produces(MediaType.APPLICATION_JSON) @Path("/eliminar/{id}")
	public Respuesta eliminarMateria(@PathParam("id") int ID) {
		Respuesta respuesta = new Respuesta();
		
		try {	
			//Verificar si la materia está registrada en el servidor.
			Materia busquedaMateria = ArchivoJson.<Materia>obtenerRegistroEspecifico(nombreArchivo, ID, new Materia());
			if(busquedaMateria == null) {
				respuesta.setEstado(false);
				respuesta.setMensaje("La materia con el ID no. " + ID + " no está registrada en el servidor.");
				return respuesta;
			}
			
			if(ArchivoJson.<Materia>eliminarRegistro(nombreArchivo, ID, new Materia())) {
				//Eliminar los registros relacionados con el ID de la materia.
				eliminarRegistrosMateria(ID);
				
				respuesta.setEstado(true);
				respuesta.setMensaje("La materia con el ID no. " + ID + " fue exitosamente eliminada del servidor.");
				return respuesta;
			}
			else {
				throw new Exception();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			respuesta.setEstado(false);
			respuesta.setMensaje("Ocurrió un error al tratar de eliminar la materia con el ID no. " + ID + " del servidor. "
			+ e.getMessage());
			return respuesta;
		}
	}
	
	private void actualizarRegistrosMateria(int IDReferencia, int IDNuevo) throws Exception {
		if(IDReferencia != IDNuevo) {
			//Actualizar los tutores con el ID de la materia actualizada.
			Map<Integer, TutorSimplificado> registrosTutores = ArchivoJson.<TutorSimplificado>obtenerRegistros(ImplementacionServicioTutor.nombreArchivo, new TutorSimplificado());
			
			if(registrosTutores != null) {
				registrosTutores.forEach((indice, valor) -> {
					valor.getIDsMateriasAsesorias().replaceAll(valorID -> valorID == IDReferencia ? IDNuevo : valorID);
				});
				ArchivoJson.<TutorSimplificado>sobreescribirArchivo(ImplementacionServicioTutor.nombreArchivo, registrosTutores, new TutorSimplificado());
			}
			
			//Actualizar las solicitudes con el ID de la materia actualizada.
			Map<Integer, SolicitudSimplificada> registrosSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(ImplementacionServicioSolicitud.nombreArchivo, new SolicitudSimplificada());
			
			if(registrosSolicitudes != null) {
				registrosSolicitudes.forEach((indice, valor) -> {
					if(valor.getMateriaAsesoria() == IDReferencia) {
						valor.setMateriaAsesoria(IDNuevo);
					}
				});
				ArchivoJson.<SolicitudSimplificada>sobreescribirArchivo(ImplementacionServicioSolicitud.nombreArchivo, registrosSolicitudes, new SolicitudSimplificada());
			}
		}
	}
	
	private void eliminarRegistrosMateria(int ID) throws Exception {
		//Actualizar los tutores con el ID de la materia eliminada.
		Map<Integer, TutorSimplificado> registrosTutores = ArchivoJson.<TutorSimplificado>obtenerRegistros(ImplementacionServicioTutor.nombreArchivo, new TutorSimplificado());
		
		if(registrosTutores != null) {
			registrosTutores.forEach((indice, valor) -> {
				valor.getIDsMateriasAsesorias().removeAll(Arrays.asList(ID));
			});
			ArchivoJson.<TutorSimplificado>sobreescribirArchivo(ImplementacionServicioTutor.nombreArchivo, registrosTutores, new TutorSimplificado());
		}
					
		//Eliminar las solicitudes con el ID de la materia eliminada.
		Map<Integer, SolicitudSimplificada> registrosSolicitudes = ArchivoJson.<SolicitudSimplificada>obtenerRegistros(ImplementacionServicioSolicitud.nombreArchivo, new SolicitudSimplificada());
		
		if(registrosSolicitudes != null) {
			registrosSolicitudes.entrySet().removeIf(registro -> registro.getValue().getMateriaAsesoria() == ID);
			ArchivoJson.<SolicitudSimplificada>sobreescribirArchivo(ImplementacionServicioSolicitud.nombreArchivo, registrosSolicitudes, new SolicitudSimplificada());
		}
	}
}