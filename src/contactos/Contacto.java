package contactos;

import java.io.Serializable;

/**
 * This class is used to manage contact information
 * 
 * @author Raul Pera Pairó
 *
 */
public class Contacto implements Serializable{

	private int id;
	private String nombre;
	private String numero;

	/**
	 * This constructor initializes the variables
	 */
	public Contacto() {
		this.id = 0;
		this.nombre = null;
		this.numero = null;
	}
	
	/**
	 * This constructor parameters to assign class variables.
	 * 
	 * @param String nombre
	 * @param String numero
	 */
	public Contacto(String nombre, String numero) {
		this.nombre = nombre;
		this.numero = numero;
	}

	/**
	 * This function returns the name of the contact
	 * 
	 * @return String name
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * This function sets the name of the contact from the received parameter
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * This function returns the number of contact
	 * 
	 * @return String number
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * This function sets the name of the contact from the received parameter
	 * @param String numero
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * This function returns the ID of the contact
	 * @return int ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * This function sets the contact ID from the received parameter
	 * @param int ID
	 */
	public void setId(int id) {
		this.id = id;
	}
}