package gisela;

import hora.Hora;
import instrucciones.GestorDeInstrucciones;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import localizacion.DescargarLocalizacionYahoo;
import musica.ReproductorMusica;
import noticias.DescargarNoticias;
import speaker.Speaker;
import translate.GestorTraductor;
import weather.DescargarWeather;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.gisela.R;

import contactos.Contacto;
import database.SQLiteHelper;
import excepciones.ExcepcionLocalizacionDesactivada;
import fecha.Fecha;

/**
 * This is the main class of the app, which is responsible for the overall management
 * 
 * @author Raul Pera Pairó
 * 
 */
public class Principal extends Activity implements LocationListener, OnGestureListener {

	private SQLiteHelper helper;
	private static final int VOICE_LISTENER = 1; // REQUEST_OK: 1 =
													// VoiceListener;
	private static final int VOICE_LISTENER_PREGUNTA_PERIODICO = 2; // REQUEST_OK:
																	// 2;
	private static final int VOICE_LISTENER_PREGUNTA_CONTACTO = 3; // REQUEST_OK:
																	// 3;
	private static final int VOICE_LISTENER_PREGUNTA_CONTACTO_SMS = 4; // REQUEST_OK:
																		// 4;
	private static final int VOICE_LISTENER_RECOGER_TEXTO_SMS = 5; // REQUEST_OK:
																	// 5;
	private static final int VOICE_LISTENER_SOCORRO = 6; // REQUEST_OK: 6;
	private static final int VOICE_LISTENER_SOCORRO_ENVIO_SMS = 7; // REQUEST_OK:
																	// 7;
	private String fraseReconocida; // frase reconocida por el VoiceListener
	private String[] fraseSpliteada; // Array de palabras de fraseReconocida
	private String funcionDevuelta; // nombre de la funcion a ejecutar
	private int posicionOrden; // posicion del array fraseSpliteada donde se
								// encuentra la orden

	private Boolean endSpeaking = false;
	private Speaker voiceSpeaker;

	private ArrayList<Contacto> contactos;
	private ArrayList<Contacto> contactosNombreCoincidente;
	private int numInsulto;
	private Contacto contactoSMS;

	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean canGetLocation = false;
	private Location location;
	private double latitude;
	private double longitude;
	private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	private LocationManager locationManager;

	private String calleUbicacionActual;
	private String ciudadUbicacionActual;
	private String paisUbicacionActual;

	private String textoSocorrer;

	private GestureDetectorCompat mDetector;
	public static int active = 0;

	/**
	 * 
	 * This function is responsible for loading and initializing the layout of the key objects.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_principal);

		this.helper = new SQLiteHelper(this);

		try {
			this.voiceSpeaker = new Speaker(this);

		} catch (Exception e) {
			System.out.println("Error en la creación del Speaker");
		}

		resizeToFullScreen();
		// this.controlBrillo();

		mDetector = new GestureDetectorCompat(this, this);
		active = 0;
	}

	/**
	 * 
	 * This function takes care of closing the DB and closing the app Speaker
	 * 
	 */
	protected void onDestroy() {
		super.onDestroy();

		this.helper.close();
		this.voiceSpeaker.cerrar();
		active = 0;
	}

	protected void onStop() {
		super.onStop();

		active = 0;
	}

	// --------------------- arquitectura funcional --------------------- \\

	/**
	 * 
	 * This function is responsible for lowering the screen brightness to the minimum
	 * 
	 */
	private void controlBrillo() {
		// Bajar el brillo de la pantalla al minimo

		WindowManager.LayoutParams layout = getWindow().getAttributes();
		layout.screenBrightness = 0;
		getWindow().setAttributes(layout);
	}

	/**
	 * 
	 * This function is invoked by clicking on the screen, is responsible for calling the function that manages the collection of question
	 * 
	 * @param View
	 */
	/*
	 * public void pregunta(View v) { this.preguntar(); }
	 */

