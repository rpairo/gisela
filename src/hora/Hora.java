package hora;

import java.util.Calendar;

import speaker.Speaker;

/**
 * This class is responsible for obtaining the system clock
 * 
 * @author Raul Pera Pairó
 * 
 */
public class Hora {

	/**
	 * This constructor is responsible for obtaining the system time, and enable voiceSpeaker to say it.
	 * 
	 * @param voiceSpeaker
	 *            Get the instance of the main speaker
	 */
	public Hora(Speaker voiceSpeaker) {

		Calendar c = Calendar.getInstance();

		// int day = c.get(Calendar.DAY_OF_WEEK);

		int time = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);

		Boolean endSpeaking = false;
		voiceSpeaker.habla("Son las: " + time + " y " + min);
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);

		System.out.println("Son las: " + time + " y " + min);
	}
}