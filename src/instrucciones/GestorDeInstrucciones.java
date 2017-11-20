package instrucciones;

import java.util.ArrayList;

import gisela.Principal;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
public class GestorDeInstrucciones extends Activity implements OnGestureListener {

	private GestureDetectorCompat mDetector;
	private MediaPlayer mp;
	private boolean hablandoDeFunciones;
	private boolean fraseBienvenida;
	private String fraseReconocida;
	private String[] fraseSpliteada;

	private static final int PREGUNTA_INSTRUCCIONES = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Principal.active = 1;
		setContentView(R.layout.activity_instrucciones);

		this.resizeToFullScreen();

		this.fraseReconocida = "";
		this.fraseBienvenida = true;
		this.hablandoDeFunciones = false;

		mp = MediaPlayer.create(this, R.raw.manual_de_instrucciones);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				fraseBienvenida = false;
			}
		});

		mDetector = new GestureDetectorCompat(this, this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Principal.active = 0;
		this.mp.stop();
		this.finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.mp.stop();
		// Principal.active = 1;
	}

	private void resizeToFullScreen() {
		// Ajustar la activity a pantalla completa
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * This function is responsible for collecting tactile gestures
	 */
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
			Log.d("Direccion", "top");
			return true;
		case 2:
			Log.d("Direccion", "left");
			return true;
		case 3:
			this.mp.stop();
			this.hablandoDeFunciones = false;
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PREGUNTA_INSTRUCCIONES && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			fraseReconocida = thingsYouSaid.get(0);

			this.fraseReconocida = this.fraseReconocida.replace("!", "");
			this.fraseReconocida = this.fraseReconocida.replace("?", "");
			this.fraseReconocida = this.fraseReconocida.replace(",", "");
			this.fraseReconocida = this.fraseReconocida.replace(".", "");
			this.fraseReconocida = this.fraseReconocida.toUpperCase();

			// Divide la frase en palabras, para poder interpretar las palabras
			this.fraseSpliteada = this.fraseReconocida.split(" ");

			for (int i = 0; i < this.fraseSpliteada.length; i++) {
				if (this.fraseSpliteada[i].equals("LLAMAR")) {
					this.reproducirInstruccion(1);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("REPRODUCTOR")) {
					this.reproducirInstruccion(2);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MÚSICA")) {
					this.reproducirInstruccion(2);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("HORA")) {
					this.reproducirInstruccion(3);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("FECHA")) {
					this.reproducirInstruccion(3);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("LOCALIZACIÓN")) {
					this.reproducirInstruccion(4);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("LOCALIZAR")) {
					this.reproducirInstruccion(4);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("UBICACIÓN")) {
					this.reproducirInstruccion(4);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("NOTICIAS")) {
					this.reproducirInstruccion(5);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("TIEMPO")) {
					this.reproducirInstruccion(6);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("CLIMATOLÓGICAS")) {
					this.reproducirInstruccion(6);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("SOCORRER")) {
					this.reproducirInstruccion(7);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("SOCORRO")) {
					this.reproducirInstruccion(7);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MENSAJE")) {
					this.reproducirInstruccion(8);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("MENSAJES")) {
					this.reproducirInstruccion(8);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("TRADUCTOR")) {
					this.reproducirInstruccion(9);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("TRADUCIR")) {
					this.reproducirInstruccion(9);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("VOLUMEN")) {
					this.reproducirInstruccion(10);
					i = this.fraseSpliteada.length;
				} else if (this.fraseSpliteada[i].equals("FUNCIONES")) {
					this.reproducirInstruccion(11);
					i = this.fraseSpliteada.length;
				}
			}
		}
	}

	private void reproducirInstruccion(int instruccion) {

		int funcion = 0;

		// falta crear audios de las instrucciones individualizadas

		switch (instruccion) {
		case 1:
			// Llamar. Puede llamar a alguien de la siguiente forma: llama a Noelia. o, llama al 620 61 62 50.
			funcion = R.raw.instrucciones_llamar;
			break;
		case 2:
			// "Reproductor de música. Puedo reproducir música de la siguiente forma: reproduce música. o, gisela, ponme algo de música. El reproductor de música se gestiona a través de gestos táctiles. Un tap, pausará o reanudará la reproducción de la música. Deslizar el dedo hacia la derecha cambiará la siguiente canción. Deslizar a la izquierda, cambiará a la canción anterior, y mantener pulsada la pantalla, apagará el reproductor de música."
			funcion = R.raw.instrucciones_reproductor_musica;
			break;
		case 3:
			funcion = R.raw.instrucciones_fecha_hora;
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		case 11:
			funcion = R.raw.funciones_instrucciones;
			break;
		}

		mp.reset();
		mp = MediaPlayer.create(this, funcion);
		this.hablandoDeFunciones = true;
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				hablandoDeFunciones = false;
			}
		});
	}

	@Override
	public void onLongPress(MotionEvent e) {

		this.mp.stop();
		this.mp.reset();
		mp = MediaPlayer.create(this, R.raw.cerrando_manual_instrucciones);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				finish();
			}
		});
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

		if (!this.fraseBienvenida) {
			if (this.hablandoDeFunciones) {
				if (this.mp.isPlaying())
					this.mp.pause();
				else if (!this.mp.isPlaying())
					this.mp.start();
			} else {
				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {
					startActivityForResult(i, PREGUNTA_INSTRUCCIONES);
				} catch (Exception a) {
					// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
				}
			}

		}

		return false;
	}
}