	/**
	 * 
	 * This function handles the invocation of VoiceListener
	 */
	public void preguntar() {
		this.fraseReconocida = "";

		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
		i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

		try {
			startActivityForResult(i, VOICE_LISTENER);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
			System.out.println("Este movil no es capaz de usar el reconocimiento de voz.");
		} catch (Exception e) {
			System.out.println("No hay conexión a internet");
		}

		// eliminar, solo son para pruebas
		// this.fraseReconocida = "socorro";
		// seguirConLaPregunta();
	}

	/**
	 * 
	 * This function is responsible for clearance of the sentence received, taking away inappropriate characters to uppercase spliteandola and pasandola
	 * 
	 */
	public void seguirConLaPregunta() {

		this.fraseReconocida = this.fraseReconocida.replace("!", "");
		this.fraseReconocida = this.fraseReconocida.replace("?", "");
		this.fraseReconocida = this.fraseReconocida.replace(",", "");
		this.fraseReconocida = this.fraseReconocida.replace(".", "");
		this.fraseReconocida = this.fraseReconocida.toUpperCase();

		// Divide la frase en palabras, para poder interpretar las palabras
		this.fraseSpliteada = this.fraseReconocida.split(" ");

		this.funcionDevuelta = this.consultaFunciones(this.fraseSpliteada);

		if (this.funcionDevuelta != null)
			invocarMetodos();

		// Toast.makeText(this, "Funcion: " + this.funcionDevuelta, Toast.LENGTH_LONG).show();
		// Toast.makeText(this, this.fraseReconocida, Toast.LENGTH_LONG).show();
	}

	/**
	 * This function handles the query based on dice, word by word, until you find a match with any keyword, but invoked the "noEntiendoPregunta" function
	 * 
	 * @param String
	 *            []
	 * @return String
	 */
	private String consultaFunciones(String[] orden) {

		String[] campos = { helper.COL_FUNCIONES };
		String[] args = new String[1];
		String funcion = null;
		Cursor cursor = null;

		try {

			SQLiteDatabase db = helper.getReadableDatabase();

			for (int i = 0; i < orden.length; i++) {

				args[0] = orden[i];
				cursor = db.query(helper.TABLE_NAME, campos, helper.COL_ORDENES + " = ?", args, null, null, null);

				if (cursor.getCount() > 0) {
					this.posicionOrden = i;
					i = orden.length;
				}
			}

			if (cursor.getCount() > 0) {
				cursor.moveToNext();
				funcion = cursor.getString(0);
			} else
				funcion = "noEntiendoPregunta";

			db.close();

		} catch (CursorIndexOutOfBoundsException ciobe) {
			Log.d("Excepcion", "No se ha encontrado ningun registro");

			// Como posible funcion
			if (this.fraseReconocida.contains("SOCORRO") || (this.fraseReconocida.contains("EMERGENCIAS"))) {
				String[] socorro = { "SOCORRO" };
				funcion = consultaFunciones(socorro);
			}
		}

		return funcion;
	}

