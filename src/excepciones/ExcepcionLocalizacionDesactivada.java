package excepciones;

import speaker.Speaker;

/**
 * 
 * This exception rises if the location is off, and verbally warns
 * @author Raul Pera Pairó
 *
 */
public class ExcepcionLocalizacionDesactivada extends Exception {

	boolean endSpeaking;

	/**
	 * El constructor recibe el speaker, para avisar al usuario, de forma verbal
	 * @param speak
	 */
	public ExcepcionLocalizacionDesactivada(Speaker speak) {

		this.endSpeaking = true;
		speak.habla("El servicio de localización está desactivado, por favor, habilítelo.");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		do {
			this.endSpeaking = speak.tts.isSpeaking();
		} while (this.endSpeaking);
	}
}