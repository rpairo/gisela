package translate;

import gisela.Principal;

import java.util.ArrayList;
import java.util.Locale;

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
import android.app.Activity;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.gisela.R;

/**
 * This class manages the music player, using an interface gesture recognition
 * 
 * @author Raul Pera Pairó
 * 
 */
public class GestorTraductor extends Activity implements OnGestureListener, OnInitListener {

	private boolean endSpeaking;

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
	 * private Speaker9 speakerHindi; // no funciona bien de momento private Speaker10 speakerArabe; // no funciona bien de momento private Speaker12 speakerSueco; // no funciona bien de momento private Speaker13 speakerGriego; // no funcina bien de momento private Speaker14 speakerHebreo; // no
	 * funciona bien de momento
	 */

	private static final int VOICE_LISTENER_FRASE = 1; // REQUEST_OK: 1
	private static final int VOICE_LISTENER_PREGUNTA_IDIOMA_TRADUCCION = 2;
	private static final int VOICE_LISTENER_FRASE_EXTRANJERA = 3;

	private GestureDetectorCompat mDetector;

	private int idioma = 0;

	Configuration config;

	/**
	 * This function is responsible for creating the layout, initialize the necessary objects, create a random playlist and start the playback of the list
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Principal.active = 1;
		setContentView(R.layout.activity_traductor);
		this.resizeToFullScreen();
		mDetector = new GestureDetectorCompat(this, this);

		this.voiceSpeaker = new Speaker(this);
		this.speakerFrances = new Speaker3(this);
		this.speakerIngles = new Speaker2(this);
		this.speakerAleman = new Speaker4(this);
		this.speakerChino = new Speaker5(this);
		this.speakerItaliano = new Speaker6(this);
		this.speakerRuso = new Speaker7(this);
		this.speakerPortugues = new Speaker8(this);
		this.speakerCoreano = new Speaker15(this);
		this.speakerJapones = new Speaker11(this);
		/*
		 * this.speakerHindi = new Speaker9(this); this.speakerArabe = new Speaker10(this); this.speakerSueco = new Speaker12(this); this.speakerGriego = new Speaker13(this); this.speakerHebreo = new Speaker14(this);
		 */

