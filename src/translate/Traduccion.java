package translate;

import speaker.Speaker;
import speaker.Speaker11;
import speaker.Speaker15;
import speaker.Speaker2;
import speaker.Speaker3;
import speaker.Speaker4;
import speaker.Speaker5;
import speaker.Speaker6;
import speaker.Speaker7;
import speaker.Speaker8;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * 
 * This class is responsible for downloading information from the location, and store it in the device.
 * 
 * @author Raul Pera Pairó
 * 
 */
public class Traduccion extends AsyncTask<Bundle, Void, String> {

	private Speaker voiceSpeaker;
	private Speaker2 speakerIngles;
	private Speaker3 speakerFrances;
	private Speaker4 speakerAleman;
	private Speaker5 speakerChino;
	private Speaker6 speakerItaliano;
	private Speaker7 speakerRuso;
	private Speaker8 speakerPortugues;
	private Speaker11 speakerJapones;
	private Speaker15 speakerCoreano;
	/*
	 * private Speaker9 speakerHindi; private Speaker10 speakerArabe; private Speaker12 speakerSueco; private Speaker13 speakerGriego; private Speaker14 speakerHebreo;
	 */

	Bundle bund;
	private Boolean endSpeaking = false;
	private String frase = "";
	private int idioma = 0;
	private Boolean idiomaExtranjero = false;

	String palabra = "";
	String respuesta = "";

