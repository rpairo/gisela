package musica;

import gisela.Principal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import speaker.Speaker;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
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
public class ReproductorMusica extends Activity implements OnGestureListener {

	private boolean endSpeaking;
	private Speaker voiceSpeaker;

	private File carpetaCanciones;
	private File[] canciones;

	private String[] nombreCanciones;
	private String[] rutasCanciones;

	private MediaPlayer mp;

	private ArrayList<File> cancionesArrayList;
	private Bundle bund;

	private File[] cancionesDirectorio;
	private ArrayList<File> todasLasCanciones;

	private float x1, x2;
	static final int MIN_DISTANCE = 150;

	private int index = 0;

	private GestureDetectorCompat mDetector;

	/**
	 * This function is responsible for creating the layout, initialize the necessary objects, create a random playlist and start the playback of the list
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Principal.active = 1;
		setContentView(R.layout.activity_reproductor);

		this.resizeToFullScreen();

		this.todasLasCanciones = new ArrayList<File>();
		this.voiceSpeaker = new Speaker(this);
		this.endSpeaking = false;

		carpetaCanciones = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		canciones = carpetaCanciones.listFiles();

		for (int i = 0; i < canciones.length; i++) {
			if (canciones[i].isDirectory()) {
				this.cancionesDirectorio = canciones[i].listFiles();
				for (int a = 0; a < this.cancionesDirectorio.length; a++)
					this.todasLasCanciones.add(this.cancionesDirectorio[a]);
			} else {
				this.todasLasCanciones.add(canciones[i]);
			}
		}

		canciones = new File[this.todasLasCanciones.size()];

		for (int i = 0; i < canciones.length; i++)
			this.canciones[i] = this.todasLasCanciones.get(i);

		nombreCanciones = carpetaCanciones.list();
		rutasCanciones = new String[canciones.length];

		cancionesArrayList = new ArrayList<File>();
		this.index = 0;

		this.mp = new MediaPlayer();

		try {

			reproduceAleatorio();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		mDetector = new GestureDetectorCompat(this, this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Principal.active = 0;
		this.mp.stop();
		this.voiceSpeaker.cerrar();
		this.finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Principal.active = 1;
	}

	/**
	 * This function serves in an architecture activity, necessary for the proper workflow
	 * 
	 * @throws Throwable
	 */
	public void reproduceAleatorio() throws Throwable {

		this.ordenacio(canciones);

		reproduce();

		this.mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {

				index++;

				try {

					reproduce();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * This function handles the play list, and give the name of the current song.
	 * 
	 * @throws Throwable
	 */
	public void reproduce() throws Throwable {

		if (index >= canciones.length) {
			this.endSpeaking = false;
			voiceSpeaker.habla("Ya no quedan más canciones.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);

			this.finalize();
		} /*else if (index < 0) {
			this.endSpeaking = false;
			voiceSpeaker.habla("No hay canciones previas.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);
		}*/ else {

			String nombreCancion;
			nombreCancion = canciones[index].getName();
			nombreCancion = nombreCancion.replace(".mp3", "");
			nombreCancion = nombreCancion.replace("_", " ");

			if (this.index > 0) {
				this.endSpeaking = false;
				voiceSpeaker.habla("Sonando: " + nombreCancion);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);
			}
			this.mp.reset();
			this.mp.setDataSource(canciones[index].getAbsolutePath());
			this.mp.prepare();
			this.mp.start();
		}
	}

	/**
	 * This function has an overload that allows it to act in the event that the song is advanced or postponed.
	 * 
	 * @param boolean lesftToRight
	 * @throws Throwable
	 */
	public void reproduce(boolean leftToRight) throws Throwable {

		if (leftToRight) {
			if (this.index > 0) {
				this.index -= 1;
				this.mp.pause();
				this.reproduce();
			}
		} else if (!leftToRight) {
			if (this.index < this.canciones.length) {
				this.index += 1;
				this.mp.pause();
				this.reproduce();
			}
		}
	}

	/**
	 * This function handles the algorithm scrambles the songs, to create a random playlist.
	 * 
	 * @param File
	 */
	public void ordenacio(File[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			File a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	/**
	 * This function is responsible for converting the application to full screen
	 */
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

	/**
	 * This function is responsible for detecting the Swipe gesture to go to next or previous song.
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		final float xDistance = Math.abs(e1.getX() - e2.getX());
		final float yDistance = Math.abs(e1.getY() - e2.getY());

		velocityX = Math.abs(velocityX);
		velocityY = Math.abs(velocityY);
		boolean result = false;

		if (velocityX > 100 && xDistance > 100) {
			if (e1.getX() > e2.getX()) // right to left
			{
				try {
					this.mp.pause();
					reproduce(true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} else {
				try {
					this.mp.pause();
					reproduce(false);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			result = true;
		}

		return false;
	}

	/**
	 * This function is responsible for detecting a long press on the screen and close the activity.
	 */
	@Override
	public void onLongPress(MotionEvent e) {

		this.mp.stop();

		this.endSpeaking = false;
		voiceSpeaker.habla("Cerrando el reproductor de música");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);

		this.mp.stop();
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
		if (this.mp.isPlaying()) {
			this.mp.pause();
		} else if (!this.mp.isPlaying())
			this.mp.start();
		return false;
	}
}