	/**
	 * This function is responsible for returning a coherent response if you receive an insult parde User
	 */
	private void noInsultes() {

		numInsulto = (int) (0 + (Math.random() * ((2 - 0) + 1)));

		System.out.println(numInsulto);

		String cadena = null;

		switch (numInsulto) {
		case 0:
			cadena = "Modera el vocabulario.";
			break;
		case 1:
			cadena = "Tengo sentimientos, no me gusta que me ofendan.";
			break;
		default:
			cadena = "No insultes por favor!";
			break;
		}

		this.endSpeaking = false;
		voiceSpeaker.habla(cadena);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);
	}

	/**
	 * This function is responsible for responding in the event of failing to recognize a key word in the sentence received
	 */
	private void noEntiendoPregunta() {
		this.endSpeaking = false;
		voiceSpeaker.habla("Lo siento. no entiendo la pregunta, vuelve a repetirla.");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);

		this.preguntar();
	}

	/**
	 * This function is responsible for calling the corresponding functions by "Reflection", the function receiving the request from the DB
	 */
	private void invocarMetodos() {

		try {

			Method[] allMethods;

			allMethods = this.getClass().getDeclaredMethods();

			for (Method m : allMethods) {

				if (m.getName().equals(this.funcionDevuelta))
					m.invoke(this);
			}

		} catch (IllegalAccessException iae) {
			iae.getStackTrace();
		} catch (InvocationTargetException ite) {
			ite.getStackTrace();
		}
	}

	/**
	 * 
	 * This onActivityResult function manages all invocations startActivityForResult
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == VOICE_LISTENER && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			// Toast.makeText(this, thingsYouSaid.get(0), Toast.LENGTH_LONG).show();
			this.fraseReconocida = thingsYouSaid.get(0);

			Toast.makeText(this, this.fraseReconocida, Toast.LENGTH_SHORT).show();

			System.out.println(this.fraseReconocida);

			seguirConLaPregunta();

		} else if (requestCode == VOICE_LISTENER_PREGUNTA_PERIODICO && resultCode == RESULT_OK) {

			String periodico = "";
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

			System.out.println(segundaFraseReconocida);

			for (int i = 0; i < segundaFraseSplit.length; i++) {
				if (segundaFraseSplit[i].equals("MUNDO")) {
					periodico = "http://www.elmundo.es/rss/portada.xml";
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("PAÍS")) {
					periodico = "http://ep00.epimg.net/rss/elpais/portada.xml";
					i = segundaFraseSplit.length;
				} else if (segundaFraseSplit[i].equals("MARCA")) {
					periodico = "http://marca.feedsportal.com/rss/portada.xml";
					i = segundaFraseSplit.length;
				}
			}

			Bundle bund = new Bundle();
			bund.putSerializable("speaker", this.voiceSpeaker);
			bund.putString("url", periodico);

			new DescargarNoticias().execute(bund);

		} else if (requestCode == VOICE_LISTENER_PREGUNTA_CONTACTO && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String nombreCompleto = thingsYouSaid.get(0);

			System.out.println("Nombre completo: " + nombreCompleto);

			for (Contacto contacto : contactos) {
				if (contacto.getNombre().toUpperCase().equals(this.quitarAcentos(nombreCompleto.toUpperCase()))) {
					contactosNombreCoincidente = new ArrayList<Contacto>();
					contactosNombreCoincidente.add(contacto);
				}
			}

			Contacto contacto = this.contactosNombreCoincidente.get(0);

			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacto.getNumero()));
			startActivity(i);
		} else if (requestCode == VOICE_LISTENER_PREGUNTA_CONTACTO_SMS && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String nombreCompleto = thingsYouSaid.get(0);

			System.out.println(nombreCompleto);

			for (Contacto contacto : contactos) {
				if (contacto.getNombre().toUpperCase().equals(this.quitarAcentos(nombreCompleto.toUpperCase()))) {
					contactosNombreCoincidente = new ArrayList<Contacto>();
					contactosNombreCoincidente.add(contacto);
				}
			}

			this.contactoSMS = this.contactosNombreCoincidente.get(0);

			voiceSpeaker.habla("Diga el mensaje por favor.");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);

			Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
			i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

			try {
				startActivityForResult(i, VOICE_LISTENER_RECOGER_TEXTO_SMS);
			} catch (Exception e) {
				Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == VOICE_LISTENER_RECOGER_TEXTO_SMS && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String texto = thingsYouSaid.get(0);

			if (this.contactoSMS != null) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(this.contactoSMS.getNumero(), null, texto, null, null);

				Toast.makeText(this, "Enviando mensaje a: " + this.contactoSMS.getNombre() + " , con numero: " + this.contactoSMS.getNumero() + " diciendo: " + texto, Toast.LENGTH_LONG).show();

				voiceSpeaker.habla("El mensaje se ha enviado correctamente.");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);

			} else {
				voiceSpeaker.habla("Lo siento, ha ocurrido un error con el contacto");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);
			}
		} else if (requestCode == VOICE_LISTENER_SOCORRO && resultCode == RESULT_OK) {
			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String nombreCompleto = thingsYouSaid.get(0);

			nombreCompleto = quitarAcentos(nombreCompleto);

			System.out.println(nombreCompleto);

			conseguirContactoSocorro(nombreCompleto);

		} else if (requestCode == VOICE_LISTENER_SOCORRO_ENVIO_SMS && resultCode == RESULT_OK) {

			ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String nombreCompleto = thingsYouSaid.get(0);

			System.out.println(nombreCompleto);

			for (Contacto contacto : contactos) {
				if (contacto.getNombre().toUpperCase().equals(this.quitarAcentos(nombreCompleto.toUpperCase()))) {
					contactosNombreCoincidente = new ArrayList<Contacto>();
					contactosNombreCoincidente.add(contacto);
				}
			}

			this.contactoSMS = this.contactosNombreCoincidente.get(0);

			this.envioMensajeSocorro(contactoSMS);

		}
	}

	/**
	 * This function is responsible for managing the SMS sending relief to the contact points
	 * 
	 * @param Contacto
	 */
	private void envioMensajeSocorro(Contacto contactoSoc) {

		if (contactoSoc != null) {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(contactoSoc.getNumero(), null, this.textoSocorrer, null, null);

			System.out.println("Enviando mensaje a: " + contactoSoc.getNombre() + " , con numero: " + contactoSoc.getNumero() + " diciendo: " + this.textoSocorrer);
			Toast.makeText(this, "Enviando mensaje a: " + contactoSoc.getNombre() + " , con numero: " + contactoSoc.getNumero() + " diciendo: " + this.textoSocorrer, Toast.LENGTH_LONG).show();
		}

		voiceSpeaker.habla("Mensaje de emergencia enviado!");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);
	}

	/**
	 * This function handles to help get contact name from a received
	 * 
	 * @param String
	 */
	private void conseguirContactoSocorro(String nombreCompleto) {
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

		contactos = new ArrayList<Contacto>();
		contactosNombreCoincidente = new ArrayList<Contacto>();

		while (phones.moveToNext()) {

			Contacto con = new Contacto();
			con.setNumero(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			con.setNombre(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

			contactos.add(con);
		}

		for (Contacto contacto : contactos) {
			if (contacto.getNombre().split(" ")[0].toUpperCase().equals(nombreCompleto.toUpperCase())) {
				contactosNombreCoincidente.add(contacto);
			}
		}

		// // cuidado

		if (contactosNombreCoincidente.size() > 1) {
			this.endSpeaking = false;
			voiceSpeaker.habla("Existe mas de un contacto llamado: " + nombreCompleto + ". Elija entre: ");
			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);

			// for (Contacto contacto : contactosNombreCoincidente) {
			for (int i = 0; i < contactosNombreCoincidente.size(); i++) {
				// Toast.makeText(this, contacto.getNombre() + ". ",
				// Toast.LENGTH_LONG).show();
				this.endSpeaking = false;

				if (i == (contactosNombreCoincidente.size() - 1))
					voiceSpeaker.habla(contactosNombreCoincidente.get(i).getNombre() + ".");
				else
					voiceSpeaker.habla(contactosNombreCoincidente.get(i).getNombre() + ". o ");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);
			}

			phones.close();

			// Toast.makeText(this, "Diga el nombre completo: ",
			// Toast.LENGTH_LONG).show();
			this.endSpeaking = false;
			voiceSpeaker.habla("Diga el nombre completo: ");

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);

			Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
			i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

			try {
				startActivityForResult(i, VOICE_LISTENER_SOCORRO_ENVIO_SMS);
			} catch (Exception e) {
				Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
			}
		} else if (contactosNombreCoincidente.size() == 1) {

			Contacto contacto = this.contactosNombreCoincidente.get(0);

			envioMensajeSocorro(contacto);
			// Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
			// contacto.getNumero()));
			// startActivity(i);
		} else {
			this.endSpeaking = false;
			voiceSpeaker.habla("No exíste ningún contacto llamado: " + nombreCompleto);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);
		}
	}

	// --------------------- funciones --------------------- \\

	/**
	 * Esta función invoca la clase Hora, que dictara la hora al usuario.
	 * 
	 * @throws IOException
	 */
	private void hora() throws IOException {
		new Hora(this.voiceSpeaker);
	}

	/**
	 * This function is responsible for obtaining XML data with climatological yahoo servers, by sending our location
	 */
	private void consultarElTiempo() {

		this.comprobarServiciosLocalizacion();

		try {
			this.getLocation();

			System.out.println("Latitude: " + this.latitude);
			System.out.println("Longitude: " + this.longitude);

			String urlConseguirWoeid = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20geo.placefinder%20WHERE%20text%3D%22" + this.latitude + "%2C" + this.longitude + "%22%20and%20gflags%3D%22R%22&diagnostics=true";

			Bundle bund = new Bundle();
			bund.putString("url", urlConseguirWoeid);
			bund.putSerializable("speaker", this.voiceSpeaker);
			bund.putDouble("latitude", this.latitude);
			bund.putDouble("longitude", this.longitude);

			new DescargarWeather().execute(bund);

		} catch (ExcepcionLocalizacionDesactivada e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is responsible for greeting the user
	 */
	private void saludar() {
		this.endSpeaking = false;

		if (this.fraseSpliteada.length > this.posicionOrden)
			if (this.fraseSpliteada[this.posicionOrden + 1] == "a")
				if (this.fraseSpliteada.length >= this.posicionOrden + 2)
					voiceSpeaker.habla("Buenos días " + this.fraseSpliteada[this.posicionOrden + 2]);
				else
					voiceSpeaker.habla("Buenos días " + this.fraseSpliteada[this.posicionOrden + 1]); // ya funciona porque es mayor que posicionOrden

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);
	}

	private void buenasNoches() {

		MediaPlayer mp;
		int frase;

		// pasar a mp3
		Calendar c = Calendar.getInstance();
		int time = c.get(Calendar.HOUR_OF_DAY);

		if (time >= 6 && time <= 15) {
			frase = R.raw.bnches_ma;
			Log.d("NOCHES", "de buenas noches nada, mira que hora es, disfruta de la mañana");
		} else if (time > 15 && time <= 20) {
			frase = R.raw.bnches_trde;
			Log.d("NOCHES", "buenas noches, aún tienes mucha tarde por delante, disfrutala");
		} else {
			frase = R.raw.bnches_nche;
			Log.d("NOCHES", "grácias, pero no necesito dormir, velaré por ti, descansa y disfruta de la noche");
		}

		mp = MediaPlayer.create(this, frase);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
			}
		});
	}

	/**
	 * This function is responsible for greeting the user
	 */
	private void presentarse() {
		this.endSpeaking = false;
		voiceSpeaker.habla("Buenos días. Soy Gisela!. Una asistente digital para personas ciegas.");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		do {
			endSpeaking = voiceSpeaker.tts.isSpeaking();
		} while (endSpeaking);
	}

	/**
	 * This function is responsible for calling the User class to issue all functions that are able to run the app, for instruction manual
	 */
	private void instrucciones() {

		/*
		 * MediaPlayer mp = MediaPlayer.create(this, R.raw.instrucciones); mp.start();
		 * 
		 * mp.setOnCompletionListener(new OnCompletionListener() {
		 * 
		 * @Override public void onCompletion(MediaPlayer mp) {
		 * 
		 * } });
		 */

		Intent i = new Intent(this, GestorDeInstrucciones.class);
		startActivity(i);

		// new Instrucciones().execute(this.voiceSpeaker);
	}

	/**
	 * This function takes care of invoking the activity of playing music
	 * 
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void reproducirMusica() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		Intent i = new Intent(this, ReproductorMusica.class);
		startActivity(i);
	}

	/**
	 * This function is called when sending an SMS, this same request, calls the function in charge of getting the recipient contact
	 */
	public void enviarSMS() {
		conseguirContacto(2);
	}

	/**
	 * This function is called when the user wants to call, is in charge of calling the function to get the recipient contact
	 */
	public void llamar() {
		conseguirContacto(1);
	}

	/**
	 * This function checks if the last character is a number
	 * 
	 * @param String
	 * @return boolean
	 */
	private static Boolean comprobarSiEsNumero(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * This function is responsible for getting the contact that the user requests
	 * 
	 * @param int
	 */
	public void conseguirContacto(int cliente) {

		// cliente 1 == llamadas
		// cliente 2 == mensajes

		if (comprobarSiEsNumero(fraseSpliteada[posicionOrden + 2])) {

			StringBuilder telefono = new StringBuilder();

			for (int i = (this.posicionOrden + 2); i < this.fraseSpliteada.length; i++) {
				if (comprobarSiEsNumero(this.fraseSpliteada[i]))
					telefono.append(this.fraseSpliteada[i]);
			}

			String numero = telefono.toString();

			Contacto cont = new Contacto("Desconocido", numero);

			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cont.getNumero()));
			startActivity(i);

		} else {

			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

			contactos = new ArrayList<Contacto>();
			contactosNombreCoincidente = new ArrayList<Contacto>();

			while (phones.moveToNext()) {

				Contacto con = new Contacto();
				con.setNumero(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				con.setNombre(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

				contactos.add(con);
			}

			for (Contacto contacto : contactos) {
				if (contacto.getNombre().split(" ")[0].toUpperCase().equals(this.quitarAcentos(this.fraseSpliteada[posicionOrden + 2].toUpperCase()))) {
					contactosNombreCoincidente.add(contacto);
				}
			}

			if (contactosNombreCoincidente.size() > 1) {
				this.endSpeaking = false;
				voiceSpeaker.habla("Existe mas de un contacto llamado: " + fraseSpliteada[posicionOrden + 2] + ". Elija entre: ");
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);

				for (int i = 0; i < contactosNombreCoincidente.size(); i++) {
					this.endSpeaking = false;

					if (i == (contactosNombreCoincidente.size() - 1))
						voiceSpeaker.habla(contactosNombreCoincidente.get(i).getNombre() + ".");
					else
						voiceSpeaker.habla(contactosNombreCoincidente.get(i).getNombre() + ". o ");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					do {
						endSpeaking = voiceSpeaker.tts.isSpeaking();
					} while (endSpeaking);
				}

				phones.close();

				this.endSpeaking = false;
				voiceSpeaker.habla("Diga el nombre completo: ");

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);

				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {
					if (cliente == 1)
						startActivityForResult(i, VOICE_LISTENER_PREGUNTA_CONTACTO);
					else if (cliente == 2)
						startActivityForResult(i, VOICE_LISTENER_PREGUNTA_CONTACTO_SMS);
				} catch (Exception e) {
					Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
				}
			} else if (contactosNombreCoincidente.size() == 1) {

				Contacto contacto = this.contactosNombreCoincidente.get(0);
				this.contactoSMS = contacto;

				if (cliente == 1) {
					Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacto.getNumero()));
					startActivity(i);
				} else if (cliente == 2) {
					voiceSpeaker.habla("Diga el mensaje por favor.");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					do {
						endSpeaking = voiceSpeaker.tts.isSpeaking();
					} while (endSpeaking);

					Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
					i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

					try {
						startActivityForResult(i, VOICE_LISTENER_RECOGER_TEXTO_SMS);
					} catch (Exception e) {
						Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
					}
				}
			} else {
				this.endSpeaking = false;
				voiceSpeaker.habla("No exíste ningún contacto llamado: " + fraseSpliteada[posicionOrden + 2]);

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				do {
					endSpeaking = voiceSpeaker.tts.isSpeaking();
				} while (endSpeaking);
			}
		}
	}

	/**
	 * 
	 * This function takes care of changing all characters with strange letters or symbols, for normal letters
	 * 
	 * @param String
	 *            input
	 * @return String output
	 */
	public static String quitarAcentos(String input) {
		String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}
		return output;
	}

	/**
	 * 
	 * This function is responsible for passing the app full screen, so the user can not give any unnecessary button
	 * 
	 */
	private void resizeToFullScreen() {
		// Ajustar la activity a pantalla completa
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * This function is called when the user requests the reading of newspapers, which is responsible for asking the user wants to read the newspaper.
	 */

	private void noticias() {

		MediaPlayer mp = MediaPlayer.create(this, R.raw.elegir_periodico);
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
				i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

				try {
					startActivityForResult(i, VOICE_LISTENER_PREGUNTA_PERIODICO);
				} catch (Exception e) {
					// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * This function is invoked when to help the user, is responsible for generating a message with a standard template text, essential for the rescue of user information.
	 * 
	 * @throws IOException
	 */
	public void socorrer() throws IOException {

		Location loc;
		try {
			loc = this.getLocation();

			this.parsearPosicionString(loc);

			this.textoSocorrer = "Socorro, me encuentro en " + this.calleUbicacionActual + ", " + this.ciudadUbicacionActual + ", " + this.paisUbicacionActual + ". Y necesito ayuda!";

			MediaPlayer mp = MediaPlayer.create(this, R.raw.contacto_de_emergencia);
			mp.start();

			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
					i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, true);

					try {
						startActivityForResult(i, VOICE_LISTENER_SOCORRO);
					} catch (Exception e) {
						// Toast.makeText(this, "Este movil no es capaz de usar el reconocimiento de voz.", Toast.LENGTH_LONG).show();
					}
				}
			});

		} catch (ExcepcionLocalizacionDesactivada e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This function is responsible for checking the location service providers are active
	 */
	private void comprobarServiciosLocalizacion() {
		locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			// no network provider is enabled
		} else {
			this.canGetLocation = true;
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("activity", "LOC Network Enabled");
				if (locationManager != null) {
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						Log.d("activity", "LOC by Network");
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}
			}
		}
	}

	/**
	 * This function is invoked when you need to get the user's location
	 * 
	 * @throws IOException
	 */
	private void localizacion() throws IOException {

		this.comprobarServiciosLocalizacion();

		try {
			this.getLocation();

			System.out.println("Latitude: " + this.latitude);
			System.out.println("Longitude: " + this.longitude);

			String urlConseguirWoeid = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20geo.placefinder%20WHERE%20text%3D%22" + this.latitude + "%2C" + this.longitude + "%22%20and%20gflags%3D%22R%22&diagnostics=true";

			Bundle bund = new Bundle();
			bund.putString("url", urlConseguirWoeid);
			bund.putSerializable("speaker", this.voiceSpeaker);
			bund.putDouble("latitude", this.latitude);
			bund.putDouble("longitude", this.longitude);

			new DescargarLocalizacionYahoo().execute(bund);
		} catch (ExcepcionLocalizacionDesactivada e) {
			System.out.println("No esta habilitada la lozalización");

		}
	}

	/**
	 * 
	 * This function takes care of changing the latitude and longitude of the user, by the name of the street
	 * 
	 * @param Location
	 * @throws IOException
	 */
	private void parsearPosicionString(Location loc) throws IOException {
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(this, Locale.getDefault());
		addresses = geocoder.getFromLocation(latitude, longitude, 1);

		String address = addresses.get(0).getAddressLine(0);
		String city = addresses.get(0).getAddressLine(1);
		String country = addresses.get(0).getAddressLine(2);

		this.calleUbicacionActual = address;
		this.ciudadUbicacionActual = city;
		this.paisUbicacionActual = country;

		System.out.println("Latitud: " + loc.getLatitude() + "\nLongitud: " + loc.getLongitude());
		System.out.println("Calle: " + address + "\nCiudad: " + city + "\nPais: " + country);
	}

	/**
	 * This function is responsible for obtaining the latitude and longitude (GPS coordinates of the user)
	 * 
	 * @return Location
	 * @throws ExcepcionLocalizacionDesactivada
	 */
	public Location getLocation() throws ExcepcionLocalizacionDesactivada {

		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// getting GPS status
		this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			throw new ExcepcionLocalizacionDesactivada(this.voiceSpeaker);
		} else {
			this.canGetLocation = true;
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("activity", "LOC Network Enabled");
				if (locationManager != null) {
					this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						Log.d("activity", "LOC by Network");
						this.latitude = location.getLatitude();
						this.longitude = location.getLongitude();
					}
				}
			}
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("activity", "RLOC: GPS Enabled");
					if (locationManager != null) {
						this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							Log.d("activity", "RLOC: loc by GPS");

							this.latitude = location.getLatitude();
							this.longitude = location.getLongitude();
						}
					}
				}
			}
		}

		return location;
	}

	@Override
	public void onLocationChanged(Location arg0) {
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	/**
	 * This function is responsible for managing the jokes download web signed and delivered jokes
	 */
	private void chistes() {

		this.crearFicheroMP3();

		/*
		 * String url = "http://www.chistesdiarios.com/rss.xml";
		 * 
		 * Bundle bund = new Bundle(); bund.putSerializable("speaker", this.voiceSpeaker); bund.putString("url", url);
		 * 
		 * new DescargarChistes().execute(bund);
		 */
	}

	private void fecha() {
		new Fecha(this.voiceSpeaker);
	}

	private void controlVolumen() {

		int modoVolumen = 5;
		AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

		for (int i = 0; i < this.fraseSpliteada.length; i++) {
			if (this.fraseSpliteada[i].equals("SONIDO")) {
				modoVolumen = AudioManager.RINGER_MODE_NORMAL;
			} else if (this.fraseSpliteada[i].equals("SILENCIO")) {
				modoVolumen = AudioManager.RINGER_MODE_SILENT;
			} else if (this.fraseSpliteada[i].equals("VIBRACIÓN")) {
				modoVolumen = AudioManager.RINGER_MODE_VIBRATE;
			}
		}

		if (modoVolumen == 5) {
			this.endSpeaking = false;
			voiceSpeaker.habla("Lo siento, no he entendido bien que deseas hacer con el volumen del móvil.");

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			do {
				endSpeaking = voiceSpeaker.tts.isSpeaking();
			} while (endSpeaking);
		} else {

			manager.setRingerMode(modoVolumen);

		}
	}

	private void crearFicheroMP3() {

		HashMap<String, String> myHashRender = new HashMap();

		String frase = "De acuerdo, no me apagaré,";
		String nombreMP3 = "/" + "no_temporizador" + ".mp3";

		String ubicacionFichero = Environment.getExternalStorageDirectory().toString() + nombreMP3;

		myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, frase);
		voiceSpeaker.tts.synthesizeToFile(frase, myHashRender, ubicacionFichero);

		File[] files = Environment.getExternalStorageDirectory().listFiles();

		// Muestra todos los ficheros de la ubicacion
		for (int i = 0; i < files.length; i++)
			Log.d("files", files[i].getName());
	}

	private void traduccion() {

		Intent i = new Intent(this, GestorTraductor.class);
		startActivity(i);
	}

	private void detectorColor() {
		Intent i = new Intent(this, ViewfinderEE368.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(i);
	}

	private void reproductorRadio() {
		
		Intent i = new Intent(this, radio.GestorRadios.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(i);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {

		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		this.endSpeaking = true;
		this.voiceSpeaker.cerrar();
		try {
			this.voiceSpeaker = new Speaker(this);
		} catch (Exception e) {
			System.out.println("Error en la creación del Speaker");
		}

		Intent i = new Intent(this, gisela.Principal.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(i);
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {

		this.preguntar(); // Comentar para simular pregunta
		// this.fraseReconocida = "chistes";
		// this.seguirConLaPregunta();
		return false;
	}
}