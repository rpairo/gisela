package instrucciones;

import speaker.Speaker;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;

import com.example.gisela.R;

/**
 * This class is responsible for issuing all functions that are able to run the app, so the user knows them well. works like a manual.
 * 
 * @author Raul Pera Pairó
 * 
 */
public class Instrucciones extends AsyncTask<Speaker, String, String> {

	/**
	 * Is responsible for issuing instructions.
	 * 
	 * @param voiceSpeaker
	 *            Get an instance of the Speaker of the parent class
	 */

	@Override
	protected String doInBackground(Speaker... params) {

		Boolean endSpeaking = false;
		Speaker voiceSpeaker = params[0];

		String manual = "Manual de instrucciones. Puedo realizar las siguientes funciones:." + "Llamar. Puede llamar a alguien de la siguiente forma: llama a Cristina. o, llama al 620 61 62 50."
				+ "Reproducir música. Puedo reproducir música de la siguiente forma: reproduce música. o, gisela, ponme algo de música. " + "El reproductor de música se gestiona a través de gestos táctiles. Un tap, pausará o reanudará la reproducción de la música. "
				+ "Deslizar el dedo hacia la derecha cambiará la canción previa. Deslizar a la izquierda, cambiará a la siguiente canción. " + "Mantener pulsada la pantalla, apagará el reproductor de música."
				+ "Reloj. Puedo consultar la hora de la siguiente forma: Que hora es?. o, Gisela, dime la hora?" + "Localización. Puedo saber en que calle se encuentra de la siguiente forma: Dame mi posición. o, Me he perdido, dime donde estoy."
				+ "Lectura de noticias. Puede escuchar las noticias de los periódicos de la siguiente forma: léeme las noticias. o, dime las noticias del día. Puede elegir entre los siguientes periódicos. El Pais, El Mundo, y El Marca."
				+ "Consulta del tiempo. Puede preguntar por el tiempo de esta forma: que tiempo hace?. o, que temperatura hay?"
				+ "Socorrer. Puede pedir socorro, y se enviará un mensaje a un contacto, pidiéndole socorro, enviándole su posición GPS. Puede pedir socorro de esta forma: Socorro!. o, Socorro, me he caido!"
				+ "Envío de SMS. Puedo enviar un mensaje a un contacto de la siguiente forma: Envíale un mensaje a Víctor. Despues te preguntaré por el texto que deseas enviar, y se enviará."
				+ "Ajuste de volumen. Se puede ajustar el volumen del móvil en: Sonido, Vibración y Silencio. Se puede ajustar el volumen de la siguiente forma: Pon el volumen en silencio. o, Gisela, pon el volumen en vibración."
				+ "Traducción. Puedo traducir frases a diferentes idiomas, como el Inglés, Francés, Alemán, Ruso, Portugués, Italiano, Chino, Japonés o Coreano"
				+ "Para facilitar el uso de la aplicación, cada vez que desbloqueé la pantalla, se lanzará la aplicación, para que pueda ayudarte., Si ocurre cualquier error, o te has perdido en la navegación de la aplicación, solo tienes que bloquear y volver a desbloquear la pantalla.";

		return null;
	}
}