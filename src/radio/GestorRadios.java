package radio;

import gisela.Principal;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
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
 * 
 * 
 * @author Raul Pera Pairó
 * 
 */
public class GestorRadios extends Activity implements OnGestureListener, OnInitListener {

	private GestureDetectorCompat mDetector;
	private MediaPlayer mp;
	private String fraseReconocida;
	private String[] fraseSpliteada;
	private boolean emisoraEnMarcha;
	private int emisora;
	private boolean preparandoEmisora;

	private static final int ELEGIR_EMISORA = 1;
	private static final int PREGUNTAR_CONFIRMACION_TEMPORIZADOR = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Principal.active = 2;
		setContentView(R.layout.activity_radios);
		this.resizeToFullScreen();
		mDetector = new GestureDetectorCompat(this, this);
		this.emisoraEnMarcha = false;
		this.emisora = 0;
		this.preparandoEmisora = false;
		this.elegirEmisora();

	}

	private void elegirEmisora() {
		mp = MediaPlayer.create(this, R.raw.elegir_radio);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();

				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {
					startActivityForResult(i, ELEGIR_EMISORA);
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

		this.finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Principal.active = 2;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ELEGIR_EMISORA && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			fraseReconocida = thingsYouSaid.get(0);

			this.fraseReconocida = this.fraseReconocida.replace("!", "");
			this.fraseReconocida = this.fraseReconocida.replace("?", "");
			this.fraseReconocida = this.fraseReconocida.replace(",", "");
			this.fraseReconocida = this.fraseReconocida.replace(".", "");
			this.fraseReconocida = this.fraseReconocida.toUpperCase();

			Log.d("frase", this.fraseReconocida);

			// Divide la frase en palabras, para poder interpretar las palabras
			this.fraseSpliteada = this.fraseReconocida.split(" ");

			for (int i = 0; i < this.fraseSpliteada.length; i++) {
				if (this.fraseSpliteada[i].equals("40")) {
					this.emisora = 1;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("PRINCIPALES")) {
					this.emisora = 1;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("SER")) {
					this.emisora = 2;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("NACIONAL")) {
					this.emisora = 3;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("1042")) {
					this.emisora = 4;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MÁXIMA")) {
					this.emisora = 5;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MARÍA")) {
					this.emisora = 6;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("COPE")) {
					this.emisora = 7;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("CERO")) {
					this.emisora = 8;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("DIAL")) {
					this.emisora = 9;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("EUROPA")) {
					this.emisora = 10;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("RAC1")) {
					this.emisora = 11;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("KISS")) {
					this.emisora = 12;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MARCA")) {
					this.emisora = 13;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("RAC105")) {
					this.emisora = 14;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("CATALUNYA")) {
					if (i < (this.fraseSpliteada.length - 1)) {
						if (this.fraseSpliteada[i + 1].equals("RADIO")) {
							this.emisora = 15;
							i = this.fraseSpliteada.length;
						} else if (this.fraseSpliteada[i + 1].equals("INFORMACIÓN")) {
							this.emisora = 16;
							i = this.fraseSpliteada.length;
						} else if (this.fraseSpliteada[i + 1].equals("INFORMACIÓ")) {
							this.emisora = 16;
							i = this.fraseSpliteada.length;
						} else if (this.fraseSpliteada[i + 1].equals("MÚSICA")) {
							this.emisora = 17;
							i = this.fraseSpliteada.length;
						}
					} else
						i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("CLÁSICA")) {
					if (i < (this.fraseSpliteada.length - 1)) {
						if (this.fraseSpliteada[i + 1].equals("NACIONAL")) {
							this.emisora = 18;
							i = this.fraseSpliteada.length;
						}
					} else
						i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("ANDALUCÍA")) {
					this.emisora = 19;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("SUR")) {
					this.emisora = 20;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("FIESTA")) {
					this.emisora = 21;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("EUSKADI")) {
					if (i > 0) {
						if (this.fraseSpliteada[i - 1].equals("RADIO")) {
							this.emisora = 22;
							i = this.fraseSpliteada.length;
						}
					} else if (i < (this.fraseSpliteada.length - 1)) {
						if (this.fraseSpliteada[i + 1].equals("IRRATIA")) {
							this.emisora = 24;
							i = this.fraseSpliteada.length;
						} else if (this.fraseSpliteada[i + 1].equals("VITORIA")) {
							this.emisora = 25;
							i = this.fraseSpliteada.length;
						}
					} else
						i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("GAZTEA")) {
					this.emisora = 26;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("ESRADIO")) {
					this.emisora = 23;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("M80")) {
					this.emisora = 27;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("100")) {
					this.emisora = 28;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("RADIOLE")) {
					this.emisora = 29;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("OLÉ")) {
					this.emisora = 29;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("CHILL")) {
					this.emisora = 30;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MOZART")) {
					this.emisora = 31;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("VENECIA")) {
					this.emisora = 32;
					i = this.fraseSpliteada.length;
				}
			}

			if (this.emisora != 0)
				this.reproducirRadio(this.emisora);
			else {
				this.mp.reset();

				this.mp = MediaPlayer.create(this, R.raw.no_entiendo_emis);
				this.mp.start();

				this.mp.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {

						mp.stop();
						emisoraEnMarcha = false;
					}
				});
			}
		} else if (requestCode == PREGUNTAR_CONFIRMACION_TEMPORIZADOR && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			fraseReconocida = thingsYouSaid.get(0);

			this.fraseReconocida = this.fraseReconocida.replace("!", "");
			this.fraseReconocida = this.fraseReconocida.replace("?", "");
			this.fraseReconocida = this.fraseReconocida.replace(",", "");
			this.fraseReconocida = this.fraseReconocida.replace(".", "");
			this.fraseReconocida = this.fraseReconocida.toUpperCase();

			Log.d("frase", this.fraseReconocida);

			// Divide la frase en palabras, para poder interpretar las palabras
			this.fraseSpliteada = this.fraseReconocida.split(" ");

			int eleccion = 0;

			for (int i = 0; i < this.fraseSpliteada.length; i++) {
				if (this.fraseSpliteada[i].equals("SI")) {
					eleccion = 1;
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("NO")) {
					eleccion = 2;
					i = this.fraseSpliteada.length;
				}
			}

			switch (eleccion) {
			case 0:
				break;
			case 1:
				break;
			case 2:

				try {
					MediaPlayer mp2 = new MediaPlayer();
					mp2 = MediaPlayer.create(this, R.raw.no_temporizador);
					mp2.prepare();
					mp2.start();

					mp2.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp2) {

							mp2.stop();
							
							if (emisoraEnMarcha) {
								mp.start();
							}
						}
					});
				} catch (Exception e) {
				}
				break;
			}
		}
	}

	private void reproducirRadio(int emisora) {

		String url = "";

		switch (emisora) {
		case 1:
			// los 40 principales
			url = "http://63.243.149.5/LOS40CMP3";
			break;
		case 2:
			// cadena ser
			url = "http://live.emisoras.cadenaser.com:8085/CADENASER.mp3";
			break;
		case 3:
			// Radio Nacional
			url = "http://195.10.10.209/rtve/radio1.mp3?GKID=f1f3566077c911e4809400163e914f69";
			break;
		case 4:
			// Bliss 1042
			url = "http://mp3hdfm32.hala.jo:8132";
		case 5:
			// Maxima FM
			url = "http://188.93.73.98:8130";
			break;
		case 6:
			// Radio María
			url = "http://50.7.181.186:8060";
			break;
		case 7:
			// Cadena COPE
			url = "http://84.127.246.10:8086";
			break;
		case 8:
			// Onda Cero
			url = "http://37.59.37.201:8000";
			break;
		case 9:
			// Cadena Dial
			url = "http://188.165.132.54:8226";
			break;
		case 10:
			// Europa FM //hay que buscar un stream mejor
			url = "http://91.121.147.57:8102";
			break;
		case 11:
			// RAC 1
			url = "http://188.165.129.245:8090";
			break;
		case 12:
			// KISS FM
			url = "http://kissfm.es.audio1.glb.ipercast.net:8000/kissfm.es/mp3";
			break;
		case 13:
			// Radio Marca
			url = "http://46.105.171.200:8067";
			break;
		case 14:
			// RAC 105
			url = "http://178.32.113.2:8090";
			break;
		case 15:
			// Catalunya Radio
			url = "http://195.10.10.223/ccma/catradio.mp3";
			break;
		case 16:
			// Catalunya Informació
			url = "http://195.10.10.221/ccma/catinform.mp3";
			break;
		case 17:
			// Catalunya Música
			url = "http://195.10.10.223/ccma/catmusica.mp3";
			break;
		case 18:
			// RNE Radio Clásica
			url = "http://195.10.10.219/rtve/radioclasica.mp3";
			break;
		case 19:
			// Radio Andalucia Informacion
			url = "http://195.10.10.219/rtva/radioandalucia.mp3";
			break;
		case 20:
			// Canal Sur
			url = "http://195.10.10.219/rtva/canalsurradio_master.mp3";
			break;
		case 21:
			// Canal Fiesta
			url = "http://195.10.10.223/rtva/canalfiestaradio_master.mp3";
			break;
		case 22:
			// Radio Euskadi
			url = "http://eitbradio-origin.cires21.com/tunein/RadioEuskadi.mp3";
			break;
		case 23:
			// EsRadio
			url = "http://94.23.18.227:8822";
			break;
		case 24:
			// Euskadi Irratia
			url = "http://eitbradio-origin.cires21.com/tunein/EuskadiIrratia.mp3";
			break;
		case 25:
			// Euskadi Vitoria
			url = "http://eitbradio-origin.cires21.com/tunein/RadioVitoria.mp3";
			break;
		case 26:
			// Gaztea
			url = "http://eitbradio-origin.cires21.com/tunein/Gaztea.mp3";
			break;
		case 27:
			// M80
			url = "http://188.93.73.98:8114";
			break;
		case 28:
			// Cadena 100
			url = "http://84.127.246.10:8085";
			break;
		case 29:
			// Radiolé
			url = "http://74.52.37.26:7112";
			break;
		case 30:
			// Chill Out Zone
			url = "http://37.187.79.56:3078/320k";
			break;
		case 31:
			// Radio Mozart
			/*
			 * Bundle bun = new Bundle(); bun.putString("url", "http://yp.shoutcast.com/sbin/tunein-station.m3u?id=140955"); new obtenerURLdeM3U().execute(bun);
			 */
			url = "http://listen.radionomy.com/Radio-Mozart";
			break;
		case 32:
			// Radio Clasica de Venecia
			url = "http://109.123.116.202:8010";
			break;
		}
		// if (this.emisora != 3) {
		try {

			this.mp.reset();
			this.mp.setDataSource(url);
			this.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

			/*
			 * this.mp.prepare(); this.mp.start(); this.emisoraEnMarcha = true;
			 */
			this.preparandoEmisora = true;
			this.mp.prepareAsync();
			this.mp.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					emisoraEnMarcha = true;
				}
			});

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void resizeToFullScreen() {
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

	private void preguntarTemporizador() {

		if (this.emisoraEnMarcha) {
			this.mp.pause();
		}

		try {
			MediaPlayer mp2 = new MediaPlayer();
			mp2 = MediaPlayer.create(this, R.raw.pregunta_temporizador);
			mp2.prepare();
			mp2.start();

			mp2.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp2) {

					mp2.stop();
					
					emisoraEnMarcha = true;
					preparandoEmisora = false;

					Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
					i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

					try {
						startActivityForResult(i, PREGUNTAR_CONFIRMACION_TEMPORIZADOR);
					} catch (Exception e) {
						// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
					}
				}
			});
		} catch (Exception e) {

		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
		case 1:
			Log.d("Direccion", "top");
			this.preguntarTemporizador();
			return true;
		case 2:
			Log.d("Direccion", "left");
			return true;
		case 3:
			Log.d("Direccion", "down");

			if (this.emisoraEnMarcha) {
				this.mp.stop();
				this.mp.reset();

				this.mp = MediaPlayer.create(this, R.raw.cerrando_emisora);
				this.mp.start();

				this.mp.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {

						mp.stop();
						emisora = 0;
						emisoraEnMarcha = false;
					}
				});
			}
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

		this.mp.stop();
		this.mp.reset();

		this.mp = MediaPlayer.create(this, R.raw.cerrando_radio);
		this.mp.start();

		this.mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				cerrar();
			}
		});
	}

	public void cerrar() {
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

		if (!this.preparandoEmisora)
			if (this.emisoraEnMarcha) {
				if (this.mp.isPlaying()) {
					this.mp.pause();
				} else if (!this.mp.isPlaying())
					this.mp.start();
			} else {
				this.elegirEmisora();
			}

		return false;
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub

	}
}