	@Override
	protected String doInBackground(Bundle... urls) {

		bund = urls[0];
		this.frase = bund.getString("frase");
		this.idioma = bund.getInt("idioma");
		this.idiomaExtranjero = bund.getBoolean("idiomaExtranjero");

		Translate.setClientId("rpairo");
		Translate.setClientSecret("YDNLU8ALdvlsRoXiGPfYNwT2U6Avwwijjl4xZ/eQ9Bc=");

		try {

			/*
			 * else if (idioma == 8) { this.speakerHindi = (Speaker9) bund.getSerializable("speaker"); palabra = Translate.execute(this.frase, Language.SPANISH, Language.HINDI); } else if (idioma == 9) { this.speakerArabe = (Speaker10) bund.getSerializable("speaker"); palabra =
			 * Translate.execute(this.frase, Language.SPANISH, Language.ARABIC); } else if (idioma == 11) { this.speakerSueco = (Speaker12) bund.getSerializable("speaker"); palabra = Translate.execute(this.frase, Language.SPANISH, Language.SWEDISH); } else if (idioma == 12) { this.speakerGriego =
			 * (Speaker13) bund.getSerializable("speaker"); palabra = Translate.execute(this.frase, Language.SPANISH, Language.GREEK); } else if (idioma == 13) { this.speakerHebreo = (Speaker14) bund.getSerializable("speaker"); palabra = Translate.execute(this.frase, Language.SPANISH,
			 * Language.HEBREW); }
			 */

			if (!this.idiomaExtranjero) {

				switch (this.idioma) {
				case 1:
					this.speakerIngles = (Speaker2) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.ENGLISH);
					break;
				case 2:
					this.speakerFrances = (Speaker3) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.FRENCH);
					break;
				case 3:
					this.speakerAleman = (Speaker4) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.GERMAN);
					break;
				case 4:
					this.speakerChino = (Speaker5) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.CHINESE_SIMPLIFIED);
					break;
				case 5:
					this.speakerItaliano = (Speaker6) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.ITALIAN);
					break;
				case 6:
					this.speakerRuso = (Speaker7) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.RUSSIAN);
					break;
				case 7:
					this.speakerPortugues = (Speaker8) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.PORTUGUESE);
					break;
				case 10:
					this.speakerJapones = (Speaker11) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.JAPANESE);
					break;
				case 14:
					this.speakerCoreano = (Speaker15) bund.getSerializable("speaker");
					palabra = Translate.execute(this.frase, Language.SPANISH, Language.KOREAN);
					break;
				}
			} else {

				this.voiceSpeaker = (Speaker) bund.getSerializable("speaker");

				switch (this.idioma) {
				case 1:
					palabra = Translate.execute(this.frase, Language.ENGLISH, Language.SPANISH);
					break;
				case 2:
					palabra = Translate.execute(this.frase, Language.FRENCH, Language.SPANISH);
					break;
				case 3:
					palabra = Translate.execute(this.frase, Language.GERMAN, Language.SPANISH);
					break;
				case 4:
					palabra = Translate.execute(this.frase, Language.CHINESE_SIMPLIFIED, Language.SPANISH);
					break;
				case 5:
					palabra = Translate.execute(this.frase, Language.ITALIAN, Language.SPANISH);
					break;
				case 6:
					palabra = Translate.execute(this.frase, Language.RUSSIAN, Language.SPANISH);
					break;
				case 7:
					palabra = Translate.execute(this.frase, Language.PORTUGUESE, Language.SPANISH);
					break;
				case 10:
					palabra = Translate.execute(this.frase, Language.JAPANESE, Language.SPANISH);
					break;
				case 14:
					palabra = Translate.execute(this.frase, Language.KOREAN, Language.SPANISH);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	@Override
	protected void onPostExecute(String cadena) {

		/*
		 * else if (idioma == 8) speakerHindi.habla(palabra); else if (idioma == 9) speakerArabe.habla(palabra); else if (idioma == 11) speakerSueco.habla(palabra); else if (idioma == 12) speakerGriego.habla(palabra); else if (idioma == 13) speakerHebreo.habla(palabra);
		 */

		Log.d("Traduccion", this.palabra);
		this.endSpeaking = false;

		if (!this.idiomaExtranjero) {
			if (this.idioma == 1)
				this.speakerIngles.habla(this.palabra);
			else if (this.idioma == 2)
				this.speakerFrances.habla(this.palabra);
			else if (this.idioma == 3)
				this.speakerAleman.habla(this.palabra);
			else if (this.idioma == 4)
				this.speakerChino.habla(this.palabra);
			else if (this.idioma == 5)
				this.speakerItaliano.habla(this.palabra);
			else if (this.idioma == 6)
				this.speakerRuso.habla(this.palabra);
			else if (this.idioma == 7)
				this.speakerPortugues.habla(this.palabra);
			else if (this.idioma == 14)
				this.speakerCoreano.habla(this.palabra);
			else if (this.idioma == 10)
				this.speakerJapones.habla(this.palabra);
		} else {
			this.voiceSpeaker.habla(this.palabra);
		}

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		do {
			if (!this.idiomaExtranjero) {
				if (this.idioma == 1)
					this.endSpeaking = this.speakerIngles.tts.isSpeaking();
				else if (this.idioma == 2)
					this.endSpeaking = this.speakerFrances.tts.isSpeaking();
				else if (this.idioma == 3)
					this.endSpeaking = this.speakerAleman.tts.isSpeaking();
				else if (this.idioma == 4)
					this.endSpeaking = this.speakerChino.tts.isSpeaking();
				else if (this.idioma == 5)
					this.endSpeaking = this.speakerItaliano.tts.isSpeaking();
				else if (this.idioma == 6)
					this.endSpeaking = this.speakerRuso.tts.isSpeaking();
				else if (this.idioma == 7)
					this.endSpeaking = this.speakerPortugues.tts.isSpeaking();
				else if (this.idioma == 14)
					this.endSpeaking = this.speakerCoreano.tts.isSpeaking();
				else if (this.idioma == 10)
					this.endSpeaking = this.speakerJapones.tts.isSpeaking();
			} else {
				this.endSpeaking = this.voiceSpeaker.tts.isSpeaking();
			}
			/*
			 * else if (idioma == 8) endSpeaking = speakerHindi.tts.isSpeaking(); else if (idioma == 9) endSpeaking = speakerArabe.tts.isSpeaking(); else if (idioma == 11) endSpeaking = speakerSueco.tts.isSpeaking(); else if (idioma == 12) endSpeaking = speakerGriego.tts.isSpeaking(); else if
			 * (idioma == 13) endSpeaking = speakerHebreo.tts.isSpeaking();
			 */

		} while (endSpeaking);
	}
}