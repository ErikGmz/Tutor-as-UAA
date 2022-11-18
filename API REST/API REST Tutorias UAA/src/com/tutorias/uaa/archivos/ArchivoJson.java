package com.tutorias.uaa.archivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.tutorias.uaa.modelos.Alumno;
import com.tutorias.uaa.modelos.Materia;
import com.tutorias.uaa.modelos.SolicitudSimplificada;
import com.tutorias.uaa.modelos.TutorSimplificado;

public class ArchivoJson {
	//---Constructor---//.
	public ArchivoJson() {
		super();
	}
	
	//---Métodos---//.
	public static <K> Map<Integer, K> obtenerRegistros(String nombreArchivo, K auxiliar) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				return null;
			}
			
			//Convertir la cadena JSON a un objeto Java.
			try {
				ObjectMapper mapeador = new ObjectMapper();
				Map<Integer, K> objetos = mapeador.readValue(Paths.get(nombreArchivo).toFile(), 
				mapeador.getTypeFactory().constructMapType(Map.class, Integer.class, auxiliar.getClass()));
				
				if(objetos != null) {
					return objetos;
				}
				else {
					return null;
				}
			}
			catch(MismatchedInputException e) {
				return null;
			}
		} 
		catch(FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <K> K obtenerRegistroEspecifico(String nombreArchivo, Integer indiceRegistro, K auxiliar) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				return null;
			}
			
			//Convertir la cadena JSON a un objeto Java.
			try {
				ObjectMapper mapeador = new ObjectMapper();
				Map<Integer, K> objetos = mapeador.readValue(Paths.get(nombreArchivo).toFile(), 
				mapeador.getTypeFactory().constructMapType(Map.class, Integer.class, auxiliar.getClass()));
				
				if(objetos != null) {		
					return objetos.get(indiceRegistro);
				}
				else {
					return null;
				}
			}
			catch(MismatchedInputException e) {
				return null;
			}
		} 
		catch(FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <K> boolean agregarRegistro(String nombreArchivo, Integer indiceRegistro, K registro) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				//Crear el archivo previamente inexistente.
				File archivo = new File(nombreArchivo);
				
				if(!archivo.exists()) {
					if(!archivo.createNewFile()) {
						return false;
					}
				}
			}
			
			Map<Integer, K> auxiliar = obtenerRegistros(nombreArchivo, registro);
			HashMap<Integer, K> listaRegistros = (HashMap<Integer, K>) auxiliar;
			
			if(listaRegistros == null) {
				listaRegistros = new HashMap<>();
			}
						
			//Verificar si el registro ya existe en la base de datos.
			if(listaRegistros.containsKey(indiceRegistro)) {
				return false;
			}
			
			//Agregar el registro al correspondiente archivo.
			listaRegistros.put(indiceRegistro, registro);
			return sobreescribirArchivo(nombreArchivo, listaRegistros, registro);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static <K> boolean actualizarRegistro(String nombreArchivo, Integer indiceRegistro, K registroNuevo) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				return false;
			}
			
			Map<Integer, K> auxiliar = obtenerRegistros(nombreArchivo, registroNuevo);
			HashMap<Integer, K> listaRegistros = (HashMap<Integer, K>) auxiliar;
			
			//Verificar si existen datos en el archivo especificado.
			if(!listadoValido(listaRegistros)) {
				return false;
			}
			
			//Verificar si el registro realmente existe en la base de datos.
			if(!listaRegistros.containsKey(indiceRegistro)) {
				return false;
			}
			
			//Actualizar el correspondiente registro.
			listaRegistros.remove(indiceRegistro);
			
			if(registroNuevo instanceof Alumno) {
				listaRegistros.put(((Alumno) registroNuevo).getID(), registroNuevo);
			}
			if(registroNuevo instanceof Materia) {
				listaRegistros.put(((Materia) registroNuevo).getID(), registroNuevo);
			}
			if(registroNuevo instanceof TutorSimplificado) {
				listaRegistros.put(((TutorSimplificado) registroNuevo).getID(), registroNuevo);
			}
			if(registroNuevo instanceof SolicitudSimplificada) {
				listaRegistros.put(((SolicitudSimplificada) registroNuevo).getID(), registroNuevo);
			}
			return sobreescribirArchivo(nombreArchivo, listaRegistros, registroNuevo);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static <K> boolean eliminarRegistro(String nombreArchivo, Integer indiceRegistro, K parametroAuxiliar) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				return false;
			}
			
			Map<Integer, K> auxiliar = obtenerRegistros(nombreArchivo, parametroAuxiliar);
			HashMap<Integer, K> listaRegistros = (HashMap<Integer, K>) auxiliar;
			
			//Verificar si existen datos en el archivo especificado.
			if(!listadoValido(listaRegistros)) {
				return false;
			}
			
			//Verificar si el registro realmente existe en la base de datos.
			if(!listaRegistros.containsKey(indiceRegistro)) {
				return false;
			}
			
			//Actualizar el correspondiente registro.
			listaRegistros.remove(indiceRegistro);
			return sobreescribirArchivo(nombreArchivo, listaRegistros, parametroAuxiliar);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean archivoValido(String nombreArchivo) {
		try {
			File archivo = new File(nombreArchivo);
			return (archivo.exists() && archivo.isFile()); 
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static <K> boolean listadoValido(Map<Integer, K> listado) {
		try {
			if(listado == null) {
				return false;
			}
			
			if(listado.size() <= 0) {
				return false;
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean eliminarArchivo(String nombreArchivo) {
		try {
			if(!archivoValido(nombreArchivo)) {
				throw new Exception();
			}
			
			File archivo = new File(nombreArchivo);
			return archivo.delete();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static <K> boolean sobreescribirArchivo(String nombreArchivo, Map<Integer, K> listaRegistros, K auxiliar) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new IOException();
			}
			
			//Borrar la versión previa del archivo.
			if(!eliminarArchivo(nombreArchivo)) {
				throw new Exception();
			}
							
			//Generar la cadena JSON con todos los datos correspondientes
			//y utilizarla para sobreescribir el archivo.
			ObjectMapper mapeador = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
			mapeador.writeValue(Paths.get(nombreArchivo).toFile(), listaRegistros.entrySet().stream()
			.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			return true;
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}