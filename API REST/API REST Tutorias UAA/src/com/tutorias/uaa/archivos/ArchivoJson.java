package com.tutorias.uaa.archivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ArchivoJson {
	//---Constructor---//.
	public ArchivoJson() {
		super();
	}
	
	//---Métodos---//.
	public static <T, K> Map<T, K> obtenerRegistros(String nombreArchivo) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new IOException();
			}
			
			//Definir los parámetros para la lectura de
			//datos registrados en el archivo correspondiente.
			Reader lector = Files.newBufferedReader(Paths.get(nombreArchivo));
			
			//Convertir la cadena JSON a un objeto Java.
			Gson gson = new Gson();
			Type listaObjetos = new TypeToken<ArrayList<K>>() {}.getType();
			
			Map<T, K> objetos = gson.fromJson(lector, listaObjetos);
			lector.close();
			
			return objetos;
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
	
	public static <T, K> K obtenerRegistroEspecifico(String nombreArchivo, T indiceRegistro) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new IOException();
			}
			
			//Definir los parámetros para la lectura de
			//datos registrados en el archivo correspondiente.
			Reader lector = Files.newBufferedReader(Paths.get(nombreArchivo));
			
			//Convertir la cadena JSON a un objeto Java.
			Gson gson = new Gson();
			Type listaObjetos = new TypeToken<ArrayList<K>>() {}.getType();
			
			Map<T, K> objetos = gson.fromJson(lector, listaObjetos);
			lector.close();
			
			return objetos.get(indiceRegistro);
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
	
	public static <T, K> boolean agregarRegistro(String nombreArchivo, T indiceRegistro, K registro) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new Exception();
			}
			
			Map<T, K> auxiliar = obtenerRegistros(nombreArchivo);
			HashMap<T, K> listaRegistros = (HashMap<T, K>) auxiliar;
			
			//Verificar si existen datos en el archivo especificado.
			if(!listadoValido(listaRegistros)) {
				return false;
			}
			
			//Verificar si el registro ya existe en la base de datos.
			if(listaRegistros.containsKey(indiceRegistro)) {
				return false;
			}
			
			//Agregar el registro al correspondiente archivo.
			listaRegistros.put(indiceRegistro, registro);
			return sobreescribirArchivo(nombreArchivo, listaRegistros);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static <T, K> boolean actualizarRegistro(String nombreArchivo, T indiceRegistro, K registroNuevo) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new Exception();
			}
			
			Map<T, K> auxiliar = obtenerRegistros(nombreArchivo);
			HashMap<T, K> listaRegistros = (HashMap<T, K>) auxiliar;
			
			//Verificar si existen datos en el archivo especificado.
			if(!listadoValido(listaRegistros)) {
				return false;
			}
			
			//Verificar si el registro realmente existe en la base de datos.
			if(!listaRegistros.containsKey(indiceRegistro)) {
				return false;
			}
			
			//Actualizar el correspondiente registro.
			listaRegistros.replace(indiceRegistro, registroNuevo);
			return sobreescribirArchivo(nombreArchivo, listaRegistros);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static <T, K> boolean eliminarRegistro(String nombreArchivo, T indiceRegistro) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new Exception();
			}
			
			Map<T, K> auxiliar = obtenerRegistros(nombreArchivo);
			HashMap<T, K> listaRegistros = (HashMap<T, K>) auxiliar;
			
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
			return sobreescribirArchivo(nombreArchivo, listaRegistros);
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
	
	public static <T, K> boolean listadoValido(Map<T, K> listado) {
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
	
	public static <T, K> boolean sobreescribirArchivo(String nombreArchivo, Map<T, K> listaRegistros) {
		try {
			//Verificar si el archivo especificado es válido.
			if(!archivoValido(nombreArchivo)) {
				throw new IOException();
			}
			
			//Borrar la versión previa del archivo.
			if(!eliminarArchivo(nombreArchivo)) {
				throw new Exception();
			}
			
			//Definir los datos para efectuar la
			//sobreescritura del archivo.
			Writer escritor = Files.newBufferedWriter(Paths.get(nombreArchivo));
			
			//Serializar en forma de cadena JSON todos los datos
			//y utilizarla para generar un archivo nuevo.
			Gson gson = new Gson();
			gson.toJson(listaRegistros, escritor);
			
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