		this.preguntarIdioma();
	}

	private void preguntarIdioma() {
		MediaPlayer mp = MediaPlayer.create(this, R.raw.elija_idioma);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {
					// idioma = 5;
					startActivityForResult(i, VOICE_LISTENER_PREGUNTA_IDIOMA_TRADUCCION);
				} catch (Exception e) {
					// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	private void preguntarFraseIdiomaExtranjero() {

		MediaPlayer mp = null;
		String lenguajeRecognizer = null;

		switch (this.idioma) {
		case 1:
			mp = MediaPlayer.create(this, R.raw.diga_frase_ingles);
			lenguajeRecognizer = Locale.UK.toString();
			break;
		case 2:
			mp = MediaPlayer.create(this, R.raw.diga_frase_frances);
			// lenguajeRecognizer = Locale.FRANCE.toString();
			lenguajeRecognizer = Locale.FRANCE.toString();
			break;
		case 3:
			mp = MediaPlayer.create(this, R.raw.diga_frase_aleman);
			lenguajeRecognizer = Locale.GERMANY.toString();
			break;
		case 4:
			mp = MediaPlayer.create(this, R.raw.diga_frase_chino);
			lenguajeRecognizer = Locale.CHINA.toString();
			break;
		case 5:
			mp = MediaPlayer.create(this, R.raw.diga_frase_italiano);
			lenguajeRecognizer = Locale.ITALY.toString();
			break;
		case 6:
			mp = MediaPlayer.create(this, R.raw.diga_frase_ruso);
			lenguajeRecognizer = "ru_RU";
			break;
		case 7:
			mp = MediaPlayer.create(this, R.raw.diga_frase_portugues);
			lenguajeRecognizer = "pt_PT";
			break;
		case 10:
			mp = MediaPlayer.create(this, R.raw.diga_frase_japones);
			lenguajeRecognizer = Locale.JAPAN.toString();
			break;
		case 14:
			mp = MediaPlayer.create(this, R.raw.diga_frase_coreano);
			lenguajeRecognizer = Locale.KOREA.toString();
			break;
		}

		final String lenguaje = lenguajeRecognizer; // es necesario hacerla final para que fincione el recognizer dinamico

		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, lenguaje);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, lenguaje);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lenguaje);
				// i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {

					startActivityForResult(i, VOICE_LISTENER_FRASE_EXTRANJERA);
				} catch (Exception e) {
					// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	private void preguntarFraseIdiomaNativo() {

		MediaPlayer mp = MediaPlayer.create(this, R.raw.diga_frase_nativa);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {
					startActivityForResult(i, VOICE_LISTENER_FRASE);
				} catch (Exception e) {
					// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Principal.active = 0;

		this.voiceSpeaker.cerrar();
		this.speakerFrances.cerrar();
		this.speakerIngles.cerrar();
		this.speakerAleman.cerrar();
		this.speakerChino.cerrar();
		this.speakerItaliano.cerrar();
		this.speakerRuso.cerrar();
		this.speakerPortugues.cerrar();
		this.speakerJapones.cerrar();
		this.speakerCoreano.cerrar();
		/*
		 * this.speakerHindi.cerrar(); this.speakerArabe.cerrar(); this.speakerSueco.cerrar(); this.speakerGriego.cerrar(); this.speakerHebreo.cerrar();
		 */
		this.finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Principal.active = 0;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == VOICE_LISTENER_FRASE && resultCode == RESULT_OK) {
			String fraseReconocida = null;

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			fraseReconocida = thingsYouSaid.get(0);

			// ////////////
			Bundle bund = new Bundle();
			bund.putInt("idioma", idioma);
			bund.putString("frase", fraseReconocida);
			bund.putBoolean("idiomaExtranjero", false);

			switch (this.idioma) {
			case 1:
				bund.putSerializable("speaker", this.speakerIngles);
				break;
			case 2:
				bund.putSerializable("speaker", this.speakerFrances);
				break;
			case 3:
				bund.putSerializable("speaker", this.speakerAleman);
				break;
			case 4:
				bund.putSerializable("speaker", this.speakerChino);
				break;
			case 5:
				bund.putSerializable("speaker", this.speakerItaliano);
				break;
			case 6:
				bund.putSerializable("speaker", this.speakerRuso);
				break;
			case 7:
				bund.putSerializable("speaker", this.speakerPortugues);
				break;
			case 10:
				bund.putSerializable("speaker", this.speakerJapones);
				break;
			case 14:
				bund.putSerializable("speaker", this.speakerCoreano);
				break;
			}

			/*
			 * if (idioma == 1) bund.putSerializable("speaker", this.speakerIngles); else if (idioma == 2) bund.putSerializable("speaker", this.speakerFrances); else if (idioma == 3) bund.putSerializable("speaker", this.speakerAleman); else if (idioma == 4) bund.putSerializable("speaker",
			 * this.speakerChino); else if (idioma == 5) bund.putSerializable("speaker", this.speakerItaliano); else if (idioma == 6) bund.putSerializable("speaker", this.speakerRuso); else if (idioma == 7) bund.putSerializable("speaker", this.speakerPortugues); else if (idioma == 10)
			 * bund.putSerializable("speaker", this.speakerJapones); else if (idioma == 14) bund.putSerializable("speaker", this.speakerCoreano);
			 */

			/*
			 * else if (idioma == 8) bund.putSerializable("speaker", this.speakerHindi); else if (idioma == 9) bund.putSerializable("speaker", this.speakerArabe); else if (idioma == 11) bund.putSerializable("speaker", this.speakerSueco); else if (idioma == 12) bund.putSerializable("speaker",
			 * this.speakerGriego); else if (idioma == 13) bund.putSerializable("speaker", this.speakerHebreo);
			 */

			new Traduccion().execute(bund);

		} else if (requestCode == VOICE_LISTENER_FRASE_EXTRANJERA && resultCode == RESULT_OK) {

			String fraseReconocida = null;

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			fraseReconocida = thingsYouSaid.get(0);
			
			Log.d("FRASE", fraseReconocida);

			// ////////////
			Bundle bund = new Bundle();
			bund.putInt("idioma", idioma);
			bund.putString("frase", fraseReconocida);
			bund.putBoolean("idiomaExtranjero", true);
			bund.putSerializable("speaker", this.voiceSpeaker);

			new Traduccion().execute(bund);

			// codi per la traducció de altres idiomas al español.

		} else if (requestCode == VOICE_LISTENER_PREGUNTA_IDIOMA_TRADUCCION && resultCode == RESULT_OK) {

			String[] segundaFraseSplit = null;
			String segundaFraseReconocida = null;

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			segundaFraseReconocida = thingsYouSaid.get(0);

			segundaFraseReconocida = segundaFraseReconocida.replace("!", "");
			segundaFraseReconocida = segundaFraseReconocida.replace("?", "");
			segundaFraseReconocida = segundaFraseReconocida.replace(",", "");
			segundaFraseReconocida = segundaFraseReconocida.replace(".", "");
			segundaFraseReconocida = segundaFraseReconocida.toUpperCase();
			segundaFraseSplit = segundaFraseReconocida.split(" ");

			Log.d("idioma", segundaFraseReconocida);

			for (int i = 0; i < segundaFraseSplit.length; i++) {

				if (segundaFraseSplit[i].equals("FRANCÉS")) {
					idioma = 2;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("INGLÉS")) {
					idioma = 1;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("ALEMÁN")) {
					idioma = 3;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("CHINO")) {
					idioma = 4;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("ITALIANO")) {
					idioma = 5;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("RUSO")) {
					idioma = 6;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("PORTUGUÉS")) {
					idioma = 7;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("COREANO")) {
					idioma = 14;
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("JAPONÉS")) {
					idioma = 10;
					i = segundaFraseSplit.length;
				}/*
				 * else if (segundaFraseSplit[i].equals("ÁRABE")) { idioma = 9; i = segundaFraseSplit.length; } else if (segundaFraseSplit[i].equals("SUECO")) { idioma = 11; i = segundaFraseSplit.length; } else if (segundaFraseSplit[i].equals("GRIEGO")) { idioma = 12; i = segundaFraseSplit.length; }
				 * else if (segundaFraseSplit[i].equals("HEBREO")) { idioma = 13; i = segundaFraseSplit.length; } else if (segundaFraseSplit[i].equals("HINDI") || segundaFraseSplit[i].equals("INDIE")) { idioma = 8; i = segundaFraseSplit.length; }
				 */

			}

			if (idioma == 0) {
				MediaPlayer mp = MediaPlayer.create(this, R.raw.no_entiendo_idioma);
				mp.start();

				mp.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						preguntarIdioma();
					}
				});

			} else {

				this.preguntarFraseIdiomaNativo();
			}

		}
	}

	private void resizeToFullScreen() {
		// Ajustar la activity a pantalla completa
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
		case 1:
			this.preguntarFraseIdiomaNativo();
			Log.d("Direccion", "top");
			return true;
		case 2:
			Log.d("Direccion", "left");
			return true;
		case 3:
			this.preguntarFraseIdiomaExtranjero();
			Log.d("Direccion", "down");
			return true;
		case 4:
			Log.d("Direccion", "right");
			return true;
		}
		return false;
	}

	private int getSlope(float x1, float y1, float x2, float y2) {
		Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
		if (angle > 45 && angle <= 135)
			// top
			return 1;
		if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
			// left
			return 2;
		if (angle < -45 && angle >= -135)
			// down
			return 3;
		if (angle >= -45 && angle <= 45)
			// right
			return 4;
		return 0;
	}

	/**
	 * This function is responsible for detecting a long press on the screen and close the activity.
	 */
	@Override
	public void onLongPress(MotionEvent e) {

		// this.mp.stop();

		MediaPlayer mp = MediaPlayer.create(this, R.raw.cerrando_traductor);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				cerrar();
			}
		});
	}

	public void cerrar() {
		this.voiceSpeaker.cerrar();
		this.finish();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/**
	 * This function is responsible for detecting the tap on the screen, and pause or resume playing music.
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// this.preguntarFrase();
		return false;
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub

	}
}