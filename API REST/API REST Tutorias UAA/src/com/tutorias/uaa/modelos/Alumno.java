package com.tutorias.uaa.modelos;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="alumno")
public class Alumno {
	//---Atributos---//.
	private int ID;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private int semestre;
	private String telefono;
	private String correo;
	private String clave;
	private String imagen;
	
	//---Getters---//.
	public int getID() { return ID; }
	public String getNombre() { return nombre; }
	public String getApellidoPaterno() { return apellidoPaterno; }
	public String getApellidoMaterno() { return apellidoMaterno; }
	public int getSemestre() { return semestre; }
	public String getTelefono() { return telefono; }
	public String getCorreo() { return correo; }
	public String getClave() { return clave; }
	public String getImagen() { return imagen; }
	
	//---Setters---//.
	public void setID(int iD) { ID = iD; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
	public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
	public void setSemestre(int semestre) { this.semestre = semestre; }
	public void setTelefono(String telefono) { this.telefono = telefono; }
	public void setCorreo(String correo) { this.correo = correo; }
	public void setClave(String clave) { this.clave = clave; }
	public void setImagen(String imagen) { this.imagen = imagen; }
	
	//---Constructores---//.
	public Alumno() {
		super();
	}
	
	public Alumno(int iD, String nombre, String apellidoPaterno, String apellidoMaterno, int semestre, String telefono,
	String correo, String clave, String imagen) {
		super();
		ID = iD;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.semestre = semestre;
		this.telefono = telefono;
		this.correo = correo;
		this.clave = encriptadoMD5(clave);
		this.imagen = imagen;
	}
	
	//---Métodos---//.
	private String encriptadoMD5(String cadena) {
		try {
			MessageDigest instanciaMetodo = MessageDigest.getInstance("MD5");
			byte[] conjuntoBytes = instanciaMetodo.digest(cadena.getBytes());
			BigInteger numero = new BigInteger(1, conjuntoBytes);
			
			String textoEncriptado = numero.toString(16);
			while(textoEncriptado.length() < 32) {
				textoEncriptado = "0" + textoEncriptado;
			}
			return textoEncriptado;
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return cadena;
		}
		catch(Exception e) {
			e.printStackTrace();
			return cadena;
		}
	}
	
	@Override public String toString() {
		return "Alumno [ID=" + ID + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno="
		+ apellidoMaterno + ", semestre=" + semestre + ", telefono=" + telefono + ", correo=" + correo
		+ ", clave=" + clave + ", imagen=" + imagen + "]";
	}
}