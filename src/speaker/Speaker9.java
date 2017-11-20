package speaker;

import java.io.Serializable;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * 
 * This class is responsible for issuing through the TextToSpeech.
 * 
 * @author Raul Pera Pairó
 * 
 */
public class Speaker9 implements TextToSpeech.OnInitListener, Serializable {

	private String fraseRecivida;
	public TextToSpeech tts;
	private Context context;

	public Speaker9(Context context) {

		this.context = context;
		this.fraseRecivida = null;
		this.tts = new TextToSpeech(this.context, this);
	}

	/**
	 * This function is responsible for issuing the String received
	 * 
	 * @param String
	 *            fraseRecibida
	 */
	public void habla(String fraseRecivida) {

		this.fraseRecivida = fraseRecivida;

		tts.speak(fraseRecivida, TextToSpeech.QUEUE_FLUSH, null);
	}

	/**
	 * This function is responsible for closing the TextToSpeech
	 * 
	 */
	public void cerrar() {

		this.tts.stop();
		this.tts.shutdown();

	}

	/**
	 * This function controls and sets the TextToSpeech object to assign the appropriate dictionary and handle exceptions
	 * 
	 */
	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			//Locale locSpanish = new Locale("spa", "ES");
			Locale myLocale = new Locale("hi","IN");
			int result = tts.setLanguage(myLocale);

			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				habla(this.fraseRecivida);
			}
		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}
}
